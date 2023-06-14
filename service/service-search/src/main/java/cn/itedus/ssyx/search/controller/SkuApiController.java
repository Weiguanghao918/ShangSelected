package cn.itedus.ssyx.search.controller;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.search.service.SkuService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 16:48
 * @description: 商品搜索服务接口
 */
@RestController
@RequestMapping("/api/search/sku")
public class SkuApiController {

    @Autowired
    private SkuService skuService;

    @ApiOperation("上架商品")
    @GetMapping("inner/upperSku/{skuId}")
    public Result upperGoods(@PathVariable("skuId") Long skuId) {
        skuService.upperSku(skuId);
        return Result.ok(null);
    }

    @ApiOperation("下架商品")
    @GetMapping("inner/lowerSku/{skuId}")
    public Result lowerGoods(@PathVariable("skuId") Long skuId) {
        skuService.lowerSku(skuId);
        return Result.ok(null);
    }
}
