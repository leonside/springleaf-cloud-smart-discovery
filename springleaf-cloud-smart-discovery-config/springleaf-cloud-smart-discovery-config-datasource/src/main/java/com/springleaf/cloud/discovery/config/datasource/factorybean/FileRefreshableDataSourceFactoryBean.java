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
import com.springleaf.cloud.discovery.config.datasource.datasource.FileRefreshableDataSource;
import org.springframework.beans.factory.FactoryBean;

import java.io.File;
import java.nio.charset.Charset;

/**
 * A {@link FactoryBean} for creating {@link FileRefreshableDataSource} instance.
 *
 * @see FileRefreshableDataSource
 */
public class FileRefreshableDataSourceFactoryBean
    implements FactoryBean<FileRefreshableDataSource> {

    private String file;
    private String charset;
    private long recommendRefreshMs;
    private int bufSize;
    private Converter converter;
    private String ruleType;

	@Override
    public FileRefreshableDataSource getObject() throws Exception {
        return new FileRefreshableDataSource(new File(file), converter,
            recommendRefreshMs, bufSize, Charset.forName(charset), ruleType);
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public Class<?> getObjectType() {
        return FileRefreshableDataSource.class;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public long getRecommendRefreshMs() {
        return recommendRefreshMs;
    }

    public void setRecommendRefreshMs(long recommendRefreshMs) {
        this.recommendRefreshMs = recommendRefreshMs;
    }

    public int getBufSize() {
        return bufSize;
    }

    public void setBufSize(int bufSize) {
        this.bufSize = bufSize;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }
}
