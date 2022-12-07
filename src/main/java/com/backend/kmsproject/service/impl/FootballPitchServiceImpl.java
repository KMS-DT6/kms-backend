package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.repository.dsl.FootballPitchDslRepository;
import com.backend.kmsproject.repository.jpa.FootballPitchRepository;
import com.backend.kmsproject.request.footballpitch.CreateUpdateFootballPitchRequest;
import com.backend.kmsproject.request.footballpitch.GetListFootballPitchRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.footballpitch.GetFootballPitchResponse;
import com.backend.kmsproject.response.footballpitch.ListFootballPitchResponse;
import com.backend.kmsproject.service.FootballPitchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FootballPitchServiceImpl implements FootballPitchService {
    private final FootballPitchRepository footballPitchRepository;
    private final FootballPitchDslRepository footballPitchDslRepository;
    @Override
    public ListFootballPitchResponse getListFootballPitch(GetListFootballPitchRequest request) {
        return null;
    }

    @Override
    public GetFootballPitchResponse getFootballPitch(Long footballPitchId) {
        return null;
    }

    @Override
    public OnlyIdResponse createFootballPitch(CreateUpdateFootballPitchRequest request) {
        return null;
    }

    @Override
    public OnlyIdResponse updateFootballPitch(Long footballPitchId, CreateUpdateFootballPitchRequest request) {
        return null;
    }

    @Override
    public NoContentResponse deleteFootballPitch(Long footballPitchId) {
        return null;
    }
}
