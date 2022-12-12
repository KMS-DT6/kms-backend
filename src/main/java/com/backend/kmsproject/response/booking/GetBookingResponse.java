package com.backend.kmsproject.response.booking;

import com.backend.kmsproject.model.dto.HistoryBookingDTO;
import com.backend.kmsproject.response.ErrorResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class GetBookingResponse implements Serializable {
    private Boolean success;
    private ErrorResponse errorResponse;
    private HistoryBookingDTO bookingDTO;
}
