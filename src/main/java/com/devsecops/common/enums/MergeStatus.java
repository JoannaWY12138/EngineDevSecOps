package com.devsecops.common.enums;

/**
 * 合并状态枚举
 */
public enum MergeStatus {
    
    PENDING("待合并"),
    MERGING("合并中"),
    SUCCESS("合并成功"),
    FAILED("合并失败"),
    CONFLICT("存在冲突"),
    CANCELLED("已取消");
    
    private final String description;
    
    MergeStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}