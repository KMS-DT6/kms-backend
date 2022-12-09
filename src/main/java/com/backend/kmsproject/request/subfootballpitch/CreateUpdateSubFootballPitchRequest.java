package com.backend.kmsproject.request.subfootballpitch;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "set")
public class CreateUpdateSubFootballPitchRequest implements Serializable {
    private String subFootballPitchName;
    private Long footballPitchId;
    private Integer size;
    private String image;
    private Double pricePerHour;
}
