package com.backend.kmsproject.service;

import com.backend.kmsproject.request.subfootballpitch.CreateUpdateSubFootballPitchRequest;
import com.backend.kmsproject.request.subfootballpitch.GetListSubFootballPitchRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.subfootballpitch.GetSubFootballPitchResponse;
import com.backend.kmsproject.response.subfootballpitch.ListSubFootballPitchResponse;

public interface SubFootballPitchService {
    ListSubFootballPitchResponse getListSubFootballPitch(GetListSubFootballPitchRequest request);

    GetSubFootballPitchResponse getSubFootballPitch(Long subFootballPitchId);

    OnlyIdResponse createSubFootballPitch(CreateUpdateSubFootballPitchRequest request);

    OnlyIdResponse updateSubFootballPitch(Long subFootballPitchId, CreateUpdateSubFootballPitchRequest request);

    NoContentResponse deleteSubFootballPitch(Long subFootballPitchId);
}
