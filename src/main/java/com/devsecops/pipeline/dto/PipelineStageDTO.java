package com.devsecops.pipeline.dto;

import com.devsecops.common.enums.PipelineStatus;
import com.devsecops.common.enums.StageType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 流水线阶段DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "流水线阶段信息")
public class PipelineStageDTO {
    
    @Schema(description = "阶段ID")
    private Long id;
    
    @Schema(description = "流水线ID")
    private Long pipelineId;
    
    @Schema(description = "阶段类型")
    private StageType stageType;
    
    @Schema(description = "阶段名称")
    private String name;
    
    @Schema(description = "执行顺序")
    private Integer orderIndex;
    
    @Schema(description = "状态")
    private PipelineStatus status;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "持续时间(秒)")
    private Integer durationSeconds;
    
    @Schema(description = "日志")
    private String log;
    
    @Schema(description = "错误信息")
    private String errorMessage;
    
    @Schema(description = "配置")
    private String config;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}