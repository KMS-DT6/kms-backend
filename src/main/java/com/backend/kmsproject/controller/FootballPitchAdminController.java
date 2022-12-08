package com.backend.kmsproject.controller;

import com.backend.kmsproject.converter.FootballPitchAdminConverter;
import com.backend.kmsproject.model.dto.common.OnlyIdDTO;
import com.backend.kmsproject.request.footballpitch.CreateUpdateFootballPitchRequest;
import com.backend.kmsproject.request.footballpitchadmin.CreateUpdateFootballPitchAdminRequest;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.service.FootballPitchAdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Football Pitch Admin", description = "Football Pitch Admin APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/football-pitch-admins")
public class FootballPitchAdminController {
    private final FootballPitchAdminService footballPitchAdminService;

    private final FootballPitchAdminConverter footballPitchAdminConverter;

    @Operation(summary = "Create Football Pitch Admin")
    @PostMapping
    public Response<OnlyIdDTO> createFootballPitch(@RequestBody CreateUpdateFootballPitchAdminRequest request) {
        OnlyIdResponse response = footballPitchAdminService.cerateFootballPitchAdmin(request);
        if (response.getSuccess()) {
            return footballPitchAdminConverter.getSuccess(response);
        }
        return footballPitchAdminConverter.getError(response.getErrorResponse());
    }
}
