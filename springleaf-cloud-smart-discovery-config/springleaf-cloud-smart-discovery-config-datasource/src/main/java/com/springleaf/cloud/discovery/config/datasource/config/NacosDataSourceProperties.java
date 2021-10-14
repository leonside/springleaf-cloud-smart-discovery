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

import com.springleaf.cloud.discovery.config.datasource.factorybean.NacosDataSourceFactoryBean;
import org.springframework.util.StringUtils;

import javax.validation.constraints.NotEmpty;

/**
 * Nacos Properties class Using by {@link DataSourcePropertiesConfiguration} and
 * {@link NacosDataSourceFactoryBean}
 *
 */
public class NacosDataSourceProperties extends AbstractDataSourceProperties {


	private String serverAddr;

	@NotEmpty
	private String groupId = "DEFAULT_GROUP";

	private String dataId;

	private String endpoint;
	private String namespace;
	private String accessKey;
	private String secretKey;

	public NacosDataSourceProperties() {
		super(NacosDataSourceFactoryBean.class.getName());
	}

	@Override
	public void preCheck(String dataSourceName) {
		if (StringUtils.isEmpty(serverAddr)) {
			serverAddr = this.getEnv().getProperty("spring.cloud.nacos.discovery.server-addr", "");
			if (StringUtils.isEmpty(serverAddr)) {
				throw new IllegalArgumentException(
						"NacosDataSource server-addr is empty");
			}
		}

//		if(StringUtils.isEmpty(dataId)){
//			String appName = this.getEnv().getProperty("spring.application.name", "");
//			if(StringUtils.isEmpty(appName)){
//				throw new IllegalArgumentException( "NacosDataSource dataId is empty,and spring.application.name is empty");
//			}
//			dataId = appName + "-rules-" + getRuleType();
//		}
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
