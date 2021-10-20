package com.springleaf.cloud.discovery.filter;

import com.netflix.loadbalancer.Server;

import java.util.List;
import java.util.Optional;

/**
 * Condition Filter， filters services based on rule conditions
 *
 * It is applied when a service is Discovery
 *
 * see{@link com.springleaf.cloud.discovery.filter.serverlist.LoadBalanceServerListConditionFilter}
 * see{@link com.springleaf.cloud.discovery.filter.serverlist.GroupIsolationServerListConditionFilter}
 *
 * @author leon
 */
public interface ConditionFilter extends Filter{

    /**
     * filters services
     * @param context
     * @param servers
     * @return
     */
    Optional<List<Server>> doFilter(FilterContext context, List<? extends Server> servers);

}
