package com.backend.kmsproject.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FootballPitchDTO implements Serializable {
    private Long footballPitchId;
    private String footballPitchName;
    private AddressDTO address;
    private String image;
    private String phoneNumber;
}
