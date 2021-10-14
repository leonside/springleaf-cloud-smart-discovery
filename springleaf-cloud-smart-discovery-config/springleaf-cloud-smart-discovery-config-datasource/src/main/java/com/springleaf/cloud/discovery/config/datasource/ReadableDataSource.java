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
package com.springleaf.cloud.discovery.config.datasource;


import com.springleaf.cloud.discovery.config.datasource.property.CustomProperty;
import org.springframework.core.Ordered;

/**
 * The readable data source is responsible for retrieving configs (read-only).
 *
 * @param <S> source data type
 * @param <T> target data type
 */
public interface ReadableDataSource<S, T> extends Ordered {

    /**
     * Load data data source as the target type.
     *
     * @return the target data.
     * @throws Exception IO or other error occurs
     */
    T loadConfig() throws Exception;

    /**
     * Read original data from the data source.
     *
     * @return the original data.
     * @throws Exception IO or other error occurs
     */
    S readSource() throws Exception;

    /**
     *
     * @return the property.
     */
        CustomProperty<T> getProperty();

    /**
     * Close the data source.
     *
     * @throws Exception IO or other error occurs
     */
    void close() throws Exception;

    /**
     *
     * @return ruleType
     */
    String getRuleType();

    @Override
    default int getOrder(){
        return 0;
    }
}
