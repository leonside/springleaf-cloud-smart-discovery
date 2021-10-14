package com.springleaf.cloud.discovery.config.parser;

import com.springleaf.cloud.discovery.base.DiscoveryConstant;
import com.springleaf.cloud.discovery.base.RuleType;
import com.springleaf.cloud.discovery.config.cache.RuleCacher;
import com.springleaf.cloud.discovery.config.datasource.RuleTypeCustomizer;
import com.springleaf.cloud.discovery.config.datasource.datasource.AbstractDataSource;
import com.springleaf.cloud.discovery.config.datasource.datasource.WriteableDataSource;
import com.springleaf.cloud.discovery.config.datasource.property.CustomProperty;
import com.springleaf.cloud.discovery.config.datasource.property.PropertyListener;
import com.springleaf.cloud.discovery.config.model.RuleConfig;
import com.springleaf.cloud.discovery.config.model.RuleConfigWrapper;

/**
 * Config DataSource custom extension class of the global configuration rule type.
 * Add the PropertListener event to the CustomProperty,to update the RuleCacher when the data changes
 * see {@link AbstractDataSource}
 *
 * @author leon
 */
public class AllDiscoveryRuleTypeCustomizer implements RuleTypeCustomizer {

    private RuleCacher ruleCacher;

    public AllDiscoveryRuleTypeCustomizer(RuleCacher ruleCacher){
        this.ruleCacher = ruleCacher;
    }

    @Override
    public Class getConvertClassType() {
        return RuleConfig.class;
    }

    @Override
    public void customize(AbstractDataSource dataSource) {
        CustomProperty property = dataSource.getProperty();

        property.addListener(new PropertyListener<RuleConfig>() {
            @Override
            public void configUpdate(RuleConfig value) {
                putRouterRule(value);
            }

            @Override
            public void configLoad(RuleConfig value) {
                putRouterRule(value);
            }

            private void putRouterRule(RuleConfig ruleConfig) {

                if(ruleConfig !=null){
                    if(dataSource instanceof WriteableDataSource){
                        ruleCacher.merge(DiscoveryConstant.DYNAMIC_RULE_KEY, new RuleConfigWrapper(ruleConfig));
                    }else{
                        ruleCacher.merge(DiscoveryConstant.LOCAL_RULE_KEY, new RuleConfigWrapper(ruleConfig));
                    }
                }
            }

        });
    }

    @Override
    public String supportRuleType() {
        return RuleType.ALL.name();
    }
}
