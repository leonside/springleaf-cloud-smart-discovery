//package com.springleaf.cloud.discovery.controller.ext;
//
//import com.netflix.loadbalancer.Server;
//import com.springleaf.cloud.discovery.base.DiscoveryProperties;
//import com.springleaf.cloud.discovery.base.FilterableRegistration;
//import com.springleaf.cloud.discovery.config.cache.RuleCacher;
//import com.springleaf.cloud.discovery.config.model.BaseRule;
//import com.springleaf.cloud.discovery.config.model.RegisterRule;
//import com.springleaf.cloud.discovery.filter.FilterContext;
//import com.springleaf.cloud.discovery.filter.RegisterConditionPredicate;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
///**
// * @author leon
// */
////@Component
//public class CustomRegisterConditionPredicate implements RegisterConditionPredicate {
//
//    /**
//     *
//     * @param context 过滤执行上下文，包含 {@link FilterableRegistration}、{@link RuleCacher}、{@link DiscoveryProperties}
//     * @param server 需要校验的Server
//     * @return 是否合法
//     */
//    @Override
//    public boolean apply(FilterContext context, Server server) {
//        //获取配置规则
//        List<RegisterRule> registerRules = getConfigRules(context);
//
//        return registerRules.stream().allMatch(registerRule->{
//            // TODO: 实现registerRule规则校验逻辑
//
//            return true;
//        });
//
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
