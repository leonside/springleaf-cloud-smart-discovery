package com.springleaf.cloud.discovery.utils;

import org.apache.commons.lang.StringUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetUtils {
    private static String LOCAL_IP;

    public NetUtils() {
    }

    public static String localIP() {
        try {
            if(!StringUtils.isEmpty(LOCAL_IP)) {
                return LOCAL_IP;
            } else {
                String e = System.getProperty("com.alibaba.nacos.client.naming.local.ip", InetAddress.getLocalHost().getHostAddress());
                LOCAL_IP = e;
                return e;
            }
        } catch (UnknownHostException var1) {
            return "resolve_failed";
        }
    }

    public static void main(String []argf){
        System.out.println("ds");
    }
}