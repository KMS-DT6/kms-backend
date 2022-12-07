package com.backend.kmsproject.converter;

import com.backend.kmsproject.model.dto.SubFootballPitchDTO;
import com.backend.kmsproject.model.dto.common.ListDTO;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.subfootballpitch.GetSubFootballPitchResponse;
import com.backend.kmsproject.response.subfootballpitch.ListSubFootballPitchResponse;
import com.backend.kmsproject.util.DatetimeUtils;
import org.springframework.stereotype.Component;

@Component
public class SubFootballPitchConverter extends CommonConverter {
    public Response<ListDTO<SubFootballPitchDTO>> getSuccess(ListSubFootballPitchResponse response) {
        return Response.<ListDTO<SubFootballPitchDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<SubFootballPitchDTO>builder()
                        .setTotalItems(response.getSubFootballPitches().size())
                        .setItems(response.getSubFootballPitches())
                        .build())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }

    public Response<SubFootballPitchDTO> getSuccess(GetSubFootballPitchResponse response) {
        return Response.<SubFootballPitchDTO>builder()
                .setSuccess(true)
                .setData(response.getSubFootballPitch())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }
}
