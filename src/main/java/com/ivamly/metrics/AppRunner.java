package com.ivamly.metrics;

import com.ivamly.metrics.dto.CustomRequest1;
import com.ivamly.metrics.dto.CustomRequest2;
import com.ivamly.metrics.dto.CustomRequest3;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class AppRunner implements CommandLineRunner {

    private final RestTemplate restTemplate = new RestTemplate();

    @Override
    public void run(String... args) {
        for (int i = 0; i < 10; i++) {
            callEndpoint("http://localhost:8080/metrics/endpoint1", new CustomRequest1(), "FactoryA");
            callEndpoint("http://localhost:8080/metrics/endpoint1", new CustomRequest1(), "FactoryB");
            callEndpoint("http://localhost:8080/metrics/endpoint1", new CustomRequest1(), "FactoryC");
            callEndpoint("http://localhost:8080/metrics/endpoint1", new CustomRequest1(), null);
            callEndpoint("http://localhost:8080/metrics/endpoint2", new CustomRequest2(), "FactoryB");
            callEndpoint("http://localhost:8080/metrics/endpoint2", new CustomRequest2(), "FactoryA");
            callEndpoint("http://localhost:8080/metrics/endpoint2", new CustomRequest2(), "FactoryC");
            callEndpoint("http://localhost:8080/metrics/endpoint2", new CustomRequest2(), null);
            callEndpoint("http://localhost:8080/metrics/endpoint3", new CustomRequest3(), "FactoryC");
            callEndpoint("http://localhost:8080/metrics/endpoint3", new CustomRequest3(), "FactoryA");
            callEndpoint("http://localhost:8080/metrics/endpoint3", new CustomRequest3(), "FactoryB");
            callEndpoint("http://localhost:8080/metrics/endpoint3", new CustomRequest3(), null);
        }
    }

    private void callEndpoint(String url, Object requestDto, String factoryId) {
        if (requestDto instanceof CustomRequest1) {
            ((CustomRequest1) requestDto).setFactoryId(factoryId);
        } else if (requestDto instanceof CustomRequest2) {
            ((CustomRequest2) requestDto).setFactoryId(factoryId);
        } else if (requestDto instanceof CustomRequest3) {
            ((CustomRequest3) requestDto).setFactoryId(factoryId);
        }

        restTemplate.postForObject(url, requestDto, String.class);
    }
}
