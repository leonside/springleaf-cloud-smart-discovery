package com.springleaf.cloud.discovery.base;

import com.springleaf.cloud.discovery.exception.DiscoveryException;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang.StringUtils;

import java.net.URI;
import java.util.Map;

/**
 * Filterable Registration, implements Groupable、MetadataDynamicable interface
 *
 * @author leon
 */
public interface FilterableRegistration extends Groupable, MetadataDynamicable {

    default String getInstanceId() {
        return null;
    }

    /**
     * @return The service ID as registered.
     */
    String getServiceId();

    /**
     * @return The hostname of the registered service instance.
     */
    String getHost();

    /**
     * @return The port of the registered service instance.
     */
    int getPort();

    /**
     * @return Whether the port of the registered service instance uses HTTPS.
     */
    boolean isSecure();

    /**
     * @return The service URI address.
     */
    URI getUri();

    /**
     * @return The key / value pair metadata associated with the service instance.
     */
    Map<String, String> getMetadata();

    /**
     * @return The scheme of the service instance.
     */
    default String getScheme() {
        return null;
    }

    /**
     * gets the metadata Maps based on the specified server
     * @param server
     * @return
     */
    Map<String, String> getServerMetadata(Server server);

    /**
     * gets the metadata value based on the specified metadata key
     * @param metadataKey
     * @return
     */
    String getMetadata(String metadataKey);

    /**
     * gets the server weight based on the specified server
     * @param server
     * @return
     */
    int getServerWeight(Server server);

    /**
     * gets the server id based on the specified server
     * @param server
     * @return
     */
    default String getServerServiceId(Server server){
        String serviceId = getServerMetadata(server).get(DiscoveryConstant.METADATA_KEY_APPLICATION_NAME);
        if(StringUtils.isEmpty(serviceId)){
            throw new DiscoveryException("ServerID of [" + server.getHostPort() + "] can not be null");
        }
        return serviceId;
    }

    /**
     * gets current server group based on the default groupkey
     * @return
     */
    @Override
    default String getGroup(){
        return getGroup(getGroupKey());
    }

    /**
     * gets current server group based on the specified groupkey
     * @param groupKey
     * @return
     */
    @Override
    default String getGroup(String groupKey){
        String groupValue = getMetadata(groupKey);

        if (StringUtils.isEmpty(groupValue)) {
            throw new DiscoveryException("The value is null or empty for metadata key=" + groupKey + ", please check your configuration");
        }

        return groupValue;
    }

}
