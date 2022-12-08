package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.common.constants.ErrorCode;
import com.backend.kmsproject.common.enums.KmsRole;
import com.backend.kmsproject.model.entity.AddressEntity;
import com.backend.kmsproject.model.entity.FootballPitchEntity;
import com.backend.kmsproject.model.entity.RoleEntity;
import com.backend.kmsproject.model.entity.UserEntity;
import com.backend.kmsproject.repository.jpa.AddressRepository;
import com.backend.kmsproject.repository.jpa.FootballPitchRepository;
import com.backend.kmsproject.repository.jpa.RoleRepository;
import com.backend.kmsproject.repository.jpa.UserRepository;
import com.backend.kmsproject.request.footballpitch.CreateUpdateFootballPitchRequest;
import com.backend.kmsproject.request.footballpitchadmin.CreateUpdateFootballPitchAdminRequest;
import com.backend.kmsproject.response.ErrorResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.security.KmsPrincipal;
import com.backend.kmsproject.service.BookingService;
import com.backend.kmsproject.service.FootballPitchAdminService;
import com.backend.kmsproject.util.RequestUtils;
import com.backend.kmsproject.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FootballPitchAdminServiceImpl implements FootballPitchAdminService {

    private final FootballPitchRepository footballPitchRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final RoleRepository roleRepository;

    public void validFormatField(Map<String, String> errors, CreateUpdateFootballPitchAdminRequest request) {
        if (!StringUtils.hasText(request.getUsername())) {
            errors.put("username", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getAddress())) {
            errors.put("address", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getPhoneNumber())) {
            errors.put("phone", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getPassword())) {
            errors.put("password", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getFirstName())) {
            errors.put("firstName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getLastName())) {
            errors.put("lastName", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getFootballPitchId() == null) {
            errors.put("footballPitchId", ErrorCode.MISSING_VALUE.name());
        }
    }

    public void validExistField(Map<String, String> errors, Long footballPitchId, String username) {
        if (footballPitchId != null) {
            Optional<FootballPitchEntity> footballPitch = footballPitchRepository.findById(footballPitchId);
            if(footballPitch.isEmpty()){
                errors.put("footballPitchId", ErrorCode.MISSING_VALUE.name());
            }
        }
        if(StringUtils.hasText(username)){
            Boolean existsUser = userRepository.selectExistsUserName(username);
            if(existsUser){
                errors.put("username", ErrorCode.ALREADY_EXIST.name());
            }
        }
    }

    @Override
    public OnlyIdResponse cerateFootballPitchAdmin(CreateUpdateFootballPitchAdminRequest request) {
        Map<String, String> errors = new HashMap<>();
        validFormatField(errors, request);
        validExistField(errors, request.getFootballPitchId(), request.getUsername());
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        AddressEntity address = new AddressEntity();
        address.setAddress(request.getAddress());
        address.setDistrict(RequestUtils.blankIfNull(request.getDistrict()));
        address.setCity(RequestUtils.blankIfNull(request.getCity()));
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setFootballPitch(footballPitchRepository.findById(request.getFootballPitchId()).get());
        user.setCreatedBy(principal.getUserId());
        user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        Optional<RoleEntity> role = roleRepository.findByRoleName(KmsRole.FOOTBALL_PITCH_ROLE.getRole());
        if(!role.isEmpty()){
            user.setRole(role.get());
        }
        addressRepository.save(address);
        user.setAddress(address);
        userRepository.save(user);

        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(user.getUserId())
                .setName(user.getFirstName()+" "+user.getLastName())
                .build();
    }
}
