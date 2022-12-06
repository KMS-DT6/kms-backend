package com.backend.kmsproject.response;

import lombok.*;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class OnlyIdResponse {
    private Long id;
    private String name;
    private Boolean success;
    private ErrorResponse errorResponse;
}
