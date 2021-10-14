/*
 * Copyright 1999-2012 Alibaba Group.
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
import com.springleaf.cloud.discovery.config.model.RouterRule;
import com.springleaf.cloud.discovery.exception.DiscoveryException;
import com.springleaf.cloud.discovery.utils.NetUtils;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Get ConditionRouter with {@link IConditionFactory}
 *
 * @author leon
 */
public class ConditionRouter extends AbstractConditionSupport implements IRouter, Comparable<IRouter> {

    private static final Logger logger = LoggerFactory.getLogger(ConditionRouter.class);

    protected RouterRule routerRule;

    protected int priority;

    protected boolean force = false;

    public ConditionRouter(RouterRule routerRule) {
        super(routerRule.getConditions());
        this.routerRule = routerRule;
        this.priority = routerRule.getPriority();
        this.force = routerRule.isForce();
    }

    @Override
    public  List<? extends Server> route(List<? extends Server> servers, FilterableRegistration registrationServer) throws DiscoveryException {
        if (servers == null || servers.size() == 0) {
            return servers;
        }
        try {
            if (! matchWhen(registrationServer)) {
                return servers;
            }
            List<Server> result = new ArrayList<Server>();
            if (thenCondition == null) {
                logger.warn("The current consumer in the service blacklist. consumer: " + NetUtils.localIP() + ", service: " + registrationServer.getServiceId());
                return result;
            }
            for (Server server : servers) {
                if (matchThen(registrationServer, server)) {
                    result.add(server);
                }
            }
            if (result.size() > 0) {
                return result;
            } else if (force) {
                logger.warn("The route result is empty and force execute. consumer: " + NetUtils.localIP() + ", service: " + registrationServer.getServiceId() + ", router: " + routerRule.getConditions());
                return result;
            }
        } catch (Throwable t) {
            logger.error("Failed to execute condition router rule: " + routerRule.getConditions() + ", invokers: " + servers + ", cause: " + t.getMessage(), t);
        }
        return servers;
    }

    public RouterRule getRouterRule() {
        return routerRule;
    }

    @Override
    public int compareTo(IRouter o) {
        if (o == null || o.getClass() != ConditionRouter.class) {
            return 1;
        }
        ConditionRouter c = (ConditionRouter) o;
        return this.priority == c.priority ? routerRule.getConditions().compareTo(c.getRouterRule().getConditions()) : (this.priority > c.priority ? 1 : -1);
    }

}