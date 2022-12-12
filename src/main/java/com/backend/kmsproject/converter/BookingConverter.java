package com.backend.kmsproject.converter;

import com.backend.kmsproject.model.dto.FootballPitchAdminDTO;
import com.backend.kmsproject.model.dto.HistoryBookingDTO;
import com.backend.kmsproject.model.dto.common.ListDTO;
import com.backend.kmsproject.response.Response;
import com.backend.kmsproject.response.booking.GetBookingResponse;
import com.backend.kmsproject.response.booking.ListHistoryBookingResponse;
import com.backend.kmsproject.response.footballpitchadmin.GetFootballPitchAdminResponse;
import com.backend.kmsproject.util.DatetimeUtils;
import org.springframework.stereotype.Component;

@Component
public class BookingConverter extends CommonConverter {
    public Response<ListDTO<HistoryBookingDTO>> getSuccess(ListHistoryBookingResponse response) {
        return Response.<ListDTO<HistoryBookingDTO>>builder()
                .setSuccess(true)
                .setData(ListDTO.<HistoryBookingDTO>builder()
                        .setTotalItems(response.getHistoryBookings().size())
                        .setItems(response.getHistoryBookings())
                        .build())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }

    public Response<HistoryBookingDTO> getSuccess(GetBookingResponse response) {
        return Response.<HistoryBookingDTO>builder()
                .setSuccess(true)
                .setData(response.getBookingDTO())
                .setDateTime(DatetimeUtils.formatLocalDateTimeNow())
                .build();
    }
}
