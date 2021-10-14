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

package com.springleaf.cloud.discovery.config.datasource.factorybean;

import com.springleaf.cloud.discovery.config.datasource.converter.Converter;
import com.springleaf.cloud.discovery.config.datasource.datasource.ApolloDataSource;
import org.springframework.beans.factory.FactoryBean;

/**
 * A {@link FactoryBean} for creating {@link ApolloDataSource} instance.
 *
 * @see ApolloDataSource
 */
public class ApolloDataSourceFactoryBean implements FactoryBean<ApolloDataSource> {

	private String namespaceName;
	private String ruleKey;
	private String defaultFlowRuleValue;
	private Converter converter;
	private String ruleType;

	@Override
	public ApolloDataSource getObject() throws Exception {
		return new ApolloDataSource(namespaceName, ruleKey, defaultFlowRuleValue,
				converter,ruleType);
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	@Override
	public Class<?> getObjectType() {
		return ApolloDataSource.class;
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

	public Converter getConverter() {
		return converter;
	}

	public void setConverter(Converter converter) {
		this.converter = converter;
	}
}
