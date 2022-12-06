package com.backend.kmsproject.converter;

import com.backend.kmsproject.common.exception.ErrorResponseRuntimeException;
import com.backend.kmsproject.model.dto.common.ErrorDTO;
import com.backend.kmsproject.model.dto.common.NoContentDTO;
import com.backend.kmsproject.model.dto.common.OnlyIdDTO;
import com.backend.kmsproject.response.ErrorResponse;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.util.DatetimeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommonConverter {
    public Response<OnlyIdDTO> getSuccess(OnlyIdResponse response) {
        return Response.<OnlyIdDTO>builder()
                .setSuccess(true)
                .setData(OnlyIdDTO.builder()
                        .setId(response.getId())
                        .setName(response.getName())
                        .build())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }

    public Response<NoContentDTO> getSuccess(NoContentResponse response) {
        return Response.<NoContentDTO>builder()
                .setSuccess(true)
                .setMessage("Successful")
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }

    public <T> Response<T> getError(ErrorResponse errorResponse) {
        throw new ErrorResponseRuntimeException(errorResponse, getErrorDTOs(errorResponse.getErrors()));
    }

    public List<ErrorDTO> getErrorDTOs(Map<String, String> errors) {
        List<ErrorDTO> errorDTOS = new ArrayList<>();
        for (var v : errors.entrySet()) {
            errorDTOS.add(ErrorDTO.builder()
                    .setKey(v.getKey())
                    .setValue(v.getValue())
                    .build());
        }
        return errorDTOS;
    }
}
