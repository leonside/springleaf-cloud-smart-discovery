package com.springleaf.cloud.discovery.config.model;

import javax.validation.Valid;
import java.util.List;

/**
 * For details about route configuration, see the online documentation
 *
 * @author leon
 */
public class RuleConfig {

    /**
     * Configure the service weight rule and set the weight of the service list after service route filtering
     */
    @Valid
    private List<WeightRule> weights;

    /**
     * Service Discovery rule configuration takes effect when DiscoveryClient or ServerList
     */
    @Valid
    private List<RouterRule> discovery;

    /**
     *
     * The service routing rule configuration takes effect when Service routing of Ribbon Loadbalance
     */
    @Valid
    private List<RouterRule> routers;

    /**
     * The service registration rule configuration takes effect when the service is registered to the registry center
     */
    @Valid
    private List<RegisterRule> registers;

    public RuleConfig copy(){
        RuleConfig ruleConfig = new RuleConfig();
        ruleConfig.setDiscovery(this.getDiscovery());
        ruleConfig.setRegisters(this.getRegisters());
        ruleConfig.setRouters(this.getRouters());
        ruleConfig.setWeights(this.getWeights());
        return ruleConfig;
    }

    public List<WeightRule> getWeights() {
        return weights;
    }

    public void setWeights(List<WeightRule> weights) {
        this.weights = weights;
    }

    public List<RouterRule> getDiscovery() {
        return discovery;
    }

    public void setDiscovery(List<RouterRule> discovery) {
        this.discovery = discovery;
    }

    public List<RouterRule> getRouters() {
        return routers;
    }

    public void setRouters(List<RouterRule> routers) {
        this.routers = routers;
    }

    public List<RegisterRule> getRegisters() {
        return registers;
    }

    public void setRegisters(List<RegisterRule> registers) {
        this.registers = registers;
    }

    @Override
    public String toString() {
        return "RuleConfig{" +
                "weights=" + weights +
                ", discovery=" + discovery +
                ", routers=" + routers +
                ", registers=" + registers +
                '}';
    }
}
