package cn.itedus.ssyx.order.service.impl;

import cn.itedus.ssyx.activity.client.ActivityFeignClient;
import cn.itedus.ssyx.client.cart.CartFeignClient;
import cn.itedus.ssyx.client.product.ProductFeignClient;
import cn.itedus.ssyx.client.user.UserFeignClient;
import cn.itedus.ssyx.common.auth.AuthContextHolder;
import cn.itedus.ssyx.common.constant.RedisConst;
import cn.itedus.ssyx.common.exception.SsyxException;
import cn.itedus.ssyx.common.result.ResultCodeEnum;
import cn.itedus.ssyx.enums.SkuType;
import cn.itedus.ssyx.model.order.CartInfo;
import cn.itedus.ssyx.model.order.OrderInfo;
import cn.itedus.ssyx.order.mapper.OrderInfoMapper;
import cn.itedus.ssyx.order.service.OrderInfoService;
import cn.itedus.ssyx.vo.order.OrderConfirmVo;
import cn.itedus.ssyx.vo.order.OrderSubmitVo;
import cn.itedus.ssyx.vo.product.SkuStockLockVo;
import cn.itedus.ssyx.vo.user.LeaderAddressVo;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import jodd.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
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


        //返回订单号
        return null;
    }

    @Override
    public OrderInfo getOrderInfoById(Long orderId) {
        return null;
    }
}
