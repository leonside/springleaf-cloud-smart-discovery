package com.springleaf.cloud.discovery.filter;

import com.springleaf.cloud.discovery.base.RuleType;

/**
 * @author leon
 */
public interface RegisterConditionPredicate extends ConditionPredicate, Configurable{

    @Override
    default RuleType support() {
        return RuleType.REGISTER;
    }

}
