package com.backend.kmsproject.request.booking;

import com.backend.kmsproject.request.bookingotherservice.BookingOtherServiceRequest;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
public class CreateBookingRequest {
    private Long subFootballPitchId;
    private String bookDay;
    private String timeStart;
    private String timeEnd;
    private List<BookingOtherServiceRequest> bookingOtherService;
}
