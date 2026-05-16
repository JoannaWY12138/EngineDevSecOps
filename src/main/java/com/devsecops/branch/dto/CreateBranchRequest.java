package com.devsecops.branch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 创建分支请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "创建分支请求")
public class CreateBranchRequest {
    
    @NotBlank(message = "分支名称不能为空")
    @Size(max = 100, message = "分支名称长度不能超过100")
    @Schema(description = "分支名称")
    private String name;
    
    @NotNull(message = "项目ID不能为空")
    @Schema(description = "项目ID")
    private Long projectId;
    
    @NotNull(message = "分支类型不能为空")
    @Schema(description = "分支类型")
    private String type;
    
    @Schema(description = "源分支")
    private String sourceBranch;
    
    @NotNull(message = "创建者ID不能为空")
    @Schema(description = "创建者ID")
    private Long creatorId;
    
    @Schema(description = "分支描述")
    private String description;
    
    @Schema(description = "是否受保护")
    private Boolean isProtected = false;
}