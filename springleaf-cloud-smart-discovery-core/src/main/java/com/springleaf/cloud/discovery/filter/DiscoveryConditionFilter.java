package com.springleaf.cloud.discovery.filter;

import com.springleaf.cloud.discovery.base.RuleType;

/**
 * @author leon
 */
public interface DiscoveryConditionFilter extends ConditionFilter, Configurable{

    @Override
    default RuleType support(){
        return RuleType.DISCOVERY;
    }
}
