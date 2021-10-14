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



import com.springleaf.cloud.discovery.config.datasource.factorybean.ApolloDataSourceFactoryBean;

import javax.validation.constraints.NotEmpty;

/**
 * Apollo Properties class Using by {@link DataSourcePropertiesConfiguration} and
 * {@link ApolloDataSourceFactoryBean}
 *
 */
public class ApolloDataSourceProperties extends AbstractDataSourceProperties {

	@NotEmpty
	private String namespaceName = "application";
	@NotEmpty
	private String ruleKey;
	private String defaultFlowRuleValue;

	public ApolloDataSourceProperties() {
		super(ApolloDataSourceFactoryBean.class.getName());
	}

	public String getNamespaceName() {
		return namespaceName;
	}

	public void setNamespaceName(String namespaceName) {
		this.namespaceName = namespaceName;
	}

	public String getRuleKey() {
		return ruleKey;
	}

	public void setRuleKey(String ruleKey) {
		this.ruleKey = ruleKey;
	}

	public String getDefaultFlowRuleValue() {
		return defaultFlowRuleValue;
	}

	public void setDefaultFlowRuleValue(String defaultFlowRuleValue) {
		this.defaultFlowRuleValue = defaultFlowRuleValue;
	}
}
