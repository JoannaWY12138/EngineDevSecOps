package com.devsecops.common.enums;

/**
 * 用户角色枚举
 */
public enum UserRole {
    
    ADMIN("管理员"),
    DEVELOPER("开发者"),
    TESTER("测试人员"),
    OPERATOR("运维人员"),
    GUEST("访客");
    
    private final String description;
    
    UserRole(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}