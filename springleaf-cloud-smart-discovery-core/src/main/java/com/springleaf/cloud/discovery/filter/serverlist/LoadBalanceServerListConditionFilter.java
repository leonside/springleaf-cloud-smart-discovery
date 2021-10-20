package com.springleaf.cloud.discovery.filter.serverlist;

import com.springleaf.cloud.discovery.base.RuleType;
import com.springleaf.cloud.discovery.condition.ConditionFactoryBuilder;
import com.springleaf.cloud.discovery.condition.IConditionFactory;
import com.springleaf.cloud.discovery.condition.IRouter;
import com.springleaf.cloud.discovery.config.model.BaseRule;
import com.springleaf.cloud.discovery.config.model.RouterRule;
import com.springleaf.cloud.discovery.filter.ConditionFilter;
import com.springleaf.cloud.discovery.filter.DiscoveryConditionFilter;
import com.springleaf.cloud.discovery.filter.FilterContext;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/16 16:00
 */
@Order
public class LoadBalanceServerListConditionFilter implements DiscoveryConditionFilter {

    private static final Logger logger = LoggerFactory.getLogger(LoadBalanceServerListConditionFilter.class);

    @Autowired
    private ConditionFactoryBuilder conditionFactoryBuilder;

    @Override
    public Optional<List<Server>> doFilter(FilterContext context, List<? extends Server> servers) {

        List<? extends BaseRule> allRouters = getConfigRules(context);

        if(CollectionUtils.isEmpty(allRouters)){
            return Optional.empty();
        }

        List filterRouters = servers;

        for(BaseRule routerRule : allRouters){
            IRouter router = conditionFactoryBuilder.getFactory(routerRule).getRouter((RouterRule) routerRule);
            filterRouters = router.route(filterRouters, context.getFilterableRegistration());
        }

        if(logger.isDebugEnabled()){
            logger.debug("[Discovery filtering] List of Servers for {} obtained from Discovery client: {} ,The service before filtering is :{}",context.getFilterableRegistration().getServiceId(),filterRouters, servers);
        }

        return Optional.of(filterRouters);
    }

}
