package com.springleaf.cloud.discovery.config.model;

/**
 *
 * For details about route configuration, see the online documentation
 *
 * @author leon
 */
public class RouterRule extends BaseRule{

    /**
     * Access path, support * placeholder
     */
    private String path;

    private boolean force =false;

    public RouterRule(){
    }

    public RouterRule(String serviceId, String conditions) {
        super(serviceId, conditions);
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public boolean isForce() {
        return force;
    }

    public void setForce(boolean force) {
        this.force = force;
    }

    public static RouterRule of(String serviceId, String conditions){
        return new RouterRule(serviceId, conditions);
    }

    @Override
    public String toString() {
        return "RouterRule{" +
                "serviceId='" + serviceId + '\'' +
                ", conditions='" + conditions + '\'' +
                ", enabled=" + enabled +
                ", description='" + description + '\'' +
                ", priority=" + priority +
                ", path='" + path + '\'' +
                ", force=" + force +
                '}';
    }
}
