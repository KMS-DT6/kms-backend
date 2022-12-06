package com.backend.kmsproject.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class MyAccountDTO implements Serializable {
    private UserDTO user;
    private List<String> authorities;
}
