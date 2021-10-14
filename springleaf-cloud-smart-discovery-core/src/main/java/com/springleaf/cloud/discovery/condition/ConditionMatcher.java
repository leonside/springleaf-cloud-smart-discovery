package com.springleaf.cloud.discovery.condition;

import com.netflix.loadbalancer.Server;
import com.springleaf.cloud.discovery.base.FilterableRegistration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Get ConditionMatcher with {@link IConditionFactory}
 *
 * @author leon
 */
public class ConditionMatcher extends AbstractConditionSupport implements IMatcher {

    private static final Logger logger = LoggerFactory.getLogger(ConditionRouter.class);

    public ConditionMatcher(String conditions) {
        super(conditions);
    }

    @Override
    public boolean isMatch(FilterableRegistration registrationServer) {

        if(whenCondition != null && !whenCondition.isEmpty()){
            throw new IllegalArgumentException("Illegal route rule! whenCondition must be null on single condition matching");
        }

        //The service registration only validates the WHEN condition expression
        if(thenCondition.containsKey("false")){
            return false;
        }
        return matchCondition(thenCondition, registrationServer.getMetadata(), null);
    }

    @Override
    public boolean isMatch(Server targetServer, FilterableRegistration registrationServer){

        try {
            if (! matchWhen(registrationServer)) {
                return true;
            }
            if (thenCondition == null) {
                return false;
            }
            if (matchThen(registrationServer, targetServer)) {
                return true;
            }else{
                return false;
            }
        } catch (Throwable t) {
            logger.error("Failed to execute condition router rule: " + conditions + ", invokers: " + targetServer + ", cause: " + t.getMessage(), t);
        }
        return true;
    }


}


