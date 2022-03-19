//package com.springleaf.cloud.discovery.controller.ext;
//
//import com.springleaf.cloud.discovery.condition.IConditionFactory;
//import com.springleaf.cloud.discovery.condition.IMatcher;
//import com.springleaf.cloud.discovery.condition.IRouter;
//import com.springleaf.cloud.discovery.config.model.BaseRule;
//import com.springleaf.cloud.discovery.config.model.RouterRule;
//
///**
// * @author leon
// */
//public class CustomConditionFactory implements IConditionFactory {
//
//    /**
//     * 支持的条件表达式语言，见规则配置的language属性，框架默认值为simpleCondition
//     * @return
//     */
//    @Override
//    public String support() {
//        return "groovyCondition";
//    }
//
//    /**
//     * 实现自定义的IRouter条件路由实现：GroovyConditionRouter
//     * @param routerEntity
//     * @return
//     */
//    @Override
//    public IRouter getRouter(RouterRule routerEntity) {
//        return new GroovyConditionRouter();
//    }
//
//    /**
//     * 实现自定义的IMatcher条件匹配实现：GroovyConditionMatcher
//     * @param baseRule
//     * @return
//     */
//    @Override
//    public IMatcher getMatcher(BaseRule baseRule) {
//        return new GroovyConditionMatcher();
//    }
//}
