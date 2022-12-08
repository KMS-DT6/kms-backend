package com.backend.kmsproject.request.footballpitchadmin;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateFootballPitchAdminRequest {
    private Long footballPitchId;
    private String address;
    private String district;
    private String city;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}
