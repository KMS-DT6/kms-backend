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
public class AddressDTO implements Serializable {
    private Long addressId;
    private String address;
    private String district;
    private String city;
}
