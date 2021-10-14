package com.springleaf.cloud.discovery.condition;

import com.springleaf.cloud.discovery.config.model.BaseRule;
import com.springleaf.cloud.discovery.exception.DiscoveryException;

import java.util.Map;

/**
 *
 * ConditionFactory Builder
 * Get the {@link IConditionFactory} implementation according to the configured rule language
 *
 * @author leon
 */
public class ConditionFactoryBuilder {

    private Map<String, IConditionFactory> conditionFactoryMap;

    public ConditionFactoryBuilder(Map<String, IConditionFactory> conditionFactoryMap){
        this.conditionFactoryMap = conditionFactoryMap;
    }

    public IConditionFactory getFactory(BaseRule baseRule){
        IConditionFactory conditionFactory = conditionFactoryMap.get(baseRule.getLanguage());

        if(conditionFactory == null){
            throw new DiscoveryException("there is no conditional factory whose expression is:" + baseRule.getLanguage());
        }

        return conditionFactoryMap.get(baseRule.getLanguage());
    }

}
