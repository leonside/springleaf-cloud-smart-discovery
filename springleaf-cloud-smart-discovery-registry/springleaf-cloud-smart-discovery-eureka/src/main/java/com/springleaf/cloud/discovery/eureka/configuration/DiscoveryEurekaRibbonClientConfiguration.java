package com.springleaf.cloud.discovery.eureka.configuration;

import com.netflix.client.config.IClientConfig;
import com.netflix.discovery.EurekaClient;
import com.netflix.loadbalancer.ServerList;
import com.netflix.niws.loadbalancer.DiscoveryEnabledNIWSServerList;
import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.eureka.decorator.FilterableEurekaServerList;
import com.springleaf.cloud.discovery.filter.FilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.ribbon.PropertiesFactory;
import org.springframework.cloud.netflix.ribbon.RibbonClientName;
import org.springframework.cloud.netflix.ribbon.eureka.EurekaRibbonClientConfiguration;
import org.springframework.context.annotation.Bean;

import javax.inject.Provider;

/**
 *
 * @author leon
 */
@ConditionalOnBean(DiscoveryProperties.class)
@AutoConfigureAfter(EurekaRibbonClientConfiguration.class)
public class DiscoveryEurekaRibbonClientConfiguration {

    @Value("${ribbon.eureka.approximateZoneFromHostname:false}")
    private boolean approximateZoneFromHostname = false;

    @Autowired
    private PropertiesFactory propertiesFactory;

    @RibbonClientName
    private String serviceId = "client";

    @Bean
    @ConditionalOnMissingBean
    public ServerList<?> ribbonServerList(IClientConfig config, Provider<EurekaClient> eurekaClientProvider, FilterChain filterChain) {

        if (this.propertiesFactory.isSet(ServerList.class, serviceId)) {
            return this.propertiesFactory.get(ServerList.class, config, serviceId);
        }

        DiscoveryEnabledNIWSServerList discoveryServerList = new DiscoveryEnabledNIWSServerList(config, eurekaClientProvider);
        FilterableEurekaServerList serverList = new FilterableEurekaServerList(discoveryServerList, config, this.approximateZoneFromHostname, filterChain);
        serverList.setServiceId(config.getClientName());

        return serverList;
    }

}
