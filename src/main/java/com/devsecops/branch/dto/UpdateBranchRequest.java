package com.devsecops.branch.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新分支请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "更新分支请求")
public class UpdateBranchRequest {
    
    @Schema(description = "分支描述")
    private String description;
    
    @Schema(description = "最新提交ID")
    private String commitId;
    
    @Schema(description = "是否受保护")
    private Boolean isProtected;
}