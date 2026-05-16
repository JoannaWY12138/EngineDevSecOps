package com.devsecops.common.enums;

/**
 * 发布状态枚举
 */
public enum DeployStatus {
    
    PENDING("待发布"),
    DEPLOYING("发布中"),
    SUCCESS("发布成功"),
    FAILED("发布失败"),
    ROLLBACK("已回滚"),
    CANCELLED("已取消");
    
    private final String description;
    
    DeployStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}