package com.backend.kmsproject.model.entity;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "otherservice")
public class OtherServiceEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "otherserviceid")
    private Long otherServiceId;
    @Column(name = "otherservicename")
    private String otherServiceName;
    @Column(name = "priceperone")
    private Double pricePerOne;
    @Column(name = "priceperhour")
    private Double pricePerHour;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "footballpitchid")
    private FootballPitchEntity footballPitch;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "quantity")
    private Integer quantity;
    @Column(name = "createddate")
    private Timestamp createdDate;
    @Column(name = "modifieddate")
    private Timestamp modifiedDate;
    @Column(name = "createdby")
    private Long createdBy;
    @Column(name = "modifiedby")
    private Long modifiedBy;
}
