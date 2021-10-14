/*
 * Copyright 1999-2012 Alibaba Group.
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.springleaf.cloud.discovery.condition;

import com.springleaf.cloud.discovery.base.FilterableRegistration;
import com.netflix.loadbalancer.Server;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Abstract Simple condition that supports conditional routing rules and Conditional matching rules, such as:
 * <p>
 * 1) Conditional routing rules, such as service routing and service discovery scenarios,for example:
 *
 *      host=192.168.* & region=3502 => host=192.168.10.1
 * </p>
 * <p>
 * 2) Conditional matching rules, such as for service registration scenarios,for example:
 *      host=192.168.*
 * </p>
 * See the online documentation for details
 * see {@link ConditionMatcher}
 * see {@link ConditionRouter}
 * @author leon
 */
public abstract class AbstractConditionSupport {

    private static final Logger logger = LoggerFactory.getLogger(AbstractConditionSupport.class);

    /**
     * Conditional string
     */
    protected String conditions;

    /**
     * Conditional rules for parsing
     */
    protected Map<String, MatchPair> whenCondition;
    /**
     * If false=>version=1.0, then select the opposite value. If true=>version! = 1.0
     */
    protected Map<String, MatchPair> thenCondition;

    private static Pattern ROUTE_PATTERN = Pattern.compile("([&!=,]*)\\s*([^&!=,\\s]+)");

    public AbstractConditionSupport(String conditions) {

        try {
            if (conditions == null || conditions.trim().length() == 0) {
                throw new IllegalArgumentException("Illegal route rule!");
            }
//            rule = rule.replace("consumer.", "").replace("provider.", "");
            int i = conditions.indexOf("=>");
            String whenRule = null;
            String thenRule = null;
            if(i < 0){
                whenRule = null;
                thenRule = conditions.trim();
//                singleCondition = true;
            }else{
                whenRule = conditions.substring(0, i).trim();
                thenRule = conditions.substring(i + 2).trim();
//                singleCondition = false;
            }
            Map<String, MatchPair> when = StringUtils.isBlank(whenRule) || "true".equals(whenRule) ? new HashMap<String, MatchPair>() : parseRule(whenRule);
//            isWhenConditionContainsFalse = when.containsKey("false") ? true : false;
            Map<String, MatchPair> then = StringUtils.isBlank(thenRule) || "false".equals(thenRule) ? null : parseRule(thenRule);
            // NOTE: When条件是允许为空的，外部业务来保证类似的约束条件
            this.whenCondition = when;
            this.thenCondition = then;
        } catch (ParseException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

    }

    protected boolean matchWhen(FilterableRegistration registration) {
        //when 条件判断不支持
        if(whenCondition.containsKey("false")){
            return false;
        }
        return matchCondition(whenCondition, registration.getMetadata(), null);
    }

    protected boolean matchThen(FilterableRegistration registration, Server server) {
        //then条件支持引用when的服务元数据作为表达式条件
        boolean ismatch = thenCondition != null && matchCondition(thenCondition, registration.getServerMetadata(server), registration.getMetadata());
        //todo  modify by zl 判断如果包含isWhenConditionContainsFalse 为true，直接返回then相反值
       /* if(isWhenConditionContainsFalse){
            return !ismatch;
        }else{
            return ismatch;
        }*/
        return ismatch;
    }

    protected boolean matchCondition(Map<String, AbstractConditionSupport.MatchPair> condition, Map<String, String> serverMetadata, Map<String, String> expressionParam) {
        for (Map.Entry<String, String> entry : serverMetadata.entrySet()) {
            String key = entry.getKey();
            AbstractConditionSupport.MatchPair pair = condition.get(key);
            if (pair != null && ! pair.isMatch(entry.getValue(), expressionParam)) {
                return false;
            }
        }
        return true;
    }


    private Map<String, MatchPair> parseRule(String rule)
            throws ParseException {
        Map<String, MatchPair> condition = new HashMap<String, MatchPair>();
        if(StringUtils.isBlank(rule)) {
            return condition;
        }
        // 匹配或不匹配Key-Value对
        MatchPair pair = null;
        // 多个Value值
        Set<String> values = null;
        final Matcher matcher = ROUTE_PATTERN.matcher(rule);
        while (matcher.find()) { // 逐个匹配
            String separator = matcher.group(1);
            String content = matcher.group(2);
            // 表达式开始
            if (separator == null || separator.length() == 0) {
                pair = new MatchPair();
                condition.put(content, pair);
            }
            // KV开始
            else if ("&".equals(separator) || "=&".equals(separator)) {
                if (condition.get(content) == null) {
                    pair = new MatchPair();
                    condition.put(content, pair);
                } else {
                    condition.put(content, pair);
                }
            }
            // KV的Value部分开始
            else if ("=".equals(separator)) {
                if (pair == null)
                    throw new ParseException("Illegal route rule \""
                            + rule + "\", The error char '" + separator
                            + "' at index " + matcher.start() + " before \""
                            + content + "\".", matcher.start());

                values = pair.matches;
                values.add(content);
            }
            // KV的Value部分开始
            else if ("!=".equals(separator)) {
                if (pair == null)
                    throw new ParseException("Illegal route rule \""
                            + rule + "\", The error char '" + separator
                            + "' at index " + matcher.start() + " before \""
                            + content + "\".", matcher.start());

                values = pair.mismatches;
                values.add(content);
            }
            // KV的Value部分的多个条目
            else if (",".equals(separator)) { // 如果为逗号表示
                if (values == null || values.size() == 0)
                    throw new ParseException("Illegal route rule \""
                            + rule + "\", The error char '" + separator
                            + "' at index " + matcher.start() + " before \""
                            + content + "\".", matcher.start());
                values.add(content);
            } else {
                throw new ParseException("Illegal route rule \"" + rule
                        + "\", The error char '" + separator + "' at index "
                        + matcher.start() + " before \"" + content + "\".", matcher.start());
            }
        }
        return condition;
    }


    /**
     * match pair
     */
    public static final class MatchPair {
        final Set<String> matches = new HashSet<String>();
        final Set<String> mismatches = new HashSet<String>();
        public boolean isMatch(String value, Map<String, String> param) {
            for (String match : matches) {
                if ( !MatchUtils.isMatchGlobPattern(match, value, param)) {
                    return false;
                }
            }
            for (String mismatch : mismatches) {
                if (MatchUtils.isMatchGlobPattern(mismatch, value, param)) {
                    return false;
                }
            }
            return true;
        }
    }
}