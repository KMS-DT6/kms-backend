package com.backend.kmsproject.controller;

import com.backend.kmsproject.converter.FootballPitchAdminConverter;
import com.backend.kmsproject.model.dto.FootballPitchAdminDTO;
import com.backend.kmsproject.model.dto.common.ListDTO;
import com.backend.kmsproject.model.dto.common.NoContentDTO;
import com.backend.kmsproject.model.dto.common.OnlyIdDTO;
import com.backend.kmsproject.request.footballpitchadmin.CreateUpdateFootballPitchAdminRequest;
import com.backend.kmsproject.request.footballpitchadmin.GetListFootballPitchAdminRequest;
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

import javax.validation.Valid;

@Tag(name = "Football Pitch Admin", description = "Football Pitch Admin APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/football-pitch-admins")
public class FootballPitchAdminController {
    private final FootballPitchAdminService footballPitchAdminService;

    private final FootballPitchAdminConverter footballPitchAdminConverter;

    @Operation(summary = "Create FootballPitch Admin")
    @PostMapping
    public Response<OnlyIdDTO> createFootballPitch(@RequestBody CreateUpdateFootballPitchAdminRequest request) {
        OnlyIdResponse response = footballPitchAdminService.createFootballPitchAdmin(request);
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

    @Operation(summary = "Update FootballPitch Admin")
    @PutMapping("/{id}")
    public Response<OnlyIdDTO> updateFootballPitchAdmin(@PathVariable("id") Long footballPitchAdminId,
                                                        @RequestBody CreateUpdateFootballPitchAdminRequest request) {
        OnlyIdResponse response = footballPitchAdminService.updateFootballPitchAdmin(footballPitchAdminId, request);
        if (response.getSuccess()) {
            return footballPitchAdminConverter.getSuccess(response);
        }
        return footballPitchAdminConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Delete FootballPitch Admin")
    @DeleteMapping("/{id}")
    public Response<NoContentDTO> deleteFootballPitchAdmin(@PathVariable("id") Long footballPitchAdminId) {
        NoContentResponse response = footballPitchAdminService.deleteFootballPitchAdmin(footballPitchAdminId);
        if (response.getSuccess()) {
            return footballPitchAdminConverter.getSuccess(response);
        }
        return footballPitchAdminConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Get List FootballPitch Admin")
    @GetMapping
    public Response<ListDTO<FootballPitchAdminDTO>> getListFootballPitchAdmin(@ModelAttribute @Valid GetListFootballPitchAdminRequest request) {
        ListFootballPitchAdminResponse response = footballPitchAdminService.getListFootballPitchAdmin(request);
        if (response.getSuccess()) {
            return footballPitchAdminConverter.getSuccess(response);
        }
        return footballPitchAdminConverter.getError(response.getErrorResponse());
    }

}
