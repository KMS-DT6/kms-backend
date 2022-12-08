package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.common.constants.ErrorCode;
import com.backend.kmsproject.common.enums.KmsRole;
import com.backend.kmsproject.common.exception.NotFoundException;
import com.backend.kmsproject.model.dto.AddressDTO;
import com.backend.kmsproject.model.dto.FootballPitchAdminDTO;
import com.backend.kmsproject.model.dto.FootballPitchDTO;
import com.backend.kmsproject.model.entity.AddressEntity;
import com.backend.kmsproject.model.entity.FootballPitchEntity;
import com.backend.kmsproject.model.entity.RoleEntity;
import com.backend.kmsproject.model.entity.UserEntity;
import com.backend.kmsproject.repository.jpa.AddressRepository;
import com.backend.kmsproject.repository.jpa.FootballPitchRepository;
import com.backend.kmsproject.repository.jpa.RoleRepository;
import com.backend.kmsproject.repository.jpa.UserRepository;
import com.backend.kmsproject.request.footballpitchadmin.CreateFootballPitchAdminRequest;
import com.backend.kmsproject.request.footballpitchadmin.UpdateFootballPitchAdminRequest;
import com.backend.kmsproject.response.ErrorResponse;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.footballpitchadmin.GetFootballPitchAdminResponse;
import com.backend.kmsproject.response.footballpitchadmin.ListFootballPitchAdminResponse;
import com.backend.kmsproject.security.KmsPrincipal;
import com.backend.kmsproject.service.FootballPitchAdminService;
import com.backend.kmsproject.util.RequestUtils;
import com.backend.kmsproject.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
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
public class FootballPitchAdminServiceImpl implements FootballPitchAdminService {

    private final FootballPitchRepository footballPitchRepository;

    private final PasswordEncoder passwordEncoder;

    private final UserRepository userRepository;

    private final AddressRepository addressRepository;

    private final RoleRepository roleRepository;

