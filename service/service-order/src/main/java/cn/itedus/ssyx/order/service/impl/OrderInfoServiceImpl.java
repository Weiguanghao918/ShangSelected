package cn.itedus.ssyx.order.service.impl;

import cn.itedus.ssyx.activity.client.ActivityFeignClient;
import cn.itedus.ssyx.client.cart.CartFeignClient;
import cn.itedus.ssyx.client.product.ProductFeignClient;
import cn.itedus.ssyx.client.user.UserFeignClient;
import cn.itedus.ssyx.common.auth.AuthContextHolder;
import cn.itedus.ssyx.common.constant.RedisConst;
import cn.itedus.ssyx.common.exception.SsyxException;
import cn.itedus.ssyx.common.result.ResultCodeEnum;
import cn.itedus.ssyx.common.utils.DateUtil;
import cn.itedus.ssyx.enums.*;
import cn.itedus.ssyx.model.activity.ActivityRule;
import cn.itedus.ssyx.model.activity.CouponInfo;
import cn.itedus.ssyx.model.order.CartInfo;
import cn.itedus.ssyx.model.order.OrderInfo;
import cn.itedus.ssyx.model.order.OrderItem;
import cn.itedus.ssyx.mq.constant.MqConst;
import cn.itedus.ssyx.mq.service.RabbitService;
import cn.itedus.ssyx.order.mapper.OrderInfoMapper;
import cn.itedus.ssyx.order.service.OrderInfoService;
import cn.itedus.ssyx.order.service.OrderItemService;
import cn.itedus.ssyx.vo.order.CartInfoVo;
import cn.itedus.ssyx.vo.order.OrderConfirmVo;
import cn.itedus.ssyx.vo.order.OrderSubmitVo;
import cn.itedus.ssyx.vo.order.OrderUserQueryVo;
import cn.itedus.ssyx.vo.product.SkuStockLockVo;
import cn.itedus.ssyx.vo.user.LeaderAddressVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: Guanghao Wei
 * @date: 2023-07-03 15:21
 * @description: 订单服务接口实现类
 */
@Service
public class OrderInfoServiceImpl extends ServiceImpl<OrderInfoMapper, OrderInfo> implements OrderInfoService {
    @Autowired
    private OrderInfoMapper orderInfoMapper;

    @Autowired
    private ActivityFeignClient activityFeignClient;

    @Autowired
    private UserFeignClient userFeignClient;

    @Autowired
    private CartFeignClient cartFeignClient;

    @Resource
    private RedisTemplate redisTemplate;

    @Autowired
    private ProductFeignClient productFeignClient;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private RabbitService rabbitService;




    @Override
    public OrderConfirmVo confirmOrder() {
        //获取用户Id
        Long userId = AuthContextHolder.getUserId();

        //获取用户地址团长信息
        LeaderAddressVo leaderAddressVo = userFeignClient.getUserAddressByUserId(userId);

        //获取用户想购买的商品
        List<CartInfo> cartCheckedList = cartFeignClient.getCartCheckedList(userId);

        //生成防重订单id
        String orderNo = System.currentTimeMillis() + "";
        redisTemplate.opsForValue().set(RedisConst.ORDER_REPEAT + orderNo, orderNo, 24, TimeUnit.HOURS);

        //获取购物车满足条件的促销与优惠券信息
        OrderConfirmVo orderConfirmVo = activityFeignClient.findCartActivityAndCoupon(cartCheckedList, userId);
        orderConfirmVo.setLeaderAddressVo(leaderAddressVo);
        orderConfirmVo.setOrderNo(orderNo);

        return orderConfirmVo;
    }

