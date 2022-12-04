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
@Table(name = "address")
public class AddressEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "addressid")
    private Long addressId;
    @Column(name = "address")
    private String address;
    @Column(name = "district")
    private String district;
    @Column(name = "city")
    private String city;
    @Column(name = "createddate")
    private Timestamp createdDate;
    @Column(name = "modifieddate")
    private Timestamp modifiedDate;
    @Column(name = "createdby")
    private Long createdBy;
    @Column(name = "modifiedby")
    private Long modifiedBy;
}
