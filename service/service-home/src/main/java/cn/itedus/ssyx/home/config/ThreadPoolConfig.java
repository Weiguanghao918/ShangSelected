package cn.itedus.ssyx.home.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-25 16:19
 * @description: 线程池配置类
 */
@Configuration
public class ThreadPoolConfig {

    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                3,
                5,
                2,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(3),
                Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        return executor;
    }
}
