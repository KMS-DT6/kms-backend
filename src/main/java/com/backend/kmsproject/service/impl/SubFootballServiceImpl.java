package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.model.entity.SubFootballPitchEntity;
import com.backend.kmsproject.request.subfootballpitch.CreateUpdateSubFootballPitchRequest;
import com.backend.kmsproject.request.subfootballpitch.GetListSubFootballPitchRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.subfootballpitch.GetSubFootballPitchResponse;
import com.backend.kmsproject.response.subfootballpitch.ListSubFootballPitchResponse;
import com.backend.kmsproject.service.SubFootballPitchService;
import org.springframework.stereotype.Service;

import com.backend.kmsproject.common.constants.ErrorCode;
import com.backend.kmsproject.common.exception.NotFoundException;
import com.backend.kmsproject.model.dto.AddressDTO;
import com.backend.kmsproject.model.dto.FootballPitchDTO;
import com.backend.kmsproject.model.entity.AddressEntity;
import com.backend.kmsproject.model.entity.FootballPitchEntity;
import com.backend.kmsproject.repository.dsl.FootballPitchDslRepository;
import com.backend.kmsproject.repository.jpa.AddressRepository;
import com.backend.kmsproject.repository.jpa.FootballPitchRepository;
import com.backend.kmsproject.repository.jpa.SubFootballPitchRepository;
import com.backend.kmsproject.request.footballpitch.CreateUpdateFootballPitchRequest;
import com.backend.kmsproject.request.footballpitch.GetListFootballPitchRequest;
import com.backend.kmsproject.response.ErrorResponse;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.footballpitch.GetFootballPitchResponse;
import com.backend.kmsproject.response.footballpitch.ListFootballPitchResponse;
import com.backend.kmsproject.security.KmsPrincipal;
import com.backend.kmsproject.service.FootballPitchService;
import com.backend.kmsproject.util.RequestUtils;
import com.backend.kmsproject.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class SubFootballServiceImpl implements SubFootballPitchService {
    private final FootballPitchRepository footballPitchRepository;
    private final SubFootballPitchRepository subFootballPitchRepository;

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
        Map<String, String> errors = new HashMap<>();
//        validFormatField(errors, request);
//        validExistField(errors, request.getFootballPitchId(), null);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        SubFootballPitchEntity subFootballPitch = new SubFootballPitchEntity();
        subFootballPitch.setSubFootballPitchName(request.getSubFootballPitchName());
        subFootballPitch.setFootballPitch(footballPitchRepository.findById(principal.getFootballPitchId()).get());
        subFootballPitch.setSize(request.getSize());
        subFootballPitch.setPricePerHour(request.getPricePerHour());
        subFootballPitch.setImage(RequestUtils.blankIfNull(request.getImage()));

        subFootballPitch.setCreatedBy(principal.getUserId());
        subFootballPitch.setCreatedDate(new Timestamp(System.currentTimeMillis()));

        subFootballPitchRepository.save(subFootballPitch);
        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(subFootballPitch.getSubFootBallPitchId())
                .setName(subFootballPitch.getSubFootballPitchName())
                .build();

    }

    public void validFormatField(Map<String, String> errors, CreateUpdateSubFootballPitchRequest request) {
        if (!StringUtils.hasText(request.getSubFootballPitchName())) {
            errors.put("subFootballPitchName", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getSize() == null) {
            errors.put("size", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getPricePerHour() == null) {
            errors.put("pricePerHour", ErrorCode.MISSING_VALUE.name());
        }

    }

    public void validExistField(Map<String, String> errors, Long footballPitchId, String username) {
//        if (footballPitchId != null) {
//            Optional<FootballPitchEntity> footballPitch = footballPitchRepository.findById(footballPitchId);
//            if (footballPitch.isEmpty()) {
//                errors.put("footballPitchId", ErrorCode.MISSING_VALUE.name());
//            }
//        }
        // if(StringUtils.hasText(username)){
        // Boolean existsUser = userRepository.selectExistsUserName(username);
        // if(existsUser){
        // errors.put("username", ErrorCode.ALREADY_EXIST.name());
        // }
        // }
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
