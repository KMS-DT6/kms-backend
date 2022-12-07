package com.backend.kmsproject.response.footballpitch;

import com.backend.kmsproject.model.dto.FootballPitchDTO;
import com.backend.kmsproject.response.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class GetFootballPitchResponse implements Serializable {
    private Boolean success;
    private ErrorResponse errorResponse;
    private FootballPitchDTO footballPitch;
}
