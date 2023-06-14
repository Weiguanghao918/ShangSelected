package cn.itedus.ssyx.mq.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 17:39
 * @description: 消息的确认机制
 */
@Component
public class MQProducerAckConfig implements RabbitTemplate.ReturnCallback, RabbitTemplate.ConfirmCallback {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    private Logger logger = LoggerFactory.getLogger(MQProducerAckConfig.class);

    //如果发送消息使用的是private RabbitTemplate rabbitTemplate;对象，没有设置的话，当前的rabbitTemplate与当前的配置类没有任何关系
    @PostConstruct
    public void init() {
        rabbitTemplate.setReturnCallback(this);
        rabbitTemplate.setConfirmCallback(this);
    }

    /**
     * 表示消息是否正确发送到了交换机上
     *
     * @param correlationData 消息载体
     * @param ack             判断是否发送到交换机上
     * @param cause           原因
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        if (ack) {
            logger.info("消息发送成功");
        } else {
            logger.info("消息发送失败！原因是：{}", cause);
        }
    }

    /**
     * 消息如果没有正确发送到队列中，则会走这个方法！如果消息被正常处理，则这个方法不会走！
     *
     * @param message    消息主题
     * @param replyCode  应答码
     * @param replyText  描述
     * @param exchange   交换机
     * @param routingKey 路由键
     */
    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        logger.info("消息主体是：{}", new String(message.getBody()));
        logger.info("应答码是：{}", replyCode);
        logger.info("描述：{}", replyText);
        logger.info("消息使用的交换机exchange是：{}", exchange);
        logger.info("消息使用的路由键routingKey是：{}", routingKey);
    }
}
