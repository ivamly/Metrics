package com.ivamly.metrics.controller;

import com.ivamly.metrics.annotation.Time;
import com.ivamly.metrics.dto.CustomRequest1;
import com.ivamly.metrics.dto.CustomRequest2;
import com.ivamly.metrics.dto.CustomRequest3;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class MetricController {

    @PostMapping("/endpoint1")
    @Time
    public void endpoint1(@RequestBody CustomRequest1 request) {
        simulateWork();
    }

    @PostMapping("/endpoint2")
    @Time
    public void endpoint2(@RequestBody CustomRequest2 request) {
        simulateWork();
    }

    @PostMapping("/endpoint3")
    @Time
    public void endpoint3(@RequestBody CustomRequest3 request) {
        simulateWork();
    }

    private void simulateWork() {
        try {
            Thread.sleep((long) (Math.random() * 100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
