package com.backend.kmsproject.model.dto.common;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class OnlyIdDTO implements Serializable {
    private Long id;
    private String name;
}
