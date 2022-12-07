package com.backend.kmsproject.controller;

import com.backend.kmsproject.converter.MyAccountConverter;
import com.backend.kmsproject.model.dto.MyAccountDTO;
import com.backend.kmsproject.model.dto.common.NoContentDTO;
import com.backend.kmsproject.model.dto.common.OnlyIdDTO;
import com.backend.kmsproject.request.myaccount.ChangePasswordRequest;
import com.backend.kmsproject.request.myaccount.UpdateMyAccountRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.user.MyAccountResponse;
import com.backend.kmsproject.service.MyAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "My Account", description = "My Account APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my-account")
public class MyAccountController {
    private final MyAccountService myAccountService;
    private final MyAccountConverter myAccountConverter;

    @Operation(summary = "Get My Information")
    @GetMapping
    public Response<MyAccountDTO> getMyAccount() {
        MyAccountResponse response = myAccountService.getMyAccount();
        return myAccountConverter.getSuccess(response);
    }

    @Operation(summary = "Update My Information")
    @PutMapping
    public Response<OnlyIdDTO> updateMyAccount(@RequestBody UpdateMyAccountRequest request) {
        OnlyIdResponse response = myAccountService.updateMyAccount(request);
        if (response.getSuccess()) {
            return myAccountConverter.getSuccess(response);
        }
        return myAccountConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Change My Password")
    @PutMapping("/change-password")
    public Response<NoContentDTO> changeMyPassword(@RequestBody ChangePasswordRequest request) {
        NoContentResponse response = myAccountService.changeMyPassword(request);
        if (response.getSuccess()) {
            return myAccountConverter.getSuccess(response);
        }
        return myAccountConverter.getError(response.getErrorResponse());
    }
}
