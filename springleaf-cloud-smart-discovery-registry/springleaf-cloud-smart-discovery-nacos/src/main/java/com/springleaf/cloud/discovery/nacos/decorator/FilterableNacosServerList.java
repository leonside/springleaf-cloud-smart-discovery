package com.springleaf.cloud.discovery.nacos.decorator;

import com.springleaf.cloud.discovery.filter.FilterChain;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServer;
import org.springframework.cloud.alibaba.nacos.ribbon.NacosServerList;

import java.util.List;

/**
 *
 * nacos ServerList enhancements to support service discovery filtering
 *
 * @author leon
 */
public class FilterableNacosServerList extends NacosServerList {

    private FilterChain filterChain;

    public FilterableNacosServerList(NacosDiscoveryProperties discoveryProperties, FilterChain filterChain) {
        super(discoveryProperties);
        this.filterChain = filterChain;
    }

    @Override
    public List<NacosServer> getInitialListOfServers() {
        List<NacosServer> servers = super.getInitialListOfServers();

        filter(servers);

        return servers;
    }

    @Override
    public List<NacosServer> getUpdatedListOfServers() {
        List<NacosServer> servers = super.getUpdatedListOfServers();

        filter(servers);

        return servers;
    }

    private void filter(List servers) {
        filterChain.onDiscoveryServerListFilter(servers);
    }

}