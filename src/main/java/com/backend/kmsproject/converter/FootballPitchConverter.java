package com.backend.kmsproject.converter;

import com.backend.kmsproject.model.dto.FootballPitchDTO;
import com.backend.kmsproject.model.dto.common.ListDTO;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.footballpitch.GetFootballPitchResponse;
import com.backend.kmsproject.response.footballpitch.ListFootballPitchResponse;
import com.backend.kmsproject.util.DatetimeUtils;
import org.springframework.stereotype.Component;

@Component
public class FootballPitchConverter extends CommonConverter {
    public Response<ListDTO<FootballPitchDTO>> getSuccess(ListFootballPitchResponse response) {
        return Response.<ListDTO<FootballPitchDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<FootballPitchDTO>builder()
                        .setTotalItems(response.getFootballPitches().size())
                        .setItems(response.getFootballPitches())
                        .build())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }

    public Response<FootballPitchDTO> getSuccess(GetFootballPitchResponse response) {
        return Response.<FootballPitchDTO>builder()
                .setSuccess(true)
                .setData(response.getFootballPitch())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }
}
