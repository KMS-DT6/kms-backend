package com.backend.kmsproject.controller;

import com.backend.kmsproject.service.FootballPitchService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Football Pitch", description = "Football Pitch APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/football-pitches")
public class FootballPitchController {
    private final FootballPitchService footballPitchService;
}
