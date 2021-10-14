package com.springleaf.cloud.discovery.consul.configuration;

import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.configuration.ConditionalOnSmartDiscoveryEnabled;
import com.springleaf.cloud.discovery.configuration.DiscoveryRibbonClientConfiguration;
import com.springleaf.cloud.discovery.consul.ConsulFilterableRegistration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 功能说明:
 *
 * @author leon
 */
@ConditionalOnSmartDiscoveryEnabled
@Configuration
@RibbonClients(defaultConfiguration = { DiscoveryRibbonClientConfiguration.class, DiscoveryConsulRibbonClientConfiguration.class })
public class DiscoveryRibbonConsulAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ConsulFilterableRegistration filterableRegistration(Registration registration, DiscoveryProperties discoveryProperties, Environment environment){
        return new ConsulFilterableRegistration(registration, discoveryProperties, environment);
    }


}
