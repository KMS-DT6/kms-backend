package com.backend.kmsproject.model.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "set")
public class FootballPitchAdminDTO {
    private Long id;
    private Long footballPitchId;
    private AddressDTO address;
    private String firstName;
    private String lastName;
    private String username;
    private String phoneNumber;
}
