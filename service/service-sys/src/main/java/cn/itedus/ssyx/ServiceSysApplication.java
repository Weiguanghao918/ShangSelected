package cn.itedus.ssyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @author: Guanghao Wei
 * @date: 2023-06-09 15:50
 * @description: ServiceSysApplication启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceSysApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceSysApplication.class, args);
    }
}
