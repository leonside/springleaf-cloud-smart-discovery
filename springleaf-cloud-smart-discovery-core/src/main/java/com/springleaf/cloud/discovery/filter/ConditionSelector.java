package com.springleaf.cloud.discovery.filter;

import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.Optional;

/**
 *
 * Condition selector, select the single service based on rule conditions
 *
 * It is applied on weight routing
 *
 * @see {@link WeightLoadBalance}
 *
 * @author leon
 */
public interface ConditionSelector extends Filter, Configurable {

    /**
     * choose the single service
     * @param context
     * @param servers
     * @return
     */
    Optional<Server> choose(FilterContext context, List<? extends Server> servers);

}
