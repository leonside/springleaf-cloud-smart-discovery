package com.springleaf.cloud.discovery.config.datasource.exception;

/**
 * config datasource exception
 *
 * @author leon
 */
public class ConfigDataSourceException extends RuntimeException {
    private static final long serialVersionUID = 7975167663357170655L;

    public ConfigDataSourceException() {
        super();
    }

    public ConfigDataSourceException(String message) {
        super(message);
    }

    public ConfigDataSourceException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigDataSourceException(Throwable cause) {
        super(cause);
    }
}
