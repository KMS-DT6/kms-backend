package com.backend.kmsproject.service;

import com.backend.kmsproject.request.footballpitch.CreateUpdateFootballPitchRequest;
import com.backend.kmsproject.request.footballpitchadmin.CreateUpdateFootballPitchAdminRequest;
import com.backend.kmsproject.response.OnlyIdResponse;

public interface FootballPitchAdminService {
    public OnlyIdResponse cerateFootballPitchAdmin(CreateUpdateFootballPitchAdminRequest request);
}
