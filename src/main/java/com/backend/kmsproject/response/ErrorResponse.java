package com.backend.kmsproject.response;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class ErrorResponse {
    private String message;
    private Map<String, String> errors;
}
