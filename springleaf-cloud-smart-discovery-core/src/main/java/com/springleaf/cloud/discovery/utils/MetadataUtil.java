package com.springleaf.cloud.discovery.utils;

import com.springleaf.cloud.discovery.base.DiscoveryConstant;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class MetadataUtil {
    /**
     * 提取系统环境变量中配置的服务元数据
     * @param metadata
     */
    public static void filter(Map<String, String> metadata) {
        Properties properties = System.getProperties();
        Set<String> propertyNames = properties.stringPropertyNames();
        for (String propertyName : propertyNames) {
            if (propertyName.startsWith(DiscoveryConstant.METADATA_ENV_EXTENSIONS_PREFIX + ".")) {
                String key = propertyName.substring((DiscoveryConstant.METADATA_ENV_EXTENSIONS_PREFIX + ".").length());
                String value = properties.get(propertyName).toString();
                metadata.put(key, value);
            }
        }
    }

    /**
     * 提取系统环境变量中配置的服务元数据
     * @param metadata
     */
    public static void filter(List<String> metadata) {
        Properties properties = System.getProperties();
        Set<String> propertyNames = properties.stringPropertyNames();
        for (String propertyName : propertyNames) {
            if (propertyName.startsWith(DiscoveryConstant.METADATA_ENV_EXTENSIONS_PREFIX + ".")) {
                String key = propertyName.substring((DiscoveryConstant.METADATA_ENV_EXTENSIONS_PREFIX + ".").length());
                String value = properties.get(propertyName).toString();

                int index = getIndex(metadata, key);
                if (index > -1) {
                    metadata.set(index, key + "=" + value);
                } else {
                    metadata.add(key + "=" + value);
                }
            }
        }
    }

    private static int getIndex(List<String> metadata, String key) {
        for (int i = 0; i < metadata.size(); i++) {
            String result = metadata.get(i);
            if (result.startsWith(key + "=")) {
                return i;
            }
        }

        return -1;
    }
}