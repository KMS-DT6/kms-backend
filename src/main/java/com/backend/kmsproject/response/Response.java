package com.backend.kmsproject.response;

import com.backend.kmsproject.model.dto.common.ErrorDTO;
import com.backend.kmsproject.util.DatetimeUtils;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class Response<T> implements Serializable {
    private Boolean success;
    private String message;
    private List<ErrorDTO> errorDTOs;
    private T data;
    private String dateTime;
}
