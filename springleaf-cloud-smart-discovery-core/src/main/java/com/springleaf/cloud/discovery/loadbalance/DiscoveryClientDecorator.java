package com.springleaf.cloud.discovery.loadbalance;

import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.springleaf.cloud.discovery.filter.Filter;
import com.springleaf.cloud.discovery.filter.FilterChain;
import com.netflix.loadbalancer.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * wrap discoverClient to support service discovery filting
 *
 * @author leon
 */
public class DiscoveryClientDecorator implements DiscoveryClient {

     private static final Logger logger = LoggerFactory.getLogger(DiscoveryClientDecorator.class);

    private DiscoveryClient discoveryClient;

    private ConfigurableApplicationContext applicationContext;

    public DiscoveryClientDecorator(DiscoveryClient discoveryClient, ConfigurableApplicationContext applicationContext) {
        this.discoveryClient = discoveryClient;
        this.applicationContext = applicationContext;
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {

        List<ServiceInstance> instances = getRealInstances(serviceId);

        if(isDiscoveryEnabled()){
            FilterChain filterChain = applicationContext.getBean(FilterChain.class);

            int originalSize = instances.size();

            List filteredInstances = instances.stream().map(ServiceInstanceAdapter::new).collect(Collectors.toList());

            filterChain.onDiscoveryServerListFilter(filteredInstances);

            List<ServiceInstance> serviceInstances = (List<ServiceInstance>) filteredInstances.stream().map(it -> {
                return ((ServiceInstanceAdapter) it).getServiceInstance();
            }).collect(Collectors.toList());

            logger.debug("[Discovery Filtering] Number of original service instances is {}, Number of filtered service instances is {}", originalSize, serviceInstances.size());

            return serviceInstances;

        }else{
            return instances;
        }

    }

    private boolean isDiscoveryEnabled() {
        if( !applicationContext.containsBean(FilterChain.BEAN_ID)){
            return false;
        }

        return DiscoveryProperties.isDiscoveryEnabled(applicationContext.getEnvironment());
    }

    public List<ServiceInstance> getRealInstances(String serviceId) {
        return discoveryClient.getInstances(serviceId);
    }

    @Override
    public List<String> getServices() {
        return discoveryClient.getServices();
    }

    @Override
    public String description() {
        return discoveryClient.description();
    }


    public class ServiceInstanceAdapter extends Server {

        private ServiceInstance serviceInstance ;
        public ServiceInstanceAdapter(ServiceInstance serviceInstance) {
            super(serviceInstance.getScheme(), serviceInstance.getHost(), serviceInstance.getPort());
            this.serviceInstance = serviceInstance;
        }

        public ServiceInstance getServiceInstance() {
            return serviceInstance;
        }

        public Map<String,String> getMetadata() {
            return serviceInstance.getMetadata();
        }
    }
}