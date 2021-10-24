package com.springleaf.cloud.discovery.condition;

import com.springleaf.cloud.discovery.base.FilterableRegistration;
import com.springleaf.cloud.discovery.exception.DiscoveryException;
import com.netflix.loadbalancer.Server;

import java.util.List;

/**
 *
 * Conditional routing rules, such as service routing and service discovery scenarios,for example:
 *  host=192.168.* & region=3502 => host=192.168.10.1
 * @author leon
 */
public interface IRouter extends Comparable<IRouter> {

    /**
     * route.
     */
    List<? extends Server> route(List<? extends Server> targetServers, FilterableRegistration registrationServer) throws DiscoveryException;

}