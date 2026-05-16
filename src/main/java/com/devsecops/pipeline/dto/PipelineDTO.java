package com.devsecops.pipeline.dto;

import com.devsecops.common.enums.PipelineStatus;
import com.devsecops.common.enums.StageType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 流水线DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "流水线信息")
public class PipelineDTO {
    
    @Schema(description = "流水线ID")
    private Long id;
    
    @NotNull(message = "项目ID不能为空")
    @Schema(description = "项目ID")
    private Long projectId;
    
    @Schema(description = "流水线名称")
    private String name;
    
    @Schema(description = "描述")
    private String description;
    
    @Schema(description = "状态")
    private PipelineStatus status;
    
    @Schema(description = "触发用户ID")
    private Long triggerUserId;
    
    @Schema(description = "分支名称")
    private String branchName;
    
    @Schema(description = "提交ID")
    private String commitId;
    
    @Schema(description = "流水线编号")
    private Integer pipelineNumber;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "持续时间(秒)")
    private Integer durationSeconds;
    
    @Schema(description = "配置")
    private String config;
    
    @Schema(description = "错误信息")
    private String errorMessage;
    
    @Schema(description = "是否活跃")
    private Boolean isActive;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}