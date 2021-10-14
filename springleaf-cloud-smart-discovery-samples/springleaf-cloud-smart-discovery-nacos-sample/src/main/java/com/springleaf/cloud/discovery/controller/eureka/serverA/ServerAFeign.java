package com.springleaf.cloud.discovery.controller.eureka.serverA;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/6 17:11
 */
@FeignClient(value = "demo-a")
public interface ServerAFeign {


    @RequestMapping(path = "/demo-a/echo", method = RequestMethod.GET)
    String echo(@RequestParam("input") String input);


}
