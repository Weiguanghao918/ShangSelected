package cn.itedus.ssyx.activity.service.impl;

import cn.itedus.ssyx.activity.mapper.*;
import cn.itedus.ssyx.activity.service.ActivityInfoService;
import cn.itedus.ssyx.activity.service.CouponInfoService;
import cn.itedus.ssyx.client.product.ProductFeignClient;
import cn.itedus.ssyx.enums.ActivityType;
import cn.itedus.ssyx.model.activity.*;
import cn.itedus.ssyx.model.order.CartInfo;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.vo.activity.ActivityRuleVo;
import cn.itedus.ssyx.vo.order.CartInfoVo;
import cn.itedus.ssyx.vo.order.OrderConfirmVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 14:50
 * @description: 营销活动服务接口实现类
 */
@Service
public class ActivityInfoServiceImpl extends ServiceImpl<ActivityInfoMapper, ActivityInfo> implements ActivityInfoService {
    @Autowired
    private ActivityInfoMapper activityInfoMapper;
    @Autowired
    private ActivityRuleMapper activityRuleMapper;
    @Autowired
    private ActivitySkuMapper activitySkuMapper;
    @Autowired
    private ProductFeignClient productFeignClient;
    @Autowired
    private CouponInfoService couponInfoService;



    @Override
    public Map<String, Object> findActivityAndCoupon(Long skuId, Long userId) {
        //1. 封装活动信息，一个sku只能参加一个活动，但是一个活动可以有多个规则，获取规则集合即可
        List<ActivityRule> activityRuleList = this.findActivityRule(skuId);

        //2. 获取优惠券信息
        List<CouponInfo> couponInfoList = couponInfoService.findCouponInfo(skuId, userId);

        //3. 封装结果
        Map<String, Object> result = new HashMap<>();
        result.put("activityRuleList", activityRuleList);
        result.put("couponInfoList", couponInfoList);
        return result;

    }

