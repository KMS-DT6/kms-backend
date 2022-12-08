package com.backend.kmsproject.response.footballpitchadmin;

import com.backend.kmsproject.model.dto.FootballPitchAdminDTO;
import com.backend.kmsproject.model.dto.FootballPitchDTO;
import com.backend.kmsproject.response.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class GetFootballPitchAdminResponse implements Serializable {
    private Boolean success;
    private ErrorResponse errorResponse;
    private FootballPitchAdminDTO footballPitchAdminDTO;
}
