package com.springleaf.cloud.discovery.nacos.configuration;

import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.filter.FilterChain;
import com.springleaf.cloud.discovery.nacos.decorator.FilterableNacosServerList;
import com.netflix.client.config.IClientConfig;
import com.netflix.loadbalancer.ServerList;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author leon
 */
@ConditionalOnBean(DiscoveryProperties.class)
@AutoConfigureAfter(org.springframework.cloud.alibaba.nacos.ribbon.NacosRibbonClientConfiguration.class)
public class DiscoveryNacosRibbonClientConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ServerList<?> ribbonServerList(IClientConfig config, NacosDiscoveryProperties nacosDiscoveryProperties, FilterChain filterChain) {
        FilterableNacosServerList serverList = new FilterableNacosServerList(nacosDiscoveryProperties,filterChain);
        serverList.initWithNiwsConfig(config);
        return serverList;
    }

}
