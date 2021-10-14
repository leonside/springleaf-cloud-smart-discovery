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

package com.springleaf.cloud.discovery.config.datasource.converter;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.List;

/**
 * Convert  rules for json or xml array Using strict mode to parse json or xml
 *
 */
public abstract class AbstractJsonConverter<T extends Object>
		implements Converter<String, Object> {

	private static final Logger log = LoggerFactory.getLogger(AbstractJsonConverter.class);

	private final ObjectMapper objectMapper;

	private final Class<T> ruleClass;

	public AbstractJsonConverter(ObjectMapper objectMapper, Class<T> ruleClass) {
		this.objectMapper = objectMapper;
		this.ruleClass = ruleClass;
	}

	@Override
	public Object convert(String source) {

		if (StringUtils.isEmpty(source)) {
			log.warn("converter can not convert rules because source is empty");
			return null;
		}


		try {
			// hard code
			if (!source.startsWith("[")) {
				T result = objectMapper.readValue(source, ruleClass);
				Validations.getInstance().validate(result);
				return result;
			} else {
				JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, ruleClass);
				List<T> resultLists = objectMapper.readValue(source, javaType);
				Validations.getInstance().validateList(resultLists);
				return resultLists;
			}
		} catch (IOException e) {
			log.error("json configuration parses exceptions ", e);
		}

		return null;
	}

}