    public void validFormatField(Map<String, String> errors, CreateFootballPitchAdminRequest request) {
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

    public void validFormatFieldForUpdate(Map<String, String> errors, UpdateFootballPitchAdminRequest request) {
        if (!StringUtils.hasText(request.getAddress())) {
            errors.put("address", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getPhoneNumber())) {
            errors.put("phone", ErrorCode.MISSING_VALUE.name());
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

    public void validExistFieldUserName(Map<String, String> errors, String username) {
        if(StringUtils.hasText(username)){
            Boolean existsUser = userRepository.selectExistsUserName(username);
            if(existsUser){
                errors.put("username", ErrorCode.ALREADY_EXIST.name());
            }
        }
    }
    public void validExistFieldFootBallPitch(Map<String, String> errors, Long footballPitchId) {
        if (footballPitchId != null) {
            Optional<FootballPitchEntity> footballPitch = footballPitchRepository.findById(footballPitchId);
            if (footballPitch.isEmpty()) {
                errors.put("footballPitchId", ErrorCode.MISSING_VALUE.name());
            }
        }
    }

    @Override
    public OnlyIdResponse cerateFootballPitchAdmin(CreateFootballPitchAdminRequest request) {
        Map<String, String> errors = new HashMap<>();
        validFormatField(errors, request);
        validExistFieldUserName(errors, request.getUsername());
        validExistFieldFootBallPitch(errors,request.getFootballPitchId());
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
        address.setCreatedBy(principal.getUserId());
        address.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setFootballPitch(footballPitchRepository.findById(request.getFootballPitchId()).get());
        user.setCreatedBy(principal.getUserId());
        user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        Optional<RoleEntity> role = roleRepository.findById(KmsRole.FOOTBALL_PITCH_ROLE.getRoleId());
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

    @Override
    public GetFootballPitchAdminResponse getFootballPitchAdmin(Long id) {

        Optional<UserEntity> user = userRepository.findByIdAndRole(id,KmsRole.FOOTBALL_PITCH_ROLE.getRole());
        if(user.isEmpty()){
            throw new NotFoundException("Not Found FootBallPitch Admin");
        }

        return GetFootballPitchAdminResponse.builder()
                .setSuccess(true)
                .setFootballPitchAdminDTO(getBuilder(user.get()))
                .build();
    }

    @Override
    public OnlyIdResponse updateFootballPitchAdmin(Long id,UpdateFootballPitchAdminRequest request) {
        UserEntity user = userRepository.findByIdAndRole(id,KmsRole.FOOTBALL_PITCH_ROLE.getRole())
                .orElseThrow(() -> new NotFoundException("Not found footballpitch admin"));
        Map<String, String> errors = new HashMap<>();
        validFormatFieldForUpdate(errors, request);
        validExistFieldFootBallPitch(errors, request.getFootballPitchId());
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        user.setPhoneNumber(request.getPhoneNumber());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setFootballPitch(footballPitchRepository.findById(request.getFootballPitchId()).get());
        user.setModifiedBy(principal.getUserId());
        user.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        AddressEntity address;
        if (user.getAddress() == null && StringUtils.hasText(request.getAddress())) {
            address = new AddressEntity();
            address.setCreatedDate(new Timestamp(System.currentTimeMillis()));
            address.setCreatedBy(principal.getUserId());
        } else {
            address = addressRepository.findById(user.getAddress().getAddressId())
                    .orElseThrow(() -> new NotFoundException("Not found address"));
        }
        address.setAddress(request.getAddress());
        address.setDistrict(RequestUtils.blankIfNull(request.getDistrict()));
        address.setCity(RequestUtils.blankIfNull(request.getCity()));
        address.setModifiedBy(principal.getUserId());
        address.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        user.setAddress(address);

        userRepository.save(user);

        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(user.getUserId())
                .setName(user.getFirstName()+" "+user.getLastName())
                .build();
    }

    @Override
    public NoContentResponse deleteFootballPitchAdmin(Long id) {
        UserEntity user = userRepository.findByIdAndRole(id,KmsRole.FOOTBALL_PITCH_ROLE.getRole())
                .orElseThrow(() -> new NotFoundException("Not found footballpitch admin"));
        userRepository.delete(user);
        addressRepository.delete(user.getAddress());
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    @Override
    public ListFootballPitchAdminResponse getListFootballPitchAdmins(String name) {
        List<UserEntity> listFootballPitchAdmins = userRepository.findByRoleAndName(name,KmsRole.FOOTBALL_PITCH_ROLE.getRole());
        return ListFootballPitchAdminResponse.builder()
                .setSuccess(true)
                .setFootballPitchAdminS(listFootballPitchAdmins.stream()
                        .map(fp -> getBuilder(fp))
                        .collect(Collectors.toList()))
                .build();
    }

    public FootballPitchAdminDTO getBuilder(UserEntity user) {
        return FootballPitchAdminDTO.builder()
                .setUsername(user.getUsername())
                .setFirstName(user.getFirstName())
                .setLastName(user.getLastName())
                .setPhoneNumber(user.getPhoneNumber())
                .setId(user.getUserId())
                .setFootballPitch(FootballPitchDTO.builder()
                        .setFootballPitchId(user.getFootballPitch().getFootballPitchId())
                        .setFootballPitchName(user.getFootballPitch().getFootballPitchName())
                        .setImage(RequestUtils.blankIfNull(user.getFootballPitch().getImage()))
                        .setPhoneNumber(RequestUtils.blankIfNull(user.getFootballPitch().getPhoneNumber()))
                        .setAddress(AddressDTO.builder()
                                .setAddressId(user.getFootballPitch().getAddress() != null ? user.getFootballPitch().getAddress().getAddressId() : -1L)
                                .setAddress(user.getFootballPitch().getAddress() != null ? user.getFootballPitch().getAddress().getAddress() : "")
                                .setDistrict(user.getFootballPitch().getAddress() != null ? user.getFootballPitch().getAddress().getDistrict() : "")
                                .setCity(user.getFootballPitch().getAddress() != null ? user.getFootballPitch().getAddress().getCity() : "")
                                .build())
                        .build())
                .setAddress(AddressDTO.builder()
                        .setAddressId(user.getAddress() != null ? user.getAddress().getAddressId() : -1L)
                        .setAddress(user.getAddress() != null ? user.getAddress().getAddress() : "")
                        .setDistrict(user.getAddress() != null ? user.getAddress().getDistrict() : "")
                        .setCity(user.getAddress() != null ? user.getAddress().getCity() : "")
                        .build())
                .build();
    }

}
