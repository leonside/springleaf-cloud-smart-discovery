package com.springleaf.cloud.discovery.rest;

import com.springleaf.cloud.discovery.base.DiscoveryConstant;
import com.springleaf.cloud.discovery.config.cache.RuleCacherDelegate;
import com.springleaf.cloud.discovery.config.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 *
 * @author leon
 */
@RestController
@RequestMapping(value = "/api/discovery")
public class DiscoveryController {

    @Autowired
    private RuleCacherDelegate ruleCacherDelegate;

    /**
     * Get the all rule config
     * @return
     */
    @RequestMapping(value = "/rule", method = RequestMethod.GET)
    public RuleConfig getRule() {

        RuleConfigWrapper configWrapper = ruleCacherDelegate.getPriority();

        return  configWrapper == null ? null : configWrapper.getRuleConfig();
    }

    /**
     * Get the local rule config
     * @return
     */
    @RequestMapping(value = "/localrule", method = RequestMethod.GET)
    @ResponseBody
    public RuleConfig getLocalrule() {

        RuleConfig ruleConfig = ruleCacherDelegate.getRuleConfig(DiscoveryConstant.LOCAL_RULE_KEY);

        return ruleConfig;
    }

    /**
     * Get the dynamic rule config
     * @return
     */
    @RequestMapping(value = "/dynamicrule", method = RequestMethod.GET)
    @ResponseBody
    public RuleConfig getDynamicrule() {

        RuleConfig ruleEntity = ruleCacherDelegate.getRuleConfig(DiscoveryConstant.DYNAMIC_RULE_KEY);

        return ruleEntity;
    }

    /**
     * put the all dynamic rule
     * @param ruleEntity
     * @return
     */
    @RequestMapping(value = "/dynamicrule", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage putrule(@RequestBody RuleConfig ruleEntity) {

        ruleCacherDelegate.putRuleConfig(DiscoveryConstant.DYNAMIC_RULE_KEY, ruleEntity);

        return new ResponseMessage("200","set dynamic configuration success");
    }

    /**
     * put the weight dynamic rule
     * @param weightEntities
     * @return
     */
    @RequestMapping(value = "/dynamicrule/weights", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage putweight(@RequestBody List<WeightRule> weightEntities) {

        ruleCacherDelegate.putWeight(DiscoveryConstant.DYNAMIC_RULE_KEY, weightEntities);

        return new ResponseMessage("200","set dynamic configuration of weight success");
    }

    /**
     * put the dynamic router rule
     * @param routerEntities
     * @return
     */
    @RequestMapping(value = "/dynamicrule/routers", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage putRouters(@RequestBody List<RouterRule> routerEntities) {

        ruleCacherDelegate.putRouter(DiscoveryConstant.DYNAMIC_RULE_KEY, routerEntities);

        return new ResponseMessage("200","set dynamic configuration of router success");
    }

    /**
     * put the dynamic discovery rule
     * @param discovery
     * @return
     */
    @RequestMapping(value = "/dynamicrule/discovery", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage putDiscovery(@RequestBody List<RouterRule> discovery) {

        ruleCacherDelegate.putDiscovery(DiscoveryConstant.DYNAMIC_RULE_KEY, discovery);

        return new ResponseMessage("200","set dynamic configuration of discovery success");
    }

    /**
     * put the dynamic register rule
     * @param registerEntities
     * @return
     */
    @RequestMapping(value = "/dynamicrule/registers", method = RequestMethod.POST)
    @ResponseBody
    public ResponseMessage putRegisters(@RequestBody List<RegisterRule> registerEntities) {

        ruleCacherDelegate.putRegister(DiscoveryConstant.DYNAMIC_RULE_KEY, registerEntities);

        return new ResponseMessage("200","set dynamic configuration of register success");
    }

    public static class ResponseMessage {
        protected String statusCode;
        protected String message;


        public static ResponseMessage of(String statusCode, String message){
            return new ResponseMessage(statusCode, message);
        }

        public ResponseMessage(String statusCode, String message) {
            this.statusCode = statusCode;
            this.message = message;
        }

        public String getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(String statusCode) {
            this.statusCode = statusCode;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
