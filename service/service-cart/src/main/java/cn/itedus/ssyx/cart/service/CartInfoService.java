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

    /**
     * 更改购物项状态
     *
     * @param userId    userId
     * @param isChecked 状态值
     * @param skuId     商品skuId
     */
    void checkCart(Long userId, Integer isChecked, Long skuId);

    /**
     * 更改所有购物项状态
     *
     * @param userId    userId
     * @param isChecked 状态值
     */
    void checkAllCart(Long userId, Integer isChecked);

    /**
     * 批量更新购物项状态
     *
     * @param skuIdList 购物项id集合
     * @param userId    userId
     * @param isChecked 状态值
     */
    void batchCheckCart(List<Long> skuIdList, Long userId, Integer isChecked);

    /**
     * 根据用户Id获取已选中的购物项
     *
     * @param userId userId
     * @return 购物项集合
     */
    List<CartInfo> getCartCheckedList(Long userId);

    /**
     * 根据userId删除购物车已下单项目
     * @param userId userId
     */
    void deleteCartChecked(Long userId);
}
