package cn.itedus.ssyx.product.api;

import cn.itedus.ssyx.model.product.Category;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.product.service.CategoryService;
import cn.itedus.ssyx.product.service.SkuInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 16:30
 * @description: 开发商品上下架操作ES接口
 */
@Api(tags = "商品上下架操作ES接口")
@RestController
@RequestMapping("/api/product")
public class ProductInnerController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SkuInfoService skuInfoService;

    @ApiOperation("根据分类Id获取分类信息")
    @GetMapping("inneer/getCategory/{categoryId}")
    public Category getCategory(@PathVariable("categoryId") Long categoryId) {
        Category category = categoryService.getById(categoryId);
        return category;
    }

    @ApiOperation("很具SkuId获取Sku信息")
    @GetMapping("inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId) {
        SkuInfo skuInfo = skuInfoService.getById(skuId);
        return skuInfo;
    }

}
