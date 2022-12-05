package com.backend.kmsproject.controller;

import com.backend.kmsproject.service.SystemAdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "System Admin", description = "System Admin APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/system-admins")
public class SystemAdminController {
    private final SystemAdminService systemAdminService;
}
