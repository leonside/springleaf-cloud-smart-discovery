package com.springleaf.cloud.discovery.controller.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @author leon
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaServerApplication {

    public static void main(String[] args) {

        System.setProperty("spring.profiles.active", "eureka");

        SpringApplication.run(EurekaServerApplication.class, args);
    }
}