    @Override
    public OrderConfirmVo findCartActivityAndCoupon(List<CartInfo> cartInfoList, Long userId) {
        List<CartInfoVo> cartInfoVoList = this.findCartActivityList(cartInfoList);
        //促销活动优惠的总金额
        BigDecimal activityReduceAmount = cartInfoVoList.stream().filter(cartInfoVo -> null != cartInfoVo.getActivityRule()).map(cartInfoVo -> cartInfoVo.getActivityRule().getReduceAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        //购物车可使用的优惠券列表
        List<CouponInfo> couponInfoList = couponInfoService.findCartCouponInfo(cartInfoList, userId);
        //优惠券可使用的总金额，一次购物只能使用一张优惠券
        BigDecimal couponReduceAmount = new BigDecimal("0");
        if (!CollectionUtils.isEmpty(couponInfoList)) {
            couponReduceAmount = couponInfoList.stream().filter(couponInfo -> couponInfo.getIsOptimal().intValue() == 1).map(couponInfo -> couponInfo.getAmount()).reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        //购物车总金额
        BigDecimal cartInfoTotalAmount = cartInfoList.stream().filter(cartInfo -> cartInfo.getIsChecked() == 1).map(cartInfo -> cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()))).reduce(BigDecimal.ZERO, BigDecimal::add);

        //购物车原始总金额
        BigDecimal originalTotalAmount = cartInfoList.stream()
                .filter(cartInfo -> cartInfo.getIsChecked() == 1)
                .map(cartInfo -> cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //最终金额
        BigDecimal totalAmount = originalTotalAmount.subtract(activityReduceAmount).subtract(couponReduceAmount);

        OrderConfirmVo orderTradeVo = new OrderConfirmVo();
        orderTradeVo.setCarInfoVoList(cartInfoVoList);
        orderTradeVo.setActivityReduceAmount(activityReduceAmount);
        orderTradeVo.setCouponInfoList(couponInfoList);
        orderTradeVo.setCouponReduceAmount(couponReduceAmount);
        orderTradeVo.setOriginalTotalAmount(originalTotalAmount);
        orderTradeVo.setTotalAmount(totalAmount);


        return orderTradeVo;
    }

    @Override
    public List<CartInfoVo> findCartActivityList(List<CartInfo> cartInfoList) {
        List<CartInfoVo> cartInfoVoList = new ArrayList<>();
        //第一步：将购物车中参与相同活动的购物项放在一起 Map<Long,Set<Long>>
        //获取skuId集合
        List<Long> skuIdList = cartInfoList.stream().map(CartInfo::getSkuId).collect(Collectors.toList());
        //获取skuIdList对应的全部促销规则
        List<ActivitySku> activitySkuList = activityInfoMapper.selectCartActivityList(skuIdList);
        //分组处理，活动id为键，skuId集合为值（参与同一活动）
        Map<Long, Set<Long>> activityIdToSkuIdListMap = activitySkuList.stream().collect(Collectors.groupingBy(ActivitySku::getActivityId, Collectors.mapping(ActivitySku::getSkuId, Collectors.toSet())));

        //第二步：将同一个活动下的多个规则放在一起 Map<Long,List<ActivityRule>>
        //获取活动对应的促销信息（活动规则）
        Set<Long> activityIdSet = activitySkuList.stream().map(ActivitySku::getActivityId).collect(Collectors.toSet());
        Map<Long, List<ActivityRule>> activityIdToActivityRuleListMap = new HashMap<>();

        if (!CollectionUtils.isEmpty(activityIdSet)) {
            LambdaQueryWrapper<ActivityRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
            lambdaQueryWrapper.orderByDesc(ActivityRule::getConditionAmount, ActivityRule::getConditionNum);
            lambdaQueryWrapper.in(ActivityRule::getActivityId, activityIdSet);
            List<ActivityRule> activityRuleList = activityRuleMapper.selectList(lambdaQueryWrapper);

            //按照活动Id分组，获取活动对应的规则集合
            activityIdToActivityRuleListMap = activityRuleList.stream().collect(Collectors.groupingBy(activityRule -> activityRule.getActivityId()));
        }

        //第三步：根据活动汇总购物项，相同活动的购物项为为一组显示在页面，并且计算最优惠金额
        //记录有活动的购物项skuId
        Set<Long> activitySkuIdSet = new HashSet<>();
        if (!CollectionUtils.isEmpty(activityIdToSkuIdListMap)) {
            Iterator<Map.Entry<Long, Set<Long>>> iterator = activityIdToSkuIdListMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<Long, Set<Long>> entry = iterator.next();
                Long activityId = entry.getKey();
                //当前活动对应的购物项skuId列表
                Set<Long> currentActivitySkuIdSet = entry.getValue();
                //当前活动对应的购物项列表
                List<CartInfo> currentActivityCartInfoList = cartInfoList.stream().filter(cartInfo -> currentActivitySkuIdSet.contains(cartInfo.getSkuId())).collect(Collectors.toList());
                //当前活动的总金额
                BigDecimal activityTotalAmount = this.computeTotalAmount(currentActivityCartInfoList);
                //当前活动的购物项总个数
                Integer activityCartTotalNum = this.computeCartNum(currentActivityCartInfoList);

                //计算当前活动的最优惠规则
                //活动当前的对应的规则
                List<ActivityRule> currentActivityRuleList = activityIdToActivityRuleListMap.get(activityId);
                ActivityType activityType = currentActivityRuleList.get(0).getActivityType();
                ActivityRule optimalActivityRule = null;
                if (activityType == ActivityType.FULL_REDUCTION) {
                    optimalActivityRule = this.computeFullReduction(activityTotalAmount, currentActivityRuleList);
                } else {
                    optimalActivityRule = this.computeFullDiscount(activityCartTotalNum, activityTotalAmount, currentActivityRuleList);
                }

                //同一活动对应的购物项列表与对应优化规则
                CartInfoVo cartInfoVo = new CartInfoVo();
                cartInfoVo.setCartInfoList(currentActivityCartInfoList);
                cartInfoVo.setActivityRule(optimalActivityRule);
                cartInfoVoList.add(cartInfoVo);
                //记录
                activitySkuIdSet.addAll(currentActivitySkuIdSet);
            }
        }
        //第四步：无活动的购物项，每一项一组
        skuIdList.removeAll(activitySkuIdSet);
        if (!CollectionUtils.isEmpty(skuIdList)) {
            //获取skuId对应的购物项
            Map<Long, CartInfo> skuIdCartInfoMap = cartInfoList.stream().collect(Collectors.toMap(CartInfo::getSkuId, CartInfo -> CartInfo));
            for (Long skuId : skuIdList) {
                CartInfoVo cartInfoVo = new CartInfoVo();
                cartInfoVo.setActivityRule(null);
                List<CartInfo> currentCartInfoList = new ArrayList<>();
                currentCartInfoList.add(skuIdCartInfoMap.get(skuId));
                cartInfoVo.setCartInfoList(currentCartInfoList);
                cartInfoVoList.add(cartInfoVo);
            }
        }
        return cartInfoVoList;
    }




