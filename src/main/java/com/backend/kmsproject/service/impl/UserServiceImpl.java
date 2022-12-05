package com.backend.kmsproject.service.impl;

import com.backend.kmsproject.common.exception.NotFoundException;
import com.backend.kmsproject.model.entity.UserEntity;
import com.backend.kmsproject.repository.jpa.UserRepository;
import com.backend.kmsproject.security.CustomUser;
import com.backend.kmsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {
    private final UserRepository userRepository;

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
}
