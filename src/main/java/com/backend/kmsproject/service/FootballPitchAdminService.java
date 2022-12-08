package com.backend.kmsproject.service;

import com.backend.kmsproject.request.footballpitchadmin.CreateFootballPitchAdminRequest;
import com.backend.kmsproject.request.footballpitchadmin.UpdateFootballPitchAdminRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.footballpitchadmin.GetFootballPitchAdminResponse;

public interface FootballPitchAdminService {
    public OnlyIdResponse cerateFootballPitchAdmin(CreateFootballPitchAdminRequest request);
    public GetFootballPitchAdminResponse getFootballPitchAdmin(Long id);
    public OnlyIdResponse updateFootballPitchAdmin(Long id,UpdateFootballPitchAdminRequest request);
    public NoContentResponse deleteFootballPitchAdmin(Long id);
}
