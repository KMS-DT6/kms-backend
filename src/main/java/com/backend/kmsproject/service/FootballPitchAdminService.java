package com.backend.kmsproject.service;

import com.backend.kmsproject.request.footballpitchadmin.CreateFootballPitchAdminRequest;
import com.backend.kmsproject.request.footballpitchadmin.GetListFootballPitchAdminRequest;
import com.backend.kmsproject.request.footballpitchadmin.UpdateFootballPitchAdminRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.footballpitchadmin.GetFootballPitchAdminResponse;
import com.backend.kmsproject.response.footballpitchadmin.ListFootballPitchAdminResponse;

public interface FootballPitchAdminService {
    OnlyIdResponse createFootballPitchAdmin(CreateFootballPitchAdminRequest request);

    GetFootballPitchAdminResponse getFootballPitchAdmin(Long id);

    OnlyIdResponse updateFootballPitchAdmin(Long id, UpdateFootballPitchAdminRequest request);

    NoContentResponse deleteFootballPitchAdmin(Long id);

    ListFootballPitchAdminResponse getListFootballPitchAdmin(GetListFootballPitchAdminRequest request);

}
