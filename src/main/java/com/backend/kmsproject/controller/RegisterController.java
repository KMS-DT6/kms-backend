package com.backend.kmsproject.controller;

import com.backend.kmsproject.converter.UserConverter;
import com.backend.kmsproject.model.dto.common.NoContentDTO;
import com.backend.kmsproject.request.user.RegisterAccountRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Customer", description = "Customer APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/customer")
public class RegisterController {
    private final UserService userService;
    private final UserConverter userConverter;

    @Operation(summary = "Customer Register Account")
    @PostMapping("/register")
    public Response<NoContentDTO> registerAccount(@RequestBody RegisterAccountRequest request) {
        NoContentResponse response = userService.registerAccount(request);
        if (response.getSuccess()) {
            return userConverter.getSuccess(response);
        }
        return userConverter.getError(response.getErrorResponse());
    }
}
