package com.springleaf.cloud.discovery.base;

import com.springleaf.cloud.discovery.config.datasource.config.DataSourcePropertiesConfiguration;

/**
 * discovery general properties, see {@link DiscoveryProperties}, contains weight register router and discovery config
 *
 *  @author leon
 */
public class DiscoveryCommonProperties {
    /**
     * whether to enable this functions, default is true
     */
    private boolean enabled = true;
    /**
     * the configuration of datasource, contains file、nacos、apollo、env.
     */
    private DataSourcePropertiesConfiguration config;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public DataSourcePropertiesConfiguration getConfig() {
        return config;
    }

    public void setConfig(DataSourcePropertiesConfiguration config) {
        this.config = config;
    }
}