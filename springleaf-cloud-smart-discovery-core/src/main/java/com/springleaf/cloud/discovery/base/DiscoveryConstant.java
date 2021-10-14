package com.springleaf.cloud.discovery.base;

/**
 * Constant config
 *
 * @author leon
 */
public class DiscoveryConstant {

    /**
     * Local rule configuration keys, such as local configuration files and environment variables
     */
    public static final String LOCAL_RULE_KEY = "localRule";
    /**
     * Dynamic rules Configure keys. Dynamic rules are added through the REST Ful interface. Dynamic rules have a higher priority and are used for grayscale publishing
     */
    public static final String DYNAMIC_RULE_KEY = "dynamicRule";

    /**
     * The host key in the service metadata
     */
    public static final String METADATA_KEY_HOST = "host";
    /**
     * Port key in the service metadata
     */
    public static final String METADATA_KEY_PORT = "port";
    /**
     * Weight key values in the service metadata
     */
    public static final String METADATA_KEY_WEIGHT = "weight";

    /**
     * Application name key values in the service metadata
     */
    public static final String METADATA_KEY_APPLICATION_NAME = "application";

    /**
     * Context key values in the service metadata
     */
    public static final String METADATA_KEY_CONTEXT_PATH = "contextpath";

    /**
     * Servlet context configures the key
     */
    public static final String SERVLET_CONTEXT_PATH = "server.servlet.context-path";

    /**
     * spring application name key
     */
    public static final String SPRING_APPLICATION_NAME = "spring.application.name";

    /**
     * Environment variables pass extended attribute prefixes
     */
    public static final String METADATA_ENV_EXTENSIONS_PREFIX = "metadata";
    /**
     * default Condition Expression, see {@link com.springleaf.cloud.discovery.condition.SimpleConditionFactory}
     */
    public static final String EXPRESSION_LANGUAGE_DEFAULT = "simpleCondition";

    /**
     * smart discovery global enabled configuration
     */
    public static final String DISCOVERY_GLOBAL_CONFIG_ENABLED = "springleaf.smart.discovery.enabled";

    /**
     * smart discovery register enabled configuration
     */
    public static final String DISCOVERY_REGISTER_CONFIG_ENABLED = "springleaf.smart.discovery.register.enabled";

    /**
     * smart discovery discovery enabled configuration
     */
    public static final String DISCOVERY_DISCOVERY_CONFIG_ENABLED = "springleaf.smart.discovery.discovery.enabled";
}
