package com.backend.kmsproject.mapper;

import com.backend.kmsproject.common.constants.KmsConstant;
import com.backend.kmsproject.model.dto.UserDTO;
import com.backend.kmsproject.model.entity.UserEntity;

public class UserMapper {
    public static UserDTO entity2dto(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setUserId(entity.getUserId());
        dto.setUsername(entity.getUsername());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setPhoneNumber(entity.getPhoneNumber());
        dto.setPassword(KmsConstant.PROTECTED);
        dto.setRole(UserDTO.Role.builder()
                .setRoleId(entity.getRole().getRoleId())
                .setRoleName(entity.getRole().getRoleName())
                .build());
        if (entity.getAddress() != null) {
            dto.setAddress(AddressMapper.entity2dto(entity.getAddress()));
        }
        if (entity.getFootballPitch() != null) {
            dto.setFootballPitch(UserDTO.FootballPitch.builder()
                    .setFootballPitchId(entity.getFootballPitch().getFootballPitchId())
                    .setFootballPitchName(entity.getFootballPitch().getFootballPitchName())
                    .build());
        }
        return dto;
    }

}
