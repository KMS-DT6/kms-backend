package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.request.subfootballpitch.CreateUpdateSubFootballPitchRequest;
import com.backend.kmsproject.request.subfootballpitch.GetListSubFootballPitchRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.subfootballpitch.GetSubFootballPitchResponse;
import com.backend.kmsproject.response.subfootballpitch.ListSubFootballPitchResponse;
import com.backend.kmsproject.service.SubFootballPitchService;
import org.springframework.stereotype.Service;

@Service
public class SubFootballServiceImpl implements SubFootballPitchService {
    @Override
    public ListSubFootballPitchResponse getListSubFootballPitch(GetListSubFootballPitchRequest request) {
        return null;
    }

    @Override
    public GetSubFootballPitchResponse getSubFootballPitch(Long subFootballPitchId) {
        return null;
    }

    @Override
    public OnlyIdResponse createSubFootballPitch(CreateUpdateSubFootballPitchRequest request) {
        return null;
    }

    @Override
    public OnlyIdResponse updateSubFootballPitch(Long subFootballPitchId, CreateUpdateSubFootballPitchRequest request) {
        return null;
    }

    @Override
    public NoContentResponse deleteSubFootballPitch(Long subFootballPitchId) {
        return null;
    }
}
