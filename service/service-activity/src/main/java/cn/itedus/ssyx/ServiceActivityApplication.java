package cn.itedus.ssyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-15 14:47
 * @description: 营销活动板块启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceActivityApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceActivityApplication.class, args);
    }
}
