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
@Table(name = "subfootbalpitch")
public class SubFootballPitchEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subfootbalpitchid")
    private Long subFootBallPitchId;
    @Column(name = "subfootballpitchname")
    private String subFootballPitchName;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "footballpitchid")
    private FootballPitchEntity footballPitch;
    @Column(name = "image")
    private String image;
    @Column(name = "size")
    private Integer size;
    @Column(name = "priceperhour")
    private Double pricePerHour;
    @Column(name = "status")
    private Boolean status;
    @Column(name = "createddate")
    private Timestamp createdDate;
    @Column(name = "modifieddate")
    private Timestamp modifiedDate;
    @Column(name = "createdby")
    private Long createdBy;
    @Column(name = "modifiedby")
    private Long modifiedBy;
}
