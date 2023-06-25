package cn.itedus.ssyx.search.controller;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.model.search.SkuEs;
import cn.itedus.ssyx.search.service.SkuService;
import cn.itedus.ssyx.vo.search.SkuEsQueryVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 16:48
 * @description: 商品搜索服务接口
 */
@RestController
@RequestMapping("/api/search/sku")
public class SkuSearchController {

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

    @ApiOperation("获取爆款商品")
    @GetMapping("/inner/findHotSkuList")
    public List<SkuEs> findHotSkuList() {
        List<SkuEs> skuEsList = skuService.findHotSkuList();
        return skuEsList;
    }

    @ApiOperation("根据条件搜索商品")
    @GetMapping("{page}/{limit}")
    public Result getList(@PathVariable("page") Integer page,
                          @PathVariable("limit") Integer limit,
                          SkuEsQueryVo skuEsQueryVo) {
        Pageable pageable = PageRequest.of(page - 1, limit);
        Page<SkuEs> pageModel = skuService.search(pageable, skuEsQueryVo);
        return Result.ok(pageModel);
    }

    @ApiOperation("增加商品热度")
    @GetMapping("inner/incrHotScore/{skuId}")
    public Boolean incrHotScore(@PathVariable("skuId") Long skuId) {
        skuService.incrHtScore(skuId);
        return true;
    }


}
