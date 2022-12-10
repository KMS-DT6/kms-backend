package com.backend.kmsproject.controller;

import com.backend.kmsproject.converter.OtherServiceConverter;
import com.backend.kmsproject.model.dto.OtherServiceDTO;
import com.backend.kmsproject.model.dto.common.ListDTO;
import com.backend.kmsproject.model.dto.common.NoContentDTO;
import com.backend.kmsproject.model.dto.common.OnlyIdDTO;
import com.backend.kmsproject.request.otherservice.CreateUpdateOtherServiceRequest;
import com.backend.kmsproject.request.otherservice.GetListOtherServiceRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.otherservice.GetOtherServiceResponse;
import com.backend.kmsproject.response.otherservice.ListOtherServiceResponse;
import com.backend.kmsproject.service.OtherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Other Service", description = "Other Service APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/other-services")
public class OtherServiceController {
    private final OtherService otherService;
    private final OtherServiceConverter otherServiceConverter;

    @Operation(summary = "Get List Other Service")
    @GetMapping
    public Response<ListDTO<OtherServiceDTO>> getListOtherService(@ModelAttribute @Valid GetListOtherServiceRequest request) {
        ListOtherServiceResponse response = otherService.getListOtherService(request);
        if (response.getSuccess()) {
            return otherServiceConverter.getSuccess(response);
        }
        return otherServiceConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Get Other Service")
    @GetMapping("/{id}")
    public Response<OtherServiceDTO> getOtherService(@PathVariable("id") Long otherServiceId) {
        GetOtherServiceResponse response = otherService.getOtherService(otherServiceId);
        if (response.getSuccess()) {
            return otherServiceConverter.getSuccess(response);
        }
        return otherServiceConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Create Other Service")
    @PostMapping
    public Response<OnlyIdDTO> createOtherService(@RequestBody CreateUpdateOtherServiceRequest request) {
        OnlyIdResponse response = otherService.createOtherService(request);
        if (response.getSuccess()) {
            return otherServiceConverter.getSuccess(response);
        }
        return otherServiceConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Update Other Service")
    @PutMapping("/{id}")
    public Response<OnlyIdDTO> updateOtherService(@PathVariable("id") Long otherServiceId,
                                                  @RequestBody CreateUpdateOtherServiceRequest request) {
        OnlyIdResponse response = otherService.updateOtherService(otherServiceId, request);
        if (response.getSuccess()) {
            return otherServiceConverter.getSuccess(response);
        }
        return otherServiceConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Delete Other Service")
    @DeleteMapping("/{id}")
    public Response<NoContentDTO> deleteOtherService(@PathVariable("id") Long otherServiceId) {
        NoContentResponse response = otherService.deleteOtherService(otherServiceId);
        return otherServiceConverter.getSuccess(response);
    }
}
