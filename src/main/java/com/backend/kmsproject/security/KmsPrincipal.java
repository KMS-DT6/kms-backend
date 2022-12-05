package com.backend.kmsproject.security;

import com.backend.kmsproject.common.enums.KmsRole;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
public class KmsPrincipal implements Serializable {
    private Long userId;
    private String username;
    private String firstName;
    private String lastName;
    private String role;
    private String address;
    private String district;
    private Long footballPitchId;
    private Timestamp createdDate;
    private Timestamp modifiedDate;

    public boolean isSystemAmin() {
        return KmsRole.ADMIN_ROLE.getRole().equalsIgnoreCase(role);
    }

    public boolean isFootballPitchAdmin() {
        return KmsRole.FOOTBALL_PITCH_ROLE.getRole().equalsIgnoreCase(role);
    }

    public boolean isCustomer() {
        return KmsRole.CUSTOMER_ROLE.getRole().equalsIgnoreCase(role);
    }
}
