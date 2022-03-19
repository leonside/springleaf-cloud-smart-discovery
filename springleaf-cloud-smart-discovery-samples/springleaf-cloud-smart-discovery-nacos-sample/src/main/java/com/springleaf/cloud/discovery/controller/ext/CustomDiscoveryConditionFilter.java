//package com.springleaf.cloud.discovery.controller.ext;
//
//import com.netflix.loadbalancer.Server;
//import com.springleaf.cloud.discovery.base.DiscoveryProperties;
//import com.springleaf.cloud.discovery.base.FilterableRegistration;
//import com.springleaf.cloud.discovery.config.cache.RuleCacher;
//import com.springleaf.cloud.discovery.config.model.RouterRule;
//import com.springleaf.cloud.discovery.filter.DiscoveryConditionFilter;
//import com.springleaf.cloud.discovery.filter.FilterContext;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Optional;
//
///**
// * @author leon
// */
//@Component
//public class CustomDiscoveryConditionFilter implements DiscoveryConditionFilter {
//
//
//    /**
//     * 过滤服务提供方列表
//     * @param context 过滤执行上下文，包含 {@link FilterableRegistration}、{@link RuleCacher}、{@link DiscoveryProperties}
//     * @param servers 服务提供方列表集合
//     * @return 返回筛选后的服务列表
//     */
//    @Override
//    public Optional<List<Server>> doFilter(FilterContext context, List<? extends Server> servers) {
//
//        List<RouterRule> discoveryRule = getConfigRules(context);
//
//        // TODO: 根据规则进行servers列表进行过滤
//        List filterRouters = servers;
//
//        return Optional.of(filterRouters);
//    }
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
