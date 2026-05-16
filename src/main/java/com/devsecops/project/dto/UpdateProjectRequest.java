package com.devsecops.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新项目请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "更新项目请求")
public class UpdateProjectRequest {
    
    @Schema(description = "项目描述")
    private String description;
    
    @Schema(description = "Git仓库地址")
    private String gitUrl;
    
    @Schema(description = "默认分支")
    private String defaultBranch;
    
    @Schema(description = "项目负责人ID")
    private Long ownerId;
    
    @Schema(description = "构建工具")
    private String buildTool;
    
    @Schema(description = "编程语言")
    private String language;
    
    @Schema(description = "项目状态")
    private String status;
}