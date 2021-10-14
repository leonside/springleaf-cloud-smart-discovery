package com.springleaf.cloud.discovery.config.model;

/**
 *
 * For details about register configuration, see the online documentation
 *
 * @author leon
 */
public class RegisterRule extends BaseRule{

    @Override
    public String toString() {
        return "RegisterRule{" +
                "serviceId='" + serviceId + '\'' +
                ", conditions='" + conditions + '\'' +
                ", enabled=" + enabled +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                '}';
    }
}
