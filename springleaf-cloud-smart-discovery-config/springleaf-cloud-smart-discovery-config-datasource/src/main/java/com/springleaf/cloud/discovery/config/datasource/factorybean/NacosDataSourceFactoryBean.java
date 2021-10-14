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
import com.springleaf.cloud.discovery.config.datasource.datasource.NacosDataSource;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * A {@link FactoryBean} for creating {@link NacosDataSource} instance.
 *
 * @see NacosDataSource
 */
public class NacosDataSourceFactoryBean implements FactoryBean<NacosDataSource> {

	public static final String NAMESPACE = "namespace";
	public static final String ACCESS_KEY = "accessKey";
	public static final String SECRET_KEY = "secretKey";
	public static final String SERVER_ADDR = "serverAddr";
	public static final String ENDPOINT = "endpoint";

	private String serverAddr;
	private String groupId;
	private String dataId;
	private Converter converter;

	private String endpoint;
	private String namespace;
	private String accessKey;
	private String secretKey;

	private String ruleType;

	@Override
	public NacosDataSource getObject() throws Exception {
		Properties properties = new Properties();
		if (!StringUtils.isEmpty(this.serverAddr)) {
			properties.setProperty(SERVER_ADDR, this.serverAddr);
		}
		else {
			properties.setProperty(ACCESS_KEY, this.accessKey);
			properties.setProperty(SECRET_KEY, this.secretKey);
			properties.setProperty(ENDPOINT, this.endpoint);
		}
		if (!StringUtils.isEmpty(this.namespace)) {
			properties.setProperty(NAMESPACE, this.namespace);
		}
		return new NacosDataSource(properties, groupId, dataId, converter,ruleType);
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	@Override
	public Class<?> getObjectType() {
		return NacosDataSource.class;
	}

	public String getServerAddr() {
		return serverAddr;
	}

	public void setServerAddr(String serverAddr) {
		this.serverAddr = serverAddr;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getDataId() {
		return dataId;
	}

	public void setDataId(String dataId) {
		this.dataId = dataId;
	}

	public Converter getConverter() {
		return converter;
	}

	public void setConverter(Converter converter) {
		this.converter = converter;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}
}
