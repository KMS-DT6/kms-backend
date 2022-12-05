package com.backend.kmsproject.controller;

import com.backend.kmsproject.service.FootballPitchAdminService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Football Pitch Admin", description = "Football Pitch Admin APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/football-pitch-admins")
public class FootballPitchAdminController {
    private final FootballPitchAdminService footballPitchAdminService;
}
