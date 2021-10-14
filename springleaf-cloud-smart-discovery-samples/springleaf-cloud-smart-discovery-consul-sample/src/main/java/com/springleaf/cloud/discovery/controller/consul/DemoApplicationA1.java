package com.springleaf.cloud.discovery.controller.consul;

import com.springleaf.cloud.discovery.EnableSmartDiscoveryClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/6 17:07
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableSmartDiscoveryClient
public class DemoApplicationA1 {

    public static void main(String[] args) {

        System.setProperty("spring.profiles.active", "a1");

        SpringApplication.run(DemoApplicationA1.class, args);
    }


    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(){
        return new RestTemplate();
    }



}
