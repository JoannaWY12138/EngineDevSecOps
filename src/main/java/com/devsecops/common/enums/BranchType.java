package com.devsecops.common.enums;

/**
 * 分支类型枚举
 */
public enum BranchType {
    
    FEATURE("特性分支"),
    DEVELOP("开发分支"),
    RELEASE("发布分支"),
    HOTFIX("热修复分支"),
    MASTER("主分支");
    
    private final String description;
    
    BranchType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}