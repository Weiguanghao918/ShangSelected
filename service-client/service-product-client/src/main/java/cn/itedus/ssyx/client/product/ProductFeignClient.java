package cn.itedus.ssyx.client.product;

import cn.itedus.ssyx.model.product.Category;
import cn.itedus.ssyx.model.product.SkuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 16:43
 * @description: 产品远程调用接口
 */
@FeignClient(value = "service-product")
public interface ProductFeignClient {
    /**
     * 根据分类Id获取分类信息
     *
     * @param categoryId 分类Id
     * @return 分类信息
     */
    @GetMapping("/api/product/inneer/getCategory/{categoryId}")
    public Category getCategory(@PathVariable("categoryId") Long categoryId);

    /**
     * 根据Sku id获取Sku信息
     *
     * @param skuId Sku Id
     * @return Sku信息
     */
    @GetMapping("/api/product/inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId);
}
