package com.springleaf.cloud.discovery.config.parser;

import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.base.RuleType;
import com.springleaf.cloud.discovery.config.datasource.ConfigRuleLoadListener;
import com.springleaf.cloud.discovery.config.datasource.config.AbstractDataSourceProperties;
import com.springleaf.cloud.discovery.config.datasource.config.ConfigRuleProperties;
import com.springleaf.cloud.discovery.config.datasource.config.DataSourcePropertiesConfiguration;

import java.util.List;

/**
 *
 * ConfigRuleProperties listens for events after the ConfigRuleProperties configuration is loaded.
 * This is used to convert the DiscoveryProperties to ConfigRuleProperties configuration
 * {@link com.springleaf.cloud.discovery.config.datasource.ConfigDataSourceHandler}
 *
 * @author leon
 */
public class DiscoveryConfigRuleLoadListener implements ConfigRuleLoadListener {

    private DiscoveryProperties discoveryProperties;

    public DiscoveryConfigRuleLoadListener(DiscoveryProperties discoveryProperties){
        this.discoveryProperties = discoveryProperties;
    }

    @Override
    public void onLoaded(ConfigRuleProperties configRuleProperties) {

        if(discoveryProperties.getDiscovery() != null && discoveryProperties.getDiscovery().getConfig() != null && discoveryProperties.isDiscoveryEnabled()){
            DataSourcePropertiesConfiguration config = discoveryProperties.getDiscovery().getConfig();
            putConfigRule(configRuleProperties, config, RuleType.DISCOVERY );
        }

        if(discoveryProperties.getRegister() != null && discoveryProperties.getRegister().getConfig() != null && discoveryProperties.isRegistersEnabled()){
            DataSourcePropertiesConfiguration config = discoveryProperties.getRegister().getConfig();
            putConfigRule(configRuleProperties, config, RuleType.REGISTER );
        }

        if(discoveryProperties.getRouter() != null && discoveryProperties.getRouter().getConfig() != null && discoveryProperties.isRoutersEnabled()){
            DataSourcePropertiesConfiguration config = discoveryProperties.getRouter().getConfig();
            putConfigRule(configRuleProperties, config, RuleType.ROUTER );
        }

        if(discoveryProperties.getWeight() != null && discoveryProperties.getWeight().getConfig() != null && discoveryProperties.isWeightsEnabled()){
            DataSourcePropertiesConfiguration config = discoveryProperties.getWeight().getConfig();
            putConfigRule(configRuleProperties, config, RuleType.WEIGHT );
        }

        if(discoveryProperties.getConfig() != null){
            DataSourcePropertiesConfiguration config = discoveryProperties.getConfig();
            putConfigRule(configRuleProperties, config, RuleType.ALL );
        }
    }

    private void putConfigRule(ConfigRuleProperties configRuleProperties, DataSourcePropertiesConfiguration appendConfig, RuleType ruleType) {
        List<AbstractDataSourceProperties> allValidDataSourceProperties = appendConfig.getAllValidDataSourceProperties();
        allValidDataSourceProperties.stream().forEach(it->{
            it.setRuleType(ruleType.name());
            String ruleName = ruleType.name() + "-" + it.getClass().getSimpleName();

            DataSourcePropertiesConfiguration dataSourcePropertiesConfiguration = new DataSourcePropertiesConfiguration();
            dataSourcePropertiesConfiguration.set(it);
            configRuleProperties.getDatasource().put(ruleName, dataSourcePropertiesConfiguration);
        });
    }
}
