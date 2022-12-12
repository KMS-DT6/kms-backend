package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.common.constants.ErrorCode;
import com.backend.kmsproject.common.constants.KmsConstant;
import com.backend.kmsproject.common.enums.KmsRole;
import com.backend.kmsproject.common.exception.NotFoundException;
import com.backend.kmsproject.model.dto.AddressDTO;
import com.backend.kmsproject.model.dto.FootballPitchAdminDTO;
import com.backend.kmsproject.model.dto.FootballPitchDTO;
import com.backend.kmsproject.model.entity.AddressEntity;
import com.backend.kmsproject.model.entity.RoleEntity;
import com.backend.kmsproject.model.entity.UserEntity;
import com.backend.kmsproject.repository.dsl.UserDslRepository;
import com.backend.kmsproject.repository.jpa.AddressRepository;
import com.backend.kmsproject.repository.jpa.FootballPitchRepository;
import com.backend.kmsproject.repository.jpa.RoleRepository;
import com.backend.kmsproject.repository.jpa.UserRepository;
import com.backend.kmsproject.request.footballpitchadmin.CreateUpdateFootballPitchAdminRequest;
import com.backend.kmsproject.request.footballpitchadmin.GetListFootballPitchAdminRequest;
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
    private final UserDslRepository userDslRepository;

    public void validFormatField(Map<String, String> errors, CreateUpdateFootballPitchAdminRequest request) {
        if (!StringUtils.hasText(request.getUsername())) {
            errors.put("username", ErrorCode.MISSING_VALUE.name());
        } else if (request.getUsername().length() > KmsConstant.USERNAME_MAX_SIZE) {
            errors.put("username", ErrorCode.TOO_LONG.name());
        } else if (request.getUsername().length() < KmsConstant.USERNAME_MIN_SIZE) {
            errors.put("username", ErrorCode.TOO_SHORT.name());
        }
        if (!StringUtils.hasText(request.getPhoneNumber())) {
            errors.put("phone", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getPassword())) {
            errors.put("password", ErrorCode.MISSING_VALUE.name());
        } else if (request.getPassword().length() > KmsConstant.PASSWORD_MAX_SIZE) {
            errors.put("password", ErrorCode.TOO_LONG.name());
        } else if (request.getUsername().length() < KmsConstant.PASSWORD_MIN_SIZE) {
            errors.put("password", ErrorCode.TOO_SHORT.name());
        }
        if (!StringUtils.hasText(request.getFirstName())) {
            errors.put("firstName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getLastName())) {
            errors.put("lastName", ErrorCode.MISSING_VALUE.name());
        }
        if (request.getFootballPitchId() == null) {
            errors.put("footballPitchId", ErrorCode.MISSING_VALUE.name());
        } else if (request.getFootballPitchId() < 0) {
            errors.put("footballPitchId", ErrorCode.INVALID_VALUE.name());
        }
    }

    public void validExistFieldUserName(Map<String, String> errors, String username, Long footballPitchAdminId) {
        if (StringUtils.hasText(username)) {
            Optional<UserEntity> footballPitchAdmin = userRepository.findByUsername(username);
            if (footballPitchAdmin.isPresent() && !footballPitchAdmin.get().getUserId().equals(footballPitchAdminId)) {
                errors.put("username", ErrorCode.ALREADY_EXIST.name());
            }
        }
    }

    @Override
    public OnlyIdResponse createFootballPitchAdmin(CreateUpdateFootballPitchAdminRequest request) {
        Map<String, String> errors = new HashMap<>();
        validFormatField(errors, request);
        validExistFieldUserName(errors, request.getUsername(), null);
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
        Long footballPitchId = principal.isFootballPitchAdmin() ? principal.getFootballPitchId() : request.getFootballPitchId();
        user.setFootballPitch(footballPitchRepository.findById(footballPitchId).get());
        user.setCreatedBy(principal.getUserId());
        user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        Optional<RoleEntity> role = roleRepository.findById(KmsRole.FOOTBALL_PITCH_ROLE.getRoleId());
        if (!role.isEmpty()) {
            user.setRole(role.get());
        }
        addressRepository.save(address);
        user.setAddress(address);
        userRepository.save(user);

        return OnlyIdResponse.builder()
                .setSuccess(true)
                .setId(user.getUserId())
                .setName(user.getFirstName() + " " + user.getLastName())
                .build();
    }

    @Override
    public GetFootballPitchAdminResponse getFootballPitchAdmin(Long id) {
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        Optional<UserEntity> user = userRepository.findByIdAndRole(id, KmsRole.FOOTBALL_PITCH_ROLE.getRole());
        if (user.isEmpty()) {
            throw new NotFoundException("Not Found FootBallPitch Admin");
        }

        return GetFootballPitchAdminResponse.builder()
                .setSuccess(true)
                .setFootballPitchAdminDTO(getBuilder(user.get()))
                .build();
    }

    @Override
    public OnlyIdResponse updateFootballPitchAdmin(Long id, CreateUpdateFootballPitchAdminRequest request) {
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        UserEntity user = userRepository.findByIdAndRole(id, KmsRole.FOOTBALL_PITCH_ROLE.getRole())
                .orElseThrow(() -> new NotFoundException("Not found football pitch admin"));
        Map<String, String> errors = new HashMap<>();
        validFormatField(errors, request);
        validExistFieldUserName(errors, request.getUsername(), user.getUserId());
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        user.setPhoneNumber(request.getPhoneNumber());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        Long footballPitchId = principal.isFootballPitchAdmin() ? principal.getFootballPitchId() : request.getFootballPitchId();
        user.setFootballPitch(footballPitchRepository.findById(footballPitchId).get());
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
                .setName(user.getFirstName() + " " + user.getLastName())
                .build();
    }

    @Override
    public NoContentResponse deleteFootballPitchAdmin(Long id) {
        UserEntity user = userRepository.findByIdAndRole(id, KmsRole.FOOTBALL_PITCH_ROLE.getRole())
                .orElseThrow(() -> new NotFoundException("Not found football pitch admin"));
        userRepository.delete(user);
        addressRepository.delete(user.getAddress());
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    @Override
    public ListFootballPitchAdminResponse getListFootballPitchAdmin(GetListFootballPitchAdminRequest request) {
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        request.setFootballPitchId(principal.isFootballPitchAdmin() ? principal.getFootballPitchId() : request.getFootballPitchId());
        List<UserEntity> footballPitchAdmins = userDslRepository.listFootballPitchAdmin(request);
        return ListFootballPitchAdminResponse.builder()
                .setSuccess(true)
                .setFootballPitchAdminS(footballPitchAdmins.stream()
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
