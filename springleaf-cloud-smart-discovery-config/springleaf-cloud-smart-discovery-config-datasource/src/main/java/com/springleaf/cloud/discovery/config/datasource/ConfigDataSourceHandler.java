package com.springleaf.cloud.discovery.config.datasource;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springleaf.cloud.discovery.config.datasource.config.AbstractDataSourceProperties;
import com.springleaf.cloud.discovery.config.datasource.config.ConfigRuleProperties;
import com.springleaf.cloud.discovery.config.datasource.converter.JsonConverter;
import com.springleaf.cloud.discovery.config.datasource.datasource.AbstractDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.env.Environment;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * <p>
 *Config datasource handler dynamically registers the spring Bean
 * according to the configured datasource {@link ConfigRuleProperties},
 * </p>
 * <p>
 * In addition, you can extend the {@link ConfigRuleLoadListener} listener to implement a custom extension
 * after the ConfigRuleProperties configuration is loaded.
 * And also extend the {@link RuleTypeCustomizer} interface to implement personalized extensions for different types of rules
 *
 * </p>
 */
public class ConfigDataSourceHandler implements SmartInitializingSingleton {

    private final List<ConfigRuleLoadListener> loadListeners;

    private ObjectMapper objectMapper = new ObjectMapper();

    private static final Logger log = LoggerFactory
            .getLogger(ConfigDataSourceHandler.class);

    private Map<String, RuleTypeCustomizer> ruleTypeCustomizerMap = new ConcurrentHashMap<>();

    private List<String> dataTypeList = Arrays.asList("json", "xml");

    private final String DATA_TYPE_FIELD = "dataType";
    private final String CUSTOM_DATA_TYPE = "custom";
    private final String CONVERTER_CLASS_FIELD = "converterClass";
    private final String NAMESAPCE_FIELD = "namespace";
    private final String RULETYPE_FIELD = "ruleType";

    private final DefaultListableBeanFactory beanFactory;

    private final ConfigRuleProperties configRuleProperties;

    private final Environment env;

    public ConfigDataSourceHandler(DefaultListableBeanFactory beanFactory,
                                   ConfigRuleProperties configRuleProperties, Environment env, List<RuleTypeCustomizer> ruleTypeCustomizers, List<ConfigRuleLoadListener> loadListeners) {
        this.beanFactory = beanFactory;
        this.configRuleProperties = configRuleProperties;
        this.env = env;
        this.loadListeners = loadListeners;
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ruleTypeCustomizers.stream().forEach(it->{
            ruleTypeCustomizerMap.put(it.supportRuleType(), it);
        });
    }

    @Override
    public void afterSingletonsInstantiated() {

        fireConfigRuleLoadListener(loadListeners);

        configRuleProperties.getDatasource()
                .forEach((dataSourceName, dataSourceProperties) -> {
                    try {

                        List<String> validFields = dataSourceProperties.getValidField();
                        if (validFields.size() != 1) {
                            log.error("[Rule Starter] DataSource " + dataSourceName
                                    + " multi datasource active and won't loaded: "
                                    + dataSourceProperties.getValidField());
                            return;
                        }
                        AbstractDataSourceProperties abstractDataSourceProperties = dataSourceProperties
                                .getFirstValidDataSourceProperties();

                        if (!ruleTypeCustomizerMap.containsKey(abstractDataSourceProperties.getRuleType())) {
                            log.warn("未知的RuleType[" + abstractDataSourceProperties.getRuleType() + "]...");
                            return;
                        }

                        abstractDataSourceProperties.setEnv(env);
                        abstractDataSourceProperties.preCheck(dataSourceName);
                        registerBean(abstractDataSourceProperties, makeDataSourceName(dataSourceName, validFields.get(0)));
                    } catch (Exception e) {
                        log.error("[Rule Starter] DataSource " + dataSourceName
                                + " build error: " + e.getMessage(), e);
                    }
                });
    }

    private String makeDataSourceName(String dataSourceName, String validField) {
        return dataSourceName  + "-rule-" + validField + "-datasource";
    }

    private void fireConfigRuleLoadListener(List<ConfigRuleLoadListener> loadListeners) {
        loadListeners.stream().forEach(it->{
            it.onLoaded(configRuleProperties);
        });
    }

