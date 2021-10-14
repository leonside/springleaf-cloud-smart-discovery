package com.springleaf.cloud.discovery.configuration;


import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.filter.FilterChain;
import com.springleaf.cloud.discovery.filter.FilterContext;
import com.springleaf.cloud.discovery.filter.WeightLoadBalance;
import com.springleaf.cloud.discovery.loadbalance.rule.ConfigurableWeightZoneAvoidanceRule;
import com.springleaf.cloud.discovery.loadbalance.rule.DynamicRouteZoneAvoidancePredicate;
import com.springleaf.cloud.discovery.loadbalance.rule.DynamicRouteZoneAvoidanceRule;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.cloud.netflix.ribbon.RibbonClientConfiguration;
import org.springframework.cloud.netflix.ribbon.RibbonClientName;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author  leon
 */
@AutoConfigureAfter(RibbonClientConfiguration.class)
public class DiscoveryRibbonClientConfiguration {
    @RibbonClientName
    private String serviceId = "client";

    @Autowired
    private PropertiesFactory propertiesFactory;

    @Autowired
    private DiscoveryProperties discoveryProperties;
    @Autowired(required = false)
    private WeightLoadBalance weightLoadBalance;
    @Autowired
    private FilterChain filterChain;
    @Autowired
    private FilterContext filterContext;

    @Bean
    @ConditionalOnMissingBean
    public IRule ribbonRule(IClientConfig config) {
        if (this.propertiesFactory.isSet(IRule.class, serviceId)) {
            return this.propertiesFactory.get(IRule.class, config, serviceId);
        }

        if(discoveryProperties.isRoutersEnabled()){
            DynamicRouteZoneAvoidanceRule routeZoneAvoidanceRule = new DynamicRouteZoneAvoidanceRule();
            routeZoneAvoidanceRule.initWithNiwsConfig(config);
            routeZoneAvoidanceRule.setFilterChain(filterChain);
            routeZoneAvoidanceRule.setFilterContext(filterContext);
            routeZoneAvoidanceRule.setWeightLoadBalance(weightLoadBalance);

            DynamicRouteZoneAvoidancePredicate dynamicRouteZoneAvoidancePredicate = routeZoneAvoidanceRule.getDynamicRouteZoneAvoidancePredicate();
            dynamicRouteZoneAvoidancePredicate.setFilterChain(filterChain);

            return routeZoneAvoidanceRule;
        }else{
            ConfigurableWeightZoneAvoidanceRule rule = new ConfigurableWeightZoneAvoidanceRule();
            rule.setWeightLoadBalance(weightLoadBalance);
            rule.initWithNiwsConfig(config);
            rule.setFilterContext(filterContext);
            return rule;
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public ILoadBalancer ribbonLoadBalancer(IClientConfig config, ServerList<Server> serverList, ServerListFilter<Server> serverListFilter, IRule rule, IPing ping, ServerListUpdater serverListUpdater) {
        if (this.propertiesFactory.isSet(ILoadBalancer.class, serviceId)) {
            return this.propertiesFactory.get(ILoadBalancer.class, config, serviceId);
        }

        ZoneAwareLoadBalancer<?> loadBalancer = new ZoneAwareLoadBalancer<>(config, rule, ping, serverList, serverListFilter, serverListUpdater);

        return loadBalancer;
    }

}