package cn.itedus.ssyx.cart.controller;

import cn.itedus.ssyx.cart.service.CartInfoService;
import cn.itedus.ssyx.common.auth.AuthContextHolder;
import cn.itedus.ssyx.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Results;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-26 13:55
 * @description: 购物车接口管理
 */
@Api(tags = "购物车管理接口")
@RestController
@RequestMapping("/api/cart")
public class CartInfoController {
    @Autowired
    private CartInfoService cartInfoService;

    @ApiOperation("添加购物车")
    @GetMapping("addToCart/{skuId}/{skuNum}")
    public Result addToCart(@PathVariable("skuId") Long skuId,
                            @PathVariable("skuNum") Integer skuNum) {
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.addToCart(userId, skuId, skuNum);
        return Result.ok();
    }

    @ApiOperation("删除购物车项")
    @DeleteMapping("deleteCart/{skuId}")
    public Result deleteCart(@PathVariable("skuId") Long skuId) {
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteCart(userId, skuId);
        return Result.ok();
    }

    @ApiOperation("清空购物车")
    @DeleteMapping("deleteAllCart")
    public Result deleteAllCart() {
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.deleteAllCart(userId);
        return Result.ok();
    }

    @ApiOperation("批量删除购物车")
    @DeleteMapping("batchDeleteCart")
    public Result batchDeleteCart(@RequestBody List<Long> skuIdList) {
        Long userId = AuthContextHolder.getUserId();
        cartInfoService.batchDeleteCart(userId, skuIdList);
        return Result.ok();
    }


}
