package com.springleaf.cloud.discovery.loadbalance.rule;

import com.springleaf.cloud.discovery.filter.FilterChain;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;

/**
 *
 * Extended ZoneAvoidancePredicate to support dynamic router
 *
 * @author leon
 */
public class DynamicRouteZoneAvoidancePredicate extends ZoneAvoidancePredicate {

    private FilterChain filterChain;

    public DynamicRouteZoneAvoidancePredicate(IRule rule, IClientConfig clientConfig) {
        super(rule, clientConfig);
    }

    public DynamicRouteZoneAvoidancePredicate(LoadBalancerStats lbStats, IClientConfig clientConfig) {
        super(lbStats, clientConfig);
    }

    public DynamicRouteZoneAvoidancePredicate(IRule rule) {
        super(rule,null);
    }

    @Override
    public boolean apply(PredicateKey input) {
        if (!super.apply(input)) {
            return false;
        }

        return apply(input.getServer());
    }

    public void setFilterChain(FilterChain filterChain) {
        this.filterChain = filterChain;
    }

    protected boolean apply(Server server) {
        return filterChain.onRouterPredicate(server);
    }
}
