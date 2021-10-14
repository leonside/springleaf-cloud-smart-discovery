package com.springleaf.cloud.discovery.consul;

import com.springleaf.cloud.discovery.base.AbstractFilterableRegistration;
import com.springleaf.cloud.discovery.base.DiscoveryConstant;
import com.springleaf.cloud.discovery.base.DiscoveryProperties;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang.StringUtils;
import org.springframework.cloud.client.serviceregistry.Registration;
import org.springframework.cloud.consul.discovery.ConsulServer;
import org.springframework.core.env.Environment;

import java.util.Map;

/**
 * 功能说明:
 *
 * @author leon
 */
public class ConsulFilterableRegistration extends AbstractFilterableRegistration {

    public ConsulFilterableRegistration(Registration registration, DiscoveryProperties filterableProperties, Environment environment) {
        super(registration, filterableProperties,environment);
    }

    @Override
    public Map<String, String> getServerMetadata(Server server) {

        if (server instanceof ConsulServer) {
            ConsulServer consulServer = (ConsulServer) server;
            Map<String, String> metadata = consulServer.getMetadata();
            metadata.put(DiscoveryConstant.METADATA_KEY_HOST, server.getHost());
            metadata.put(DiscoveryConstant.METADATA_KEY_PORT,String.valueOf(server.getPort()));
            return metadata;
        }else{
            return super.getServerMetadata(server);
        }
    }

    @Override
    public String getMetadata(String metadataKey) {

        String value = getDynamicMetadata(metadataKey);

        if(StringUtils.isEmpty(value)){
            value = getMetadata().get(metadataKey);
        }
        return value;
    }

    @Override
    public int getServerWeight(Server server) {
        String weight =  getServerMetadata(server).get(DiscoveryConstant.METADATA_KEY_WEIGHT);
        return StringUtils.isEmpty(weight) ? 0 : Integer.valueOf(weight);
    }

    @Override
    public String getGroupKey() {
        if(filterableProperties.getGroup() == null){
            return null;
        }
        return filterableProperties.getGroup().getKey();
    }
}
