package ch1.aop;

import ch1.annotation.Time;
import ch1.service.MetricsService;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
@RequiredArgsConstructor
public class TimeAspect {

    private final MetricsService metricsService;

    @Around("@annotation(ch1.annotation.Time)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();
        String factoryId = methodSignature.getMethod().getAnnotation(Time.class).endpoint();

        Timer timer = metricsService.getTimer(methodName, factoryId);

        return timer.record(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }
}