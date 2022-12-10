package com.backend.kmsproject.model.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
@Builder(setterPrefix = "set")
public class HistoryBookingDTO implements Serializable {
    private Long bookingId;
    private String bookDate;
    private String timeStart;
    private String timeEnd;
    private Boolean isPaid;
    private Boolean status;
    private Double pricePitch;
    private Double totalPriceIncludeService;
    private OtherService otherService;
    private FootballPitch footballPitch;

    @Getter
    @Setter
    @Builder(setterPrefix = "set")
    public static class FootballPitch {
        private String footballPitchName;
        private String subFootballPitchName;
        private Address address;

        @Getter
        @Setter
        @Builder(setterPrefix = "set")
        public static class Address {
            private String address;
            private String district;
            private String city;
        }
    }

    @Getter
    @Setter
    @Builder(setterPrefix = "set")
    public static class OtherService {
        private Integer totalItems;
        private Double totalPriceOtherService;
        private List<Item> items;

        @Getter
        @Setter
        @Builder(setterPrefix = "set")
        public static class Item {
            private String name;
            private Integer quantity;
            private Double price;
        }
    }
}
