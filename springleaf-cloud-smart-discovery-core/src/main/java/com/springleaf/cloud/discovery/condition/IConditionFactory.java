/*
 * Copyright 1999-2011 Alibaba Group.
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