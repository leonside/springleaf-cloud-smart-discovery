package com.springleaf.cloud.discovery.config.model;

import com.springleaf.cloud.discovery.base.RuleType;
import com.springleaf.cloud.discovery.exception.DiscoveryException;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *  the wrapper class  of RuleConfig, and enhances RuleConfig functionality
 *
 * @author leon
 */
public class RuleConfigWrapper {

    private RuleConfig ruleConfig;

    /**
     * the list of register rule
     */
    private List<RegisterRule> globalRegisters = new ArrayList<>();

    /**
     * the map of register rule, the key is serviceId
     */
    private Map<String,List<RegisterRule>> serverRegisters = new ConcurrentHashMap<>();

    /**
     * the list of router rule
     */
    private List<RouterRule> globalRouters = new ArrayList<>();

    /**
     * the mep of router rule, the key is serviceId
     */
    private Map<String, List<RouterRule>> serverRouters = new ConcurrentHashMap<>();

    /**
     * the global of discovery rule
     */
    private List<RouterRule> globalDiscovery = new ArrayList<>();

    /**
     * the map of discovery rule, the key is serviceId
     */
    private Map<String, List<RouterRule>> serverDiscovery = new ConcurrentHashMap<>();

    /**
     * the list of global weight rule
     */
    private List<WeightRule> globalWeights = new ArrayList<>();

    /**
     * the mep of weight rule, the key is serviceId
     */
    private Map<String, List<WeightRule>> serverWeights = new ConcurrentHashMap<>();


    public RuleConfigWrapper(RuleConfig ruleConfig){
        this.ruleConfig = ruleConfig;
        if(ruleConfig == null){
            throw new DiscoveryException(" ruleConfig can not be null");
        }

        init();
    }

    public RuleConfig getRuleConfig() {
        return ruleConfig;
    }

    /**
     * init the ruleConfig wrapper
     */
    private void init() {
        //Initialize the service registration configuration
        if(!CollectionUtils.isEmpty(ruleConfig.getRegisters())){
            init(ruleConfig.getRegisters(), globalRegisters, serverRegisters);
        }

        //Example Initialize the service route configuration
        if(!CollectionUtils.isEmpty(ruleConfig.getRouters())){
            init(ruleConfig.getRouters(), globalRouters, serverRouters);
        }

        //Initialize the service discovery configuration
        if(!CollectionUtils.isEmpty(ruleConfig.getDiscovery())){
            init(ruleConfig.getDiscovery(), globalDiscovery, serverDiscovery);
        }

        // Initialize the service weight configuration
        if(!CollectionUtils.isEmpty(ruleConfig.getWeights())){
            init(ruleConfig.getWeights(), globalWeights, serverWeights);
        }

    }

    private <T extends BaseRule> void init(List<T> allConfig, List<T> globalConfig, Map<String,List<T>> serviceConfig){
        for(T entity :  allConfig){
            if(StringUtils.isEmpty(entity.getServiceId())){
                globalConfig.add(entity);
            }else{
                if(serviceConfig.get(entity.getServiceId()) != null){
                    serviceConfig.get(entity.getServiceId()).add(entity);
                }else{
                    List<T> entities = new ArrayList<>();
                    entities.add(entity);
                    serviceConfig.put(entity.getServiceId(),entities);
                }
            }
        }
    }

    public List<RegisterRule> getGlobalRegisters() {
        return globalRegisters;
    }


    /**
     * Get all ordered discovery rule, The service configuration takes precedence over the global configuration
     * @param serverId
     * @return
     */
    public List<RouterRule> getAllDiscovery(String serverId){
        if(ruleConfig == null){
            return null;
        }
        return getAllConfig(serverId, globalDiscovery, serverDiscovery);
    }

    /**
     * Get all ordered register rule, The service configuration takes precedence over the global configuration
     * @param serverId
     * @return
     */
    public List<RegisterRule> getAllRegisters(String serverId){
        if(ruleConfig == null){
            return null;
        }
        return getAllConfig(serverId, globalRegisters, serverRegisters);
    }

    /**
     * Get all ordered router rule, The service configuration takes precedence over the global configuration
     * @param serverId
     * @return
     */
    public List<RouterRule> getAllRouters(String serverId){
        if(ruleConfig == null){
            return null;
        }
        return getAllConfig(serverId, globalRouters, serverRouters);
    }

    /**
     * Get all ordered weight rule, The service configuration takes precedence over the global configuration
     * @param serverId
     * @return
     */
    public List<WeightRule> getAllWeights(String serverId){
        if(ruleConfig == null){
            return null;
        }
        return getAllConfig(serverId, globalWeights, serverWeights);
    }

    /**
     * Get all ordered rule by specified ruleType and serviceId, The service configuration takes precedence over the global configuration
     * @param ruleType
     * @param serviceId
     * @return
     */
    public List<? extends BaseRule> getAllRules(RuleType ruleType, String serviceId) {
        if(ruleType == RuleType.DISCOVERY){
            return getAllDiscovery(serviceId);
        }else if(ruleType == RuleType.WEIGHT){
            return getAllWeights(serviceId);
        }else if(ruleType == RuleType.REGISTER){
            return getAllRegisters(serviceId);
        }else if(ruleType == RuleType.ROUTER){
            return getAllRouters(serviceId);
        }else{
            throw new UnsupportedOperationException("暂不支持的规则类型[" + ruleType + "]");
        }
    }

    private <T extends BaseRule> List<T> getAllConfig(String serverId, List<T> globalConfig, Map<String,List<T>> serviceConfigMap){
        List<T> allConfigs = new ArrayList<>();

        //添加服务单独的配置，优先级高于全局配置
        if(!CollectionUtils.isEmpty(serviceConfigMap)  ){
            if(serviceConfigMap.containsKey(serverId) && !CollectionUtils.isEmpty(serviceConfigMap.get(serverId))){
                List<T> serviceConfigs = serviceConfigMap.get(serverId);
                Collections.sort(serviceConfigs);
                mergeRuleConfigs(allConfigs, serviceConfigs );
            }
        }

        //添加全局配置
        Collections.sort(globalConfig);
        mergeRuleConfigs(allConfigs, globalConfig);
        return allConfigs;
    }

    private <T extends BaseRule> void mergeRuleConfigs(List<T> targetConfigs, List<T> addConfigs){
        //添加配置，并判断是否启用
        if(!CollectionUtils.isEmpty(addConfigs)){
            for (T config : addConfigs){
                if(config.isEnabled()){
                    targetConfigs.add(config);
                }
            }
        }
    }

    public Map<String, List<RegisterRule>> getServerRegisters() {
        return serverRegisters;
    }

    public List<RouterRule> getGlobalRouters() {
        return globalRouters;
    }

    public Map<String, List<RouterRule>> getServerRouters() {
        return serverRouters;
    }

    public List<RouterRule> getGlobalDiscovery() {
        return globalDiscovery;
    }

    public Map<String, List<RouterRule>> getServerDiscovery() {
        return serverDiscovery;
    }

    public List<WeightRule> getGlobalWeights() {
        return globalWeights;
    }

    public Map<String, List<WeightRule>> getServerWeights() {
        return serverWeights;
    }

}
