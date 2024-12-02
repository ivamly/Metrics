package com.ivamly.metrics.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
@Getter
@RequiredArgsConstructor
public class MetricsService {

    private final MeterRegistry meterRegistry;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, Timer>> timerMap = new ConcurrentHashMap<>();

    public Timer getOrCreateTimer(String methodName, String factoryId) {
        return timerMap
                .computeIfAbsent(methodName, k -> new ConcurrentHashMap<>())
                .computeIfAbsent(factoryId, f -> Timer.builder("CUSTOM.MEASURED")
                        .tag("CUSTOM.METRIC.METHOD", methodName)
                        .tag("CUSTOM.METRIC.FACTORY", factoryId)
                        .publishPercentiles(0.95)
                        .register(meterRegistry));
    }
}
