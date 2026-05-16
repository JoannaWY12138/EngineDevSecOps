package com.devsecops.project.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建项目请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "创建项目请求")
public class CreateProjectRequest {
    
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 100, message = "项目名称长度不能超过100")
    @Schema(description = "项目名称")
    private String name;
    
    @Schema(description = "项目描述")
    private String description;
    
    @NotBlank(message = "Git URL不能为空")
    @Schema(description = "Git仓库地址")
    private String gitUrl;
    
    @Schema(description = "默认分支，默认为main")
    private String defaultBranch = "main";
    
    @Schema(description = "项目负责人ID")
    private Long ownerId;
    
    @Schema(description = "构建工具，如Maven、Gradle")
    private String buildTool;
    
    @Schema(description = "编程语言，如Java、Go")
    private String language;
}