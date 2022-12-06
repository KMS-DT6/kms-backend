package com.backend.kmsproject.request;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class UpdateMyAccountRequest implements Serializable {
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
    private String address;
    private String district;
    private String city;
}
