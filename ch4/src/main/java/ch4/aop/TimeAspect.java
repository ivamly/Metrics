package ch4.aop;

import ch4.service.MetricsService;
import io.micrometer.core.instrument.Timer;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Objects;

@Aspect
@Component
@RequiredArgsConstructor
public class TimeAspect {

    private final MetricsService metricsService;

    @Around("@annotation(org.springframework.web.bind.annotation.GetMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PostMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PutMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.DeleteMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.PatchMapping) ||" +
            "@annotation(org.springframework.web.bind.annotation.RequestMapping)")
    public Object measureExecutionTime(ProceedingJoinPoint joinPoint) {
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        String methodName = method.getName();
        String factoryId = getFactoryId(joinPoint.getArgs());

        Timer timer = metricsService.getOrCreateTimer(methodName, factoryId);

        return timer.record(() -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        });
    }

    private String getFactoryId(Object[] args) {
        if (Objects.nonNull(args)) {
            for (Object arg : args) {
                if (Objects.nonNull(arg)) {
                    try {
                        Field field = arg.getClass().getDeclaredField("id");
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