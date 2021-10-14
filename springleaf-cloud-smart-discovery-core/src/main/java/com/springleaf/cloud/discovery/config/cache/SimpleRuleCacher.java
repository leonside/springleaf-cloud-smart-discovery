package com.springleaf.cloud.discovery.config.cache;

import com.springleaf.cloud.discovery.config.model.RuleConfigWrapper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * simple ruleCacher, Memory cache is used by default
 *
 * @author leon
 */
public class SimpleRuleCacher extends AbstractRuleCacher{

    private Map<String, RuleConfigWrapper> simpleCache = new ConcurrentHashMap<>();

    @Override
    public boolean put(String key, RuleConfigWrapper ruleEntity) {

        simpleCache.put(key, ruleEntity);

        return Boolean.TRUE;
    }

    @Override
    public RuleConfigWrapper get(String key) {
        try {
            return simpleCache.get(key);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean clear(String key) {

        simpleCache.remove(key);

        return Boolean.TRUE;
    }

    @Override
    public boolean containsKey(String key) {
        return simpleCache.containsKey(key);
    }

}
