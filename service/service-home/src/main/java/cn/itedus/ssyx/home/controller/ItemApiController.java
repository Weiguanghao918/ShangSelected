package cn.itedus.ssyx.home.controller;

import cn.itedus.ssyx.common.auth.AuthContextHolder;
import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.home.service.ItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-25 16:09
 * @description: 小程序端商品详情接口
 */
@Api(tags = "商品详情")
@RestController
@RequestMapping("/api/home")
public class ItemApiController {

    @Autowired
    private ItemService itemService;

    @ApiOperation("获取商品Sku详情接口")
    @GetMapping("item/{id}")
    public Result getSkuDetailIndex(@PathVariable("id") Long skuId) {
        Long userId = AuthContextHolder.getUserId();
        Map<String, Object> skuDetailMap = itemService.getSkuDetailIndex(skuId, userId);
        return Result.ok(skuDetailMap);
    }
}
