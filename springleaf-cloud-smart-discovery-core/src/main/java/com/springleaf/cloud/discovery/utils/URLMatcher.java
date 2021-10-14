package com.springleaf.cloud.discovery.utils;

import org.apache.commons.lang.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 功能说明:
 *
 * @author leon
 * @date 2021/9/22 19:45
 */
public class URLMatcher {

    public static boolean isMatchPath(HttpServletRequest request, String pattern) {
        if(StringUtils.isEmpty(pattern) || "*".equals(pattern)){
            return true;
        }
        String requestURI = request.getRequestURI();
        int i = pattern.lastIndexOf('*');
        // 没有找到星号
        if(i == -1) {
            return requestURI.equals(pattern);
        }
        // 星号在末尾
        else if (i == pattern.length() - 1) {
            return requestURI.startsWith(pattern.substring(0, i));
        }
        // 星号的开头
        else if (i == 0) {
            return requestURI.endsWith(pattern.substring(i + 1));
        }
        // 星号的字符串的中间
        else {
            String prefix = pattern.substring(0, i);
            String suffix = pattern.substring(i + 1);
            return requestURI.startsWith(prefix) && requestURI.endsWith(suffix);
        }
    }

}
