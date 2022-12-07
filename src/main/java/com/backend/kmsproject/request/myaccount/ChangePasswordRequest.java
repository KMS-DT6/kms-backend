package com.backend.kmsproject.request.myaccount;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class ChangePasswordRequest implements Serializable {
    private String currentPassword;
    private String newPassword;
    private String confirmPassword;
}
