package com.springleaf.cloud.discovery.config.cache;


import com.springleaf.cloud.discovery.config.model.*;

import java.util.List;

/**
 * Rule caching interface， default use {@link SimpleRuleCacher}
 *
 */
public interface RuleCacher {

    boolean put(String key, RuleConfigWrapper ruleEntity);

    /**
     *
     * Merge the configuration and determine if the added RuleConfig configuration item is not empty, the original configuration is overwritten
     * for example, You can configure weightRule to override the previous configuration
     * @param key
     * @param ruleEntity
     * @return
     */
    boolean merge(String key, RuleConfigWrapper ruleEntity);

    /**
     * merge the weight configuration
     * @param key
     * @param weightRule
     * @return
     */
    boolean putWeight(String key, List<WeightRule> weightRule);

    /**
     * merge the discovery configuration
     * @param key
     * @param routerRule
     * @return
     */
    boolean putDiscovery(String key, List<RouterRule> routerRule);

    /**
     * merge the register configuration
     * @param key
     * @param registerRule
     * @return
     */
    boolean putRegister(String key, List<RegisterRule> registerRule);

    /**
     * merge the router configuration
     * @param key
     * @param routerRule
     * @return
     */
    boolean putRouter(String key, List<RouterRule> routerRule);


    /**
     * get the ruleConfigWrapper by the key
     * @param key
     * @return
     */
    RuleConfigWrapper get(String key);

    /**
     * put the ruleconfig according to the key
     * @param key
     * @param ruleEntity
     * @return
     */
    boolean putRuleConfig(String key, RuleConfig ruleEntity);

    /**
     * get the rueconfig according to the key
     * @param key
     * @return
     */
    RuleConfig getRuleConfig(String key);

    /**
     * clear the configuration
     * @param key
     * @return
     */
    boolean clear(String key) ;

    /**
     * Returns configuration information based on priority, local rule and dynamic rule in sequence
     * @return
     */
    RuleConfigWrapper getPriority();

    /**
     * Check whether the key is included
     * @param key
     * @return
     */
    boolean containsKey(String key);
}