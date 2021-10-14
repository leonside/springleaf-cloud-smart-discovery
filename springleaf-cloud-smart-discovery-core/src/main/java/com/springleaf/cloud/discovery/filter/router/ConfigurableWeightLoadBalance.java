package com.springleaf.cloud.discovery.filter.router;

import com.google.common.collect.Maps;
import com.springleaf.cloud.discovery.base.FilterableRegistration;
import com.springleaf.cloud.discovery.base.RuleType;
import com.springleaf.cloud.discovery.condition.ConditionFactoryBuilder;
import com.springleaf.cloud.discovery.condition.IConditionFactory;
import com.springleaf.cloud.discovery.condition.IMatcher;
import com.springleaf.cloud.discovery.config.model.BaseRule;
import com.springleaf.cloud.discovery.config.model.WeightRule;
import com.springleaf.cloud.discovery.exception.DiscoveryException;
import com.springleaf.cloud.discovery.filter.FilterContext;
import com.springleaf.cloud.discovery.filter.WeightLoadBalance;
import com.netflix.loadbalancer.Server;
import javafx.util.Pair;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.*;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/21 11:14
 */
public class ConfigurableWeightLoadBalance implements WeightLoadBalance {

    public static final Logger logger = LoggerFactory.getLogger(ConfigurableWeightLoadBalance.class);

    @Autowired
    private ConditionFactoryBuilder conditionFactoryBuilder;

    @Override
    public RuleType support() {
        //todo
        return RuleType.WEIGHT;
    }

    @Override
    public Optional<Server> choose(FilterContext context, List<? extends Server> eligibleServers) {

        if(CollectionUtils.isEmpty(eligibleServers)){
            return Optional.empty();
        }
        if(eligibleServers.size() == 1){
            return Optional.of(eligibleServers.get(0));
        }

        Server choosedServer = null;

        try {
            List<Pair<Server,Integer>> serverList = new ArrayList<>();
            for(Server server : eligibleServers){
                int weight = getWeight(context, server);
                serverList.add(new Pair<Server, Integer>(server, weight));
            }
            choosedServer = new WeightRandom<Server, Integer>(serverList).random();
        } catch (Exception e) {
            logger.error("Exception causes for strategy weight-random-loadbalance, used default loadbalance", e);
        }
        return Optional.of(choosedServer);
    }

    private int getWeight(FilterContext context, Server server) {

        FilterableRegistration filterableRegistration = context.getFilterableRegistration();

        List<BaseRule> weightRule = getConfigRules(context, filterableRegistration.getServerServiceId(server));

        WeightRule priorityWeight = (CollectionUtils.isEmpty(weightRule)) ? null : (WeightRule)weightRule.get(0);

        int serverWeight = 0;


        if(ObjectUtils.isEmpty(priorityWeight.getWeightMap()) ){
            //若未配置weight规则，则读取服务配置的weight meta元数据
            serverWeight = filterableRegistration.getServerWeight(server);

        }else{
            if(StringUtils.isEmpty(priorityWeight.getType())){
                throw new DiscoveryException("Incorrect weight policy configuration. Weight type cannot be empty！");
            }

            Map<String, String> serverMetadata = filterableRegistration.getServerMetadata(server);
            String typeValue = serverMetadata.get(priorityWeight.getType());
            Map<String, Integer> weightMap = priorityWeight.getWeightMap();

            if(StringUtils.isEmpty(typeValue)){
                logger.debug("The weight metadata attribute [{}] is not configured for the service [{}],default return weight 0",priorityWeight.getType(),server.getHostPort());
                return serverWeight;
            }
            if(!weightMap.containsKey(typeValue)){
                logger.debug("The weight metadata map [{}] is not configured for the service [{}],default return weight 0",StringUtils.join(weightMap.keySet(),","),server.getHostPort());
                return serverWeight;
            }
            serverWeight = weightMap.get(typeValue);
        }

        return serverWeight;
    }


    @Override
    public boolean containWeightRule(FilterContext context, Server server) {

        FilterableRegistration filterableRegistration = context.getFilterableRegistration();
        //采用目标服务的server id
        List<BaseRule> weightRule = getConfigRules(context, filterableRegistration.getServerServiceId(server));

        WeightRule priorityWeight = (CollectionUtils.isEmpty(weightRule)) ? null : (WeightRule)weightRule.get(0);

        if(priorityWeight == null){
            return false;
        }

        //校验配置type是否合法（提供方中存在），未配置则采用服务方配置的权重元数据值
        boolean hasweight = StringUtils.isEmpty(priorityWeight.getType()) ? true : filterableRegistration.getServerMetadata(server).get(priorityWeight.getType()) != null;

        //若不存在权重配置则直接返回false
        if(!hasweight){
            return hasweight;
        }

        //存在权重配置则判断是否满足权重条件
        if(StringUtils.isEmpty(priorityWeight.getConditions())){
            return hasweight;
        }

        return matchCondition(context, priorityWeight);
    }

    private boolean matchCondition(FilterContext context,WeightRule priorityWeight) {

        IMatcher matcher = conditionFactoryBuilder.getFactory(priorityWeight).getMatcher(priorityWeight);

        return matcher.isMatch(context.getFilterableRegistration());
    }

    public class WeightRandom<K, V extends Number> {

        private TreeMap<Double, K> weightMap = new TreeMap<Double, K>();

        public WeightRandom(List<Pair<K, V>> list) {
            for (Pair<K, V> pair : list) {
                //Fixme 判断权重不为0的时候才放入路由列表
                if (pair.getValue().doubleValue() != 0D) {
                    double lastWeight = this.weightMap.size() == 0 ? 0 : this.weightMap.lastKey().doubleValue();//统一转为double
                    this.weightMap.put(pair.getValue().doubleValue() + lastWeight, pair.getKey());//权重累加
                }
            }
        }

        public K random() {
            if(weightMap.size()==0){
                return null;
            }
            double randomWeight = this.weightMap.lastKey() * Math.random();
            SortedMap<Double, K> tailMap = this.weightMap.tailMap(randomWeight, false);
            return this.weightMap.get(tailMap.firstKey());
        }
    }

}
