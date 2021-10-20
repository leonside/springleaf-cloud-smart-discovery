package com.springleaf.cloud.discovery.condition;

import com.springleaf.cloud.discovery.base.FilterableRegistration;
import com.netflix.loadbalancer.Server;
import com.springleaf.cloud.discovery.filter.register.GenericRegisterConditionPredicate;

/**
 *
 * Conditional matching rules, such as for service registration scenarios
 *  for example:host=192.168.*
 *
 * Use in the service registry predicate,see {@link GenericRegisterConditionPredicate}
 *
 * @author leon
 */
public interface IMatcher {

    /**
     * predicate whether conditions match based on the specified FilterableRegistration
     * @param registrationServer
     * @return
     */
    boolean isMatch(FilterableRegistration registrationServer);

    /**
     * predicate whether conditions match based on the specified Server、 FilterableRegistration
     * @param targetServer
     * @param registrationServer
     * @return
     */
    boolean isMatch(Server targetServer, FilterableRegistration registrationServer);
}
