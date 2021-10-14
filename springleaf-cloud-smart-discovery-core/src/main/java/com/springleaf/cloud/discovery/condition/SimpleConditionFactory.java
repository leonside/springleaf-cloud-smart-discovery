/*
 * Copyright 1999-2012 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springleaf.cloud.discovery.condition;


import com.springleaf.cloud.discovery.base.DiscoveryConstant;
import com.springleaf.cloud.discovery.config.IPlaceholderResolver;
import com.springleaf.cloud.discovery.config.model.BaseRule;
import com.springleaf.cloud.discovery.config.model.RouterRule;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Default ConditionFactory
 *
 * @author leon
 */
public class SimpleConditionFactory implements IConditionFactory {

    @Autowired
    private ObjectProvider<IPlaceholderResolver> placeholderResolvers;

    @Override
    public IRouter getRouter(RouterRule routerEntity) {

        routerEntity.setConditions(convertPlaceholder(routerEntity.getConditions()));

        return new ConditionRouter(routerEntity);
    }

    @Override
    public IMatcher getMatcher(BaseRule baseRule) {

        return new ConditionMatcher(convertPlaceholder(baseRule.getConditions()));
    }

    @Override
    public String support() {
        return DiscoveryConstant.EXPRESSION_LANGUAGE_DEFAULT;
    }

    private String convertPlaceholder(String conditions) {

        String conditionsCopy = conditions;

        if(placeholderResolvers != null){
            for (IPlaceholderResolver resolver : placeholderResolvers) {
                if(resolver.support(conditions)){
                    conditionsCopy = resolver.resolve(conditionsCopy);
                }
            }
        }

        return conditionsCopy;
    }
}