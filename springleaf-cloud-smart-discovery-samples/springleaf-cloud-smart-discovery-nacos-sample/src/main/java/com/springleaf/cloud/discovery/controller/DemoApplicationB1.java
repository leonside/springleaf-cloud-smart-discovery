package com.springleaf.cloud.discovery.controller;

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
public class DemoApplicationB1 {

    public static void main(String[] args) {

        System.setProperty("spring.profiles.active", "b1");

        SpringApplication.run(DemoApplicationB1.class, args);
    }

}
