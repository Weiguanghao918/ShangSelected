package cn.itedus.ssyx.home.controller;

import cn.itedus.ssyx.client.product.ProductFeignClient;
import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.model.product.Category;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-20 15:24
 * @description: 商品分类接口
 */
@Api(tags = "商品分类")
@RestController
@RequestMapping("/api/home")
public class CategoryApiController {
    @Autowired
    private ProductFeignClient productFeignClient;

    @ApiOperation("获取全部分类信息")
    @GetMapping("category")
    public Result index() {
        List<Category> categoryList = productFeignClient.findAllCategoryList();
        return Result.ok(categoryList);
    }
}
