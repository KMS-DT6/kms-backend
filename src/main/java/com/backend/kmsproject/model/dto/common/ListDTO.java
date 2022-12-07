package com.backend.kmsproject.model.dto.common;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class ListDTO<T> implements Serializable {
    private Integer totalItems;
    private List<T> items;
}
