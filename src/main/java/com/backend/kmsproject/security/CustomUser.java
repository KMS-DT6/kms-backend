package com.backend.kmsproject.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
@Setter
public class CustomUser extends User {
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

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities,
                      Long userId, String firstName, String lastName, String role, String address, String district,
                      Long footballPitchId, Timestamp createdDate, Timestamp modifiedDate) {
        super(username, password, authorities);
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.address = address;
        this.district = district;
        this.footballPitchId = footballPitchId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }

    public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired,
                      boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities,
                      Long userId, String username1, String firstName, String lastName, String role, String address, String district,
                      Long footballPitchId, Timestamp createdDate, Timestamp modifiedDate) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userId = userId;
        this.username = username1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role;
        this.address = address;
        this.district = district;
        this.footballPitchId = footballPitchId;
        this.createdDate = createdDate;
        this.modifiedDate = modifiedDate;
    }
}
