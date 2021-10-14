package com.springleaf.cloud.discovery.condition;

import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhangliang on 2019-07-12.
 */
public class MatchUtils {

    /**
     * Verify the match according to regular expression and metadata
     * @param pattern
     * @param value
     * @param metaData
     * @return
     */
    public static boolean isMatchGlobPattern(String pattern, String value, Map<String,String> metaData) {
        if (metaData != null && !metaData.isEmpty() && pattern.startsWith("$")) {
            pattern = metaData.get(pattern.substring(1));
        }
        return isMatchGlobPattern(pattern, value);
    }


    public static boolean isMatchGlobPattern(String pattern, String value) {
        if ("*".equals(pattern))
            return true;
        //todo 空串的判断 modify by zl
        if("''".equals(pattern)){
            return StringUtils.isEmpty(value) ? true : false;
        }
        if((pattern == null || pattern.length() == 0)
                && (value == null || value.length() == 0))
            return true;
        if((pattern == null || pattern.length() == 0)
                || (value == null || value.length() == 0))
            return false;
        //todo  判断是否是正则表达式，以^开头，$结尾
        if(pattern.startsWith("^") && pattern.endsWith("$")){
            Pattern regexPattern = Pattern.compile(pattern);
            Matcher matcher = regexPattern.matcher(value);
            return matcher.find();
        }
        int i = pattern.lastIndexOf('*');
        // 没有找到星号
        if(i == -1) {
            return value.equals(pattern);
        }
        // 星号在末尾
        else if (i == pattern.length() - 1) {
            return value.startsWith(pattern.substring(0, i));
        }
        // 星号的开头
        else if (i == 0) {
            return value.endsWith(pattern.substring(i + 1));
        }
        // 星号的字符串的中间
        else {
            String prefix = pattern.substring(0, i);
            String suffix = pattern.substring(i + 1);
            return value.startsWith(prefix) && value.endsWith(suffix);
        }
    }

}