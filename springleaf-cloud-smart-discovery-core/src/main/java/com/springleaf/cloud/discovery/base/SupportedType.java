package com.springleaf.cloud.discovery.base;

/**
 * Supported types，see {@link com.springleaf.cloud.discovery.condition.IConditionFactory}
 *
 * @author leon
 */
public interface SupportedType<T> {

    /**
     * Supported types
     * @return
     */
    T support();

}
