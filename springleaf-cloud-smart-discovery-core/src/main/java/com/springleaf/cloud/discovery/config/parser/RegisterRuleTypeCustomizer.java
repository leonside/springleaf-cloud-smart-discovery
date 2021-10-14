package com.springleaf.cloud.discovery.config.parser;

import com.springleaf.cloud.discovery.base.DiscoveryConstant;
import com.springleaf.cloud.discovery.base.RuleType;
import com.springleaf.cloud.discovery.config.cache.RuleCacher;
import com.springleaf.cloud.discovery.config.datasource.RuleTypeCustomizer;
import com.springleaf.cloud.discovery.config.datasource.datasource.AbstractDataSource;
import com.springleaf.cloud.discovery.config.datasource.datasource.WriteableDataSource;
import com.springleaf.cloud.discovery.config.datasource.property.CustomProperty;
import com.springleaf.cloud.discovery.config.datasource.property.PropertyListener;
import com.springleaf.cloud.discovery.config.model.RegisterRule;
import com.springleaf.cloud.discovery.config.model.RuleConfig;
import com.springleaf.cloud.discovery.config.model.RuleConfigWrapper;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Config DataSource custom extension class of the register configuration rule type.
 *
 * Add the PropertListener event to the CustomProperty,to update the RuleCacher when the data changes
 *
 * see {@link AbstractDataSource}
 *
 * @author leon
 */
public class RegisterRuleTypeCustomizer implements RuleTypeCustomizer {

    private RuleCacher ruleCacher;

    public RegisterRuleTypeCustomizer(RuleCacher ruleCacher){
        this.ruleCacher = ruleCacher;
    }

    @Override
    public Class getConvertClassType() {
        return RegisterRule.class;
    }

    @Override
    public void customize(AbstractDataSource dataSource) {
        CustomProperty property = dataSource.getProperty();

        property.addListener(new PropertyListener<List<RegisterRule>>() {
            @Override
            public void configUpdate(List<RegisterRule> value) {
                putRouterRule(value);
            }

            @Override
            public void configLoad(List<RegisterRule> value) {
                putRouterRule(value);
            }

            private void putRouterRule(List<RegisterRule> value) {
                if(CollectionUtils.isEmpty(value)){
                    return;
                }

                RuleConfig ruleConfig = new RuleConfig();
                ruleConfig.setRegisters(value);

                if(dataSource instanceof WriteableDataSource){
                    ruleCacher.merge(DiscoveryConstant.DYNAMIC_RULE_KEY, new RuleConfigWrapper(ruleConfig));
                }else{
                    ruleCacher.merge(DiscoveryConstant.LOCAL_RULE_KEY, new RuleConfigWrapper(ruleConfig));
                }
            }

        });
    }

    @Override
    public String supportRuleType() {
        return RuleType.REGISTER.name();
    }
}
