/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.springleaf.cloud.discovery.config.datasource.config;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.core.env.Environment;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Abstract class Using by {@link DataSourcePropertiesConfiguration}
 *
 */
public class AbstractDataSourceProperties {

    @NotEmpty
    private String dataType = "json";
    @NotNull
    private String ruleType;
    private String converterClass;
    @JsonIgnore
    private final String factoryBeanName;
    @JsonIgnore
    private Environment env;

    public AbstractDataSourceProperties(String factoryBeanName) {
        this.factoryBeanName = factoryBeanName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    public String getConverterClass() {
        return converterClass;
    }

    public void setConverterClass(String converterClass) {
        this.converterClass = converterClass;
    }

    public String getFactoryBeanName() {
        return factoryBeanName;
    }

    public Environment getEnv() {
        return env;
    }

    public void setEnv(Environment env) {
        this.env = env;
    }

    public void preCheck(String dataSourceName) {

    }
}
