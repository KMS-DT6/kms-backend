package com.backend.kmsproject.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "booking")
public class BookingEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookingid")
    private Long bookingId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid")
    private UserEntity customer;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subfootballpitchid")
    private SubFootballPitchEntity subFootballPitch;
    @Column(name = "bookday")
    private LocalDate bookDay;
    @Column(name = "timestart")
    private LocalTime timeStart;
    @Column(name = "timeend")
    private LocalTime timeEnd;
    @Column(name = "totalprice")
    private Double totalPrice;
    @Column(name = "ispaid")
    private Boolean isPaid;
    @Column(name = "status")
    private String status;
    @Column(name = "createddate")
    private Timestamp createdDate;
    @Column(name = "modifieddate")
    private Timestamp modifiedDate;
    @Column(name = "createdby")
    private Long createdBy;
    @Column(name = "modifiedby")
    private Long modifiedBy;
}
