/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
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
package com.springleaf.cloud.discovery.config.datasource.datasource;

import com.springleaf.cloud.discovery.config.datasource.ReadableDataSource;
import com.springleaf.cloud.discovery.config.datasource.converter.Converter;
import com.springleaf.cloud.discovery.config.datasource.property.CustomProperty;
import com.springleaf.cloud.discovery.config.datasource.property.DynamicCustomProperty;

/**
 * The abstract readable data source provides basic functionality for loading and parsing config.
 *
 */
public abstract class AbstractDataSource<S, T> implements ReadableDataSource<S, T> {

    protected final Converter<S, T> parser;
    protected final CustomProperty<T> property;

    public AbstractDataSource(Converter<S, T> parser) {
        if (parser == null) {
            throw new IllegalArgumentException("parser can't be null");
        }
        this.parser = parser;
        this.property = new DynamicCustomProperty<>();
    }

    @Override
    public T loadConfig() throws Exception {
        return loadConfig(readSource());
    }

    public T loadConfig(S conf) throws Exception {
        T value = parser.convert(conf);
        return value;
    }

    @Override
    public CustomProperty<T> getProperty() {
        return property;
    }
}
