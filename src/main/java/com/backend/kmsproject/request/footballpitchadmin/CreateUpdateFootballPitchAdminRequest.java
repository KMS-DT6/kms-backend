package com.backend.kmsproject.request.footballpitchadmin;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreateUpdateFootballPitchAdminRequest implements Serializable {
    private Long footballPitchId;
    private String address;
    private String district;
    private String city;
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private String phoneNumber;
}
