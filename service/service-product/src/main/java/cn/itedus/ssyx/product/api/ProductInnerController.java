package cn.itedus.ssyx.product.api;

import cn.itedus.ssyx.model.product.Category;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.product.service.CategoryService;
import cn.itedus.ssyx.product.service.SkuInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 16:30
 * @description: 开发商品上下架操作ES接口
 */
@Api(tags = "商品上下架操作ES接口")
@RestController
@RequestMapping("/api/product")
@SuppressWarnings({"unchecked", "rawtypes"})
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

    @ApiOperation("根据SkuId获取Sku信息")
    @GetMapping("inner/getSkuInfo/{skuId}")
    public SkuInfo getSkuInfo(@PathVariable("skuId") Long skuId) {
        SkuInfo skuInfo = skuInfoService.getById(skuId);
        return skuInfo;
    }

    @ApiOperation("批量获取Sku信息")
    @PostMapping("inner/findSkuInfoList")
    public List<SkuInfo> findSkuInfoList(@RequestBody List<Long> idList) {
        List<SkuInfo> skuInfoList = skuInfoService.findSkuInfoList(idList);
        return skuInfoList;
    }

    @ApiOperation("根据关键字获取Sku列表")
    @PostMapping("inner/findSkuInfoByKeyword/{keyword}")
    public List<SkuInfo> findSkuInfoListByKeyword(@PathVariable("keyword") String keyword) {
        List<SkuInfo> skuInfoList = skuInfoService.findSkuInfoListByKeyword(keyword);
        return skuInfoList;
    }

    @ApiOperation("根据Id列表获取分类集合")
    @PostMapping("inner/findCategoryList")
    public List<Category> findCategoryList(@RequestBody List<Long> idList) {
        List<Category> categoryList=categoryService.findCategoryList(idList);
        return categoryList;
    }

}