    private void registerBean(final AbstractDataSourceProperties dataSourceProperties,
                              String dataSourceName) {

        Map<String, Object> propertyMap = Arrays
                .stream(dataSourceProperties.getClass().getDeclaredFields())
                .collect(HashMap::new, (m, v) -> {
                    try {
                        v.setAccessible(true);
                        m.put(v.getName(), v.get(dataSourceProperties));
                    } catch (IllegalAccessException e) {
                        log.error("[Rule Starter] DataSource " + dataSourceName
                                + " field: " + v.getName() + " invoke error");
                        throw new RuntimeException(
                                "[Rule Starter] DataSource " + dataSourceName
                                        + " field: " + v.getName() + " invoke error",
                                e);
                    }
                }, HashMap::putAll);
        propertyMap.put(CONVERTER_CLASS_FIELD, dataSourceProperties.getConverterClass());
        propertyMap.put(DATA_TYPE_FIELD, dataSourceProperties.getDataType());
        propertyMap.put(NAMESAPCE_FIELD, dataSourceProperties.getEnv().getProperty("spring.cloud.nacos.discovery.namespace",""));
        propertyMap.put(RULETYPE_FIELD, dataSourceProperties.getRuleType());

        BeanDefinitionBuilder builder = BeanDefinitionBuilder
                .genericBeanDefinition(dataSourceProperties.getFactoryBeanName());

        final RuleTypeCustomizer ruleTypeCustomizer = ruleTypeCustomizerMap.get(dataSourceProperties.getRuleType());

        propertyMap.forEach((propertyName, propertyValue) -> {
            Field field = ReflectionUtils.findField(dataSourceProperties.getClass(),
                    propertyName);
            if (null == field) {
                return;
            }
            if (DATA_TYPE_FIELD.equals(propertyName)) {
                String dataType = StringUtils.trimAllWhitespace(propertyValue.toString());
                if (CUSTOM_DATA_TYPE.equals(dataType)) {
                    try {
                        if (StringUtils
                                .isEmpty(dataSourceProperties.getConverterClass())) {
                            throw new RuntimeException("[Rule Starter] DataSource "
                                    + dataSourceName
                                    + "dataType is custom, please set converter-class "
                                    + "property");
                        }
                        // construct custom Converter with 'converterClass'
                        // configuration and register
                        String customConvertBeanName = "rule-"
                                + dataSourceProperties.getConverterClass();
                        if (!this.beanFactory.containsBean(customConvertBeanName)) {
                            this.beanFactory.registerBeanDefinition(customConvertBeanName,
                                    BeanDefinitionBuilder
                                            .genericBeanDefinition(
                                                    Class.forName(dataSourceProperties
                                                            .getConverterClass()))
                                            .getBeanDefinition());
                        }
                        builder.addPropertyReference("converter", customConvertBeanName);
                    } catch (ClassNotFoundException e) {
                        log.error("[Rule Starter] DataSource " + dataSourceName
                                + " handle "
                                + dataSourceProperties.getClass().getSimpleName()
                                + " error, class name: "
                                + dataSourceProperties.getConverterClass());
                        throw new RuntimeException("[ConfigRule Starter] DataSource "
                                + dataSourceName + " handle "
                                + dataSourceProperties.getClass().getSimpleName()
                                + " error, class name: "
                                + dataSourceProperties.getConverterClass(), e);
                    }
                } else {
                    if (!dataTypeList.contains(
                            StringUtils.trimAllWhitespace(propertyValue.toString()))) {
                        throw new RuntimeException("[Rule Starter] DataSource "
                                + dataSourceName + " dataType: " + propertyValue
                                + " is not support now. please using these types: "
                                + dataTypeList.toString());
                    }
                    // converter type now support xml or json.
                    // The bean name of these converters wrapped by
                    builder.addPropertyValue("converter", new JsonConverter(objectMapper, ruleTypeCustomizer.getConvertClassType()));
                }
            } else if (CONVERTER_CLASS_FIELD.equals(propertyName)) {
                return;
            } else {
                // wired properties
                Optional.ofNullable(propertyValue)
                        .ifPresent(v -> builder.addPropertyValue(propertyName, v));
            }
        });

        this.beanFactory.registerBeanDefinition(dataSourceName,
                builder.getBeanDefinition());
        // init in Spring
        AbstractDataSource newDataSource = (AbstractDataSource) this.beanFactory
                .getBean(dataSourceName);

        ruleTypeCustomizer.customize(newDataSource);

    }

}
