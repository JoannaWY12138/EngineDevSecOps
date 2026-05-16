package com.devsecops.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 更新用户请求
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "更新用户请求")
public class UpdateUserRequest {
    
    @Schema(description = "邮箱")
    private String email;
    
    @Schema(description = "真实姓名")
    private String realName;
    
    @Schema(description = "手机号")
    private String phone;
    
    @Schema(description = "用户角色")
    private String role;
    
    @Schema(description = "是否启用")
    private Boolean enabled;
}