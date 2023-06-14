package cn.itedus.ssyx;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;


/**
 * @author: Guanghao Wei
 * @date: 2023-06-08 15:51
 * @description: ServiceAclApplication启动类
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ServiceAclApplication {
    public static void main(String[] args) {
        SpringApplication.run(ServiceAclApplication.class, args);
    }
}
