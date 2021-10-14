package com.springleaf.cloud.discovery.config.cache;

import com.springleaf.cloud.discovery.base.DiscoveryConstant;
import com.springleaf.cloud.discovery.config.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 *
 *
 * @author leon
 */
public abstract class AbstractRuleCacher implements RuleCacher {

    public static final Logger logger = LoggerFactory.getLogger(AbstractRuleCacher.class);

    @Override
    public boolean merge(String key, RuleConfigWrapper ruleEntity) {

        if(!containsKey(key)){
            put(key, ruleEntity);
        }else{
            synchronized (this){
                RuleConfig oldRuleConfig = get(key).getRuleConfig();
                RuleConfig newRuleConfig = ruleEntity.getRuleConfig();
                RuleConfig mergedConfig = merge(oldRuleConfig, newRuleConfig);
                putRuleConfig(key, mergedConfig);
            }
        }
        return true;
    }

    private RuleConfig merge(RuleConfig oldRuleConfig, RuleConfig newRuleConfig) {
        RuleConfig mergedRuleConfig = oldRuleConfig.copy();
        if( !CollectionUtils.isEmpty(newRuleConfig.getDiscovery())){
            mergedRuleConfig.setDiscovery(newRuleConfig.getDiscovery());
        }

        if(!CollectionUtils.isEmpty(newRuleConfig.getRegisters())){
            mergedRuleConfig.setRegisters(newRuleConfig.getRegisters());
        }

        if(!CollectionUtils.isEmpty(newRuleConfig.getWeights())){
            mergedRuleConfig.setWeights(newRuleConfig.getWeights());
        }

        if(!CollectionUtils.isEmpty(newRuleConfig.getRouters())){
            mergedRuleConfig.setRouters(newRuleConfig.getRouters());
        }

        return mergedRuleConfig;
    }

    @Override
    public boolean putWeight(String key, List<WeightRule> weightRule) {
        RuleConfig rule = new RuleConfig();
        rule.setWeights(weightRule);
        return merge(key, new RuleConfigWrapper(rule));
    }

    @Override
    public boolean putDiscovery(String key, List<RouterRule> routerRule) {
        RuleConfig rule = new RuleConfig();
        rule.setDiscovery(routerRule);
        return merge(key, new RuleConfigWrapper(rule));
    }

    @Override
    public boolean putRegister(String key, List<RegisterRule> registerRule) {
        RuleConfig rule = new RuleConfig();
        rule.setRegisters(registerRule);
        return merge(key, new RuleConfigWrapper(rule));
    }

    @Override
    public boolean putRouter(String key, List<RouterRule> routerRule) {
        RuleConfig rule = new RuleConfig();
        rule.setRouters(routerRule);
        return merge(key, new RuleConfigWrapper(rule));
    }

    @Override
    public boolean putRuleConfig(String key, RuleConfig ruleEntity){
        RuleConfigWrapper ruleEntityWapper = new RuleConfigWrapper(ruleEntity);
        return put(key, ruleEntityWapper);
    }

    @Override
    public RuleConfigWrapper getPriority() {

        RuleConfigWrapper dynamicRule = get(DiscoveryConstant.DYNAMIC_RULE_KEY);
        RuleConfigWrapper localRule = get(DiscoveryConstant.LOCAL_RULE_KEY);

        if(dynamicRule != null){
            if(localRule != null){
                RuleConfig merge = merge(localRule.getRuleConfig(), dynamicRule.getRuleConfig());
                return new RuleConfigWrapper(merge);
            }else{
                return dynamicRule;
            }
        }else{
            return localRule;
        }
    }

    @Override
    public RuleConfig getRuleConfig(String key){
        RuleConfigWrapper ruleEntityWapper = get(key);

        return ruleEntityWapper != null ? ruleEntityWapper.getRuleConfig() : null;
    }

}
