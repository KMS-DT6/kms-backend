package com.backend.kmsproject.util;

import org.springframework.util.StringUtils;

public class RequestUtils {
    public static String blankIfNull(String str) {
        return StringUtils.hasText(str) ? str : "";
    }

    public static String defaultIfNull(String str, String df) {
        return StringUtils.hasText(str) ? str : df;
    }

    public static Long defaultIfNull(Long id, Long df) {
        return id == null ? df : id;
    }
}
