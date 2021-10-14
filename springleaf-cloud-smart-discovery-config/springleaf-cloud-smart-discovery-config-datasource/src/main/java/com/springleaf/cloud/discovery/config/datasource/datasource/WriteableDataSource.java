package com.springleaf.cloud.discovery.config.datasource.datasource;

import com.springleaf.cloud.discovery.config.datasource.ReadableDataSource;

/**
 *
 * The writeable data source is Write configuration changes to the configuration center
 * @author leon
 */
public interface WriteableDataSource<S, T>  extends ReadableDataSource<S, T> {

    /**
     * write changes data to data source
     * @param content
     */
    void writeSource(String content);

}
