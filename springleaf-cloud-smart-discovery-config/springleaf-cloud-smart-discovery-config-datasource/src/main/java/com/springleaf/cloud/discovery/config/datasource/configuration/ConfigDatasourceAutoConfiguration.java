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

package com.springleaf.cloud.discovery.config.datasource.configuration;

import com.springleaf.cloud.discovery.config.datasource.ConfigDataSourceHandler;
import com.springleaf.cloud.discovery.config.datasource.ConfigRuleLoadListener;
import com.springleaf.cloud.discovery.config.datasource.RuleTypeCustomizer;
import com.springleaf.cloud.discovery.config.datasource.config.ConfigRuleProperties;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 */
@Configuration
@ConditionalOnConfigDatasourceEnabled
@EnableConfigurationProperties(ConfigRuleProperties.class)
public class ConfigDatasourceAutoConfiguration {

	@Bean
	@ConditionalOnMissingBean
	public ConfigDataSourceHandler configDataSourceHandler(
			DefaultListableBeanFactory beanFactory, ConfigRuleProperties configRuleProperties,
			Environment env, List<RuleTypeCustomizer> ruleTypeCustomizerList, List<ConfigRuleLoadListener> loadListeners) {

		return new ConfigDataSourceHandler(beanFactory,configRuleProperties, env, ruleTypeCustomizerList, loadListeners);
	}

}
