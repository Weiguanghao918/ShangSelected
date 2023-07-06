package cn.itedus.ssyx.order.controller;

import cn.itedus.ssyx.common.auth.AuthContextHolder;
import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.model.order.OrderInfo;
import cn.itedus.ssyx.order.service.OrderInfoService;
import cn.itedus.ssyx.vo.order.OrderConfirmVo;
import cn.itedus.ssyx.vo.order.OrderSubmitVo;
import cn.itedus.ssyx.vo.order.OrderUserQueryVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author: Guanghao Wei
 * @date: 2023-07-03 15:19
 * @description: 订单服务接口
 */
@Api(tags = "订单管理接口")
@RestController
@RequestMapping("/api/order")
public class OrderApiController {

    @Autowired
    private OrderInfoService orderInfoService;

    @ApiOperation("订单确认接口")
    @GetMapping("auth/confirmOrder")
    public Result confirmOrder() {
        OrderConfirmVo orderConfirmVo = orderInfoService.confirmOrder();
        return Result.ok(orderConfirmVo);
    }


    @ApiOperation("生成订单接口")
    @PostMapping("auth/submitOrder")
    public Result submitOrder(@RequestBody OrderSubmitVo orderSubmitVo) {
        Long orderId = orderInfoService.submitOrder(orderSubmitVo);
        return Result.ok(orderId);

    }

    @ApiOperation("订单详情接口")
    @GetMapping("auth/getOrderInfoById/{orderId}")
    public Result getOrderInfoById(@PathVariable("orderId") Long orderId) {
        OrderInfo orderInfo = orderInfoService.getOrderInfoById(orderId);
        return Result.ok(orderInfo);
    }

    @ApiOperation("根据订单编号获取订单项")
    @GetMapping("inner/getOrderInfoByOrderNo/{orderNo}")
    public OrderInfo getOrderInfoByOrderNo(@PathVariable("orderNo") String orderNo) {
        return orderInfoService.getOrderInfoByOrderNo(orderNo);
    }

    @ApiOperation("获取用户订单分页列表")
    @GetMapping("auth/findUserOrderPage/{page}/{limit}")
    public Result findUserOrderPage(@PathVariable("page") Long page,
                                    @PathVariable("limit") Long limit,
                                    OrderUserQueryVo orderUserQueryVo) {
        Long userId = AuthContextHolder.getUserId();
        orderUserQueryVo.setUserId(userId);
        Page<OrderInfo> pageModel = new Page<>(page, limit);
        IPage<OrderInfo> iPage = orderInfoService.findUserOrderPage(pageModel, orderUserQueryVo);
        return Result.ok(iPage);
    }

}
