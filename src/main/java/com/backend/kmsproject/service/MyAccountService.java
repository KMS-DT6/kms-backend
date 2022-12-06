package com.backend.kmsproject.service;

import com.backend.kmsproject.request.UpdateMyAccountRequest;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.user.MyAccountResponse;

public interface MyAccountService {
    MyAccountResponse getMyAccount();
    OnlyIdResponse updateMyAccount(UpdateMyAccountRequest request);
}
