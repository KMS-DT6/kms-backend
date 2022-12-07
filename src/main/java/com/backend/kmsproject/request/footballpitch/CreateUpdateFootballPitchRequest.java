package com.backend.kmsproject.request.footballpitch;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateUpdateFootballPitchRequest implements Serializable {
    private String footballPitchName;
    private String address;
    private String district;
    private String city;
    private String image;
    private String phoneNumber;
}
