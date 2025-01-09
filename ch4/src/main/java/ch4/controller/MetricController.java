package ch4.controller;

import ch4.dto.Request;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/metrics")
public class MetricController {

    @PostMapping("/endpoint1")
    public void endpoint1(@RequestBody Request request) {
    }

    @PostMapping("/endpoint2")
    public void endpoint2(@RequestBody Request request) {
    }

    @PostMapping("/endpoint3")
    public void endpoint3(@RequestBody Request request) {
    }
}
