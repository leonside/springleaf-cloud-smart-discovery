package com.springleaf.cloud.discovery.consul.decorator;

import com.ecwid.consul.v1.ConsulClient;
import com.springleaf.cloud.discovery.filter.FilterChain;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.ConsulServer;
import org.springframework.cloud.consul.discovery.ConsulServerList;

import java.util.List;

public class FilterableConsulServerList extends ConsulServerList {

    private FilterChain filterChain;

    public FilterableConsulServerList(ConsulClient client, ConsulDiscoveryProperties properties, FilterChain filterChain) {
        super(client, properties);
        this.filterChain = filterChain;
    }

    @Override
    public List<ConsulServer> getInitialListOfServers() {
        List<ConsulServer> servers = super.getInitialListOfServers();

        filter(servers);

        return servers;
    }

    @Override
    public List<ConsulServer> getUpdatedListOfServers() {
        List<ConsulServer> servers = super.getUpdatedListOfServers();

        filter(servers);

        return servers;
    }

    private void filter(List servers) {
        filterChain.onDiscoveryServerListFilter(servers);
    }

}