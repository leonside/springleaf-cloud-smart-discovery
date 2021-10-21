package com.springleaf.cloud.discovery.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/6 17:07
 */
@SpringBootApplication
@EnableDiscoveryClient
public class DemoApplicationA1_groupisolation {

    public static void main(String[] args) {


        System.setProperty("spring.profiles.active", "a1-groupisolation");

        SpringApplication.run(DemoApplicationA1_groupisolation.class, args);
    }
}
