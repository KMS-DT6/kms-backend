package com.backend.kmsproject.request.myaccount;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GetListHistoryBookingRequest implements Serializable {
    private String footballPitchName;
    private String fromDate;
    private String toDate;
    private String status; // accepted?
    private Boolean isPaid;
}
