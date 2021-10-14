package com.springleaf.cloud.discovery.filter.serverlist;

import com.springleaf.cloud.discovery.base.FilterableRegistration;
import com.springleaf.cloud.discovery.base.RuleType;
import com.springleaf.cloud.discovery.condition.ConditionFactoryBuilder;
import com.springleaf.cloud.discovery.condition.IConditionFactory;
import com.springleaf.cloud.discovery.condition.IRouter;
import com.springleaf.cloud.discovery.config.model.BaseRule;
import com.springleaf.cloud.discovery.config.model.RouterRule;
import com.springleaf.cloud.discovery.filter.FilterContext;
import com.springleaf.cloud.discovery.filter.ConditionFilter;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import com.google.common.collect.Lists;
/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/17 15:22
 */
public class GroupIsolationServerListConditionFilter implements ConditionFilter {

    public static final Logger logger = LoggerFactory.getLogger(GroupIsolationServerListConditionFilter.class);

    @Autowired
    private ConditionFactoryBuilder conditionFactoryBuilder;

    @Override
    public Optional<List<Server>> doFilter(FilterContext context, List< ? extends Server> servers) {

        List<? extends BaseRule> routerRules = getConfigRules(context);

        RouterRule routerRule = (RouterRule) routerRules.get(0);

        IRouter router = conditionFactoryBuilder.getFactory(routerRule).getRouter(routerRule);

        List filterRouters = router.route(servers, context.getFilterableRegistration());

        logger.debug("[GroupIsolation filtering] List of GroupServers for {} obtained from Discovery client: {} ,The service before filtering is :{}",context.getFilterableRegistration().getServiceId(),filterRouters, servers);

        return Optional.of(filterRouters);
    }

    @Override
    public List<? extends BaseRule> getConfigRules(FilterContext context) {

        FilterableRegistration filterableRegistration = context.getFilterableRegistration();

        String groupKey = context.getFilterableRegistration().getGroupKey();

        String groupValue = context.getFilterableRegistration().getMetadata(groupKey);

        String value = StringUtils.isEmpty(groupValue) ? "''" : groupValue;

        String conditions = groupKey + " = " + value + "=>" + groupKey + " = " + value;

        RouterRule routerRule = RouterRule.of(filterableRegistration.getServiceId(), conditions);

        return Lists.newArrayList(routerRule);
    }

    @Override
    public RuleType support() {
        return RuleType.DISCOVERY;
    }

}
