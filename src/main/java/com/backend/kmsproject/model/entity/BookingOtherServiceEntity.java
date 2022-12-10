package com.backend.kmsproject.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bookingotherservice")
public class BookingOtherServiceEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookingotherserviceid")
    private Long bookingOtherServiceId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bookingid")
    private BookingEntity booking;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "otherserviceid")
    private OtherServiceEntity otherService;
    @Column(name = "quantity")
    private Integer quantity;
}
