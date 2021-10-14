package com.springleaf.cloud.discovery.consul.configuration;

import com.ecwid.consul.v1.ConsulClient;
import com.springleaf.cloud.discovery.consul.decorator.FilterableConsulServerList;
import com.springleaf.cloud.discovery.filter.FilterChain;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.discovery.ConsulRibbonClientConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * 功能说明:
 *
 * @author leon
 */
@AutoConfigureAfter(ConsulRibbonClientConfiguration.class)
public class DiscoveryConsulRibbonClientConfiguration {

    @Autowired
    private ConsulClient client;

    @Bean
    @ConditionalOnMissingBean
    public ServerList<?> ribbonServerList(IClientConfig config, ConsulDiscoveryProperties nacosDiscoveryProperties, FilterChain filterChain) {
        FilterableConsulServerList serverList = new FilterableConsulServerList(client, nacosDiscoveryProperties,filterChain);
        serverList.initWithNiwsConfig(config);
        return serverList;
    }

}
