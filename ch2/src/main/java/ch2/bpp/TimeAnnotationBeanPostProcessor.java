package ch2.bpp;

import ch2.annotation.Time;
import ch2.config.TimeAnnotationsProperties;
import ch2.service.MetricsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TimeAnnotationBeanPostProcessor implements BeanPostProcessor, Ordered {

    private final TimeAnnotationsProperties properties;
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
                String endpoint = timeAnnotation.endpoint();
                Map<String, List<String>> endpointsMap = properties.getEndpoints();

                if (endpointsMap.containsKey(endpoint)) {
                    List<String> ids = endpointsMap.get(endpoint);
                    processTimeAnnotation(ids, method);
                } else {
                    System.err.printf("Не найдено подмножество IDs для эндпоинта '%s' в классе %s.%n",
                            endpoint, bean.getClass().getSimpleName());
                }
            }
        }
        return bean;
    }

    private void processTimeAnnotation(List<String> ids, Method method) {
        ids.forEach(id -> metricsService.createTimer(method.getName(), id));
    }
}