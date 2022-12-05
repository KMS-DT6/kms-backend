package com.backend.kmsproject.controller;

import com.backend.kmsproject.service.OtherService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Other Service", description = "Other Service APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/other-service")
public class OtherServiceController {
    private final OtherService otherService;
}
