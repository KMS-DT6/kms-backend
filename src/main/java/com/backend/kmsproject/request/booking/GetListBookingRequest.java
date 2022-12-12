package com.backend.kmsproject.request.booking;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GetListBookingRequest implements Serializable {
    private Long subFootballPitchId;
    private String fromDate;
    private String toDate;
    private Boolean status;
    private Boolean isPaid;
}