    /**
     * 计算满量打折最优规则
     *
     * @param activityCartTotalNum    总数量
     * @param activityTotalAmount     总金额
     * @param currentActivityRuleList 当前规则列表
     * @return 满量打折最优规则
     */
    private ActivityRule computeFullDiscount(Integer activityCartTotalNum, BigDecimal activityTotalAmount, List<ActivityRule> currentActivityRuleList) {
        ActivityRule optimalActivityRule = null;
        for (ActivityRule activityRule : currentActivityRuleList) {
            if (activityCartTotalNum.intValue() >= activityRule.getConditionNum()) {
                BigDecimal skuDiscountTotalAmount = activityTotalAmount.multiply(activityRule.getBenefitDiscount().divide(new BigDecimal("10")));
                BigDecimal reduceAmount = activityTotalAmount.subtract(skuDiscountTotalAmount);
                activityRule.setReduceAmount(reduceAmount);
                optimalActivityRule = activityRule;
                break;
            }
        }
        if (null == optimalActivityRule) {
            //如果没有满足条件的取最小满足条件的一项
            optimalActivityRule = currentActivityRuleList.get(currentActivityRuleList.size() - 1);
            optimalActivityRule.setReduceAmount(new BigDecimal("0"));
            optimalActivityRule.setSelectType(1);

            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionNum())
                    .append("件打")
                    .append(optimalActivityRule.getBenefitDiscount())
                    .append("折，还差")
                    .append(optimalActivityRule.getConditionNum() - activityCartTotalNum)
                    .append("件");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
        } else {
            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionNum())
                    .append("件打")
                    .append(optimalActivityRule.getBenefitDiscount())
                    .append("折，已减")
                    .append(optimalActivityRule.getReduceAmount())
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
            optimalActivityRule.setSelectType(2);
        }
        return optimalActivityRule;
    }

    /**
     * 计算满减活动中最优的规则是什么
     *
     * @param activityTotalAmount     总金额
     * @param currentActivityRuleList 规则集合
     * @return 最优满减规则
     */
    private ActivityRule computeFullReduction(BigDecimal activityTotalAmount, List<ActivityRule> currentActivityRuleList) {
        ActivityRule optimalActivityRule = null;
        //该活动skuActivityRuleList数据，已经按照优惠金额从大到小排序了
        for (ActivityRule activityRule : currentActivityRuleList) {
            //如果订单金额大于满减金额，则优惠金额
            if (activityTotalAmount.compareTo(activityRule.getConditionAmount()) > -1) {
                //优惠后减少金额
                activityRule.setReduceAmount(activityRule.getBenefitAmount());
                optimalActivityRule = activityRule;
                break;
            }
        }
        if (null == optimalActivityRule) {
            //如果没有满足条件的取最小满足条件的一项
            optimalActivityRule = currentActivityRuleList.get(currentActivityRuleList.size() - 1);
            optimalActivityRule.setReduceAmount(new BigDecimal("0"));
            optimalActivityRule.setSelectType(1);

            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionAmount())
                    .append("元减")
                    .append(optimalActivityRule.getBenefitAmount())
                    .append("元，还差")
                    .append(optimalActivityRule.getConditionAmount().subtract(activityTotalAmount))
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
        } else {
            StringBuffer ruleDesc = new StringBuffer()
                    .append("满")
                    .append(optimalActivityRule.getConditionAmount())
                    .append("元减")
                    .append(optimalActivityRule.getBenefitAmount())
                    .append("元，已减")
                    .append(optimalActivityRule.getReduceAmount())
                    .append("元");
            optimalActivityRule.setRuleDesc(ruleDesc.toString());
            optimalActivityRule.setSelectType(2);
        }

        return optimalActivityRule;

    }

    /**
     * 计算当前购物项有多少件
     *
     * @param currentActivityCartInfoList 当前购物车集合
     * @return 总数量
     */
    private Integer computeCartNum(List<CartInfo> currentActivityCartInfoList) {
        int total = 0;
        for (CartInfo cartInfo : currentActivityCartInfoList) {
            if (cartInfo.getIsChecked().intValue() == 1) {
                total += cartInfo.getSkuNum();
            }
        }
        return total;
    }

    /**
     * 计算购物项集合总金额
     *
     * @param currentActivityCartInfoList 当前购物项
     * @return 总金额
     */
    private BigDecimal computeTotalAmount(List<CartInfo> currentActivityCartInfoList) {
        BigDecimal total = new BigDecimal(0);
        for (CartInfo cartInfo : currentActivityCartInfoList) {
            //是否选中
            if (cartInfo.getIsChecked().intValue() == 1) {
                BigDecimal itemAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                total = total.add(itemAmount);
            }
        }
        return total;
    }

    @Override
    public IPage<ActivityInfo> selectPage(Page<ActivityInfo> pageParam) {
        QueryWrapper<ActivityInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByDesc("id");
        IPage<ActivityInfo> iPageModel = activityInfoMapper.selectPage(pageParam, queryWrapper);
        return iPageModel;
    }

    @Override
    public Map<String, Object> findActivityRuleList(Long activityId) {
        Map<String, Object> result = new HashMap<>();
        //首先获取规则列表
        LambdaQueryWrapper<ActivityRule> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ActivityRule::getActivityId, activityId);
        List<ActivityRule> activityRuleList = activityRuleMapper.selectList(lambdaQueryWrapper);
        result.put("activityRuleList", activityRuleList);

        //获取ActivitySKu表中所有Sku的Id信息，并且根据Id列表查询出所有的Sku商品信息
        LambdaQueryWrapper<ActivitySku> activitySkuLambdaQueryWrapper = new LambdaQueryWrapper<>();
        activitySkuLambdaQueryWrapper.eq(ActivitySku::getActivityId, activityId);
        List<ActivitySku> activitySkuList = activitySkuMapper.selectList(activitySkuLambdaQueryWrapper);
        List<Long> skuInfoIdList = activitySkuList.stream().map(ActivitySku::getSkuId).collect(Collectors.toList());
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoList(skuInfoIdList);
        result.put("skuInfoList", skuInfoList);

        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveActivityRule(ActivityRuleVo activityRuleVo) {
        //首先删除表中原有的数据
        activityRuleMapper.delete(new LambdaQueryWrapper<ActivityRule>().eq(ActivityRule::getActivityId, activityRuleVo.getActivityId()));
        activitySkuMapper.delete(new LambdaQueryWrapper<ActivitySku>().eq(ActivitySku::getActivityId, activityRuleVo.getActivityId()));

        List<ActivityRule> activityRuleList = activityRuleVo.getActivityRuleList();
        List<ActivitySku> activitySkuList = activityRuleVo.getActivitySkuList();
        List<Long> couponIdList = activityRuleVo.getCouponIdList();

        ActivityInfo activityInfo = activityInfoMapper.selectById(activityRuleVo.getActivityId());
        for (ActivityRule activityRule : activityRuleList) {
            activityRule.setActivityId(activityRuleVo.getActivityId());
            activityRule.setActivityType(activityInfo.getActivityType());
            activityRuleMapper.insert(activityRule);
        }

        for (ActivitySku activitySku : activitySkuList) {
            activitySku.setActivityId(activityRuleVo.getActivityId());
            activitySkuMapper.insert(activitySku);
        }

    }

    @Override
    public List<SkuInfo> findSkuInfoByKeyword(String keyword) {
        //首先根据关键词查询出所有的Sku信息
        List<SkuInfo> skuInfoList = productFeignClient.findSkuInfoListByKeyword(keyword);
        if (skuInfoList.size() == 0) {
            return skuInfoList;
        }
        //获取所有Sku的Id信息
        List<Long> skuIdList = skuInfoList.stream().map(SkuInfo::getId).collect(Collectors.toList());
        //排除已经参与活动的Sku id列表，判断标准是Sku Id信息在我的skuIdList中，并且该活动在当前时间还没有结束，所以需要连表查询
        List<Long> skuExistIdList = activityInfoMapper.selecExistIdList(skuIdList);
        //最后就是排除已经参与活动的Sku信息，并且把未参与活动的Sku信息进行返回即可
        List<SkuInfo> noExistSkuInfoList = new ArrayList<>();
        for (SkuInfo skuInfo : skuInfoList) {
            if (!skuExistIdList.contains(skuInfo.getId())) {
                noExistSkuInfoList.add(skuInfo);
            }

        }
        return noExistSkuInfoList;
    }

    @Override
    public List<ActivityRule> findActivityRule(Long skuId) {
        //首先获取根据skuId获取对应的活动，用活动Id获取对应的规则，这里需要查询三张表
        List<ActivityRule> activityRuleList = activityInfoMapper.selectActivityRuleList(skuId);
        if (!CollectionUtils.isEmpty(activityRuleList)) {
            for (ActivityRule activityRule : activityRuleList) {
                activityRule.setRuleDesc(this.getRuleDesc(activityRule));
            }
        }

        return activityRuleList;
    }

    //根据skuId列表获取促销信息
    @Override
    public Map<Long, List<String>> findActivity(List<Long> skuIdList) {
        Map<Long, List<String>> result = new HashMap<>();
        //skuIdList遍历，得到每个skuId
        skuIdList.forEach(skuId -> {
            //根据skuId进行查询，查询sku对应活动里面规则列表
            List<ActivityRule> activityRuleList =
                    baseMapper.selectActivityRuleList(skuId);
            //数据封装，规则名称
            if (!CollectionUtils.isEmpty(activityRuleList)) {
                List<String> ruleList = new ArrayList<>();
                //把规则名称处理
                for (ActivityRule activityRule : activityRuleList) {
                    ruleList.add(this.getRuleDesc(activityRule));
                }
                result.put(skuId, ruleList);
            }
        });
        return result;
    }


    //构造规则名称的方法
    private String getRuleDesc(ActivityRule activityRule) {
        ActivityType activityType = activityRule.getActivityType();
        StringBuffer ruleDesc = new StringBuffer();
        if (activityType == ActivityType.FULL_REDUCTION) {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionAmount())
                    .append("元减")
                    .append(activityRule.getBenefitAmount())
                    .append("元");
        } else {
            ruleDesc
                    .append("满")
                    .append(activityRule.getConditionNum())
                    .append("件打")
                    .append(activityRule.getBenefitDiscount())
                    .append("折");
        }
        return ruleDesc.toString();
    }
}
