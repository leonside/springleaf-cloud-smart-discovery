package com.springleaf.cloud.discovery.config.datasource;

import com.springleaf.cloud.discovery.config.datasource.config.ConfigRuleProperties;

/**
 *
 * A listener to implement a custom extension  after the ConfigRuleProperties configuration is loaded.
 *
 * @author leon
 */
public interface ConfigRuleLoadListener {

    /**
     * Triggered after the {@link ConfigRuleProperties} configuration has been loaded, allowing developers to extend the loaded Rule
     * @param configRuleProperties
     */
    void onLoaded(ConfigRuleProperties configRuleProperties);

}
