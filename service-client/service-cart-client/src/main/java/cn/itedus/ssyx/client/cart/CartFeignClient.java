package cn.itedus.ssyx.client.cart;

import cn.itedus.ssyx.model.order.CartInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-07-03 15:31
 * @description: cart远程调用接口
 */
@FeignClient(value = "service-cart")
public interface CartFeignClient {
    /**
     * 根据用户Id 返回选中的购物车项
     *
     * @param userId userId
     * @return 购物车项
     */
    @GetMapping("/api/cart/inner/getCartCheckedList/{userId}")
    public List<CartInfo> getCartCheckedList(@PathVariable("userId") Long userId);
}
