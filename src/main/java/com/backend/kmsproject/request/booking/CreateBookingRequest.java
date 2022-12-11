package com.backend.kmsproject.request.booking;

import com.backend.kmsproject.model.entity.SubFootballPitchEntity;
import com.backend.kmsproject.model.entity.UserEntity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class CreateBookingRequest {
    private Long userid;
    private Long subFootballPitchId;
    private LocalDate bookDay;
    private LocalTime timeStart;
    private LocalTime timeEnd;
}
