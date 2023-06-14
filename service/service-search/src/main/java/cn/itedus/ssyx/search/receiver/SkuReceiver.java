package cn.itedus.ssyx.search.receiver;

import cn.itedus.ssyx.mq.constant.MqConst;
import cn.itedus.ssyx.search.service.SkuService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 17:56
 * @description: 接受MQ消息接口
 */
@Component
public class SkuReceiver {
    @Autowired
    private SkuService skuService;

    /**
     * 商品上架
     *
     * @param skuId   Sku id
     * @param message 消息体
     * @param channel 频道
     * @throws IOException 异常
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_GOODS_UPPER, durable = "true"),
            exchange = @Exchange(value = MqConst.EXCHANGE_GOODS_DIRECT),
            key = {MqConst.ROUTING_GOODS_UPPER}
    ))
    public void upperSku(Long skuId, Message message, Channel channel) throws IOException {
        if (null != skuId) {
            skuService.upperSku(skuId);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }

    /**
     * 商品下架
     *
     * @param skuId   Sku id
     * @param message 消息体
     * @param channel 频道
     * @throws IOException 异常
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(value = MqConst.QUEUE_GOODS_LOWER),
            exchange = @Exchange(value = MqConst.EXCHANGE_GOODS_DIRECT),
            key = {MqConst.ROUTING_GOODS_LOWER}
    ))
    public void lowerSku(Long skuId, Message message, Channel channel) throws IOException {
        if (null != skuId) {
            skuService.lowerSku(skuId);
        }
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }
}
