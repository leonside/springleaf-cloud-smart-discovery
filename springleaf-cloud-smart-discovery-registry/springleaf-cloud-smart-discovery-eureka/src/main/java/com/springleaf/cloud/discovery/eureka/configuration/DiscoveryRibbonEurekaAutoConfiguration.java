package com.springleaf.cloud.discovery.eureka.configuration;

import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.configuration.ConditionalOnSmartDiscoveryEnabled;
import com.springleaf.cloud.discovery.configuration.DiscoveryRibbonClientConfiguration;
import com.springleaf.cloud.discovery.eureka.EurekaFilterableRegistration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
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
@ConditionalOnBean({DiscoveryProperties.class})
@RibbonClients(defaultConfiguration = { DiscoveryRibbonClientConfiguration.class, DiscoveryEurekaRibbonClientConfiguration.class })
public class DiscoveryRibbonEurekaAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EurekaFilterableRegistration filterableRegistration(Registration registration, DiscoveryProperties discoveryProperties, Environment environment){
        return new EurekaFilterableRegistration(registration, discoveryProperties, environment);
    }


}
