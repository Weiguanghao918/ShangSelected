package cn.itedus.ssyx.payment.service;

import cn.itedus.ssyx.enums.PaymentType;
import cn.itedus.ssyx.model.order.PaymentInfo;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-07-06 13:48
 * @description: 下单详情服务接口
 */
public interface PaymentInfoService extends IService<PaymentInfo> {
    //查询交易记录
    PaymentInfo getPaymentInfo(String orderNo, PaymentType paymentType);

    //保存交易记录
    PaymentInfo savePaymentInfo(String orderNo, PaymentType paymentType);

    //修改支付状态
    void paySuccess(String outTradeNo, PaymentType paymentType, Map<String, String> resultMap);
}
