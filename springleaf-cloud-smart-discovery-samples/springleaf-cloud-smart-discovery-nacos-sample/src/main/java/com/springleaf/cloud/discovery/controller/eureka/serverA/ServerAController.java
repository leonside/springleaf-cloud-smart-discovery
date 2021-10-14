package com.springleaf.cloud.discovery.controller.eureka.serverA;

import com.springleaf.cloud.discovery.controller.eureka.serverB.ServiceBFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/6 17:11
 */
@RestController
@RequestMapping("demo-a")
public class ServerAController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private ServiceBFeign serviceBFeign;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(path = "/router/rule1", method = RequestMethod.GET)
    public String rule1(@RequestParam("input") String input){

        return call(input);

    }

    @RequestMapping(path = "/router/rule2", method = RequestMethod.GET)
    public String rule2(@RequestParam("input") String input){

        return call(input);
    }

    @RequestMapping(path = "/router/rule3", method = RequestMethod.GET)
    public String version(@RequestParam("input") String input){

        return call(input);
    }

    private String call(String input) {

        System.out.println("server a say: " + input);

//        String echo = serviceBFeign.echo(input);
        String echo = restTemplate.getForObject("http://demo-b/demo-b/echo?input=" + input, String.class);

        return "serverA say: " + echo;
    }

    @RequestMapping(path = "/discovery", method = RequestMethod.GET)
    public String discovery(@RequestParam("serviceId") String serviceId){

        List<ServiceInstance> instances = discoveryClient.getInstances(serviceId);

        return String.valueOf(instances.size());
    }
}
