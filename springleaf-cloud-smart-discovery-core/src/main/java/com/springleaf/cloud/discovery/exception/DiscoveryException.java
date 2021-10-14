package com.springleaf.cloud.discovery.exception;

/**
 *
 * @author leon
 */
public class DiscoveryException extends RuntimeException {
    private static final long serialVersionUID = 7975167663357170655L;

    public DiscoveryException() {
        super();
    }

    public DiscoveryException(String message) {
        super(message);
    }

    public DiscoveryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DiscoveryException(Throwable cause) {
        super(cause);
    }
}
