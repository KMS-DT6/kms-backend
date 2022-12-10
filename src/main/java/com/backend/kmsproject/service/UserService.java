package com.backend.kmsproject.service;

import com.backend.kmsproject.request.user.RegisterAccountRequest;
import com.backend.kmsproject.response.NoContentResponse;

public interface UserService {
    NoContentResponse registerAccount(RegisterAccountRequest request);
}
