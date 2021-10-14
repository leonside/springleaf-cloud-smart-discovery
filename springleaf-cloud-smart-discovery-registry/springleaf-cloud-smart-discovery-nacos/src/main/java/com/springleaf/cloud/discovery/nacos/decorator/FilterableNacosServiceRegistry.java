package com.springleaf.cloud.discovery.nacos.decorator;


import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.filter.FilterChain;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.alibaba.nacos.NacosDiscoveryProperties;
import org.springframework.cloud.alibaba.nacos.registry.NacosServiceRegistry;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.context.ConfigurableApplicationContext;

/**
 *
 * nacos ServerRegistry enhancements to support service register intercept
 *
 * @author  leon
 */
public class FilterableNacosServiceRegistry extends NacosServiceRegistry {

    private ConfigurableApplicationContext applicationContext;
    private NacosServiceRegistry nacosServiceRegistry;
    public FilterableNacosServiceRegistry(NacosDiscoveryProperties nacosDiscoveryProperties,NacosServiceRegistry nacosServiceRegistry, ConfigurableApplicationContext applicationContext) {
        super(nacosDiscoveryProperties);
        this.nacosServiceRegistry = nacosServiceRegistry;
        this.applicationContext = applicationContext;
    }

    @Override
    public void register(Registration registration) {

        if(isRegisterEnabled()){
            FilterChain filterChain = applicationContext.getBean(FilterChain.class);

            filterChain.onRegisterPredicate(new Server(registration.getScheme(),registration.getHost(), registration.getPort()));
        }

        nacosServiceRegistry.register(registration);
    }

    private boolean isRegisterEnabled() {
        if( !applicationContext.containsBean(FilterChain.BEAN_ID)){
            return false;
        }

        return DiscoveryProperties.isRegisterEnabled(applicationContext.getEnvironment());
    }

    @Override
    public void deregister(Registration registration) {
        nacosServiceRegistry.deregister(registration);
    }

    @Override
    public void close() {
        nacosServiceRegistry.close();
    }

    @Override
    public void setStatus(Registration registration, String status) {
        nacosServiceRegistry.setStatus(registration, status);
    }

    @Override
    public Object getStatus(Registration registration) {
        return nacosServiceRegistry.getStatus(registration);
    }
}