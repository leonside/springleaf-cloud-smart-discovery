package com.springleaf.cloud.discovery.controller.eureka.serverB;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/14 14:52
 */
@FeignClient(name = "demo-b")
public interface ServiceBFeign {

    @RequestMapping(path = "/demo-b/echo", method = RequestMethod.GET)
    String echo(@RequestParam("input") String input);

}
