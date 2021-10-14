package com.springleaf.cloud.discovery.filter.register;

import com.springleaf.cloud.discovery.base.RuleType;
import com.springleaf.cloud.discovery.condition.ConditionFactoryBuilder;
import com.springleaf.cloud.discovery.condition.IConditionFactory;
import com.springleaf.cloud.discovery.condition.IMatcher;
import com.springleaf.cloud.discovery.config.model.BaseRule;
import com.springleaf.cloud.discovery.exception.DiscoveryException;
import com.springleaf.cloud.discovery.filter.ConditionPredicate;
import com.springleaf.cloud.discovery.filter.FilterContext;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author leon
 */
public class RegisterConditionPredicate implements ConditionPredicate {

    @Autowired
    private ConditionFactoryBuilder conditionFactoryBuilder;

    @Override
    public boolean apply(FilterContext context, Server server) {

        List<? extends BaseRule> registerRules = getConfigRules(context);

        Optional.ofNullable(registerRules).orElse(new ArrayList<>()).stream().forEach(it->{

            IMatcher matcher = conditionFactoryBuilder.getFactory(it).getMatcher(it);

            boolean isMatch = matcher.isMatch(context.getFilterableRegistration());
            if(!isMatch){
                throw new DiscoveryException("Server registration is error, and ["+it.getConditions()+"] condition verification fails");
            }
        });

        return true;
    }


    @Override
    public RuleType support() {
        return RuleType.REGISTER;
    }
}
