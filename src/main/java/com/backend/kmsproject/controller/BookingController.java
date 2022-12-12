package com.backend.kmsproject.controller;

import com.backend.kmsproject.converter.BookingConverter;
import com.backend.kmsproject.model.dto.BookingDTO;
import com.backend.kmsproject.model.dto.FootballPitchAdminDTO;
import com.backend.kmsproject.model.dto.HistoryBookingDTO;
import com.backend.kmsproject.model.dto.common.ListDTO;
import com.backend.kmsproject.model.dto.common.NoContentDTO;
import com.backend.kmsproject.model.dto.common.OnlyIdDTO;
import com.backend.kmsproject.request.booking.CreateBookingRequest;
import com.backend.kmsproject.request.booking.GetListBookingRequest;
import com.backend.kmsproject.request.footballpitchadmin.CreateUpdateFootballPitchAdminRequest;
import com.backend.kmsproject.request.footballpitchadmin.GetListFootballPitchAdminRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.booking.GetBookingResponse;
import com.backend.kmsproject.response.booking.ListBookingResponse;
import com.backend.kmsproject.response.booking.ListHistoryBookingResponse;
import com.backend.kmsproject.response.footballpitchadmin.GetFootballPitchAdminResponse;
import com.backend.kmsproject.response.footballpitchadmin.ListFootballPitchAdminResponse;
import com.backend.kmsproject.service.BookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;

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
    public Response<BookingDTO> getBooking(@PathVariable("id") Long id) throws AccessDeniedException {
        GetBookingResponse response = bookingService.getBooking(id);
        if (response.getSuccess()) {
            return bookingConverter.getSuccess(response);
        }
        return bookingConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Delete Booking")
    @DeleteMapping("/{id}")
    public Response<NoContentDTO> deleteBooking(@PathVariable("id") Long id) {
        NoContentResponse response = null;
        try {
            response = bookingService.deleteBooking(id);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
        if (response.getSuccess()) {
            return bookingConverter.getSuccess(response);
        }
        return bookingConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Get List Booking")
    @GetMapping
    public Response<ListDTO<BookingDTO>> getListBooking(@ModelAttribute @Valid GetListBookingRequest request) {
        ListBookingResponse response = bookingService.getListBooking(request);
        if (response.getSuccess()) {
            return bookingConverter.getSuccess(response);
        }
        return bookingConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Update FootballPitch Admin")
    @PutMapping("/{id}")
    public Response<OnlyIdDTO> updateBooking(@PathVariable("id") Long id,
                                             @RequestBody CreateBookingRequest request) {
        OnlyIdResponse response = null;
        try {
            response = bookingService.updateBooking(request, id);
        } catch (AccessDeniedException e) {
            throw new RuntimeException(e);
        }
        if (response.getSuccess()) {
            return bookingConverter.getSuccess(response);
        }
        return bookingConverter.getError(response.getErrorResponse());
    }

    @Operation(summary = "Accept Booking")
    @PostMapping("/accept/{id}")
    public Response<NoContentDTO> acceptBooking(@PathVariable("id") Long id) throws AccessDeniedException {
        NoContentResponse response = bookingService.acceptBooking(id);
        if (response.getSuccess()) {
            return bookingConverter.getSuccess(response);
        }
        return bookingConverter.getError(response.getErrorResponse());
    }
}
