/*
 * Copyright (C) 2018 the original author or authors.
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

package com.springleaf.cloud.discovery.config.datasource.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.Map;
import java.util.TreeMap;

/**
 *Config Rule Properties， Examples of data source configuration are as follows:
 *  springleaf.smart.discovery.config.fileconfig.nacos.dataId=key1
 *  springleaf.smart.discovery.config.fileconfig.nacos.groupId=DEFAULT_GROUP
 */
@ConfigurationProperties(prefix = "springleaf.smart.discovery.config")
@Validated
public class ConfigRuleProperties {


	/**
	 * Enable sentinel auto configure, the default value is true.
	 */
	private boolean enabled = true;

	/**
	 * Configurations about datasource, like 'nacos', 'apollo', 'file', 'env'.
	 */
	private Map<String, DataSourcePropertiesConfiguration> datasource = new TreeMap<>(
			String.CASE_INSENSITIVE_ORDER);


	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public Map<String, DataSourcePropertiesConfiguration> getDatasource() {
		return datasource;
	}

	public void setDatasource(Map<String, DataSourcePropertiesConfiguration> datasource) {
		this.datasource = datasource;
	}
}
