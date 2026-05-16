package com.devsecops.deploy.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 触发发布请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "触发发布请求")
public class TriggerDeployRequest {
    
    @NotNull(message = "项目ID不能为空")
    @Schema(description = "项目ID")
    private Long projectId;
    
    @NotNull(message = "构建ID不能为空")
    @Schema(description = "构建ID")
    private Long buildId;
    
    @NotBlank(message = "部署环境不能为空")
    @Schema(description = "部署环境，如dev、test、prod")
    private String environment;
    
    @NotNull(message = "触发用户ID不能为空")
    @Schema(description = "触发用户ID")
    private Long triggerUserId;
    
    @Schema(description = "目标服务器")
    private String targetServer;
    
    @Schema(description = "发布路径")
    private String deployPath;
    
    @Schema(description = "版本号")
    private String version;
}