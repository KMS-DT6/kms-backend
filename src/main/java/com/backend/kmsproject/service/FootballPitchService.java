package com.backend.kmsproject.service;

import com.backend.kmsproject.request.footballpitch.CreateUpdateFootballPitchRequest;
import com.backend.kmsproject.request.footballpitch.GetListFootballPitchRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.footballpitch.GetFootballPitchResponse;
import com.backend.kmsproject.response.footballpitch.ListFootballPitchResponse;

public interface FootballPitchService {
    ListFootballPitchResponse getListFootballPitch(GetListFootballPitchRequest request);

    GetFootballPitchResponse getFootballPitch(Long footballPitchId);

    OnlyIdResponse createFootballPitch(CreateUpdateFootballPitchRequest request);

    OnlyIdResponse updateFootballPitch(Long footballPitchId, CreateUpdateFootballPitchRequest request);

    NoContentResponse deleteFootballPitch(Long footballPitchId);
}
