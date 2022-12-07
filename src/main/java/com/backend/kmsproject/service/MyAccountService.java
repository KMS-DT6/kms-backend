package com.backend.kmsproject.service;

import com.backend.kmsproject.request.myaccount.ChangePasswordRequest;
import com.backend.kmsproject.request.myaccount.UpdateMyAccountRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.user.MyAccountResponse;

public interface MyAccountService {
    MyAccountResponse getMyAccount();
    OnlyIdResponse updateMyAccount(UpdateMyAccountRequest request);
    NoContentResponse changeMyPassword(ChangePasswordRequest request);
}
