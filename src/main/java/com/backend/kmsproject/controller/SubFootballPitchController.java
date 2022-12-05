package com.backend.kmsproject.controller;

import com.backend.kmsproject.service.SubFootballPitchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Sub Football Pitch", description = "Sub Football Pitch APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/sub-football-pitches")
public class SubFootballPitchController {
    private final SubFootballPitchService subFootballPitchService;
}
