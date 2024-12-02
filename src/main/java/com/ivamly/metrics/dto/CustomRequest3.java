package com.ivamly.metrics.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomRequest3 {
    private String field1;
    private Integer field2;
    private CustomRequest2 field3;
    private String field4;
    private String factoryId;
}
