package com.backend.kmsproject.request.bookingotherservice;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class BookingOtherServiceRequest implements Serializable {
    private Long otherServiceId;
    private Integer quantity;
}
