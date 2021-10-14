//package com.springleaf.cloud.discovery;
//
//import com.springleaf.cloud.discovery.configuration.DiscoveryAutoConfiguration;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
//import org.springframework.cloud.commons.util.SpringFactoryImportSelector;
//import org.springframework.context.EnvironmentAware;
//import org.springframework.context.annotation.DeferredImportSelector;
//import org.springframework.core.env.Environment;
//import org.springframework.core.type.AnnotationMetadata;
//
///**
// * @author leon
// */
//public class SmartDiscoveryClientImportSelector implements DeferredImportSelector, EnvironmentAware {
//
//    private Environment environment;
//
//    @Override
//    public String[] selectImports(AnnotationMetadata metadata) {
//        if(isEnabled()){
//            return new String[]{DiscoveryAutoConfiguration.class.getName()};
//        }else{
//            return new String[0];
//        }
//    }
//
//    protected boolean isEnabled() {
//        return getEnvironment().getProperty("springleaf.smart.discovery.enabled",
//                Boolean.class, Boolean.TRUE);
//    }
//
//    public Environment getEnvironment() {
//        return environment;
//    }
//
//    @Override
//    public void setEnvironment(Environment environment) {
//        this.environment = environment;
//    }
//}
