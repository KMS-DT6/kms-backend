package com.backend.kmsproject.controller;

import com.backend.kmsproject.converter.BookingConverter;
import com.backend.kmsproject.model.dto.FootballPitchAdminDTO;
import com.backend.kmsproject.model.dto.HistoryBookingDTO;
import com.backend.kmsproject.model.dto.common.NoContentDTO;
import com.backend.kmsproject.model.dto.common.OnlyIdDTO;
import com.backend.kmsproject.request.booking.CreateBookingRequest;
import com.backend.kmsproject.request.footballpitchadmin.CreateUpdateFootballPitchAdminRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.booking.GetBookingResponse;
import com.backend.kmsproject.response.footballpitchadmin.GetFootballPitchAdminResponse;
import com.backend.kmsproject.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Booking Pitch", description = "Booking Pitch APIs")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booking-pitches")
public class BookingController {
    private final BookingService bookingService;

    private final BookingConverter bookingConverter;
    @Operation(summary = "Create Booking")
    @PostMapping
    public Response<OnlyIdDTO> createBooking(@RequestBody CreateBookingRequest request) {
        OnlyIdResponse response = bookingService.createBooking(request);
        if (response.getSuccess()) {
            return bookingConverter.getSuccess(response);
        }
        return bookingConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Get Booking")
    @GetMapping("/{id}")
    public Response<HistoryBookingDTO> getFootballPitch(@PathVariable("id") Long id) {
        GetBookingResponse response = bookingService.getBooking(id);
        if (response.getSuccess()) {
            return bookingConverter.getSuccess(response);
        }
        return bookingConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Delete Booking")
    @DeleteMapping("/{id}")
    public Response<NoContentDTO> deleteFootballPitchAdmin(@PathVariable("id") Long id) {
        NoContentResponse response = bookingService.deleteBooking(id);
        if (response.getSuccess()) {
            return bookingConverter.getSuccess(response);
        }
        return bookingConverter.getError(response.getErrorResponse());
    }
}
