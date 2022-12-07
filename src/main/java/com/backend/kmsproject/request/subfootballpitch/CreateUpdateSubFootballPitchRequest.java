package com.backend.kmsproject.request.subfootballpitch;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class CreateUpdateSubFootballPitchRequest implements Serializable {
    private String subFootballPitchName;
    private Long footballPitchId;
    private Integer size;
    private String image;
    private Double pricePerHour;
}
