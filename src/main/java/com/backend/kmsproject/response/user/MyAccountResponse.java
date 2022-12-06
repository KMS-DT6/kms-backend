package com.backend.kmsproject.response.user;

import com.backend.kmsproject.model.dto.UserDTO;
import com.backend.kmsproject.response.ErrorResponse;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class MyAccountResponse implements Serializable {
    private Boolean success;
    private ErrorResponse errorResponse;
    private UserDTO user;
    private List<String> authorities;
}