    @Override
    public Long submitOrder(OrderSubmitVo orderSubmitVo) {
        //添加当前用户
        Long userId = AuthContextHolder.getUserId();
        orderSubmitVo.setUserId(userId);
        //防重订单号校验：Redis
        //主要就是那这前端传来的订单号去Redis中查询，如果存在，则删除Redis中的订单号，没有的话，就抛出异常，说明此订单重复提交
        String orderNo = orderSubmitVo.getOrderNo();
        if (StringUtil.isEmpty(orderNo)) {
            throw new SsyxException(ResultCodeEnum.ILLEGAL_REQUEST);
        }
        String script = "if(redis.call('get', KEYS[1]) == ARGV[1]) then return redis.call('del', KEYS[1]) else return 0 end";
        Boolean flag = (Boolean) redisTemplate.execute(new DefaultRedisScript(script, Boolean.class), Arrays.asList(RedisConst.ORDER_REPEAT + orderNo), orderNo);
        if (!flag) {
            throw new SsyxException(ResultCodeEnum.REPEAT_SUBMIT);
        }

        //验证商品库存并且锁定商品（这里主要针对普通商品）
        List<CartInfo> cartInfoList = cartFeignClient.getCartCheckedList(userId);
        List<CartInfo> commonSkuList = cartInfoList.stream().filter(cartInfo -> cartInfo.getSkuType().intValue() == SkuType.COMMON.getCode()).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(commonSkuList)) {
            List<SkuStockLockVo> commonStockLockVoList = commonSkuList.stream().map(cartInfo -> {
                SkuStockLockVo skuStockLockVo = new SkuStockLockVo();
                skuStockLockVo.setSkuId(cartInfo.getSkuId());
                skuStockLockVo.setSkuNum(cartInfo.getSkuNum());
                return skuStockLockVo;
            }).collect(Collectors.toList());

            Boolean isLockCommon = productFeignClient.checkAndLock(commonStockLockVoList, orderNo);
            if (!isLockCommon) {
                throw new SsyxException(ResultCodeEnum.ORDER_STOCK_FALL);
            }
        }

        //下单锁定库存操作:向两张表添加数据：order_info , order_item
        Long orderId = this.saveOrder(orderSubmitVo, cartInfoList);

        //发送异步消息，清除购物车中的已下单选项
        rabbitService.sendMessage(MqConst.EXCHANGE_ORDER_DIRECT, MqConst.ROUTING_DELETE_CART, orderSubmitVo.getUserId());

