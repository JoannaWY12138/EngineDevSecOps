package com.devsecops.merge.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建合并请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "创建合并请求")
public class CreateMergeRequest {
    
    @NotNull(message = "项目ID不能为空")
    @Schema(description = "项目ID")
    private Long projectId;
    
    @NotNull(message = "源分支ID不能为空")
    @Schema(description = "源分支ID")
    private Long sourceBranchId;
    
    @NotNull(message = "目标分支ID不能为空")
    @Schema(description = "目标分支ID")
    private Long targetBranchId;
    
    @NotBlank(message = "合并标题不能为空")
    @Schema(description = "合并标题")
    private String title;
    
    @Schema(description = "合并描述")
    private String description;
    
    @NotNull(message = "创建者ID不能为空")
    @Schema(description = "创建者ID")
    private Long creatorId;
    
    @Schema(description = "提交ID列表")
    private String commitIds;
}