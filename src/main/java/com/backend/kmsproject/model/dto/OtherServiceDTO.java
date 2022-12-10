package com.backend.kmsproject.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class OtherServiceDTO implements Serializable {
    private Long otherServiceId;
    private String otherServiceName;
    private Double pricePerOne;
    private Double pricePerHour;
    private Long footballPitchId;
    private String footballPitchName;
}
