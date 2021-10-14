package com.springleaf.cloud.discovery.filter;

import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.base.RuleType;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * filter chain delegate.
 *
 * @author leon
 */
public class FilterChainDelegate implements FilterChain, InitializingBean {

    public static final Logger logger = LoggerFactory.getLogger(FilterChainDelegate.class);

    @Autowired
    private DiscoveryProperties filterableProperties;
    @Autowired
    private FilterContext filterContext;
    @Autowired
    private ObjectProvider<ConditionFilter> conditionFilters;
    @Autowired
    private ObjectProvider<ConditionPredicate> conditionPredicates;

    private List<ConditionFilter> orderedConditionFilters;

    private List<ConditionPredicate> orderedRegisterPredicates;

    private List<ConditionPredicate> orderedRouterPredicates;

    @Override
    public void onDiscoveryServerListFilter(List<Server> servers){

        if(!filterableProperties.isRoutersEnabled()){
            logger.debug("did not open routing When pulling the list of services, check the configuration of [springleaf.smart.discovery.router.enabled]");
            return;
        }
        orderedConditionFilters.stream().forEach(filter->{
            Optional<List<Server>> filterServer = filter.doFilter(filterContext, servers);

            if(filterServer.isPresent() && filterServer.get().size() != servers.size()){
                servers.clear();
                filterServer.get().stream().forEach(item->{
                    servers.add(item);
                });
            }
        });
    }

    @Override
    public boolean onRegisterPredicate(Server server){

        if(!filterableProperties.isRegistersEnabled()){
            logger.debug("did not open register check, check the configuration of [springleaf.smart.discovery.register.enabled]");
            return true;
        }

        return orderedRegisterPredicates.stream().allMatch(conditionPredicate -> conditionPredicate.apply(filterContext, server));
    }

    @Override
    public boolean onRouterPredicate(Server server){
        if(!filterableProperties.isRoutersEnabled()){
            logger.debug("did not open routing, check the configuration of [springleaf.smart.discovery.router.enabled]");
            return true;
        }

        return orderedRouterPredicates.stream().allMatch(conditionPredicate -> conditionPredicate.apply(filterContext, server));
    }

    @Override
    public void afterPropertiesSet() {
        if(conditionPredicates != null){
            orderedRegisterPredicates = conditionPredicates.orderedStream().filter(it -> it.support() == RuleType.REGISTER).collect(Collectors.toList());
            orderedRouterPredicates = conditionPredicates.orderedStream().filter(it -> it.support() == RuleType.ROUTER).collect(Collectors.toList());
        }

        if(conditionFilters != null){
            orderedConditionFilters = conditionFilters.orderedStream().filter(it->it.support() == RuleType.DISCOVERY).collect(Collectors.toList());
        }

    }
}
