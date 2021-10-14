package com.springleaf.cloud.discovery.nacos.configuration;

import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.configuration.ConditionalOnSmartDiscoveryEnabled;
import com.springleaf.cloud.discovery.configuration.DiscoveryRibbonClientConfiguration;
import com.springleaf.cloud.discovery.nacos.NacosFilterableRegistration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 *
 * @author leon
 */
@ConditionalOnSmartDiscoveryEnabled
@Configuration
@ConditionalOnBean({DiscoveryProperties.class})
@RibbonClients(defaultConfiguration = { DiscoveryRibbonClientConfiguration.class, DiscoveryNacosRibbonClientConfiguration.class })
public class DiscoveryRibbonNacosAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public NacosFilterableRegistration filterableRegistration(Registration registration, DiscoveryProperties discoveryProperties, Environment environment){
        return new NacosFilterableRegistration(registration, discoveryProperties, environment);
    }

//    @Bean
//    @ConditionalOnMissingBean
//    public NacosDiscoveryBeanPostProcessor discoveryBeanPostProcessor(){
//        return new NacosDiscoveryBeanPostProcessor();
//    }
}
