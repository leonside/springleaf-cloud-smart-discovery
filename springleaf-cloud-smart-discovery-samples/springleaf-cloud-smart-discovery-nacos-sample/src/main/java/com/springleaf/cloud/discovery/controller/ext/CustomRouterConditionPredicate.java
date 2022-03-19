//package com.springleaf.cloud.discovery.controller.ext;
//
//import com.netflix.loadbalancer.Server;
//import com.springleaf.cloud.discovery.base.DiscoveryProperties;
//import com.springleaf.cloud.discovery.base.FilterableRegistration;
//import com.springleaf.cloud.discovery.config.cache.RuleCacher;
//import com.springleaf.cloud.discovery.config.model.RegisterRule;
//import com.springleaf.cloud.discovery.config.model.RouterRule;
//import com.springleaf.cloud.discovery.filter.FilterContext;
//import com.springleaf.cloud.discovery.filter.RouterConditionPredicate;
//
//import java.util.List;
//
///**
// * @author leon
// */
//public class CustomRouterConditionPredicate implements RouterConditionPredicate {
//
//    /**
//     * 判断是否匹配
//     * @param context 过滤执行上下文，包含 {@link FilterableRegistration}、{@link RuleCacher}、{@link DiscoveryProperties}
//     * @param server 服务提供方Server
//     * @return 是否匹配
//     */
//    @Override
//    public boolean apply(FilterContext context, Server server) {
//
//        //获取配置规则
//        List<RouterRule> routerRules = getConfigRules(context);
//
//        return routerRules.stream().allMatch(routerRule->{
//            // TODO: 实现routerRule规则校验逻辑
//
//            return true;
//        });
//
//    }
//
//
//    /**
//     * 优先级，默认0
//     * @return
//     */
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}
