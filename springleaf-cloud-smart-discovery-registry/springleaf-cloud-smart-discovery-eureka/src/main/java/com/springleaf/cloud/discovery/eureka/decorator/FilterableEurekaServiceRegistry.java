package com.springleaf.cloud.discovery.eureka.decorator;


import com.netflix.loadbalancer.Server;
import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.filter.FilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaRegistration;
import org.springframework.cloud.netflix.eureka.serviceregistry.EurekaServiceRegistry;
import org.springframework.context.ConfigurableApplicationContext;

public class FilterableEurekaServiceRegistry extends EurekaServiceRegistry {

    private ConfigurableApplicationContext applicationContext;

    private EurekaServiceRegistry eurekaServiceRegistry;

    public FilterableEurekaServiceRegistry(EurekaServiceRegistry eurekaServiceRegistry, ConfigurableApplicationContext applicationContext) {
        this.eurekaServiceRegistry = eurekaServiceRegistry;
        this.applicationContext = applicationContext;
    }

    @Override
    public void register(EurekaRegistration registration) {

        if(isRegisterEnabled()){
            FilterChain filterChain = applicationContext.getBean(FilterChain.class);
            filterChain.onRegisterPredicate(new Server(registration.getScheme(),registration.getHost(), registration.getPort()));
        }

        eurekaServiceRegistry.register(registration);
    }

    private boolean isRegisterEnabled() {
        if(!applicationContext.containsBean(FilterChain.BEAN_ID)){
            return false;
        }

        return DiscoveryProperties.isRegisterEnabled(applicationContext.getEnvironment());
    }

    @Override
    public void deregister(EurekaRegistration registration) {
        eurekaServiceRegistry.deregister(registration);
    }

    @Override
    public void close() {
        eurekaServiceRegistry.close();
    }

    @Override
    public void setStatus(EurekaRegistration registration, String status) {
        eurekaServiceRegistry.setStatus(registration, status);
    }

    @Override
    public Object getStatus(EurekaRegistration registration) {
        return eurekaServiceRegistry.getStatus(registration);
    }
}