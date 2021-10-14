package com.springleaf.cloud.discovery.controller.consul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/6 17:08
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DemoApplicationB2 {

    public static void main(String[] args) {

        System.setProperty("spring.profiles.active", "b2");

        SpringApplication.run(DemoApplicationB2.class, args);
    }

}
