package com.springleaf.cloud.discovery.config.cache;

import com.alibaba.fastjson.JSON;
import com.springleaf.cloud.discovery.base.RuleType;
import com.springleaf.cloud.discovery.config.datasource.datasource.WriteableDataSource;
import com.springleaf.cloud.discovery.config.datasource.exception.ConfigDataSourceException;
import com.springleaf.cloud.discovery.config.model.*;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *RuleCacher delegate class that persists the rule to the configuration center
 * when the rule configuration datasource implementation {@Link WriteableDataSource} (such as nacosDataSource) is saved
 *
 * @author leon
 */
public class RuleCacherDelegate implements RuleCacher, SmartInitializingSingleton, BeanFactoryAware {

    private AbstractRuleCacher simpleRuleCacher;

    private ConfigurableListableBeanFactory beanFactory;

    private Map<String, WriteableDataSource> dataSourceMap = new ConcurrentHashMap<>();

    public RuleCacherDelegate(AbstractRuleCacher ruleCacher){
        this.simpleRuleCacher = ruleCacher;
    }

    @Override
    public boolean putWeight(String key, List<WeightRule> weightRule) {
        if(dataSourceMap.containsKey(RuleType.WEIGHT.name())){
            dataSourceMap.get(RuleType.WEIGHT.name()).writeSource(JSON.toJSONString(weightRule));
            return true;
        }else{
            return simpleRuleCacher.putWeight(key, weightRule);
        }
    }

    @Override
    public boolean putDiscovery(String key, List<RouterRule> routerRule) {
        if(dataSourceMap.containsKey(RuleType.DISCOVERY.name())){
            dataSourceMap.get(RuleType.DISCOVERY.name()).writeSource(JSON.toJSONString(routerRule));
            return true;
        }else{
            return simpleRuleCacher.putDiscovery(key, routerRule);
        }
    }

    @Override
    public boolean putRegister(String key, List<RegisterRule> registerRule) {
        if(dataSourceMap.containsKey(RuleType.REGISTER.name())){
            dataSourceMap.get(RuleType.REGISTER.name()).writeSource(JSON.toJSONString(registerRule));
            return true;
        }else{
            return simpleRuleCacher.putRegister(key, registerRule);
        }
    }

    @Override
    public boolean putRouter(String key, List<RouterRule> routerRule) {
        if(dataSourceMap.containsKey(RuleType.ROUTER.name())){
            dataSourceMap.get(RuleType.ROUTER.name()).writeSource(JSON.toJSONString(routerRule));
            return true;
        }else{
            return simpleRuleCacher.putRouter(key, routerRule);
        }
    }
    @Override
    public boolean putRuleConfig(String key, RuleConfig ruleEntity) {
        if(dataSourceMap.containsKey(RuleType.ALL.name())){
            dataSourceMap.get(RuleType.ALL.name()).writeSource(JSON.toJSONString(ruleEntity));
            return true;
        }else{
            return simpleRuleCacher.putRuleConfig(key, ruleEntity);
        }
    }

    @Override
    public RuleConfigWrapper get(String key) {
        return simpleRuleCacher.get(key);
    }


    @Override
    public RuleConfig getRuleConfig(String key) {
        return simpleRuleCacher.getRuleConfig(key);
    }

    @Override
    public boolean clear(String key) {
        return simpleRuleCacher.clear(key);
    }

    @Override
    public RuleConfigWrapper getPriority() {
        return simpleRuleCacher.getPriority();
    }

    @Override
    public boolean containsKey(String key) {
        return simpleRuleCacher.containsKey(key);
    }

    @Override
    public boolean put(String key, RuleConfigWrapper ruleEntity) {
        return simpleRuleCacher.put(key, ruleEntity);
    }

    @Override
    public boolean merge(String key, RuleConfigWrapper ruleEntity) {
        return simpleRuleCacher.merge(key, ruleEntity);
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }

    @Override
    public void afterSingletonsInstantiated() {
        Map<String, WriteableDataSource> dataSourceBeanMap = beanFactory.getBeansOfType(WriteableDataSource.class);
        dataSourceBeanMap.values().stream().forEach(it->{
            if(dataSourceMap.containsKey(it.getRuleType())){
                throw new ConfigDataSourceException("multiple configuration dynamic datasource of " + it.getRuleType() + " exsits ");
            }
            dataSourceMap.put(it.getRuleType(), it);
        });
    }
}
