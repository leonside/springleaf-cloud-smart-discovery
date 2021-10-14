package com.springleaf.cloud.discovery.config.model;

import java.util.Map;

/**
 * For details about weight configuration, see the online documentation
 *
 * @author leon
 */
public class WeightRule extends BaseRule  implements Comparable {

    /**
     * Weight allocation Key values, such as host
     */
    private String type;

    /**
     * Weight allocation ratio, e.g: {"192.168.0.1"："10,192.168.0.2":90}
     */
    private Map<String,Integer> weightMap;


    @Override
    public int compareTo(Object o) {
        return 0;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, Integer> getWeightMap() {
        return weightMap;
    }

    public void setWeightMap(Map<String, Integer> weightMap) {
        this.weightMap = weightMap;
    }

    @Override
    public String toString() {
        return "WeightRule{" +
                "serviceId='" + serviceId + '\'' +
                ", conditions='" + conditions + '\'' +
                ", enabled=" + enabled +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", type='" + type + '\'' +
                ", weightMap=" + weightMap +
                '}';
    }
}
