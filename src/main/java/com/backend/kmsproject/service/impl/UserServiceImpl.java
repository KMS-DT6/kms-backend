package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.common.constants.ErrorCode;
import com.backend.kmsproject.common.constants.KmsConstant;
import com.backend.kmsproject.common.enums.KmsRole;
import com.backend.kmsproject.common.exception.NotFoundException;
import com.backend.kmsproject.model.entity.RoleEntity;
import com.backend.kmsproject.model.entity.UserEntity;
import com.backend.kmsproject.repository.jpa.RoleRepository;
import com.backend.kmsproject.repository.jpa.UserRepository;
import com.backend.kmsproject.request.user.RegisterAccountRequest;
import com.backend.kmsproject.response.ErrorResponse;
import com.backend.kmsproject.response.NoContentResponse;
import com.backend.kmsproject.security.CustomUser;
import com.backend.kmsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.sql.Timestamp;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundException("Username invalid"));
        Collection<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
        List.of(user.getRole()).forEach(role -> {
            simpleGrantedAuthorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });
        String address = user.getAddress() != null ? user.getAddress().getAddress() : "";
        String district = user.getAddress() != null ? user.getAddress().getDistrict() : "";
        Timestamp createdDate = user.getCreatedDate() != null ? user.getCreatedDate() : new Timestamp(System.currentTimeMillis());
        Timestamp modifiedDate = user.getModifiedDate() != null ? user.getModifiedDate() : new Timestamp(System.currentTimeMillis());
        Long footballPitchId = user.getFootballPitch() != null ? user.getFootballPitch().getFootballPitchId() : -1L;
        return new CustomUser(user.getUsername(), user.getPassword(), simpleGrantedAuthorities, user.getUserId(), user.getFirstName(),
                user.getLastName(), user.getRole().getRoleName(), address, district, footballPitchId, createdDate, modifiedDate);
    }

    @Override
    public NoContentResponse registerAccount(RegisterAccountRequest request) {
        Map<String, String> errors = new HashMap<>();
        validFormatField(errors, request);
        validExistField(errors, request);
        if (!errors.isEmpty()) {
            return NoContentResponse.builder()
                    .setSuccess(false)
                    .setErrorResponse(ErrorResponse.builder()
                            .setErrors(errors)
                            .build())
                    .build();
        }
        UserEntity user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        RoleEntity role = roleRepository.findById(KmsRole.CUSTOMER_ROLE.getRoleId()).get();
        user.setRole(role);
        user.setCreatedDate(new Timestamp(System.currentTimeMillis()));
        userRepository.save(user);
        return NoContentResponse.builder()
                .setSuccess(true)
                .build();
    }

    private void validFormatField(Map<String, String> errors, RegisterAccountRequest request) {
        if (!StringUtils.hasText(request.getUsername())) {
            errors.put("username", ErrorCode.MISSING_VALUE.name());
        } else if (request.getUsername().length() > KmsConstant.USERNAME_MAX_SIZE) {
            errors.put("username", ErrorCode.TOO_LONG.name());
        } else if (request.getUsername().length() < KmsConstant.USERNAME_MIN_SIZE) {
            errors.put("username", ErrorCode.TOO_SHORT.name());
        }
        if (!StringUtils.hasText(request.getPassword())) {
            errors.put("password", ErrorCode.MISSING_VALUE.name());
        } else if (request.getUsername().length() > KmsConstant.PASSWORD_MAX_SIZE) {
            errors.put("password", ErrorCode.TOO_LONG.name());
        } else if (request.getUsername().length() < KmsConstant.PASSWORD_MIN_SIZE) {
            errors.put("password", ErrorCode.TOO_SHORT.name());
        }
        if (!StringUtils.hasText(request.getConfirmPassword())) {
            errors.put("confirmPassword", ErrorCode.MISSING_VALUE.name());
        } else if (StringUtils.hasText("password") && !request.getPassword().equals(request.getConfirmPassword())) {
            errors.put("confirmPassword", ErrorCode.INVALID_VALUE.name());
        }
        if (!StringUtils.hasText(request.getFirstName())) {
            errors.put("firstName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getLastName())) {
            errors.put("lastName", ErrorCode.MISSING_VALUE.name());
        }
        if (!StringUtils.hasText(request.getPhoneNumber())) {
            errors.put("phoneNumber", ErrorCode.MISSING_VALUE.name());
        }
    }

    private void validExistField(Map<String, String> errors, RegisterAccountRequest request) {
        if (!errors.containsKey("username")) {
            Optional<UserEntity> user = userRepository.findByUsername(request.getUsername());
            if (user.isPresent()) {
                errors.put("username", ErrorCode.ALREADY_EXIST.name());
            }
        }
        if (StringUtils.hasText(request.getPhoneNumber())) {
            Optional<UserEntity> user = userRepository.findByPhoneNumber(request.getPhoneNumber());
            if (user.isPresent()) {
                errors.put("phoneNumber", ErrorCode.ALREADY_EXIST.name());
            }
        }
    }
}
