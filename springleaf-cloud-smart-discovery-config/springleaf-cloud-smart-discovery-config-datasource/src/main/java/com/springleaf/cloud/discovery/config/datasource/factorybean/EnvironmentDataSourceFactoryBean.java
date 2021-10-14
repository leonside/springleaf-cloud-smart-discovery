package com.springleaf.cloud.discovery.config.datasource.factorybean;

import com.springleaf.cloud.discovery.config.datasource.converter.Converter;
import com.springleaf.cloud.discovery.config.datasource.datasource.ApolloDataSource;
import com.springleaf.cloud.discovery.config.datasource.datasource.EnvironmentDataSource;
import org.springframework.beans.factory.FactoryBean;

/**
 * A {@link FactoryBean} for creating {@link EnvironmentDataSource} instance.
 *
 * @author leon
 */
public class EnvironmentDataSourceFactoryBean implements FactoryBean<EnvironmentDataSource> {

    private Converter converter;

    private String value;
    private String ruleType;
    @Override
    public EnvironmentDataSource getObject() throws Exception {
        return new EnvironmentDataSource(converter, value,ruleType);
    }

    public String getRuleType() {
        return ruleType;
    }

    public void setRuleType(String ruleType) {
        this.ruleType = ruleType;
    }

    @Override
    public Class<?> getObjectType() {
        return EnvironmentDataSource.class;
    }

    public Converter getConverter() {
        return converter;
    }

    public void setConverter(Converter converter) {
        this.converter = converter;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
