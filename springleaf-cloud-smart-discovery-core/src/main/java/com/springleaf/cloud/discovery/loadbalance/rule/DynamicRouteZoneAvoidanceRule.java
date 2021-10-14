package com.springleaf.cloud.discovery.loadbalance.rule;

import com.springleaf.cloud.discovery.filter.FilterChain;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.AbstractServerPredicate;
import com.netflix.loadbalancer.AvailabilityPredicate;
import com.netflix.loadbalancer.CompositePredicate;
import com.netflix.loadbalancer.ZoneAvoidancePredicate;

/**
 * Expand {@link ConfigurableWeightZoneAvoidanceRule} and delegate {@link DynamicRouteZoneAvoidancePredicate} to implement dynamic routes
 * @author leon
 */
public class DynamicRouteZoneAvoidanceRule extends ConfigurableWeightZoneAvoidanceRule{

    private CompositePredicate compositePredicate;
    private DynamicRouteZoneAvoidancePredicate dynamicRouteZoneAvoidancePredicate;

    private FilterChain filterChain;

    public DynamicRouteZoneAvoidanceRule() {
        super();
        dynamicRouteZoneAvoidancePredicate = new DynamicRouteZoneAvoidancePredicate(this);
        AvailabilityPredicate availabilityPredicate = new AvailabilityPredicate(this,null);
        compositePredicate = createCompositePredicate(dynamicRouteZoneAvoidancePredicate, availabilityPredicate);
    }

    public void setFilterChain(FilterChain filterChain) {
        this.filterChain = filterChain;
    }

    private CompositePredicate createCompositePredicate(ZoneAvoidancePredicate p1, AvailabilityPredicate p2) {
        return CompositePredicate.withPredicates(p1, p2)
                //todo
//                .addFallbackPredicate(p2)
//                .addFallbackPredicate(AbstractServerPredicate.alwaysTrue())
                .build();
    }

    @Override
    public void initWithNiwsConfig(IClientConfig clientConfig) {
        dynamicRouteZoneAvoidancePredicate = new DynamicRouteZoneAvoidancePredicate(this, clientConfig);
        AvailabilityPredicate availabilityPredicate = new AvailabilityPredicate(this, clientConfig);
        compositePredicate = createCompositePredicate(dynamicRouteZoneAvoidancePredicate, availabilityPredicate);
    }

    @Override
    public AbstractServerPredicate getPredicate() {
        return compositePredicate;
    }

    public DynamicRouteZoneAvoidancePredicate getDynamicRouteZoneAvoidancePredicate() {
        return dynamicRouteZoneAvoidancePredicate;
    }
}
