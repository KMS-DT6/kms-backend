package com.backend.kmsproject.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class AddressDTO implements Serializable {
    private Long addressId;
    private String address;
    private String district;
    private String city;
}
