package com.backend.kmsproject.controller;

import com.backend.kmsproject.converter.SubFootballPitchConverter;
import com.backend.kmsproject.model.dto.SubFootballPitchDTO;
import com.backend.kmsproject.model.dto.common.ListDTO;
import com.backend.kmsproject.model.dto.common.NoContentDTO;
import com.backend.kmsproject.model.dto.common.OnlyIdDTO;
import com.backend.kmsproject.request.subfootballpitch.CreateUpdateSubFootballPitchRequest;
import com.backend.kmsproject.request.subfootballpitch.GetListSubFootballPitchRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.subfootballpitch.GetSubFootballPitchResponse;
import com.backend.kmsproject.response.subfootballpitch.ListSubFootballPitchResponse;
import com.backend.kmsproject.service.SubFootballPitchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Sub Football Pitch", description = "Sub Football Pitch APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sub-football-pitches")
public class SubFootballPitchController {
    private final SubFootballPitchService subFootballPitchService;
    private final SubFootballPitchConverter subFootballPitchConverter;

    @Operation(summary = "Get List Sub Football Pitch")
    @GetMapping
    public Response<ListDTO<SubFootballPitchDTO>> getListSubFootballPitch(@ModelAttribute @Valid GetListSubFootballPitchRequest request) {
        ListSubFootballPitchResponse response = subFootballPitchService.getListSubFootballPitch(request);
        if (response.getSuccess()) {
            return subFootballPitchConverter.getSuccess(response);
        }
        return subFootballPitchConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Get Sub Football Pitch")
    @GetMapping("/{id}")
    public Response<SubFootballPitchDTO> getSubFootballPitch(@PathVariable("id") Long subFootballPitchId) {
        GetSubFootballPitchResponse response = subFootballPitchService.getSubFootballPitch(subFootballPitchId);
        if (response.getSuccess()) {
            return subFootballPitchConverter.getSuccess(response);
        }
        return subFootballPitchConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Create Sub Football Pitch")
    @PostMapping
    public Response<OnlyIdDTO> createSubFootballPitch(@RequestBody CreateUpdateSubFootballPitchRequest request) {
        OnlyIdResponse response = subFootballPitchService.createSubFootballPitch(request);
        if (response.getSuccess()) {
            return subFootballPitchConverter.getSuccess(response);
        }
        return subFootballPitchConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Update Sub Football Pitch")
    @PutMapping("/{id}")
    public Response<OnlyIdDTO> updateSubFootballPitch(@PathVariable("id") Long subFootballPitchId,
                                                      @RequestBody CreateUpdateSubFootballPitchRequest request) {
        OnlyIdResponse response = subFootballPitchService.updateSubFootballPitch(subFootballPitchId, request);
        if (response.getSuccess()) {
            return subFootballPitchConverter.getSuccess(response);
        }
        return subFootballPitchConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Delete Sub Football Pitch")
    @DeleteMapping("/{id}")
    public Response<NoContentDTO> deleteSubFootballPitch(@PathVariable("id") Long subFootballPitchId) {
        NoContentResponse response = subFootballPitchService.deleteSubFootballPitch(subFootballPitchId);
        if (response.getSuccess()) {
            return subFootballPitchConverter.getSuccess(response);
        }
        return subFootballPitchConverter.getError(response.getErrorResponse());
    }
}
