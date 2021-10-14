package com.springleaf.cloud.discovery.eureka;

import com.springleaf.cloud.discovery.base.DiscoveryConstant;
import com.springleaf.cloud.discovery.context.DiscoveryApplicationContextInitializer;
import com.springleaf.cloud.discovery.eureka.decorator.FilterableEurekaServiceRegistry;
import com.springleaf.cloud.discovery.filter.FilterChain;
import com.springleaf.cloud.discovery.utils.MetadataUtil;
import org.springframework.beans.BeansException;
import org.springframework.cloud.netflix.eureka.EurekaInstanceConfigBean;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Map;

public class EurekaApplicationContextInitializer extends DiscoveryApplicationContextInitializer {
    @Override
    protected Object afterInitialization(ConfigurableApplicationContext applicationContext, Object bean, String beanName) throws BeansException {
        if (bean instanceof EurekaServiceRegistry) {
            EurekaServiceRegistry eurekaServiceRegistry = (EurekaServiceRegistry) bean;

            return new FilterableEurekaServiceRegistry(eurekaServiceRegistry, applicationContext);

        } else if (bean instanceof EurekaInstanceConfigBean) {
            ConfigurableEnvironment environment = applicationContext.getEnvironment();

            EurekaInstanceConfigBean eurekaInstanceConfigBean = (EurekaInstanceConfigBean) bean;

            Map<String, String> metadata = eurekaInstanceConfigBean.getMetadataMap();
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