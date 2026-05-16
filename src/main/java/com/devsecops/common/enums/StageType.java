package com.devsecops.common.enums;

/**
 * 流水线阶段类型枚举
 */
public enum StageType {
    
    CODE_CHECKOUT("代码检出"),
    CODE_SCAN("代码扫描"),
    UNIT_TEST("单元测试"),
    BUILD("构建"),
    DOCKER_BUILD("Docker构建"),
    DEPLOY("部署"),
    NOTIFICATION("通知");
    
    private final String description;
    
    StageType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}