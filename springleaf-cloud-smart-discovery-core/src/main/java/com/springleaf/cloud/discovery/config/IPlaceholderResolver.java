package com.springleaf.cloud.discovery.config;

/**
 * Conditional rules support placeholder expressions
 * Custom placeholder can be extended by implementing the IPlaceholderResolver interface and registering as a Spring bean
 *
 * see{@link com.springleaf.cloud.discovery.condition.SpELPlaceholderResolver}
 * @author leon
 */
public interface IPlaceholderResolver {

    /**
     * Placeholder parameter analysis
     * @param value
     * @return
     */
    String resolve(String value);

    /**
     * Determine whether supported
     * @param value
     * @return
     */
    boolean support(String value);

}
