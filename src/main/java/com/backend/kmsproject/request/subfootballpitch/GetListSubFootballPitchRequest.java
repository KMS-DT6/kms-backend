package com.backend.kmsproject.request.subfootballpitch;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GetListSubFootballPitchRequest implements Serializable {
    private String subFootballPitchName;
    private String footballPitchName;
    private Long footballPitchId;
    private Boolean status;
    private Integer size;
}
