package ch2.service;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Getter
@Service
@RequiredArgsConstructor
public class MetricsService {

    private final MeterRegistry meterRegistry;
    private final Map<String, Map<String, Timer>> timerMap = new HashMap<>();

    public void createTimer(String methodName, String factoryId) {
        timerMap
                .computeIfAbsent(methodName, k -> new HashMap<>())
                .computeIfAbsent(factoryId, f -> Timer.builder("CUSTOM.MEASURED")
                        .tag("CUSTOM.METRIC.METHOD", methodName)
                        .tag("CUSTOM.METRIC.FACTORY", factoryId)
                        .publishPercentiles(0.95)
                        .register(meterRegistry));
    }

    public Timer getTimer(String methodName, String factoryId) {
        return timerMap.get(methodName).get(factoryId);
    }
}