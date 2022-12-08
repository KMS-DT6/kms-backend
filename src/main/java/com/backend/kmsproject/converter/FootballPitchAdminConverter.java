package com.backend.kmsproject.converter;

import com.backend.kmsproject.model.dto.FootballPitchAdminDTO;
import com.backend.kmsproject.model.dto.FootballPitchDTO;
import com.backend.kmsproject.model.dto.common.ListDTO;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.footballpitch.ListFootballPitchResponse;
import com.backend.kmsproject.response.footballpitchadmin.GetFootballPitchAdminResponse;
import com.backend.kmsproject.response.footballpitchadmin.ListFootballPitchAdminResponse;
import com.backend.kmsproject.util.DatetimeUtils;
import org.springframework.stereotype.Component;

@Component
public class FootballPitchAdminConverter extends CommonConverter{
    public Response<FootballPitchAdminDTO> getSuccess(GetFootballPitchAdminResponse response) {
        return Response.<FootballPitchAdminDTO>builder()
                .setSuccess(true)
                .setData(response.getFootballPitchAdminDTO())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }

    public Response<ListDTO<FootballPitchAdminDTO>> getSuccess(ListFootballPitchAdminResponse response) {
        return Response.<ListDTO<FootballPitchAdminDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<FootballPitchAdminDTO>builder()
                        .setTotalItems(response.getFootballPitchAdminS().size())
                        .setItems(response.getFootballPitchAdminS())
                        .build())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }
}
