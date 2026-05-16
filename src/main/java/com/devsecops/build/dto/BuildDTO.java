package com.devsecops.build.dto;

import com.devsecops.common.enums.BuildStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 构建DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "构建信息")
public class BuildDTO {
    
    @Schema(description = "构建ID")
    private Long id;
    
    @NotNull(message = "项目ID不能为空")
    @Schema(description = "项目ID")
    private Long projectId;
    
    @NotNull(message = "分支ID不能为空")
    @Schema(description = "分支ID")
    private Long branchId;
    
    @Schema(description = "分支名称")
    private String branchName;
    
    @Schema(description = "提交ID")
    private String commitId;
    
    @Schema(description = "构建状态")
    private BuildStatus status;
    
    @Schema(description = "构建编号")
    private Integer buildNumber;
    
    @Schema(description = "触发用户ID")
    private Long triggerUserId;
    
    @Schema(description = "构建日志")
    private String buildLog;
    
    @Schema(description = "构建时长(秒)")
    private Integer durationSeconds;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "构建工具")
    private String buildTool;
    
    @Schema(description = "制品路径")
    private String artifactPath;
    
    @Schema(description = "错误信息")
    private String errorMessage;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}