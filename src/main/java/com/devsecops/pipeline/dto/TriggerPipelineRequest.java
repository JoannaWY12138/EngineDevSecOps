package com.devsecops.pipeline.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 触发流水线请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "触发流水线请求")
public class TriggerPipelineRequest {
    
    @NotNull(message = "项目ID不能为空")
    @Schema(description = "项目ID")
    private Long projectId;
    
    @NotNull(message = "触发用户ID不能为空")
    @Schema(description = "触发用户ID")
    private Long triggerUserId;
    
    @Schema(description = "分支名称")
    private String branchName;
    
    @Schema(description = "提交ID")
    private String commitId;
    
    @Schema(description = "流水线配置")
    private String config;
}