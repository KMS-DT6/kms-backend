package com.backend.kmsproject.service;

import com.backend.kmsproject.request.booking.CreateBookingRequest;
import com.backend.kmsproject.response.OnlyIdResponse;

public interface BookingService {
    OnlyIdResponse createBooking(CreateBookingRequest request);
}
