package com.backend.kmsproject.response.footballpitchadmin;

import com.backend.kmsproject.model.dto.FootballPitchAdminDTO;
import com.backend.kmsproject.model.dto.FootballPitchDTO;
import com.backend.kmsproject.response.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class ListFootballPitchAdminResponse implements Serializable {
    private Boolean success;
    private ErrorResponse errorResponse;
    private List<FootballPitchAdminDTO> footballPitchAdminS;
}
