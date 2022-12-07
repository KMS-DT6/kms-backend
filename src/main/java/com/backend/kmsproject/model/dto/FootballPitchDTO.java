package com.backend.kmsproject.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class FootballPitchDTO implements Serializable {
    private Long footballPitchId;
    private String footballPitchName;
    private AddressDTO address;
    private String image;
    private String phoneNumber;
}
