package cn.itedus.ssyx.mq.service;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 17:33
 * @description:
 */
@Service
public class RabbitService {
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送消息
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息
     * @return true/false
     */
    public boolean sendMessage(String exchange, String routingKey, Object message) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
        return true;
    }

    /**
     * 发送延迟消息的方法
     *
     * @param exchange   交换机
     * @param routingKey 路由键
     * @param message    消息
     * @param delayTime  延迟时间
     * @return true/false
     */
    public boolean sendDelayMessage(String exchange, String routingKey, Object message, int delayTime) {
        rabbitTemplate.convertAndSend(exchange, routingKey, message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置延迟时间
                message.getMessageProperties().setDelay(delayTime * 1000);
                return message;
            }
        });
        return true;
    }


}
