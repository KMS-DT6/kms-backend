package com.backend.kmsproject.response.otherservice;

import com.backend.kmsproject.model.dto.OtherServiceDTO;
import com.backend.kmsproject.response.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class GetOtherServiceResponse implements Serializable {
    private Boolean success;
    private ErrorResponse errorResponse;
    private OtherServiceDTO otherService;
}
