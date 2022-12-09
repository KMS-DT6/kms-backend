package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.common.constants.ErrorCode;
import com.backend.kmsproject.common.exception.NotFoundException;
import com.backend.kmsproject.model.dto.OtherServiceDTO;
import com.backend.kmsproject.model.entity.FootballPitchEntity;
import com.backend.kmsproject.model.entity.OtherServiceEntity;
import com.backend.kmsproject.repository.dsl.OtherDslRepository;
import com.backend.kmsproject.repository.jpa.FootballPitchRepository;
import com.backend.kmsproject.repository.jpa.OtherServiceRepository;
import com.backend.kmsproject.request.otherservice.CreateUpdateOtherServiceRequest;
import com.backend.kmsproject.request.otherservice.GetListOtherServiceRequest;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.otherservice.GetOtherServiceResponse;
import com.backend.kmsproject.response.otherservice.ListOtherServiceResponse;
import com.backend.kmsproject.security.KmsPrincipal;
import com.backend.kmsproject.service.OtherService;
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
public class OtherServiceImpl implements OtherService {
    private final OtherDslRepository otherServiceDslRepository;
    private final OtherServiceRepository otherServiceRepository;
    private final FootballPitchRepository footballPitchRepository;

    @Override
    public ListOtherServiceResponse getListOtherService(GetListOtherServiceRequest request) {
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        request.setFootballPitchId(principal.getFootballPitchId());
        List<OtherServiceEntity> otherServices = otherServiceDslRepository.getListOtherService(request);
        return ListOtherServiceResponse.builder()
                .setSuccess(true)
                .setOtherServices(otherServices.stream()
                        .map(os -> toBuilder(os))
                        .collect(Collectors.toList()))
                .build();
    }

    @Override
    public GetOtherServiceResponse getOtherService(Long otherServiceId) {
        OtherServiceEntity otherService = otherServiceRepository.findByOtherId(otherServiceId)
                .orElseThrow(() -> new NotFoundException("Not found service"));
        return GetOtherServiceResponse.builder()
                .setSuccess(true)
                .setOtherService(toBuilder(otherService))
                .build();
    }

    private OtherServiceDTO toBuilder(OtherServiceEntity otherService) {
        OtherServiceDTO.OtherServiceDTOBuilder builder = OtherServiceDTO.builder();
        builder.setOtherServiceId(otherService.getOtherServiceId());
        builder.setOtherServiceName(otherService.getOtherServiceName());
        builder.setPricePerOne(RequestUtils.defaultIfNull(otherService.getPricePerOne(), 0D));
        builder.setPricePerHour(RequestUtils.defaultIfNull(otherService.getPricePerHour(), 0D));
        builder.setFootballPitchId(otherService.getFootballPitch().getFootballPitchId());
        return builder.build();
    }

    @Override
    public OnlyIdResponse createOtherService(CreateUpdateOtherServiceRequest request) {
        Map<String, String> errors = new HashMap<>();
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        request.setFootballPitchId(principal.getFootballPitchId());
        validFormatField(errors, request);
        validExistField(errors, request, null);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .build();
        }
        request.setQuantity(RequestUtils.defaultIfNull(request.getQuantity(), 0));
        OtherServiceEntity otherService = new OtherServiceEntity();
        otherService.setOtherServiceName(request.getOtherServiceName());
        FootballPitchEntity footballPitch = footballPitchRepository.findById(principal.getFootballPitchId()).get();
        otherService.setFootballPitch(footballPitch);
        otherService.setQuantity(request.getQuantity());
        otherService.setStatus(request.getQuantity() > 0 ? Boolean.TRUE : Boolean.FALSE);
        otherService.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        if (request.getPricePerOne() != null && request.getPricePerOne() > 0) {
            otherService.setPricePerOne(request.getPricePerOne());
        }
        if (request.getPricePerHour() != null && request.getPricePerHour() > 0) {
            otherService.setPricePerHour(request.getPricePerHour());
        }
        otherService.setCreatedBy(principal.getUserId());
        otherServiceRepository.save(otherService);
        return null;
    }

    @Override
    public OnlyIdResponse updateOtherService(Long otherServiceId, CreateUpdateOtherServiceRequest request) {
        return null;
    }

    private void validFormatField(Map<String, String> errors, CreateUpdateOtherServiceRequest request) {
        if (!StringUtils.hasText(request.getOtherServiceName())) {
            errors.put("otherServiceName", ErrorCode.MISSING_VALUE.name());
        }
    }

    private void validExistField(Map<String, String> errors, CreateUpdateOtherServiceRequest request, Long otherServiceId) {
        if (StringUtils.hasText(request.getOtherServiceName())) {
            Optional<OtherServiceEntity> otherService = otherServiceRepository.findByNameAndFootballPitchId(request.getOtherServiceName(),
                    request.getFootballPitchId());
            if (otherService.isPresent() && !otherService.get().getOtherServiceId().equals(otherServiceId)) {
                errors.put("otherServiceName", ErrorCode.ALREADY_EXIST.name());
            }
        }
    }


    @Override
    public NoContentResponse deleteOtherService(Long otherServiceId) {
        return null;
    }
}
