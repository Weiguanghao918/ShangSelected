package cn.itedus.ssyx.payment.service.impl;

import cn.itedus.ssyx.client.order.OrderFeignClient;
import cn.itedus.ssyx.common.exception.SsyxException;
import cn.itedus.ssyx.common.result.ResultCodeEnum;
import cn.itedus.ssyx.enums.PaymentStatus;
import cn.itedus.ssyx.enums.PaymentType;
import cn.itedus.ssyx.model.order.OrderInfo;
import cn.itedus.ssyx.model.order.PaymentInfo;
import cn.itedus.ssyx.mq.constant.MqConst;
import cn.itedus.ssyx.mq.service.RabbitService;
import cn.itedus.ssyx.payment.mapper.PaymentInfoMapper;
import cn.itedus.ssyx.payment.service.PaymentInfoService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * @author: Guanghao Wei
 * @date: 2023-07-06 13:48
 * @description: 下单详情服务接口实现类
 */
@Service
public class PaymentInfoServiceImpl extends ServiceImpl<PaymentInfoMapper, PaymentInfo> implements PaymentInfoService {

    @Autowired
    private PaymentInfoMapper paymentInfoMapper;

    @Autowired
    private OrderFeignClient orderFeignClient;

    @Autowired
    private RabbitService rabbitService;

    @Override
    public PaymentInfo getPaymentInfo(String orderNo, PaymentType paymentType) {
        PaymentInfo paymentInfo = paymentInfoMapper.selectOne(new LambdaQueryWrapper<PaymentInfo>().eq(PaymentInfo::getOrderNo, orderNo).eq(PaymentInfo::getPaymentType, paymentType));
        return paymentInfo;
    }

    @Override
    public PaymentInfo savePaymentInfo(String orderNo, PaymentType paymentType) {
        OrderInfo order = orderFeignClient.getOrderInfoByOrderNo(orderNo);
        if (order == null) {
            throw new SsyxException(ResultCodeEnum.DATA_ERROR);
        }

        // 保存交易记录
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderId(order.getId());
        paymentInfo.setPaymentType(paymentType);
        paymentInfo.setUserId(order.getUserId());
        paymentInfo.setOrderNo(order.getOrderNo());
        paymentInfo.setPaymentStatus(PaymentStatus.UNPAID);
        String subject = "test";
        paymentInfo.setSubject(subject);
        //paymentInfo.setTotalAmount(order.getTotalAmount());
        paymentInfo.setTotalAmount(new BigDecimal("0.01"));

        paymentInfoMapper.insert(paymentInfo);
        return paymentInfo;

    }

    /**
     * 该方法需要完成三件事：
     * 1.修改支付记录状态
     * 2.修改订单状态
     * 3.扣减库存删除Redis中存储的订单数据
     */
    @Override
    public void paySuccess(String outTradeNo, PaymentType paymentType, Map<String, String> paramMap) {
        PaymentInfo paymentInfo = paymentInfoMapper.selectOne(new LambdaQueryWrapper<PaymentInfo>().eq(PaymentInfo::getOrderNo, outTradeNo).eq(PaymentInfo::getPaymentType, paymentType));
        if (paymentInfo.getPaymentStatus() != PaymentStatus.UNPAID) {
            return;
        }

        PaymentInfo paymentInfoUpd = new PaymentInfo();
        paymentInfoUpd.setPaymentStatus(PaymentStatus.PAID);
        String tradeNo = paymentType == PaymentType.WEIXIN ? paramMap.get("ransaction_id") : paramMap.get("trade_no");
        paymentInfoUpd.setTradeNo(tradeNo);
        paymentInfoUpd.setCallbackTime(new Date());
        paymentInfoUpd.setCallbackContent(paramMap.toString());
        paymentInfoMapper.update(paymentInfoUpd, new LambdaQueryWrapper<PaymentInfo>().eq(PaymentInfo::getOrderNo, outTradeNo));

        rabbitService.sendMessage(MqConst.EXCHANGE_PAY_DIRECT, MqConst.ROUTING_PAY_SUCCESS, outTradeNo);

    }
}
