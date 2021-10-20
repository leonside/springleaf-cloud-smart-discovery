package com.springleaf.cloud.discovery.filter;

import com.springleaf.cloud.discovery.base.RuleType;

/**
 * @author leon
 */
public interface RouterConditionPredicate extends ConditionPredicate, Configurable{

    @Override
    default RuleType support() {
        return RuleType.ROUTER;
    }


}
