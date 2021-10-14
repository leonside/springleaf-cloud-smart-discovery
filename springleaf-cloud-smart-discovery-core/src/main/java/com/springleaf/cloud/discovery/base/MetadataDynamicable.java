package com.springleaf.cloud.discovery.base;

import java.util.Map;

/**
 * Supports dynamic service meta metadata configuration. Dynamic service metadata is only cached in memory and cannot be persisted
 *
 * @author leon
 */
public interface MetadataDynamicable {

    default String getDynamicMetadata(String name){
        return getDynamicMetadatas().get(name);
    }

    default void setDynamicMetadata(String name, String value){
        getDynamicMetadatas().put(name, value);
    }

    default void clearDynamicMetadata(String name){
        getDynamicMetadatas().remove(name);
    }

    Map<String,String> getDynamicMetadatas();

    void setDynamicMetadatas(Map<String,String> metadatas);
}
