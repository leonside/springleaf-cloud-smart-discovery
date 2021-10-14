package com.springleaf.cloud.discovery.config.model;

import com.springleaf.cloud.discovery.base.DiscoveryConstant;

import javax.validation.constraints.NotEmpty;

/**
 *
 * @author leon
 */
public class BaseRule implements Comparable{

    /**
     * ID of the service for which the rule takes effect.
     *
     * In weighted routing mode, ServiceId indicates the ServiceId of service provider,
     * If this parameter is not configured, ServiceId takes effect for all Service parties
     *
     */
    @NotEmpty(message = "The service ID cannot be empty")
    protected String serviceId;

    /**
     * Condition rule
     */
    @NotEmpty(message = "The conditions cannot be empty")
    protected String conditions;

    protected boolean enabled = true;

    protected String description;

    /**
     * priority
     */
    protected int priority = 0;

    /**
     * Rule conditional language, default simple Condition, extensible custom Condition expression
     */
    protected String language = DiscoveryConstant.EXPRESSION_LANGUAGE_DEFAULT;

    public BaseRule(String serviceId, String conditions) {
        this.serviceId = serviceId;
        this.conditions = conditions;
    }

    public BaseRule() {
    }

    public String getDescription() {
        return description;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServiceId() {
        return serviceId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int compareTo(Object o) {
        return this.getPriority() - ((BaseRule)o).getPriority() ;
    }
}
