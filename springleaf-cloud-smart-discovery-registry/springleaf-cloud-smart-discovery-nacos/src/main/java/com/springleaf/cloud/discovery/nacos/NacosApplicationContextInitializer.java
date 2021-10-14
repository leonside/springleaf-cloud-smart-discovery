package com.springleaf.cloud.discovery.nacos;

import com.springleaf.cloud.discovery.base.DiscoveryConstant;
import com.springleaf.cloud.discovery.context.DiscoveryApplicationContextInitializer;
import com.springleaf.cloud.discovery.filter.FilterChain;
import com.springleaf.cloud.discovery.loadbalance.DiscoveryClientDecorator;
import com.springleaf.cloud.discovery.nacos.decorator.FilterableNacosServiceRegistry;
import com.springleaf.cloud.discovery.utils.MetadataUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.registry.NacosServiceRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;


/**
 *
 * By implementing {@link DiscoveryApplicationContextInitializer}
 *  to enhanced the Spring Bean, e.g: {@link NacosServiceRegistry}、{@link NacosDiscoveryProperties}
 *
 * @author leon
 */
public class NacosApplicationContextInitializer extends DiscoveryApplicationContextInitializer {
    @Override
    protected Object afterInitialization(ConfigurableApplicationContext applicationContext, Object bean, String beanName) throws BeansException {
        if (bean instanceof NacosServiceRegistry) {

            NacosServiceRegistry nacosServiceRegistry = (NacosServiceRegistry) bean;
            NacosDiscoveryProperties nacosDiscoveryProperties = applicationContext.getBean(NacosDiscoveryProperties.class);
            return new FilterableNacosServiceRegistry(nacosDiscoveryProperties,nacosServiceRegistry, applicationContext);

        } else if (bean instanceof NacosDiscoveryProperties) {

            ConfigurableEnvironment environment = applicationContext.getEnvironment();
            NacosDiscoveryProperties nacosDiscoveryProperties = (NacosDiscoveryProperties) bean;
            Map<String, String> metadata = nacosDiscoveryProperties.getMetadata();
            metadata.put(DiscoveryConstant.METADATA_KEY_APPLICATION_NAME, environment.getProperty(DiscoveryConstant.SPRING_APPLICATION_NAME));
            metadata.put(DiscoveryConstant.METADATA_KEY_CONTEXT_PATH, environment.getProperty(DiscoveryConstant.SERVLET_CONTEXT_PATH));
            //填充环境变量中传递的服务元数据
            MetadataUtil.filter(metadata);
            return bean;
        } else {
            return bean;
        }
    }
}