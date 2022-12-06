package com.backend.kmsproject.common.exception;

import com.backend.kmsproject.model.dto.common.ErrorDTO;
import com.backend.kmsproject.response.ErrorResponse;
import lombok.Getter;

import java.util.List;

@Getter
public class ErrorResponseRuntimeException extends RuntimeException {
    private ErrorResponse errorResponse;
    private List<ErrorDTO> errorDTOs;

    public ErrorResponseRuntimeException(ErrorResponse errorResponse, List<ErrorDTO> errorDTOs) {
        this.errorResponse = errorResponse;
        this.errorDTOs = errorDTOs;
    }
}
