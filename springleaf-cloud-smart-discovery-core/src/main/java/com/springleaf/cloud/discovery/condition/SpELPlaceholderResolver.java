package com.springleaf.cloud.discovery.condition;

import com.springleaf.cloud.discovery.config.IPlaceholderResolver;
import org.apache.commons.lang.StringUtils;
import org.springframework.expression.common.TemplateParserContext;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * Conditional rules support SpEL expressions and are applicable to dynamic parameter routing scenarios
 *
 * @author leon
 */
public class SpELPlaceholderResolver implements IPlaceholderResolver {

    public static final String EXPRESSION_PREFIX = "#{";

    @Override
    public String resolve(String value) {

        javax.servlet.http.HttpServletRequest request = getRequest();

        StandardEvaluationContext ctx = new StandardEvaluationContext();

        ctx.setVariable("request",request);

        return new SpelExpressionParser().parseExpression(value, new TemplateParserContext()).getValue(ctx, String.class);
    }

    @Override
    public boolean support(String value) {
        return StringUtils.isNotEmpty(EXPRESSION_PREFIX) && value.contains(EXPRESSION_PREFIX);
    }

    private static HttpServletRequest getRequest() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attr.getRequest();
    }

}
