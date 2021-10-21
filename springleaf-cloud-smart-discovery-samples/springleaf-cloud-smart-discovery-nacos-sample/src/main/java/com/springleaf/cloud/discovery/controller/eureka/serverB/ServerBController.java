package com.springleaf.cloud.discovery.controller.eureka.serverB;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/6 17:11
 */
@RestController()
public class ServerBController {

    @Value("${server.port}")
    private int serverPort;

    @RequestMapping(path = "/demo-b/echo", method = RequestMethod.GET)
    public String echo(@RequestParam("input") String input) throws UnknownHostException {

        System.out.println("serverB say: " + input);

        return "serverB[" + InetAddress.getLocalHost().getHostAddress() + ":" + serverPort  + "] say: " + input;
    }


}
