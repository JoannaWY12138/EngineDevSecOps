package com.devsecops.common.enums;

/**
 * 项目状态枚举
 */
public enum ProjectStatus {
    
    ACTIVE("活跃"),
    ARCHIVED("已归档"),
    DELETED("已删除");
    
    private final String description;
    
    ProjectStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}