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
import org.springframework.util.ObjectUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Using By ConfigurationProperties.
 *
 * @see NacosDataSourceProperties
 * @see ApolloDataSourceProperties
 * @see FileDataSourceProperties
 */
public class DataSourcePropertiesConfiguration {

	private EnvDataSourceProperties env;

	private FileDataSourceProperties file;

	private NacosDataSourceProperties nacos;

	private ApolloDataSourceProperties apollo;

	public DataSourcePropertiesConfiguration() {
	}

	public DataSourcePropertiesConfiguration(FileDataSourceProperties file) {
		this.file = file;
	}

	public DataSourcePropertiesConfiguration(NacosDataSourceProperties nacos) {
		this.nacos = nacos;
	}

	public EnvDataSourceProperties getEnv() {
		return env;
	}

	public void setEnv(EnvDataSourceProperties env) {
		this.env = env;
	}
	public void set(AbstractDataSourceProperties dataSourceProperties){
		if(dataSourceProperties instanceof EnvDataSourceProperties){
			setEnv((EnvDataSourceProperties)dataSourceProperties);
		}else if(dataSourceProperties instanceof FileDataSourceProperties){
			setFile((FileDataSourceProperties)dataSourceProperties);
		}else if(dataSourceProperties instanceof NacosDataSourceProperties){
			setNacos((NacosDataSourceProperties)dataSourceProperties);
		}else if(dataSourceProperties instanceof ApolloDataSourceProperties){
			setApollo((ApolloDataSourceProperties)dataSourceProperties);
		}else{
			throw new UnsupportedOperationException("unsported datasource type");
		}
	}

	public DataSourcePropertiesConfiguration(ApolloDataSourceProperties apollo) {
		this.apollo = apollo;
	}

	public FileDataSourceProperties getFile() {
		return file;
	}

	public void setFile(FileDataSourceProperties file) {
		this.file = file;
	}

	public NacosDataSourceProperties getNacos() {
		return nacos;
	}

	public void setNacos(NacosDataSourceProperties nacos) {
		this.nacos = nacos;
	}

	public ApolloDataSourceProperties getApollo() {
		return apollo;
	}

	public void setApollo(ApolloDataSourceProperties apollo) {
		this.apollo = apollo;
	}

	@JsonIgnore
	public List<String> getValidField() {
		return Arrays.stream(this.getClass().getDeclaredFields()).map(field -> {
			try {
				if (!ObjectUtils.isEmpty(field.get(this))) {
					return field.getName();
				}
				return null;
			}
			catch (IllegalAccessException e) {
				// won't happen
			}
			return null;
		}).filter(Objects::nonNull).collect(Collectors.toList());
	}

	@JsonIgnore
	public AbstractDataSourceProperties getFirstValidDataSourceProperties() {
		List<String> invalidFields = getValidField();
		if (invalidFields.size() == 1) {
			try {
				this.getClass().getDeclaredField(invalidFields.get(0))
						.setAccessible(true);
				return (AbstractDataSourceProperties) this.getClass()
						.getDeclaredField(invalidFields.get(0)).get(this);
			}
			catch (IllegalAccessException e) {
				// won't happen
			}
			catch (NoSuchFieldException e) {
				// won't happen
			}
		}
		return null;
	}

	@JsonIgnore
	public List<AbstractDataSourceProperties> getAllValidDataSourceProperties() {
		List<String> invalidFields = getValidField();

		List<AbstractDataSourceProperties> collect = invalidFields.stream().map(it -> {
			try {
				this.getClass().getDeclaredField(it)
						.setAccessible(true);
				return (AbstractDataSourceProperties) this.getClass()
						.getDeclaredField(it).get(this);
				}catch (IllegalAccessException e) {
					// won't happen
				}catch (NoSuchFieldException e) {
					// won't happen
				}
			throw new UnsupportedOperationException("To obtain datasource properties exception ");
		}).collect(Collectors.toList());
		return collect;
	}
}
