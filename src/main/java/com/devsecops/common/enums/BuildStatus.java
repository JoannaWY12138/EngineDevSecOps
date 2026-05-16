package com.devsecops.common.enums;

/**
 * 构建状态枚举
 */
public enum BuildStatus {
    
    PENDING("待构建"),
    RUNNING("构建中"),
    SUCCESS("构建成功"),
    FAILED("构建失败"),
    CANCELLED("已取消");
    
    private final String description;
    
    BuildStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}