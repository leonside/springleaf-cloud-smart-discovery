package com.springleaf.cloud.discovery.configuration;

import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.base.FilterableRegistration;
import com.springleaf.cloud.discovery.condition.ConditionFactoryBuilder;
import com.springleaf.cloud.discovery.condition.SimpleConditionFactory;
import com.springleaf.cloud.discovery.condition.IConditionFactory;
import com.springleaf.cloud.discovery.condition.SpELPlaceholderResolver;
import com.springleaf.cloud.discovery.config.IPlaceholderResolver;
import com.springleaf.cloud.discovery.config.cache.RuleCacher;
import com.springleaf.cloud.discovery.config.cache.AbstractRuleCacher;
import com.springleaf.cloud.discovery.config.cache.RuleCacherDelegate;
import com.springleaf.cloud.discovery.config.cache.SimpleRuleCacher;
import com.springleaf.cloud.discovery.config.parser.*;
import com.springleaf.cloud.discovery.filter.FilterChain;
import com.springleaf.cloud.discovery.filter.FilterChainDelegate;
import com.springleaf.cloud.discovery.filter.FilterContext;
import com.springleaf.cloud.discovery.filter.WeightLoadBalance;
import com.springleaf.cloud.discovery.filter.register.GenericRegisterConditionPredicate;
import com.springleaf.cloud.discovery.filter.router.ConfigurableWeightLoadBalance;
import com.springleaf.cloud.discovery.filter.router.DynamicRouterConditionPredicate;
import com.springleaf.cloud.discovery.filter.serverlist.GroupIsolationServerListConditionFilter;
import com.springleaf.cloud.discovery.filter.serverlist.LoadBalanceServerListConditionFilter;
import com.springleaf.cloud.discovery.rest.DiscoveryController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author leon
 */
@ConditionalOnSmartDiscoveryEnabled
@Configuration
@EnableConfigurationProperties(DiscoveryProperties.class)
public class DiscoveryAutoConfiguration {


    @Bean
    public DiscoveryController discoveryController(){
        return new DiscoveryController();
    }

    @Bean
    @ConditionalOnMissingBean
    public FilterContext filterContext(FilterableRegistration filterableRegistration, RuleCacher ruleCacher, DiscoveryProperties discoveryProperties) {

        return FilterContext.builder()
                .filterableRegistration(filterableRegistration)
                .ruleCacher(ruleCacher)
                .filteraleProperties(discoveryProperties)
                .build();
    }

    @Bean
    @ConditionalOnMissingBean
    public FilterChain filterChain(){
        return new FilterChainDelegate();
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleRuleCacher ruleCacher(){
        return new SimpleRuleCacher();
    }

    @Bean
    @ConditionalOnMissingBean
    public RuleCacherDelegate ruleCacherDelegate(AbstractRuleCacher ruleCacher){
        return new RuleCacherDelegate(ruleCacher);
    }

    @Bean
    @ConditionalOnMissingBean
    public IPlaceholderResolver placeholderResolver(){
        return new SpELPlaceholderResolver();
    }

    @Bean
    @ConditionalOnMissingBean
    public SimpleConditionFactory simpleConditionFactory(){
        return new SimpleConditionFactory();
    }

    @Bean
    public ConditionFactoryBuilder conditionFactoryBuilder(List<IConditionFactory> conditionFactoryList){
        Map<String,IConditionFactory> conditionFactoryMap = new ConcurrentHashMap<>();
        conditionFactoryList.stream().forEach(it->{
            conditionFactoryMap.put(it.support(), it);
        });

        return new ConditionFactoryBuilder(conditionFactoryMap);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "springleaf.smart.discovery.weight.enabled", havingValue = "true", matchIfMissing = true)
    public WeightLoadBalance weightLoadBalance(){
        return new ConfigurableWeightLoadBalance();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "springleaf.smart.discovery.register.enabled", havingValue = "true", matchIfMissing = true)
    public GenericRegisterConditionPredicate registerConditionPredicate(){
        return new GenericRegisterConditionPredicate();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "springleaf.smart.discovery.router.enabled", havingValue = "true", matchIfMissing = true)
    public DynamicRouterConditionPredicate dynamicRouterConditionPredicate(){
        return new DynamicRouterConditionPredicate();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "springleaf.smart.discovery.discovery.enabled", havingValue = "true", matchIfMissing = true)
    public LoadBalanceServerListConditionFilter loadBalanceServerListConditionFilter(){
        return new LoadBalanceServerListConditionFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(value = "springleaf.smart.discovery.group.isolation.enabled", havingValue = "true", matchIfMissing = false)
    public GroupIsolationServerListConditionFilter groupIsolationServerListConditionFilter(){
        return new GroupIsolationServerListConditionFilter();
    }

    //    @Bean
//    @ConditionalOnMissingBean
//    public EnvironmentRuleConfigParser environmentRuleConfigParser(DiscoveryProperties discoveryProperties){
//        return new EnvironmentRuleConfigParser(discoveryProperties);
//    }
//
//    @Bean
//    @ConditionalOnMissingBean
//    public JsonFileRuleConfigParser jsonFileRuleConfigParser(DiscoveryProperties discoveryProperties){
//        return new JsonFileRuleConfigParser(discoveryProperties);
//    }

    @Bean
    @ConditionalOnMissingBean
    public DiscoveryConfigRuleLoadListener discoveryConfigRuleLoadListener(DiscoveryProperties discoveryProperties){
        return new DiscoveryConfigRuleLoadListener(discoveryProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public DiscoveryRuleTypeCustomizer discoveryRuleTypeCustomizer(AbstractRuleCacher ruleCacher){
        return new DiscoveryRuleTypeCustomizer(ruleCacher);
    }

    @Bean
    @ConditionalOnMissingBean
    public WeightRuleTypeCustomizer weightRuleTypeCustomizer(AbstractRuleCacher ruleCacher){
        return new WeightRuleTypeCustomizer(ruleCacher);
    }

    @Bean
    @ConditionalOnMissingBean
    public RouterRuleTypeCustomizer routerRuleTypeCustomizer(AbstractRuleCacher ruleCacher){
        return new RouterRuleTypeCustomizer(ruleCacher);
    }

    @Bean
    @ConditionalOnMissingBean
    public RegisterRuleTypeCustomizer registerRuleTypeCustomizer(AbstractRuleCacher ruleCacher){
        return new RegisterRuleTypeCustomizer(ruleCacher);
    }

    @Bean
    @ConditionalOnMissingBean
    public AllDiscoveryRuleTypeCustomizer allDiscoveryRuleTypeCustomizer(AbstractRuleCacher ruleCacher){
        return new AllDiscoveryRuleTypeCustomizer(ruleCacher);
    }
}
