package com.springleaf.cloud.discovery.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/6 17:09
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class DemoApplicationC1 {

    public static void main(String[] args) {

        System.setProperty("spring.profiles.active", "c1");

        SpringApplication.run(DemoApplicationA1.class, args);
    }

}
