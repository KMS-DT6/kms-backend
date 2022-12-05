package com.backend.kmsproject.common.enums;

public enum KmsRole {
    ADMIN_ROLE(1L, "ADMIN"),
    FOOTBALL_PITCH_ROLE(2L, "FOOTBALL_PITCH"),
    CUSTOMER_ROLE(3L, "CUSTOMER");
    private Long roleId;
    private String role;

    KmsRole(Long roleId, String role) {
        this.roleId = roleId;
        this.role = role;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getRoleId() {
        return this.roleId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return this.role;
    }
}
