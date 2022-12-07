package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.common.constants.ErrorCode;
import com.backend.kmsproject.common.constants.KmsConstant;
import com.backend.kmsproject.common.enums.KmsRole;
import com.backend.kmsproject.common.exception.NotFoundException;
import com.backend.kmsproject.mapper.UserMapper;
import com.backend.kmsproject.model.entity.AddressEntity;
import com.backend.kmsproject.model.entity.UserEntity;
import com.backend.kmsproject.repository.jpa.AddressRepository;
import com.backend.kmsproject.repository.jpa.UserRepository;
import com.backend.kmsproject.request.myaccount.ChangePasswordRequest;
import com.backend.kmsproject.request.myaccount.UpdateMyAccountRequest;
import com.backend.kmsproject.response.ErrorResponse;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.response.OnlyIdResponse;
import com.backend.kmsproject.response.user.MyAccountResponse;
import com.backend.kmsproject.security.KmsPrincipal;
import com.backend.kmsproject.service.MyAccountService;
import com.backend.kmsproject.util.RequestUtils;
import com.backend.kmsproject.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MyAccountServiceImpl implements MyAccountService {
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public MyAccountResponse getMyAccount() {
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        UserEntity user = userRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new NotFoundException("Not found user"));

        MyAccountResponse.MyAccountResponseBuilder builder = MyAccountResponse.builder();
        builder.setUser(UserMapper.entity2dto(user));
        List<String> authorities = new ArrayList<>();
        authorities.addAll(Arrays.asList(KmsConstant.LOGIN, KmsConstant.LOGOUT, KmsConstant.UPDATE_INFORMATION));

        if (KmsRole.ADMIN_ROLE.getRole().equalsIgnoreCase(user.getRole().getRoleName())) {
            authorities.addAll(Arrays.asList(KmsConstant.MANAGE_SYSTEM_ADMIN, KmsConstant.MANAGE_FOOTBALL_PITCH, KmsConstant.MANAGE_FOOTBALL_PITCH_ADMIN));
        } else if (KmsRole.FOOTBALL_PITCH_ROLE.getRole().equalsIgnoreCase(user.getRole().getRoleName())) {
            authorities.addAll(Arrays.asList(KmsConstant.MANAGE_FOOTBALL_PITCH_ADMIN, KmsConstant.MANAGE_SUB_FOOTBALL_PITCH,
                    KmsConstant.MANAGE_OTHER_SERVICE, KmsConstant.MANAGE_CUSTOMER, KmsConstant.MANAGE_BOOKING));
        } else {
            authorities.addAll(Arrays.asList(KmsConstant.BOOKING));
        }

        builder.setAuthorities(authorities);
        return builder.setSuccess(true).build();
    }

    @Override
    public OnlyIdResponse updateMyAccount(UpdateMyAccountRequest request) {
        Map<String, String> errors = new HashMap<>();
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        validFormatField(errors, request);
        validExistField(errors, principal.getUserId(), request);
        if (!errors.isEmpty()) {
            return OnlyIdResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        UserEntity user = userRepository.findByUserId(principal.getUserId())
                .orElseThrow(() -> new NotFoundException("Not found user"));
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setUsername(request.getUsername());
        user.setPhoneNumber(RequestUtils.blankIfNull(request.getPhoneNumber()));
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
    public NoContentResponse changeMyPassword(ChangePasswordRequest request) {
        Map<String, String> errors = new HashMap<>();
        Map<String, String> mapPassword = new HashMap<>() {{
            put("currentPassword", request.getCurrentPassword());
            put("newPassword", request.getNewPassword());
            put("confirmPassword", request.getConfirmPassword());
        }};
        mapPassword.keySet().forEach(p -> {
            validFormatPassword(errors, p, mapPassword.get(p));
        });
        KmsPrincipal principal = SecurityUtils.getPrincipal();
        UserEntity user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new NotFoundException("Not found user"));
        validChangePassword(errors, request, user.getPassword());
        if (!errors.isEmpty()) {
            return NoContentResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    public void validFormatPassword(Map<String, String> errors, String keyPassword, String valuePassword) {
        if (!StringUtils.hasText(valuePassword)) {
            errors.put(keyPassword, ErrorCode.MISSING_VALUE.name());
        } else if (valuePassword.length() < KmsConstant.PASSWORD_MIN_SIZE) {
            errors.put(keyPassword, ErrorCode.TOO_SHORT.name());
        } else if (valuePassword.length() > KmsConstant.PASSWORD_MAX_SIZE){
            errors.put(keyPassword, ErrorCode.TOO_LONG.name());
        }
    }

    public void validChangePassword(Map<String, String> errors, ChangePasswordRequest request, String myCurrentPassword) {
        if (!errors.containsKey("currentPassword") && !passwordEncoder.matches(request.getCurrentPassword(), myCurrentPassword)) {
            errors.put("currentPassword", ErrorCode.NOT_FOUND.name());
        }
        if (!errors.containsKey("newPassword") && !errors.containsKey("confirmPassword")
                && !request.getConfirmPassword().equals(request.getNewPassword())) {
            errors.put("confirmPassword", ErrorCode.INVALID_VALUE.name());
        }
    }

    public void validFormatField(Map<String, String> errors, UpdateMyAccountRequest request) {
        if (!StringUtils.hasText(request.getFirstName())) {
            errors.put("firstName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getLastName())) {
            errors.put("lastName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getUsername())) {
            errors.put("username", ErrorCode.MISSING_VALUE.name());
        } else if (request.getUsername().length() > KmsConstant.USERNAME_MAX_SIZE) {
            errors.put("username", ErrorCode.TOO_LONG.name());
        } else if (request.getUsername().length() < KmsConstant.USERNAME_MIN_SIZE) {
            errors.put("username", ErrorCode.TOO_SHORT.name());
        }
    }

    public void validExistField(Map<String, String> errors, Long userId, UpdateMyAccountRequest request) {
        if (!errors.containsKey("username")) {
            Optional<UserEntity> user = userRepository.findByUsername(request.getUsername());
            if (user.isPresent() && !user.get().getUserId().equals(userId)) {
                errors.put("username", ErrorCode.ALREADY_EXIST.name());
            }
        }
        if (StringUtils.hasText(request.getPhoneNumber())) {
            Optional<UserEntity> user = userRepository.findByPhoneNumber(request.getPhoneNumber());
            if (user.isPresent() && !user.get().getUserId().equals(userId)) {
                errors.put("phoneNumber", ErrorCode.ALREADY_EXIST.name());
            }
        }
    }

}
