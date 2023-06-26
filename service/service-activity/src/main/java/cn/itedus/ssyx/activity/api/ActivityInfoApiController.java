package cn.itedus.ssyx.activity.api;

import cn.itedus.ssyx.activity.service.ActivityInfoService;
import cn.itedus.ssyx.model.order.CartInfo;
import cn.itedus.ssyx.vo.order.OrderConfirmVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-20 15:44
 * @description: 活动内部远程调用接口
 */
@Api("促销与优惠券接口")
@RestController
@RequestMapping("/api/activity")
public class ActivityInfoApiController {
    @Autowired
    private ActivityInfoService activityInfoService;

    @ApiOperation("根据SkuId列表获取促销信息")
    @PostMapping("inner/findActivity")
    public Map<Long, List<String>> findActivity(@RequestBody List<Long> skuIdList) {
        Map<Long, List<String>> map = activityInfoService.findActivity(skuIdList);
        return map;
    }

    @ApiOperation("根据SkuId获取促销与优惠券信息")
    @GetMapping("inner/findActivityAndCoupon/{skuId}/{userId}")
    public Map<String, Object> findActivityAndCoupon(@PathVariable("skuId") Long skuId,
                                                     @PathVariable("userId") Long userId) {
        Map<String, Object> map = activityInfoService.findActivityAndCoupon(skuId, userId);
        return map;
    }

    @ApiOperation(value = "获取购物车满足条件的促销与优惠券信息")
    @PostMapping("inner/findCartActivityAndCoupon/{userId}")
    public OrderConfirmVo findCartActivityAndCoupon(@RequestBody List<CartInfo> cartInfoList, @PathVariable("userId") Long userId, HttpServletRequest request) {
        return activityInfoService.findCartActivityAndCoupon(cartInfoList, userId);
    }

}
