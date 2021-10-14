package com.springleaf.cloud.discovery.filter;

import com.springleaf.cloud.discovery.base.RuleType;
import com.springleaf.cloud.discovery.config.model.BaseRule;
import com.springleaf.cloud.discovery.config.model.RuleConfigWrapper;

import java.util.Collections;
import java.util.List;

/**
 *
 *
 *
 * @author leon
 */
public interface Configurable<T> {

    /**
     * Gets the rules of current Registration  by the specified rule type.
     * @see #support()
     * @param context
     * @return
     */
    default List<? extends BaseRule> getConfigRules(FilterContext context){

        return getConfigRules(context, context.getFilterableRegistration().getServiceId());
    }

    /**
     *
     * Gets the rules of specified effect Registration  by the specified rule type.
     *
     * @param context
     * @param effectServiceId
     * @return
     */
    default List<? extends BaseRule> getConfigRules(FilterContext context, String effectServiceId){

        RuleConfigWrapper ruleConfig = context.getRuleConfig();

        if(ruleConfig == null){
            return Collections.emptyList();
        }

        List<? extends BaseRule> registerRules = ruleConfig.getAllRules(support(), effectServiceId);

        return registerRules;
    }

    /**
     * support ruleType
     * @return
     */
    RuleType support();




}
