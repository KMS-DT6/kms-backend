package com.backend.kmsproject.request.otherservice;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateUpdateOtherServiceRequest implements Serializable {
    private String otherServiceName;
    private Double pricePerOne;
    private Double pricePerHour;
}
