package com.backend.kmsproject.converter;

import com.backend.kmsproject.model.dto.FootballPitchAdminDTO;
import com.backend.kmsproject.model.dto.FootballPitchDTO;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.footballpitchadmin.GetFootballPitchAdminResponse;
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
}
