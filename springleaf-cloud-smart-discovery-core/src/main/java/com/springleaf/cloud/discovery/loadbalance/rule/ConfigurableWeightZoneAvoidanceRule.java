package com.springleaf.cloud.discovery.loadbalance.rule;

import com.springleaf.cloud.discovery.filter.FilterContext;
import com.springleaf.cloud.discovery.filter.WeightLoadBalance;
import com.netflix.loadbalancer.Server;
import com.netflix.loadbalancer.ZoneAvoidanceRule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 *
 * Expand ZoneAvoidanceRule to support weighted routes
 *
 * @author leon
 */
public class ConfigurableWeightZoneAvoidanceRule extends ZoneAvoidanceRule {

    private static final Logger logger = LoggerFactory.getLogger(ConfigurableWeightZoneAvoidanceRule.class);

    protected FilterContext filterContext;

    private WeightLoadBalance weightLoadBalance;

    public ConfigurableWeightZoneAvoidanceRule() {
        super();
    }

    public void setFilterContext(FilterContext filterContext) {
        this.filterContext = filterContext;
    }

    public void setWeightLoadBalance(WeightLoadBalance weightLoadBalance) {
        this.weightLoadBalance = weightLoadBalance;
    }

    @Override
    public Server choose(Object key) {

        boolean withWeightRule = usingWeightLoadbalance(key);

        if(!withWeightRule){
            return super.choose(key);
        }

        List<Server> eligibleServers = getPredicate().getEligibleServers(getLoadBalancer().getAllServers(), key);

        Optional<Server> serverOptional = weightLoadBalance.choose(filterContext, eligibleServers);

        Server server = serverOptional.isPresent() ? (Server) serverOptional.get() : null;

        logger.debug("[Weight Choose] Enable the weighted load balancing policy, eligible Servers is {}.the weight random result of service is: {}", eligibleServers,(server != null ? server.getHostPort() : null));

        return server;
    }

    private boolean usingWeightLoadbalance(Object key) {

        if (weightLoadBalance == null) {
            return false;
        }

        List<Server> allServers = getLoadBalancer().getAllServers();

        if(CollectionUtils.isEmpty(allServers) || allServers.size() <= 1){
            return false;
        }

        return weightLoadBalance.containWeightRule(filterContext, allServers.get(0));
    }

}
