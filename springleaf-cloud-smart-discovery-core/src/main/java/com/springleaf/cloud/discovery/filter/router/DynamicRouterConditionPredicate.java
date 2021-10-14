package com.springleaf.cloud.discovery.filter.router;

import com.springleaf.cloud.discovery.base.RuleType;
import com.springleaf.cloud.discovery.condition.ConditionFactoryBuilder;
import com.springleaf.cloud.discovery.condition.IConditionFactory;
import com.springleaf.cloud.discovery.condition.IMatcher;
import com.springleaf.cloud.discovery.config.model.BaseRule;
import com.springleaf.cloud.discovery.config.model.RouterRule;
import com.springleaf.cloud.discovery.filter.ConditionPredicate;
import com.springleaf.cloud.discovery.filter.FilterContext;
import com.springleaf.cloud.discovery.utils.URLMatcher;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/21 16:10
 */
public class DynamicRouterConditionPredicate implements ConditionPredicate {

    public static final Logger logger = LoggerFactory.getLogger(DynamicRouterConditionPredicate.class);
    @Autowired
    private ConditionFactoryBuilder conditionFactoryBuilder;

    @Override
    public boolean apply(FilterContext context, Server server) {

        List<? extends BaseRule> allRouters = getConfigRules(context);

        if(CollectionUtils.isEmpty(allRouters)){
            return true;
        }

        for(BaseRule routerRule : allRouters){

            //当path不为空则先进行路径匹配
            if(URLMatcher.isMatchPath(getRequest(), ((RouterRule)routerRule).getPath())){

                IMatcher matcher = conditionFactoryBuilder.getFactory(routerRule).getMatcher(routerRule);

                boolean flag = matcher.isMatch(server, context.getFilterableRegistration());

                logger.debug("[Router Predicate]The server {} uses routing rule [{}] to match, The server provider {} match result is: {}",context.getFilterableRegistration().getServiceId(),routerRule.getConditions(),server.getHostPort(),flag);

                if(!flag){
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public RuleType support() {
        //todo
        return RuleType.ROUTER;
    }

    private HttpServletRequest getRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attr.getRequest();
    }
}
