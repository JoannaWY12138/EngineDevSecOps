package com.devsecops.common.enums;

/**
 * 流水线状态枚举
 */
public enum PipelineStatus {
    
    PENDING("待执行"),
    RUNNING("执行中"),
    SUCCESS("执行成功"),
    FAILED("执行失败"),
    CANCELLED("已取消");
    
    private final String description;
    
    PipelineStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}