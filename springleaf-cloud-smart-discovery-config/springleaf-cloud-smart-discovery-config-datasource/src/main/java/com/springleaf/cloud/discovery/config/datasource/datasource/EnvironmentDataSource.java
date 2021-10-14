package com.springleaf.cloud.discovery.config.datasource.datasource;

import com.springleaf.cloud.discovery.config.datasource.converter.Converter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A DataSource based on spring environment
 * @author leon
 */
public class EnvironmentDataSource<T> extends AbstractDataSource<String, T>  {

    public static final Logger logger = LoggerFactory.getLogger(NacosDataSource.class);

    private String value;
    private String ruleType;

    public EnvironmentDataSource(Converter<String, T> parser, String value, String ruleType) {
        super(parser);
        this.value = value;
        this.ruleType = ruleType;
        firstLoad();
    }

    private void firstLoad() {
        try {
            T newValue = loadConfig();
            getProperty().updateValue(newValue);
        } catch (Throwable e) {
            logger.info("loadConfig exception", e);
        }
    }


    @Override
    public String readSource() throws Exception {
        logger.info("[EnvironmentDataSource] property value read from environment, value is: {}", value);
        return value;
    }

    @Override
    public void close() throws Exception {

    }

    @Override
    public String getRuleType() {
        return ruleType;
    }
}
