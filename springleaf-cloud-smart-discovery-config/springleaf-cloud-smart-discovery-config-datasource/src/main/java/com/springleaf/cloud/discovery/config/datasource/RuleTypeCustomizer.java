package com.springleaf.cloud.discovery.config.datasource;

import com.springleaf.cloud.discovery.config.datasource.datasource.AbstractDataSource;

/**
 *
 * The Customize DataSource callback event is decoupled in this way.
 * Developers can add {@link com.springleaf.cloud.discovery.config.datasource.property.PropertyListener} to dataSource to implement custom business logic,
 * And register the class that implements the interface as a Spring Bean
 *
 * @author leon
 */
public interface RuleTypeCustomizer  {

    /**
     * The class type to which the JSON configuration rule is converted
     * @return
     */
    Class getConvertClassType();

    /**
     * Data Source custom extension method
     * @param dataSource
     */
    void customize(AbstractDataSource dataSource);

    /**
     * supported rule type
     * @return
     */
    String supportRuleType();
}

