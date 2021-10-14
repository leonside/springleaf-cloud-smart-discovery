package com.springleaf.cloud.discovery;

import com.springleaf.cloud.discovery.configuration.DiscoveryAutoConfiguration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import(DiscoveryAutoConfiguration.class)
public @interface EnableSmartDiscoveryClient {
}
