package com.springleaf.cloud.discovery.config.datasource.datasource;

import com.ctrip.framework.apollo.Config;
import com.ctrip.framework.apollo.ConfigChangeListener;
import com.ctrip.framework.apollo.ConfigService;
import com.ctrip.framework.apollo.model.ConfigChange;
import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import com.springleaf.cloud.discovery.config.datasource.converter.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A  {@code DataSource} with <a href="http://github.com/ctripcorp/apollo">Apollo</a> as its configuration
 * source.
 * <br />
 * When the rule is changed in Apollo, it will take effect in real time.
 *
 */
public class ApolloDataSource<T> extends AbstractDataSource<String, T> implements WriteableDataSource<String, T> {
    public static final Logger logger = LoggerFactory.getLogger(NacosDataSource.class);

    private final Config config;
    private final String ruleKey;
    private final String defaultRuleValue;

    private final String ruleType;
    private ConfigChangeListener configChangeListener;

    /**
     * Constructs the Apollo data source
     *
     * @param namespaceName        the namespace name in Apollo, should not be null or empty
     * @param ruleKey              the rule key in the namespace, should not be null or empty
     * @param defaultRuleValue     the default rule value when the ruleKey is not found or any error
     *                             occurred
     * @param parser               the parser to transform string configuration to actual flow rules
     */
    public ApolloDataSource(String namespaceName, String ruleKey, String defaultRuleValue,
                            Converter<String, T> parser, String ruleType) {
        super(parser);

        Preconditions.checkArgument(!Strings.isNullOrEmpty(namespaceName), "Namespace name could not be null or empty");
        Preconditions.checkArgument(!Strings.isNullOrEmpty(ruleKey), "RuleKey could not be null or empty!");

        this.ruleKey = ruleKey;
        this.ruleType = ruleType;
        this.defaultRuleValue = defaultRuleValue;

        this.config = ConfigService.getConfig(namespaceName);

        initialize();

        logger.info(String.format("Initialized rule for namespace: %s, rule key: %s", namespaceName, ruleKey));
    }

    private void initialize() {
        initializeConfigChangeListener();
        loadAndUpdateRules();
    }

    private void loadAndUpdateRules() {
        try {
            T newValue = loadConfig();
            if (newValue == null) {
                logger.warn("[ApolloDataSource] WARN: rule config is null, you may have to check your data source");
            }
            getProperty().updateValue(newValue);
        } catch (Throwable ex) {
            logger.warn("[ApolloDataSource] Error when loading rule config", ex);
        }
    }

    private void initializeConfigChangeListener() {
        configChangeListener = new ConfigChangeListener() {
            @Override
            public void onChange(ConfigChangeEvent changeEvent) {
                ConfigChange change = changeEvent.getChange(ruleKey);
                //change is never null because the listener will only notify for this key
                if (change != null) {
                    logger.info("[ApolloDataSource] Received config changes: " + change.toString());
                }
                loadAndUpdateRules();
            }
        };
        config.addChangeListener(configChangeListener, Sets.newHashSet(ruleKey));
    }

    @Override
    public String readSource() throws Exception {
        String property = config.getProperty(ruleKey, defaultRuleValue);
        logger.info("[ApolloDataSource] New property value received for (ruleKey: {}): {}", ruleKey, property);
        return property;
    }

    @Override
    public void close() throws Exception {
        config.removeChangeListener(configChangeListener);
    }

    @Override
    public String getRuleType() {
        return ruleType;
    }

    @Override
    public void writeSource(String content) {
        //todo
        throw new UnsupportedOperationException("apollo config center dost not support this operation");
    }
}
