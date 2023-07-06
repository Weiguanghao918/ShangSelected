package cn.itedus.ssyx.payment.controller;

import cn.itedus.ssyx.common.result.Result;
import cn.itedus.ssyx.common.result.ResultCodeEnum;
import cn.itedus.ssyx.enums.PaymentType;
import cn.itedus.ssyx.payment.service.PaymentInfoService;
import cn.itedus.ssyx.payment.service.WeixinService;
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
 * @date: 2023-07-06 13:46
 * @description: 微信支付接口
 */
@Api(tags = "微信支付接口")
@RestController
@RequestMapping("/api/payment/weixin")
public class WeixinController {

    @Autowired
    private WeixinService weixinService;

    @Autowired
    private PaymentInfoService paymentInfoService;


    @ApiOperation("下单小程序支付")
    @GetMapping("/createJsapi/{orderNo}")
    public Result createJsapi(@PathVariable("orderNo") String orderNo) {
        Map<String, String> result = weixinService.createJsapi(orderNo);
        return Result.ok(result);
    }

    @ApiOperation("查询微信支付状态")
    @GetMapping("queryPayStatus/{orderNo}")
    public Result queryPayStatus(@PathVariable("orderNo") String orderNo) {
        Map<String, String> resultMap = weixinService.queryPayStatus(orderNo, PaymentType.WEIXIN);
        if (resultMap == null) {
            return Result.build(null, ResultCodeEnum.PAYMENT_ERROR);
        }
        if ("SUCCESS".equals(resultMap.get("trade_state"))) {//如果成功
            //更改订单状态，处理支付结果
            String out_trade_no = resultMap.get("out_trade_no");
            paymentInfoService.paySuccess(out_trade_no, PaymentType.WEIXIN, resultMap);
            return Result.build(null, ResultCodeEnum.PAYMENT_SUCCESS);
        }
        return Result.build(null, ResultCodeEnum.PAYMENT_DOING);
    }
}
