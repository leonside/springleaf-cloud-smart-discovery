package com.springleaf.cloud.discovery.condition;


import com.springleaf.cloud.discovery.base.SupportedType;
import com.springleaf.cloud.discovery.config.model.BaseRule;
import com.springleaf.cloud.discovery.config.model.RouterRule;

/**
 * Get the IRouter and IMatcher based on the specified rule language，see {@link SimpleConditionFactory}
 * Developers can extend this factory method to implement custom rule language implementations
 *
 * @author leon
 */
public interface IConditionFactory extends SupportedType<String> {
    
    /**
     * Create router with specified rule language
     * 
     */
    IRouter getRouter(RouterRule routerEntity);

    /**
     * create matcher with specified rule language
     * @param baseRule
     * @return
     */
    IMatcher getMatcher(BaseRule baseRule);

}