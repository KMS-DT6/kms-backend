package com.backend.kmsproject.request.booking;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GetListBookingRequest implements Serializable {
    private Long subFootballPitchId;
    private String customerName;
    private String fromDate;
    private String toDate;
    private String status;
    private Boolean isPaid;
}
