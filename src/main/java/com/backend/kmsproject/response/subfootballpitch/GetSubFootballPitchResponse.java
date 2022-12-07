package com.backend.kmsproject.response.subfootballpitch;

import com.backend.kmsproject.model.dto.SubFootballPitchDTO;
import com.backend.kmsproject.response.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class GetSubFootballPitchResponse implements Serializable {
    private Boolean success;
    private ErrorResponse errorResponse;
    private SubFootballPitchDTO subFootballPitch;
}
