package com.ivamly.metrics.aop;

import com.ivamly.metrics.service.MetricsService;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
@RequiredArgsConstructor
public class TimeAspect {

    private final MetricsService metricsService;

    @Around("@annotation(com.ivamly.metrics.annotation.Time)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();
        String factoryId = getFactoryId(joinPoint.getArgs());

        Timer timer = metricsService.getOrCreateTimer(methodName, factoryId);

        long startTime = System.currentTimeMillis();
        try {
            return joinPoint.proceed();
        } finally {
            long endTime = System.currentTimeMillis();
            timer.record(endTime - startTime, TimeUnit.MILLISECONDS);
        }
    }

    private String getFactoryId(Object[] args) {
        if (Objects.nonNull(args)) {
            for (Object arg : args) {
                if (Objects.nonNull(arg)) {
                    try {
                        Field field = arg.getClass().getDeclaredField("factoryId");
                        field.setAccessible(true);

                        Object factoryId = field.get(arg);
                        if (factoryId instanceof String) {
                            return (String) factoryId;
                        }
                    } catch (NoSuchFieldException | IllegalAccessException exception) {
                        return "unknown_factory";
                    }
                }
            }
        }
        return "unknown_factory";
    }

}
