package com.springleaf.cloud.discovery.consul;

import com.springleaf.cloud.discovery.base.DiscoveryConstant;
import com.springleaf.cloud.discovery.consul.decorator.FilterableConsulServiceRegistry;
import com.springleaf.cloud.discovery.context.DiscoveryApplicationContextInitializer;
import com.springleaf.cloud.discovery.filter.Filter;
import com.springleaf.cloud.discovery.filter.FilterChain;
import com.springleaf.cloud.discovery.filter.FilterContext;
import com.springleaf.cloud.discovery.utils.MetadataUtil;
import org.springframework.beans.BeansException;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.List;

public class ConsulApplicationContextInitializer extends DiscoveryApplicationContextInitializer {
    @Override
    protected Object afterInitialization(ConfigurableApplicationContext applicationContext, Object bean, String beanName) throws BeansException {
        if (bean instanceof ConsulServiceRegistry) {
            ConsulServiceRegistry nacosServiceRegistry = (ConsulServiceRegistry) bean;

            ConsulDiscoveryProperties nacosDiscoveryProperties = applicationContext.getBean(ConsulDiscoveryProperties.class);

            return new FilterableConsulServiceRegistry(nacosDiscoveryProperties,nacosServiceRegistry, applicationContext);

        } else if (bean instanceof ConsulDiscoveryProperties) {
            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            ConsulDiscoveryProperties consulDiscoveryProperties = (ConsulDiscoveryProperties) bean;

            consulDiscoveryProperties.setPreferIpAddress(true);

            List<String> tags = consulDiscoveryProperties.getTags();

            tags.add(DiscoveryConstant.METADATA_KEY_APPLICATION_NAME + "=" + environment.getProperty(DiscoveryConstant.SPRING_APPLICATION_NAME));
            tags.add(DiscoveryConstant.METADATA_KEY_CONTEXT_PATH + "=" + environment.getProperty(DiscoveryConstant.SERVLET_CONTEXT_PATH));

            //填充环境变量中传递的服务元数据
            MetadataUtil.filter(tags);
            return bean;
        } else {
            return bean;
        }
    }
}