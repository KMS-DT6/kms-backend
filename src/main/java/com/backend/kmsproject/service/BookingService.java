package com.backend.kmsproject.service;

import com.backend.kmsproject.request.booking.CreateBookingRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.booking.GetBookingResponse;
import com.backend.kmsproject.response.footballpitchadmin.GetFootballPitchAdminResponse;

public interface BookingService {
    OnlyIdResponse createBooking(CreateBookingRequest request);
    GetBookingResponse getBooking(Long idBooking);
    NoContentResponse deleteBooking(Long idBooking);
}
