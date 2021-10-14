package com.springleaf.cloud.discovery.filter;

import com.netflix.loadbalancer.Server;

/**
 * Weighted routing loadbalancer,
 * The weight routing takes effect only for the configuration with the highest priority
 * see{@link com.springleaf.cloud.discovery.filter.router.ConfigurableWeightLoadBalance}
 * @author leon
 */
public interface WeightLoadBalance extends ConditionSelector {

    /**
     * Determines whether the weight rule is included based on the specified server
     *
     * @param context
     * @param server
     * @return
     */
    boolean containWeightRule(FilterContext context, Server server);
}
