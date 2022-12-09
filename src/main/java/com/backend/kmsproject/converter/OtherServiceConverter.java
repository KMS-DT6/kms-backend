package com.backend.kmsproject.converter;

import com.backend.kmsproject.model.dto.OtherServiceDTO;
import com.backend.kmsproject.model.dto.common.ListDTO;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.otherservice.GetOtherServiceResponse;
import com.backend.kmsproject.response.otherservice.ListOtherServiceResponse;
import com.backend.kmsproject.util.DatetimeUtils;
import org.springframework.stereotype.Component;

@Component
public class OtherServiceConverter extends CommonConverter {
    public Response<ListDTO<OtherServiceDTO>> getSuccess(ListOtherServiceResponse response) {
        return Response.<ListDTO<OtherServiceDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<OtherServiceDTO>builder()
                        .setTotalItems(response.getOtherServices().size())
                        .setItems(response.getOtherServices())
                        .build())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }

    public Response<OtherServiceDTO> getSuccess(GetOtherServiceResponse response) {
        return Response.<OtherServiceDTO>builder()
                .setSuccess(true)
                .setData(response.getOtherService())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }
}
