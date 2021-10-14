package com.springleaf.cloud.discovery.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@ConditionalOnProperty(value = "springleaf.smart.discovery.enabled", havingValue = "true", matchIfMissing = true)
public @interface ConditionalOnSmartDiscoveryEnabled {

}