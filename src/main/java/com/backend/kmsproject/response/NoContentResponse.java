package com.backend.kmsproject.response;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class NoContentResponse implements Serializable {
    private Boolean success;
    private ErrorResponse errorResponse;
}
