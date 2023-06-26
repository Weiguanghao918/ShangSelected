package cn.itedus.ssyx.cart.service;

import cn.itedus.ssyx.model.order.CartInfo;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-26 13:55
 * @description: 购物车服务接口
 */
public interface CartInfoService {
    /**
     * 添加购物车项
     *
     * @param userId userId
     * @param skuId  商品skuId
     * @param skuNum 商品数量
     */
    void addToCart(Long userId, Long skuId, Integer skuNum);

    /**
     * 删除单项购物车
     *
     * @param userId userId
     * @param skuId  商品skuId
     */
    void deleteCart(Long userId, Long skuId);

    /**
     * 清空购物车
     *
     * @param userId userId
     */
    void deleteAllCart(Long userId);

    /**
     * 批量删除购物车
     *
     * @param userId    userId
     * @param skuIdList skuId集合
     */
    void batchDeleteCart(Long userId, List<Long> skuIdList);

    /**
     * 获取不带优惠券的购物车列表
     *
     * @param userId userId
     * @return 购物车列表集合
     */
    List<CartInfo> getCartList(Long userId);
}
