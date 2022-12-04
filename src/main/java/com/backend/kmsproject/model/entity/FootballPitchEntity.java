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
@Table(name = "footballpitch")
public class FootballPitchEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "footballpitchid")
    private Long footballPitchId;
    @Column(name = "footballpitchname")
    private String footballPitchName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "addressid")
    private AddressEntity address;
    @Column(name = "image")
    private String image;
    @Column(name = "phonenumber")
    private String phoneNumber;
    @Column(name = "createddate")
    private Timestamp createdDate;
    @Column(name = "modifieddate")
    private Timestamp modifiedDate;
    @Column(name = "createdby")
    private Long createdBy;
    @Column(name = "modifiedby")
    private Long modifiedBy;
}
