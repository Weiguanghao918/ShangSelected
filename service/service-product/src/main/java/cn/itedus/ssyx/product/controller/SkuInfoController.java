package cn.itedus.ssyx.product.controller;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.model.product.SkuInfo;
import cn.itedus.ssyx.product.service.SkuInfoService;
import cn.itedus.ssyx.vo.product.SkuInfoQueryVo;
import cn.itedus.ssyx.vo.product.SkuInfoVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-13 14:42
 * @description: 商品SKU管理
 */
@Api(tags = "商品SKU管理")
@RestController
@RequestMapping("/admin/product/skuInfo")
public class SkuInfoController {
    @Autowired
    private SkuInfoService skuInfoService;

    @ApiOperation("获取商品Sku分页列表")
    @GetMapping("{page}/{limit}")
    public Result<IPage<SkuInfo>> index(@PathVariable("page") Long page,
                                        @PathVariable("limit") Long limit,
                                        SkuInfoQueryVo skuInfoQueryVo) {
        Page<SkuInfo> pageParam = new Page<>(page, limit);
        IPage<SkuInfo> iPageModel = skuInfoService.selectPageInfo(pageParam, skuInfoQueryVo);
        return Result.ok(iPageModel);
    }

    @ApiOperation("新增商品Sku")
    @PostMapping("save")
    public Result saveSkuInfo(@RequestBody SkuInfoVo skuInfoVo) {
        skuInfoService.saveSkuInfo(skuInfoVo);
        return Result.ok();
    }

    @ApiOperation("获取商品Sku信息")
    @GetMapping("get/{id}")
    public Result<SkuInfoVo> getSkuInfoById(@PathVariable("id") Long id) {
        SkuInfoVo skuInfoVo = skuInfoService.getSkuInfoVo(id);
        return Result.ok(skuInfoVo);
    }

    @ApiOperation("商品Sku修改功能")
    @PutMapping("update")
    public Result updateSkuInfoById(@RequestBody SkuInfoVo skuInfoVo) {
        skuInfoService.updateSkuInfoById(skuInfoVo);
        return Result.ok();
    }

    @ApiOperation("删除商品SKu信息")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable("id") Long skuId) {
        skuInfoService.removeBySKuId(skuId);
        return Result.ok();
    }

    @ApiOperation("批量删除Sku信息")
    @DeleteMapping("batchRemove")
    public Result batchRemoveByIds(@RequestBody List<Long> idList) {
        skuInfoService.batchRemoveBySKuIds(idList);
        return Result.ok();
    }

    @ApiOperation("商品Sku审核")
    @GetMapping("check/{skuId}/{status}")
    public Result checkStatus(@PathVariable("skuId") Long skuId,
                              @PathVariable("status") Integer status) {
        skuInfoService.checkStatus(skuId, status);
        return Result.ok();
    }

    @ApiOperation("商品上架功能")
    @GetMapping("publish/{skuId}/{status}")
    public Result publishSku(@PathVariable("skuId") Long skuId,
                             @PathVariable("status") Integer status) {
        skuInfoService.publishSku(skuId, status);
        return Result.ok();
    }

    @ApiOperation("商品新人专享")
    @GetMapping("isNewPerson/{skuId}/{status}")
    public Result isNewPerson(@PathVariable("skuId") Long skuId,
                              @PathVariable("status") Integer status) {
        skuInfoService.isNerPerson(skuId,status);
        return Result.ok();
    }

}
