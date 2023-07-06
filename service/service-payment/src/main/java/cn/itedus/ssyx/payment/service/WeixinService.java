package cn.itedus.ssyx.payment.service;

import cn.itedus.ssyx.enums.PaymentType;

import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-07-06 13:46
 * @description: 微信支付服务接口
 */
public interface WeixinService {
    /**
     * 创建下单支付
     *
     * @param orderNo 订单编号
     * @return map结果集
     */
    Map<String, String> createJsapi(String orderNo);

    /**
     * 查询订单支付状态
     *
     * @param orderNo     订单编号
     * @param paymentType 支付类型
     * @return 结果集合
     */
    Map<String, String> queryPayStatus(String orderNo, PaymentType paymentType);
}
