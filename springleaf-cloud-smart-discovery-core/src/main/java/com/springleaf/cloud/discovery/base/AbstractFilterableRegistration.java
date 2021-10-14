package com.springleaf.cloud.discovery.base;

import com.springleaf.cloud.discovery.exception.DiscoveryException;
import com.springleaf.cloud.discovery.loadbalance.DiscoveryClientDecorator;
import com.netflix.loadbalancer.Server;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.core.env.Environment;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * abstract filterable registration
 * wrap Registration and enhancements to support Smart Discovery's filterable Discovery feature
 *
 * @author leon
 */
public abstract class AbstractFilterableRegistration implements FilterableRegistration {

    protected Registration registration;

    /**
     * smart discovery properties config
     */
    protected DiscoveryProperties filterableProperties;

    /**
     * spring environment
     */
    protected Environment environment;
    /**
     * cache service dynamic metadata
     */
    private Map<String, String> dynamicMetaCache = new ConcurrentHashMap<>();

    public AbstractFilterableRegistration(Registration registration, DiscoveryProperties filterableProperties, Environment environment){
        this.registration = registration;
        this.filterableProperties = filterableProperties;
        this.environment = environment;
    }

    @Override
    public Map<String, String> getDynamicMetadatas() {
        return dynamicMetaCache;
    }

    public Registration getRegistration() {
        return registration;
    }

    public DiscoveryProperties getFilterableProperties() {
        return filterableProperties;
    }

    public Environment getEnvironment() {
        return environment;
    }

    @Override
    public void setDynamicMetadatas(Map<String, String> metadatas) {
        dynamicMetaCache.putAll(metadatas);
    }

    @Override
    public String getServiceId() {
        return registration.getServiceId();
    }
    @Override
    public String getHost() {
        return registration.getHost();
    }
    @Override
    public int getPort() {
        return registration.getPort();
    }
    @Override
    public boolean isSecure() {
        return registration.isSecure();
    }
    @Override
    public URI getUri() {
        return registration.getUri();
    }
    @Override
    public Map<String, String> getMetadata() {
        Map<String, String> metadata = registration.getMetadata();
        metadata.put(DiscoveryConstant.METADATA_KEY_HOST, getHost());
        metadata.put(DiscoveryConstant.METADATA_KEY_PORT,String.valueOf(getPort()));
        return metadata;
    }

    @Override
    public Map<String, String> getServerMetadata(Server server) {

        if (server instanceof DiscoveryClientDecorator.ServiceInstanceAdapter) {
            Map<String, String> metadata = ((DiscoveryClientDecorator.ServiceInstanceAdapter) server).getMetadata();
            metadata.put(DiscoveryConstant.METADATA_KEY_HOST, server.getHost());
            metadata.put(DiscoveryConstant.METADATA_KEY_PORT,String.valueOf(server.getPort()));
            return metadata;
        }

        throw new DiscoveryException("Server instance isn't the type of ServiceInstanceAdapter");
    }


}
