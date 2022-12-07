package com.backend.kmsproject.model.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO implements Serializable {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String password;
    private String phoneNumber;
    private Role role;
    private AddressDTO address;
    private FootballPitch footballPitch;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(setterPrefix = "set")
    public static class FootballPitch {
        private Long footballPitchId;
        private String footballPitchName;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder(setterPrefix = "set")
    public static class Role {
        private Long roleId;
        private String roleName;
    }

}
