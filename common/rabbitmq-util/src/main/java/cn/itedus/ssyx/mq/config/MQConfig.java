package cn.itedus.ssyx.mq.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-14 17:37
 * @description: 配置mq消息转换器,默认是字符串转换器
 */
@Configuration
public class MQConfig {
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
