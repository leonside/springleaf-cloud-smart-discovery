/*
 * Copyright 1999-2011 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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