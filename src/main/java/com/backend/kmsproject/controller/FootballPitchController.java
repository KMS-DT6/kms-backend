package com.backend.kmsproject.controller;

import com.backend.kmsproject.converter.FootballPitchConverter;
import com.backend.kmsproject.model.dto.FootballPitchDTO;
import com.backend.kmsproject.model.dto.common.ListDTO;
import com.backend.kmsproject.model.dto.common.NoContentDTO;
import com.backend.kmsproject.model.dto.common.OnlyIdDTO;
import com.backend.kmsproject.request.footballpitch.CreateUpdateFootballPitchRequest;
import com.backend.kmsproject.request.footballpitch.GetListFootballPitchRequest;
import com.backend.kmsproject.request.subfootballpitch.CreateUpdateSubFootballPitchRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.footballpitch.GetFootballPitchResponse;
import com.backend.kmsproject.response.footballpitch.ListFootballPitchResponse;
import com.backend.kmsproject.service.FootballPitchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Football Pitch", description = "Football Pitch APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/football-pitches")
public class FootballPitchController {
    private final FootballPitchService footballPitchService;
    private final FootballPitchConverter footballPitchConverter;

    @Operation(summary = "Get List Football Pitch")
    @GetMapping
    public Response<ListDTO<FootballPitchDTO>> getListFootballPitch(@ModelAttribute @Valid GetListFootballPitchRequest request) {
        ListFootballPitchResponse response = footballPitchService.getListFootballPitch(request);
        if (response.getSuccess()) {
            return footballPitchConverter.getSuccess(response);
        }
        return footballPitchConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Get Football Pitch")
    @GetMapping("/{id}")
    public Response<FootballPitchDTO> getFootballPitch(@PathVariable("id") Long footballPitchId) {
        GetFootballPitchResponse response = footballPitchService.getFootballPitch(footballPitchId);
        if (response.getSuccess()) {
            return footballPitchConverter.getSuccess(response);
        }
        return footballPitchConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Create Football Pitch")
    @PostMapping
    public Response<OnlyIdDTO> createFootballPitch(@RequestBody CreateUpdateFootballPitchRequest request) {
        OnlyIdResponse response = footballPitchService.createFootballPitch(request);
        if (response.getSuccess()) {
            return footballPitchConverter.getSuccess(response);
        }
        return footballPitchConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Update Football Pitch")
    @PutMapping("/{id}")
    public Response<OnlyIdDTO> updateFootballPitch(@PathVariable("id") Long footballPitchId,
                                                   @RequestBody CreateUpdateFootballPitchRequest request) {
        OnlyIdResponse response = footballPitchService.updateFootballPitch(footballPitchId, request);
        if (response.getSuccess()) {
            return footballPitchConverter.getSuccess(response);
        }
        return footballPitchConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Delete Football Pitch")
    @DeleteMapping("/{id}")
    public Response<NoContentDTO> deleteFootballPitch(@PathVariable("id") Long footballPitchId) {
        NoContentResponse response = footballPitchService.deleteFootballPitch(footballPitchId);
        if (response.getSuccess()) {
            return footballPitchConverter.getSuccess(response);
        }
        return footballPitchConverter.getError(response.getErrorResponse());
    }
}
