package com.springleaf.cloud.discovery.eureka.decorator;

import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledServer;
import com.springleaf.cloud.discovery.filter.FilterChain;
import org.springframework.cloud.netflix.ribbon.eureka.DomainExtractingServerList;

import java.util.List;

public class FilterableEurekaServerList extends DomainExtractingServerList {

    private FilterChain filterChain;

    //todo ?
    private String serviceId;

    public FilterableEurekaServerList(ServerList<DiscoveryEnabledServer> list, IClientConfig clientConfig, boolean approximateZoneFromHostname, FilterChain filterChain) {
        super(list, clientConfig, approximateZoneFromHostname);
        this.filterChain = filterChain;
    }

    @Override
    public List<DiscoveryEnabledServer> getInitialListOfServers() {
        List<DiscoveryEnabledServer> servers = super.getInitialListOfServers();

        filter(servers);

        return servers;
    }

    @Override
    public List<DiscoveryEnabledServer> getUpdatedListOfServers() {
        List<DiscoveryEnabledServer> servers = super.getUpdatedListOfServers();

        filter(servers);

        return servers;
    }

    private void filter(List servers) {
        filterChain.onDiscoveryServerListFilter(servers);
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}