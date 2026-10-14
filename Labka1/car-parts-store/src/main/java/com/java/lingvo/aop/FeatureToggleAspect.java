package com.java.lingvo.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.Set;

@Aspect
@Component
public class FeatureToggleAspect {


    private final Set<String> activeFeatures = Set.of("new-ui", "beta-export");

    @Before("@annotation(featureToggle)")
    public void checkFeature(FeatureToggle featureToggle) {
        if (!activeFeatures.contains(featureToggle.value())) {
            throw new UnsupportedOperationException("Функціонал вимкнено: " + featureToggle.value());
        }
    }
}