package com.springleaf.cloud.discovery.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/6 17:07
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DemoApplicationA1_weight {

    public static void main(String[] args) {


        System.setProperty("spring.profiles.active", "a1-weight");

        SpringApplication.run(DemoApplicationA1_weight.class, args);
    }
}
