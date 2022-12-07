package com.backend.kmsproject.request.footballpitch;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GetListFootballPitchRequest implements Serializable {
    private String footballPitchName;
    private String district;
    private String city;
}
