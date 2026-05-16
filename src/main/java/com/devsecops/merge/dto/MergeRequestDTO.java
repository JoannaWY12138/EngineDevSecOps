package com.devsecops.merge.dto;

import com.devsecops.common.enums.MergeStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 合并请求DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "合并请求信息")
public class MergeRequestDTO {
    
    @Schema(description = "合并请求ID")
    private Long id;
    
    @NotNull(message = "项目ID不能为空")
    @Schema(description = "项目ID")
    private Long projectId;
    
    @NotNull(message = "源分支ID不能为空")
    @Schema(description = "源分支ID")
    private Long sourceBranchId;
    
    @Schema(description = "源分支名称")
    private String sourceBranchName;
    
    @NotNull(message = "目标分支ID不能为空")
    @Schema(description = "目标分支ID")
    private Long targetBranchId;
    
    @Schema(description = "目标分支名称")
    private String targetBranchName;
    
    @Schema(description = "合并标题")
    private String title;
    
    @Schema(description = "合并描述")
    private String description;
    
    @Schema(description = "合并状态")
    private MergeStatus status;
    
    @Schema(description = "创建者ID")
    private Long creatorId;
    
    @Schema(description = "审核者ID")
    private Long reviewerId;
    
    @Schema(description = "提交ID列表")
    private String commitIds;
    
    @Schema(description = "冲突文件列表")
    private String conflictFiles;
    
    @Schema(description = "合并时间")
    private LocalDateTime mergeTime;
    
    @Schema(description = "审核时间")
    private LocalDateTime approvedTime;
    
    @Schema(description = "错误信息")
    private String errorMessage;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}