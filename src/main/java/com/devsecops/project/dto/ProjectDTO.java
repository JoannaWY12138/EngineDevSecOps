package com.devsecops.project.dto;

import com.devsecops.common.enums.ProjectStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 项目DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "项目信息")
public class ProjectDTO {
    
    @Schema(description = "项目ID")
    private Long id;
    
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 100, message = "项目名称长度不能超过100")
    @Schema(description = "项目名称")
    private String name;
    
    @Schema(description = "项目描述")
    private String description;
    
    @NotBlank(message = "Git URL不能为空")
    @Schema(description = "Git仓库地址")
    private String gitUrl;
    
    @Schema(description = "默认分支")
    private String defaultBranch;
    
    @Schema(description = "项目状态")
    private ProjectStatus status;
    
    @Schema(description = "项目负责人ID")
    private Long ownerId;
    
    @Schema(description = "构建工具")
    private String buildTool;
    
    @Schema(description = "编程语言")
    private String language;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}