package com.backend.kmsproject.mapper;

import com.backend.kmsproject.model.dto.AddressDTO;
import com.backend.kmsproject.model.entity.AddressEntity;

public class AddressMapper {
    public static AddressDTO entity2dto(AddressEntity entity) {
        AddressDTO dto = new AddressDTO();
        dto.setAddressId(entity.getAddressId());
        dto.setAddress(entity.getAddress());
        dto.setDistrict(entity.getDistrict());
        dto.setCity(entity.getCity());
        return dto;
    }
}
