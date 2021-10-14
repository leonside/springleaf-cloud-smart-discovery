package com.springleaf.cloud.discovery.filter;

import com.netflix.loadbalancer.Server;

/**
 *
 * Condition predicate, which determines whether the service matching the condition rules.
 *
 * It is applied when a service is registered or routed
 *
 * see{@link com.springleaf.cloud.discovery.filter.register.RegisterConditionPredicate}
 * see{@link com.springleaf.cloud.discovery.filter.router.DynamicRouterConditionPredicate}
 *
 * @author leon
 */
public interface ConditionPredicate extends Filter, Configurable {

    /**
     * server predicate
     * @param context
     * @param server
     * @return
     */
    boolean apply(FilterContext context, Server server);

}
