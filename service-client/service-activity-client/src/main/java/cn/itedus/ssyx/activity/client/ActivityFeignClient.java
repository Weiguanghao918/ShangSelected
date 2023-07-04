package cn.itedus.ssyx.activity.client;

import cn.itedus.ssyx.model.activity.CouponInfo;
import cn.itedus.ssyx.model.order.CartInfo;
import cn.itedus.ssyx.vo.order.CartInfoVo;
import cn.itedus.ssyx.vo.order.OrderConfirmVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-20 15:59
 * @description: 活动对外远程调用接口
 */
@FeignClient("service-activity")
public interface ActivityFeignClient {
    /**
     * 根据skuId集合封装对应的规则信息后存入map
     *
     * @param skuIdList skuId集合
     * @return 封装后的数据，其中skuId为键，activityRule规则为值
     */
    @PostMapping("/api/activity/inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList);

    /**
     * 根据skuId和userId获取活动与优惠券信息
     *
     * @param skuId  skuId
     * @param userId userId
     * @return 结果map
     */
    @GetMapping("/api/activity/inner/findActivityAndCoupon/{skuId}/{userId}")
    public Map<String, Object> findActivityAndCoupon(@PathVariable("skuId") Long skuId,
                                                     @PathVariable("userId") Long userId);

    /**
     * 获取购物车满足条件的促销与优惠券信息
     *
     * @param cartInfoList
     * @param userId
     * @return
     */
    @PostMapping("/api/activity/inner/findCartActivityAndCoupon/{userId}")
    OrderConfirmVo findCartActivityAndCoupon(@RequestBody List<CartInfo> cartInfoList, @PathVariable("userId") Long userId);

    /**
     * 获取购物车中满足条件得促销活动信息
     *
     * @param cartInfoList 商品列表
     * @return 封装类
     */
    @PostMapping("/api/activity/inner/findCartActivityList")
    public List<CartInfoVo> findCartActivityList(@RequestBody List<CartInfo> cartInfoList);

    /**
     * 封装优惠券信息，将优惠券设计的商品ID进行封装
     *
     * @param cartInfoList 商品集合
     * @param couponId     优惠券Id
     * @return 封装类
     */
    @PostMapping("/api/activity/inner/findRangeSkuIdList/{couponId}")
    public CouponInfo findRangeSkuIdList(@RequestBody List<CartInfo> cartInfoList, @PathVariable("couponId") Long couponId);

    /**
     * 更新优惠券使用状态
     *
     * @param couponId 优惠券ID
     * @param userId   用户ID
     * @param orderId  订单ID
     */
    @GetMapping(value = "/api/activity/inner/updateCouponInfoUseStatus/{couponId}/{userId}/{orderId}")
    public Boolean updateCouponInfoUseStatus(@PathVariable("couponId") Long couponId, @PathVariable("userId") Long userId, @PathVariable("orderId") Long orderId);
}
