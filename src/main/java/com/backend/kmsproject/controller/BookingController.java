package com.backend.kmsproject.controller;

import com.backend.kmsproject.service.BookingService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Booking Pitch", description = "Booking Pitch APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking-pitches")
public class BookingController {
    private final BookingService bookingService;
}
