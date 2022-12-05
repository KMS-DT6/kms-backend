package com.backend.kmsproject.controller;

import com.backend.kmsproject.service.MyAccountService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "My Account", description = "My Account APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/my-account")
public class MyAccountController {
    private final MyAccountService myAccountService;
}
