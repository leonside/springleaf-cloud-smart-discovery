package com.springleaf.cloud.discovery.filter;

import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.base.FilterableRegistration;
import com.springleaf.cloud.discovery.config.cache.RuleCacher;
import com.springleaf.cloud.discovery.config.model.RuleConfigWrapper;

/**
 *
 * filter context,contains {@link FilterableRegistration}、{@link RuleCacher}、{@link DiscoveryProperties}
 *
 * @author leon
 */
public class FilterContext {

    private FilterableRegistration filterableRegistration;

    private RuleCacher ruleCacher;

    protected DiscoveryProperties filterableProperties;

    public FilterableRegistration getFilterableRegistration() {
        return filterableRegistration;
    }

    public void setFilterableRegistration(FilterableRegistration filterableRegistration) {
        this.filterableRegistration = filterableRegistration;
    }

    public void setRuleCacher(RuleCacher ruleCacher) {
        this.ruleCacher = ruleCacher;
    }

    public void setFilterableProperties(DiscoveryProperties filterableProperties) {
        this.filterableProperties = filterableProperties;
    }

    public DiscoveryProperties getFilterableProperties() {
        return filterableProperties;
    }

    public RuleConfigWrapper getRuleConfig() {
        return ruleCacher.getPriority();
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder{

        private FilterableRegistration filterableRegistration;

        private RuleCacher ruleCacher;

        private DiscoveryProperties filterableProperties;

        public FilterContext build() {
            FilterContext context = new FilterContext();
            context.setFilterableRegistration(filterableRegistration);
            context.setRuleCacher(ruleCacher);
            context.setFilterableProperties(filterableProperties);
            return context;
        }

        public Builder filterableRegistration(FilterableRegistration filterableRegistration) {
            this.filterableRegistration = filterableRegistration;
            return this;
        }

        public Builder ruleCacher(RuleCacher ruleCacher){
            this.ruleCacher = ruleCacher;
            return this;
        }

        public Builder filteraleProperties(DiscoveryProperties filterableProperties){
            this.filterableProperties = filterableProperties;
            return this;
        }
    }
}
