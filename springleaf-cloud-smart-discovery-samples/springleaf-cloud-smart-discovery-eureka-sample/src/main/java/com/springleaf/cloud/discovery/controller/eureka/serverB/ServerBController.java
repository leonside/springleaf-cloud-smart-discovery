package com.springleaf.cloud.discovery.controller.eureka.serverB;

import org.springframework.web.bind.annotation.*;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/6 17:11
 */
@RestController()
public class ServerBController {


    @RequestMapping(path = "/demo-b/echo", method = RequestMethod.GET)
    public String echo(@RequestParam("input") String input){

        System.out.println("serverB say: " + input);

        return "serverB say: " + input;
    }


}
