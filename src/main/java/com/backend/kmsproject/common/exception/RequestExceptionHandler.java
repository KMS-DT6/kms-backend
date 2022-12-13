package com.backend.kmsproject.common.exception;

import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.util.DatetimeUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestControllerAdvice
public class RequestExceptionHandler {
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<?> handleErrorNotFoundException(NotFoundException exception) {
        Response<?> error = Response.builder()
                .setSuccess(false)
                .setMessage(exception.getMessage())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(ErrorResponseRuntimeException.class)
    public ResponseEntity<?> handleErrorResponseRuntimeException(ErrorResponseRuntimeException exception) {
        Response<?> errors = Response.builder()
                .setSuccess(false)
                .setMessage(exception.getMessage())
                .setErrorDTOs(exception.getErrorDTOs())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException exception){
        Response<?> errors = Response.builder()
                .setSuccess(false)
                .setMessage(exception.getMessage())
                .setErrorDTOs(null)
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errors);
    }

}
