package com.mediacallz.server.conditional;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class UICondition implements Condition {
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        String headless = System.getProperty("java.awt.headless");
        if(headless != null) {
            boolean isHeadless = Boolean.parseBoolean(headless);
            return !isHeadless;
        }
        return false;
    }
}
