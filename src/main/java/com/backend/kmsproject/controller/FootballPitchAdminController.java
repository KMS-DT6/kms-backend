package com.backend.kmsproject.controller;

import com.backend.kmsproject.converter.FootballPitchAdminConverter;
import com.backend.kmsproject.model.dto.FootballPitchAdminDTO;
import com.backend.kmsproject.model.dto.common.ListDTO;
import com.backend.kmsproject.model.dto.common.NoContentDTO;
import com.backend.kmsproject.model.dto.common.OnlyIdDTO;
import com.backend.kmsproject.request.footballpitchadmin.CreateFootballPitchAdminRequest;
import com.backend.kmsproject.request.footballpitchadmin.UpdateFootballPitchAdminRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.footballpitchadmin.GetFootballPitchAdminResponse;
import com.backend.kmsproject.response.footballpitchadmin.ListFootballPitchAdminResponse;
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

    @Operation(summary = "Create FootballPitch Admin")
    @PostMapping
    public Response<OnlyIdDTO> createFootballPitch(@RequestBody CreateFootballPitchAdminRequest request) {
        OnlyIdResponse response = footballPitchAdminService.cerateFootballPitchAdmin(request);
        if (response.getSuccess()) {
            return footballPitchAdminConverter.getSuccess(response);
        }
        return footballPitchAdminConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Get FootballPitch Admin")
    @GetMapping("/{id}")
    public Response<FootballPitchAdminDTO> getFootballPitch(@PathVariable("id") Long id) {
        GetFootballPitchAdminResponse response = footballPitchAdminService.getFootballPitchAdmin(id);
        if (response.getSuccess()) {
            return footballPitchAdminConverter.getSuccess(response);
        }
        return footballPitchAdminConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Update Football Pitch Amdin")
    @PutMapping("/{id}")
    public Response<OnlyIdDTO> updateFootballPitchAdmin(@PathVariable("id") Long footballPitchAdminId,
                                                   @RequestBody UpdateFootballPitchAdminRequest request) {
        OnlyIdResponse response = footballPitchAdminService.updateFootballPitchAdmin(footballPitchAdminId, request);
        if (response.getSuccess()) {
            return footballPitchAdminConverter.getSuccess(response);
        }
        return footballPitchAdminConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Delete Football Pitch Admin")
    @DeleteMapping("/{id}")
    public Response<NoContentDTO> deleteFootballPitchAdmin(@PathVariable("id") Long footballPitchAdminId) {
        NoContentResponse response = footballPitchAdminService.deleteFootballPitchAdmin(footballPitchAdminId);
        if (response.getSuccess()) {
            return footballPitchAdminConverter.getSuccess(response);
        }
        return footballPitchAdminConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Get List Football Pitch Admin")
    @GetMapping
    public Response<ListDTO<FootballPitchAdminDTO>> getListFootballPitchAdmin(@RequestParam(required = false, defaultValue = "") String footBallPitchAdminName) {
        ListFootballPitchAdminResponse response = footballPitchAdminService.getListFootballPitchAdmins(footBallPitchAdminName);
        if (response.getSuccess()) {
            return footballPitchAdminConverter.getSuccess(response);
        }
        return footballPitchAdminConverter.getError(response.getErrorResponse());
    }

}
