package ch1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Request {
    private String id;
    private Integer field1;
    private Boolean field2;
    private String field3;
}