package cn.itedus.ssyx.activity.service.impl;

import cn.itedus.ssyx.activity.mapper.ActivityInfoMapper;
import cn.itedus.ssyx.activity.mapper.ActivityRuleMapper;
import cn.itedus.ssyx.activity.mapper.ActivitySkuMapper;
import cn.itedus.ssyx.activity.service.ActivityInfoService;
import cn.itedus.ssyx.client.product.ProductFeignClient;
import cn.itedus.ssyx.enums.ActivityType;
import cn.itedus.ssyx.model.activity.ActivityInfo;
import cn.itedus.ssyx.model.activity.ActivityRule;
import cn.itedus.ssyx.model.activity.ActivitySku;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.vo.activity.ActivityRuleVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

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
            if(!CollectionUtils.isEmpty(activityRuleList)) {
                List<String> ruleList = new ArrayList<>();
                //把规则名称处理
                for (ActivityRule activityRule:activityRuleList) {
                    ruleList.add(this.getRuleDesc(activityRule));
                }
                result.put(skuId,ruleList);
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
                    .append("元打")
                    .append(activityRule.getBenefitDiscount())
                    .append("折");
        }
        return ruleDesc.toString();
    }
}
