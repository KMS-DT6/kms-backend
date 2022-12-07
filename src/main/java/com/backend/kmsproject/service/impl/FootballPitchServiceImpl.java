package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.common.constants.ErrorCode;
import com.backend.kmsproject.common.exception.NotFoundException;
import com.backend.kmsproject.model.dto.AddressDTO;
import com.backend.kmsproject.model.dto.FootballPitchDTO;
import com.backend.kmsproject.model.entity.AddressEntity;
import com.backend.kmsproject.model.entity.FootballPitchEntity;
import com.backend.kmsproject.repository.dsl.FootballPitchDslRepository;
import com.backend.kmsproject.repository.jpa.AddressRepository;
import com.backend.kmsproject.repository.jpa.FootballPitchRepository;
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

@Service
@RequiredArgsConstructor
public class FootballPitchServiceImpl implements FootballPitchService {
    private final FootballPitchRepository footballPitchRepository;
    private final FootballPitchDslRepository footballPitchDslRepository;
    private final AddressRepository addressRepository;

    @Override
    public ListFootballPitchResponse getListFootballPitch(GetListFootballPitchRequest request) {
        List<FootballPitchEntity> listFootballPitch = footballPitchDslRepository.listFootballPitch(request);
        return ListFootballPitchResponse.builder()
                .setSuccess(true)
                .setFootballPitches(listFootballPitch.stream()
                        .map(fp -> getBuilder(fp))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public GetFootballPitchResponse getFootballPitch(Long footballPitchId) {
        FootballPitchEntity footballPitch = footballPitchRepository.findByFootballPitchId(footballPitchId)
                .orElseThrow(() -> new NotFoundException("Not found football pitch"));
        return GetFootballPitchResponse.builder()
                .setSuccess(true)
                .setFootballPitch(getBuilder(footballPitch))
                .build();
    }

    public FootballPitchDTO getBuilder(FootballPitchEntity footballPitch) {
        return FootballPitchDTO.builder()
                .setFootballPitchId(footballPitch.getFootballPitchId())
                .setFootballPitchName(footballPitch.getFootballPitchName())
                .setImage(RequestUtils.blankIfNull(footballPitch.getImage()))
                .setPhoneNumber(RequestUtils.blankIfNull(footballPitch.getPhoneNumber()))
                .setAddress(AddressDTO.builder()
                        .setAddressId(footballPitch.getAddress() != null ? footballPitch.getAddress().getAddressId() : -1L)
                        .setAddress(footballPitch.getAddress() != null ? footballPitch.getAddress().getAddress() : "")
                        .setDistrict(footballPitch.getAddress() != null ? footballPitch.getAddress().getDistrict() : "")
                        .setCity(footballPitch.getAddress() != null ? footballPitch.getAddress().getCity() : "")
                        .build())
                .build();
    }

    @Override
    public OnlyIdResponse createFootballPitch(CreateUpdateFootballPitchRequest request) {
        Map<String, String> errors = new HashMap<>();
        validFormatField(errors, request);
        validExistField(errors, request, null);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        FootballPitchEntity footballPitch = new FootballPitchEntity();
        footballPitch.setFootballPitchName(request.getFootballPitchName());
        footballPitch.setImage(RequestUtils.blankIfNull(request.getImage()));
        footballPitch.setPhoneNumber(RequestUtils.blankIfNull(request.getPhoneNumber()));
        footballPitch.setCreatedBy(principal.getUserId());
        footballPitch.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        AddressEntity address = new AddressEntity();
        address.setAddress(request.getAddress());
        address.setDistrict(RequestUtils.blankIfNull(request.getDistrict()));
        address.setCity(RequestUtils.blankIfNull(request.getCity()));
        addressRepository.save(address);
        footballPitch.setAddress(address);
        footballPitchRepository.save(footballPitch);
        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(footballPitch.getFootballPitchId())
                .setName(footballPitch.getFootballPitchName())
                .build();
    }

    @Override
    public OnlyIdResponse updateFootballPitch(Long footballPitchId, CreateUpdateFootballPitchRequest request) {
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        FootballPitchEntity footballPitch = footballPitchRepository.findByFootballPitchId(footballPitchId)
                .orElseThrow(() -> new NotFoundException("Not found football pitch"));
        Map<String, String> errors = new HashMap<>();
        validFormatField(errors, request);
        validExistField(errors, request, footballPitch.getFootballPitchId());
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        footballPitch.setFootballPitchName(request.getFootballPitchName());
        footballPitch.setImage(RequestUtils.blankIfNull(request.getImage()));
        footballPitch.setPhoneNumber(request.getPhoneNumber());
        footballPitch.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        footballPitch.setModifiedBy(principal.getUserId());
        AddressEntity address;
        if (footballPitch.getAddress() == null && StringUtils.hasText(request.getAddress())) {
            address = new AddressEntity();
            address.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            address.setCreatedBy(principal.getUserId());
        } else {
            address = addressRepository.findById(footballPitch.getAddress().getAddressId())
                    .orElseThrow(() -> new NotFoundException("Not found address"));
        }
        address.setAddress(request.getAddress());
        address.setDistrict(RequestUtils.blankIfNull(request.getDistrict()));
        address.setCity(RequestUtils.blankIfNull(request.getCity()));
        address.setModifiedBy(principal.getUserId());
        address.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        footballPitch.setAddress(address);
        footballPitchRepository.save(footballPitch);
        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(footballPitch.getFootballPitchId())
                .setName(footballPitch.getFootballPitchName())
                .build();
    }

    public void validFormatField(Map<String, String> errors, CreateUpdateFootballPitchRequest request) {
        if (!StringUtils.hasText(request.getFootballPitchName())) {
            errors.put("footballPitchName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getAddress())) {
            errors.put("address", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getPhoneNumber())) {
            errors.put("phone", ErrorCode.MISSING_VALUE.name());
        }
    }

    public void validExistField(Map<String, String> errors, CreateUpdateFootballPitchRequest request, Long footballPitchId) {
        if (StringUtils.hasText(request.getAddress()) && StringUtils.hasText(request.getFootballPitchName())) {
            Optional<FootballPitchEntity> footballPitch = footballPitchRepository.findByNameAndAddress(request.getFootballPitchName(), request.getAddress());
            if (footballPitch.isPresent() && !footballPitch.get().getFootballPitchId().equals(footballPitchId)) {
                errors.put("footballPitchName", ErrorCode.ALREADY_EXIST.name());
            }
        }
    }

    @Override
    public NoContentResponse deleteFootballPitch(Long footballPitchId) {
        FootballPitchEntity footballPitch = footballPitchRepository.findByFootballPitchId(footballPitchId)
                .orElseThrow(() -> new NotFoundException("Not found football pitch"));
        footballPitchRepository.delete(footballPitch);
        addressRepository.delete(footballPitch.getAddress());
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }
}
