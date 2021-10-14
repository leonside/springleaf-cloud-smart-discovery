package com.springleaf.cloud.discovery.filter;

import com.netflix.loadbalancer.Server;

import java.util.List;

/**
 * Filter chain，
 *
 * see{@link FilterChainDelegate}
 *
 * @author leon
 */
public interface FilterChain {

    public static final String BEAN_ID = "filterChain";

    /**
     * fire service discovery filters
     * @param servers
     */
    void onDiscoveryServerListFilter(List<Server> servers);

    /**
     * fire service register predicate filters
     * @param server
     * @return
     */
    boolean onRegisterPredicate(Server server);

    /**
     * fire service router predicate filters
     * @param server
     * @return
     */
    boolean onRouterPredicate(Server server);
}
