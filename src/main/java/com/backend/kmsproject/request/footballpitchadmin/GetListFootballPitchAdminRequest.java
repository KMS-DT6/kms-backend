package com.backend.kmsproject.request.footballpitchadmin;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GetListFootballPitchAdminRequest implements Serializable {
    private String contextSearch; // username or firstname or lastname or phone
    private Long footballPitchId;
}
