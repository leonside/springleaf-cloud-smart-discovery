package com.springleaf.cloud.discovery.consul.decorator;


import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.filter.FilterChain;
import com.netflix.loadbalancer.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.consul.discovery.ConsulDiscoveryProperties;
import org.springframework.cloud.consul.serviceregistry.ConsulRegistration;
import org.springframework.cloud.consul.serviceregistry.ConsulServiceRegistry;
import org.springframework.context.ConfigurableApplicationContext;

public class FilterableConsulServiceRegistry extends ConsulServiceRegistry {

    private ConsulServiceRegistry consulServiceRegistry;

    private ConfigurableApplicationContext applicationContext;

    public FilterableConsulServiceRegistry(ConsulDiscoveryProperties consulDiscoveryProperties, ConsulServiceRegistry nacosServiceRegistry, ConfigurableApplicationContext applicationContext) {
        super(null, null, null, null);
        this.consulServiceRegistry = nacosServiceRegistry;
        this.applicationContext = applicationContext;
    }

    @Override
    public void register(ConsulRegistration registration) {

        if(isRegisterEnabled()){
            FilterChain filterChain = applicationContext.getBean(FilterChain.class);
            filterChain.onRegisterPredicate(new Server(registration.getScheme(),registration.getHost(), registration.getPort()));
        }

        consulServiceRegistry.register(registration);
    }

    private boolean isRegisterEnabled() {
        if(!applicationContext.containsBean(FilterChain.BEAN_ID)){
            return false;
        }

        return DiscoveryProperties.isRegisterEnabled(applicationContext.getEnvironment());
    }

    @Override
    public void deregister(ConsulRegistration registration) {
        consulServiceRegistry.deregister(registration);
    }

    @Override
    public void close() {
        consulServiceRegistry.close();
    }

    @Override
    public void setStatus(ConsulRegistration registration, String status) {
        consulServiceRegistry.setStatus(registration, status);
    }

    @Override
    public Object getStatus(ConsulRegistration registration) {
        return consulServiceRegistry.getStatus(registration);
    }
}