package cn.itedus.ssyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-19 18:52
 * @description: 小程序主页启动类
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EnableDiscoveryClient
@EnableFeignClients
public class ServiceHomeApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceHomeApplication.class, args);
    }
}
