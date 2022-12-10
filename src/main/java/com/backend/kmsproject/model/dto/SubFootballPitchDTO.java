package com.backend.kmsproject.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class SubFootballPitchDTO implements Serializable {
    private Long subFootballPitchId;
    private String subFootballPitchName;
    private Long footballPitchId;
    private Integer size;
    private Boolean status;
    private Double pricePerHour;
    private String image;

    @Getter
    @Setter
    @Builder(setterPrefix = "set")
    public static class FootballPitch {
        private Long footballPitchId;
        private String footballPitchName;
    }

}
