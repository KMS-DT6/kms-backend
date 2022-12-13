package com.backend.kmsproject.service;

import com.backend.kmsproject.request.booking.CreateBookingRequest;
import com.backend.kmsproject.request.booking.GetListBookingRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.booking.GetBookingResponse;
import com.backend.kmsproject.response.booking.ListBookingResponse;
import com.backend.kmsproject.response.booking.ListHistoryBookingResponse;
import com.backend.kmsproject.response.footballpitchadmin.GetFootballPitchAdminResponse;

import java.nio.file.AccessDeniedException;

public interface BookingService {
    OnlyIdResponse createBooking(CreateBookingRequest request);
    GetBookingResponse getBooking(Long idBooking) throws AccessDeniedException;
    NoContentResponse deleteBooking(Long idBooking) throws AccessDeniedException;
    OnlyIdResponse updateBooking(CreateBookingRequest request,Long id) throws AccessDeniedException;
    ListBookingResponse getListBooking(GetListBookingRequest request);
    NoContentResponse acceptBooking(Long idBooking) throws AccessDeniedException;
}
