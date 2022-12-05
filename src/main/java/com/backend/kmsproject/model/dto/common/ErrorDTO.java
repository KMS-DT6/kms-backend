package com.backend.kmsproject.model.dto.common;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class ErrorDTO implements Serializable {
    private String key;
    private String value;
}
