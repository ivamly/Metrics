package ch1.bpp;

import ch1.annotation.Time;
import ch1.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class TimeAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered {

    private final MetricsService metricsService;

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();

        for (Method method : beanClass.getDeclaredMethods()) {
            if (method.isAnnotationPresent(Time.class)) {
                Time timeAnnotation = method.getAnnotation(Time.class);
                processTimeAnnotation(timeAnnotation, method);
            }
        }
        return bean;
    }

    private void processTimeAnnotation(Time timeAnnotation, Method method) {
        Arrays.stream(timeAnnotation.ids()).forEach(id -> metricsService.createTimer(method.getName(), id));
    }
}