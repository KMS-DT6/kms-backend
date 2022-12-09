package com.backend.kmsproject.request.otherservice;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateUpdateOtherServiceRequest implements Serializable {
    private Long footballPitchId;
    private String otherServiceName;
    private Integer quantity;
    private Double pricePerOne;
    private Double pricePerHour;
}