        //返回订单号
        return orderId;
    }

    /**
     * 存储订单信息和订单item列表，两种表一起写，所以需要写一个事务，保证原子性
     *
     * @param orderSubmitVo 订单基本信息
     * @param cartInfoList  商品信息
     * @return 订单号
     */
    @Transactional(rollbackFor = {Exception.class})
    public Long saveOrder(OrderSubmitVo orderSubmitVo, List<CartInfo> cartInfoList) {
        Long userId = AuthContextHolder.getUserId();
        if (CollectionUtils.isEmpty(cartInfoList)) {
            throw new SsyxException(ResultCodeEnum.DATA_ERROR);
        }
        LeaderAddressVo leaderAddressVo = userFeignClient.getUserAddressByUserId(userId);
        if (null == leaderAddressVo) {
            throw new SsyxException(ResultCodeEnum.DATA_ERROR);
        }

        //计算购物项分摊的优惠减少金额，按比例分摊，退款时按实际支付金额退款
        Map<String, BigDecimal> activitySplitAmountMap = this.computeActivitySplitAmount(cartInfoList);
        Map<String, BigDecimal> couponInfoSplitAmountMap = this.computeCouponInfoSplitAmount(cartInfoList, orderSubmitVo.getCouponId());
        //sku对应的订单明细
        List<OrderItem> orderItemList = new ArrayList<>();
        // 保存订单明细
        for (CartInfo cartInfo : cartInfoList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setId(null);
            orderItem.setCategoryId(cartInfo.getCategoryId());
            if (cartInfo.getSkuType().intValue() == SkuType.COMMON.getCode()) {
                orderItem.setSkuType(SkuType.COMMON);
            } else {
                orderItem.setSkuType(SkuType.SECKILL);
            }
            orderItem.setSkuId(cartInfo.getSkuId());
            orderItem.setSkuName(cartInfo.getSkuName());
            orderItem.setSkuPrice(cartInfo.getCartPrice());
            orderItem.setImgUrl(cartInfo.getImgUrl());
            orderItem.setSkuNum(cartInfo.getSkuNum());
            orderItem.setLeaderId(orderSubmitVo.getLeaderId());

            //促销活动分摊金额
            BigDecimal splitActivityAmount = activitySplitAmountMap.get("activity:" + orderItem.getSkuId());
            if (null == splitActivityAmount) {
                splitActivityAmount = new BigDecimal(0);
            }
            orderItem.setSplitActivityAmount(splitActivityAmount);

            //优惠券分摊金额
            BigDecimal splitCouponAmount = couponInfoSplitAmountMap.get("coupon:" + orderItem.getSkuId());
            if (null == splitCouponAmount) {
                splitCouponAmount = new BigDecimal(0);
            }
            orderItem.setSplitCouponAmount(splitCouponAmount);

            //优惠后的总金额
            BigDecimal skuTotalAmount = orderItem.getSkuPrice().multiply(new BigDecimal(orderItem.getSkuNum()));
            BigDecimal splitTotalAmount = skuTotalAmount.subtract(splitActivityAmount).subtract(splitCouponAmount);
            orderItem.setSplitTotalAmount(splitTotalAmount);
            orderItemList.add(orderItem);
        }

        //保存订单
        OrderInfo order = new OrderInfo();
        order.setUserId(userId);
//		private String nickName;
        order.setOrderNo(orderSubmitVo.getOrderNo());
        order.setOrderStatus(OrderStatus.UNPAID);
        order.setProcessStatus(ProcessStatus.UNPAID);
        order.setCouponId(orderSubmitVo.getCouponId());
        order.setLeaderId(orderSubmitVo.getLeaderId());
        order.setLeaderName(leaderAddressVo.getLeaderName());
        order.setLeaderPhone(leaderAddressVo.getLeaderPhone());
        order.setTakeName(leaderAddressVo.getTakeName());
        order.setReceiverName(orderSubmitVo.getReceiverName());
        order.setReceiverPhone(orderSubmitVo.getReceiverPhone());
        order.setReceiverProvince(leaderAddressVo.getProvince());
        order.setReceiverCity(leaderAddressVo.getCity());
        order.setReceiverDistrict(leaderAddressVo.getDistrict());
        order.setReceiverAddress(leaderAddressVo.getDetailAddress());
        order.setWareId(cartInfoList.get(0).getWareId());

        //计算订单金额
        BigDecimal originalTotalAmount = this.computeTotalAmount(cartInfoList);
        BigDecimal activityAmount = activitySplitAmountMap.get("activity:total");
        if (null == activityAmount) {
            activityAmount = new BigDecimal(0);
        }
        BigDecimal couponAmount = couponInfoSplitAmountMap.get("coupon:total");
        if (null == couponAmount) {
            couponAmount = new BigDecimal(0);
        }
        BigDecimal totalAmount = originalTotalAmount.subtract(activityAmount).subtract(couponAmount);
        //计算订单金额
        order.setOriginalTotalAmount(originalTotalAmount);
        order.setActivityAmount(activityAmount);
        order.setCouponAmount(couponAmount);
        order.setTotalAmount(totalAmount);

        //计算团长佣金
        BigDecimal profitRate = new BigDecimal("0.01");
        BigDecimal commissionAmount = order.getTotalAmount().multiply(profitRate);
        order.setCommissionAmount(commissionAmount);

        orderInfoMapper.insert(order);

        //保存订单项
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderId(order.getId());
        }
        orderItemService.saveBatch(orderItemList);

        //更新优惠券使用状态
        if (null != order.getCouponId()) {
            activityFeignClient.updateCouponInfoUseStatus(order.getCouponId(), userId, order.getId());
        }


        //发送消息
        return order.getId();
    }


    @Override
    public OrderInfo getOrderInfoById(Long orderId) {
        OrderInfo orderInfo = orderInfoMapper.selectById(orderId);
        orderInfo.getParam().put("orderStatusName", orderInfo.getOrderStatus().getComment());

        List<OrderItem> orderItemList = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderInfo.getId()));
        orderInfo.setOrderItemList(orderItemList);
        return orderInfo;
    }

    @Override
    public OrderInfo getOrderInfoByOrderNo(String orderNo) {
        return orderInfoMapper.selectOne(new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getOrderNo, orderNo));
    }

    @Override
    public void orderPay(String orderNo) {
        OrderInfo orderInfo = this.getOrderInfoByOrderNo(orderNo);
        if (null == orderInfo || orderInfo.getOrderStatus() != OrderStatus.UNPAID) {
            return;
        }
        this.updateOrderStatus(orderInfo.getId(), ProcessStatus.WAITING_DELEVER);

        rabbitService.sendMessage(MqConst.EXCHANGE_ORDER_DIRECT, MqConst.ROUTING_MINUS_STOCK, orderNo);
    }

    @Override
    public IPage<OrderInfo> findUserOrderPage(Page<OrderInfo> pageModel, OrderUserQueryVo orderUserQueryVo) {

        IPage<OrderInfo> pageResult = orderInfoMapper.selectPage(pageModel, new LambdaQueryWrapper<OrderInfo>().eq(OrderInfo::getUserId, orderUserQueryVo.getUserId()).eq(OrderInfo::getOrderStatus, orderUserQueryVo.getOrderStatus()));
        List<OrderInfo> orderInfoList = pageResult.getRecords();
        for (OrderInfo orderInfo : orderInfoList) {
            List<OrderItem> orderItemList = orderItemService.list(new LambdaQueryWrapper<OrderItem>().eq(OrderItem::getOrderId, orderInfo.getId()));
            orderInfo.setOrderItemList(orderItemList);
            orderInfo.getParam().put("orderStatusName",orderInfo.getOrderStatus().getComment());
        }
        return pageModel;
    }

    private void updateOrderStatus(Long orderId, ProcessStatus processStatus) {
        OrderInfo orderInfo = new OrderInfo();
        orderInfo.setId(orderId);
        orderInfo.setProcessStatus(processStatus);
        orderInfo.setOrderStatus(processStatus.getOrderStatus());
        if (processStatus == ProcessStatus.WAITING_DELEVER) {
            orderInfo.setPaymentTime(new Date());
        } else if (processStatus == ProcessStatus.WAITING_LEADER_TAKE) {
            orderInfo.setDeliveryTime(new Date());
        } else if (processStatus == ProcessStatus.WAITING_USER_TAKE) {
            orderInfo.setTakeTime(new Date());
        }
        orderInfoMapper.updateById(orderInfo);
    }


    private BigDecimal computeTotalAmount(List<CartInfo> cartInfoList) {
        BigDecimal total = new BigDecimal(0);
        for (CartInfo cartInfo : cartInfoList) {
            BigDecimal itemTotal = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
            total = total.add(itemTotal);
        }
        return total;
    }

    /**
     * 计算购物项分摊的优惠减少金额
     * 打折：按折扣分担
     * 现金：按比例分摊
     *
     * @param cartInfoParamList
     * @return
     */
    private Map<String, BigDecimal> computeActivitySplitAmount(List<CartInfo> cartInfoParamList) {
        Map<String, BigDecimal> activitySplitAmountMap = new HashMap<>();

        //促销活动相关信息
        List<CartInfoVo> cartInfoVoList = activityFeignClient.findCartActivityList(cartInfoParamList);

        //活动总金额
        BigDecimal activityReduceAmount = new BigDecimal(0);
        if (!CollectionUtils.isEmpty(cartInfoVoList)) {
            for (CartInfoVo cartInfoVo : cartInfoVoList) {
                ActivityRule activityRule = cartInfoVo.getActivityRule();
                List<CartInfo> cartInfoList = cartInfoVo.getCartInfoList();
                if (null != activityRule) {
                    //优惠金额， 按比例分摊
                    BigDecimal reduceAmount = activityRule.getReduceAmount();
                    activityReduceAmount = activityReduceAmount.add(reduceAmount);
                    if (cartInfoList.size() == 1) {
                        activitySplitAmountMap.put("activity:" + cartInfoList.get(0).getSkuId(), reduceAmount);
                    } else {
                        //总金额
                        BigDecimal originalTotalAmount = new BigDecimal(0);
                        for (CartInfo cartInfo : cartInfoList) {
                            BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                            originalTotalAmount = originalTotalAmount.add(skuTotalAmount);
                        }
                        //记录除最后一项是所有分摊金额， 最后一项=总的 - skuPartReduceAmount
                        BigDecimal skuPartReduceAmount = new BigDecimal(0);
                        if (activityRule.getActivityType() == ActivityType.FULL_REDUCTION) {
                            for (int i = 0, len = cartInfoList.size(); i < len; i++) {
                                CartInfo cartInfo = cartInfoList.get(i);
                                if (i < len - 1) {
                                    BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                                    //sku分摊金额
                                    BigDecimal skuReduceAmount = skuTotalAmount.divide(originalTotalAmount, 2, RoundingMode.HALF_UP).multiply(reduceAmount);
                                    activitySplitAmountMap.put("activity:" + cartInfo.getSkuId(), skuReduceAmount);

                                    skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
                                } else {
                                    BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
                                    activitySplitAmountMap.put("activity:" + cartInfo.getSkuId(), skuReduceAmount);
                                }
                            }
                        } else {
                            for (int i = 0, len = cartInfoList.size(); i < len; i++) {
                                CartInfo cartInfo = cartInfoList.get(i);
                                if (i < len - 1) {
                                    BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));

                                    //sku分摊金额
                                    BigDecimal skuDiscountTotalAmount = skuTotalAmount.multiply(activityRule.getBenefitDiscount().divide(new BigDecimal("10")));
                                    BigDecimal skuReduceAmount = skuTotalAmount.subtract(skuDiscountTotalAmount);
                                    activitySplitAmountMap.put("activity:" + cartInfo.getSkuId(), skuReduceAmount);

                                    skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
                                } else {
                                    BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
                                    activitySplitAmountMap.put("activity:" + cartInfo.getSkuId(), skuReduceAmount);
                                }
                            }
                        }
                    }
                }
            }
        }
        activitySplitAmountMap.put("activity:total", activityReduceAmount);
        return activitySplitAmountMap;
    }

    private Map<String, BigDecimal> computeCouponInfoSplitAmount(List<CartInfo> cartInfoList, Long couponId) {
        Map<String, BigDecimal> couponInfoSplitAmountMap = new HashMap<>();

        if (null == couponId) {
            return couponInfoSplitAmountMap;
        }
        CouponInfo couponInfo = activityFeignClient.findRangeSkuIdList(cartInfoList, couponId);

        if (null != couponInfo) {
            //sku对应的订单明细
            Map<Long, CartInfo> skuIdToCartInfoMap = new HashMap<>();
            for (CartInfo cartInfo : cartInfoList) {
                skuIdToCartInfoMap.put(cartInfo.getSkuId(), cartInfo);
            }
            //优惠券对应的skuId列表
            List<Long> skuIdList = couponInfo.getSkuIdList();
            if (CollectionUtils.isEmpty(skuIdList)) {
                return couponInfoSplitAmountMap;
            }
            //优惠券优化总金额
            BigDecimal reduceAmount = couponInfo.getAmount();
            if (skuIdList.size() == 1) {
                //sku的优化金额
                couponInfoSplitAmountMap.put("coupon:" + skuIdToCartInfoMap.get(skuIdList.get(0)).getSkuId(), reduceAmount);
            } else {
                //总金额
                BigDecimal originalTotalAmount = new BigDecimal(0);
                for (Long skuId : skuIdList) {
                    CartInfo cartInfo = skuIdToCartInfoMap.get(skuId);
                    BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                    originalTotalAmount = originalTotalAmount.add(skuTotalAmount);
                }
                //记录除最后一项是所有分摊金额， 最后一项=总的 - skuPartReduceAmount
                BigDecimal skuPartReduceAmount = new BigDecimal(0);
                if (couponInfo.getCouponType() == CouponType.CASH || couponInfo.getCouponType() == CouponType.FULL_REDUCTION) {
                    for (int i = 0, len = skuIdList.size(); i < len; i++) {
                        CartInfo cartInfo = skuIdToCartInfoMap.get(skuIdList.get(i));
                        if (i < len - 1) {
                            BigDecimal skuTotalAmount = cartInfo.getCartPrice().multiply(new BigDecimal(cartInfo.getSkuNum()));
                            //sku分摊金额
                            BigDecimal skuReduceAmount = skuTotalAmount.divide(originalTotalAmount, 2, RoundingMode.HALF_UP).multiply(reduceAmount);
                            couponInfoSplitAmountMap.put("coupon:" + cartInfo.getSkuId(), skuReduceAmount);

                            skuPartReduceAmount = skuPartReduceAmount.add(skuReduceAmount);
                        } else {
                            BigDecimal skuReduceAmount = reduceAmount.subtract(skuPartReduceAmount);
                            couponInfoSplitAmountMap.put("coupon:" + cartInfo.getSkuId(), skuReduceAmount);
                        }
                    }
                }
            }
            couponInfoSplitAmountMap.put("coupon:total", couponInfo.getAmount());
        }
        return couponInfoSplitAmountMap;
    }

}
