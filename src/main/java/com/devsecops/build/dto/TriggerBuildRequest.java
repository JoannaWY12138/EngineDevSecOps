package com.devsecops.build.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 触发构建请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "触发构建请求")
public class TriggerBuildRequest {
    
    @NotNull(message = "项目ID不能为空")
    @Schema(description = "项目ID")
    private Long projectId;
    
    @NotNull(message = "分支ID不能为空")
    @Schema(description = "分支ID")
    private Long branchId;
    
    @Schema(description = "提交ID")
    private String commitId;
    
    @NotNull(message = "触发用户ID不能为空")
    @Schema(description = "触发用户ID")
    private Long triggerUserId;
    
    @Schema(description = "构建工具")
    private String buildTool;
}