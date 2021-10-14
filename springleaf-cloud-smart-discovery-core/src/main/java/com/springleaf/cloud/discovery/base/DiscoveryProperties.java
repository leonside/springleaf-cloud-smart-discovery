package com.springleaf.cloud.discovery.base;

import com.springleaf.cloud.discovery.config.datasource.config.DataSourcePropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.env.Environment;

/**
 * Discovery Properties, loading springleaf.smart.discovery prefix configuration
 *
 * @author leon
 */
@ConfigurationProperties(prefix = "springleaf.smart.discovery")
public class DiscoveryProperties implements InitializingBean{

    public static final Logger logger = LoggerFactory.getLogger(DiscoveryProperties.class);

    /**
     * the configuration of weight
     */
    private DiscoveryCommonProperties weight;
    /**
     * the configuration of register
     */
    private DiscoveryCommonProperties register;
    /**
     * the configuration of router
     */
    private DiscoveryCommonProperties router;
    /**
     * the configuration of discovery
     */
    private DiscoveryCommonProperties discovery;
    /**
     * the configuration of group
     */
    private DiscoveryGroupProperties group;
    /**
     * whether to enabled the discovery function
     */
    private boolean enabled = true;
    /**
     * the global configuration,contains weightConfig. registerConfig. routerConfig. discoveryConfig.groupConfig
     */
    private DataSourcePropertiesConfiguration config;

    public DiscoveryCommonProperties getWeight() {
        return weight;
    }

    public void setWeight(DiscoveryCommonProperties weight) {
        this.weight = weight;
    }

    public DiscoveryCommonProperties getRegister() {
        return register;
    }

    public void setRegister(DiscoveryCommonProperties register) {
        this.register = register;
    }

    public DiscoveryCommonProperties getRouter() {
        return router;
    }

    public void setRouter(DiscoveryCommonProperties router) {
        this.router = router;
    }

    public DiscoveryCommonProperties getDiscovery() {
        return discovery;
    }

    public void setDiscovery(DiscoveryCommonProperties discovery) {
        this.discovery = discovery;
    }

    public DiscoveryGroupProperties getGroup() {
        return group;
    }

    public void setGroup(DiscoveryGroupProperties group) {
        this.group = group;
    }

    public boolean getEnabled() {
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

    public boolean isWeightsEnabled() {
        return (weight != null && weight.isEnabled()) || (weight == null) ;
    }

    public boolean isGroupIsolation() {
        return group != null && group.isEnabled();
    }

    public boolean isDiscoveryEnabled() {
        return (discovery != null && discovery.isEnabled()) || (discovery == null) ;
    }

    public boolean isRegistersEnabled() {
        return (register != null && register.isEnabled()) || (register == null) ;
    }

    public boolean isRoutersEnabled() {
        return (router != null && router.isEnabled()) || (router == null) ;
    }

    /**
     * Whether to enable service discovery
     * @param environment
     * @return
     */
    public static final boolean isDiscoveryEnabled(Environment environment){
        return Boolean.valueOf(environment.getProperty(DiscoveryConstant.DISCOVERY_GLOBAL_CONFIG_ENABLED, "true")) &&
                Boolean.valueOf(environment.getProperty(DiscoveryConstant.DISCOVERY_DISCOVERY_CONFIG_ENABLED, "true"));
    }

    /**
     * Whether to enable service register
     * @param environment
     * @return
     */
    public static final boolean isRegisterEnabled(Environment environment){
        return Boolean.valueOf(environment.getProperty(DiscoveryConstant.DISCOVERY_GLOBAL_CONFIG_ENABLED, "true")) &&
                Boolean.valueOf(environment.getProperty(DiscoveryConstant.DISCOVERY_REGISTER_CONFIG_ENABLED, "true"));
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println();
    }

    /**
     * Discovery Group Properties
     */
    public static class DiscoveryGroupProperties {
        /**
         * the configuration of group key, default value is group
         */
        private String key = "group";
        /**
         * the configuration of groupIsolation
         */
        private DiscoveryGroupIsolationProperties isolation;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public DiscoveryGroupIsolationProperties getIsolation() {
            return isolation;
        }

        public void setIsolation(DiscoveryGroupIsolationProperties isolation) {
            this.isolation = isolation;
        }

        public boolean isEnabled() {
            return isolation != null && isolation.isEnabled() ;
        }
    }
    public static class DiscoveryGroupIsolationProperties {
        /**
         * whether to enabled gourpIosolation , default is false
         */
        private boolean enabled = false;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }
    }


}
