package com.devsecops.deploy.dto;

import com.devsecops.common.enums.DeployStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 发布DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "发布信息")
public class DeployDTO {
    
    @Schema(description = "发布ID")
    private Long id;
    
    @NotNull(message = "项目ID不能为空")
    @Schema(description = "项目ID")
    private Long projectId;
    
    @NotNull(message = "构建ID不能为空")
    @Schema(description = "构建ID")
    private Long buildId;
    
    @Schema(description = "部署环境")
    private String environment;
    
    @Schema(description = "发布状态")
    private DeployStatus status;
    
    @Schema(description = "发布编号")
    private Integer deployNumber;
    
    @Schema(description = "触发用户ID")
    private Long triggerUserId;
    
    @Schema(description = "发布日志")
    private String deployLog;
    
    @Schema(description = "发布时长(秒)")
    private Integer durationSeconds;
    
    @Schema(description = "开始时间")
    private LocalDateTime startTime;
    
    @Schema(description = "结束时间")
    private LocalDateTime endTime;
    
    @Schema(description = "目标服务器")
    private String targetServer;
    
    @Schema(description = "发布路径")
    private String deployPath;
    
    @Schema(description = "版本号")
    private String version;
    
    @Schema(description = "回滚版本")
    private String rollbackVersion;
    
    @Schema(description = "错误信息")
    private String errorMessage;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}