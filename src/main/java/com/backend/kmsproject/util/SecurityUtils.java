package com.backend.kmsproject.util;

import com.backend.kmsproject.security.CustomUser;
import com.backend.kmsproject.security.KmsPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static KmsPrincipal getPrincipal() {
        if (SecurityContextHolder.getContext().getAuthentication() != null) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            CustomUser custom = (CustomUser) auth.getPrincipal();
            KmsPrincipal principal = new KmsPrincipal();
            principal.setUserId(custom.getUserId());
            principal.setUsername(custom.getUsername());
            principal.setFirstName(custom.getFirstName());
            principal.setLastName(custom.getLastName());
            principal.setRole(custom.getRole());
            principal.setAddress(custom.getAddress());
            principal.setDistrict(custom.getDistrict());
            principal.setFootballPitchId(custom.getFootballPitchId());
            principal.setCreatedDate(custom.getCreatedDate());
            principal.setModifiedDate(custom.getModifiedDate());
            return principal;
        }
        return null;
    }
}
