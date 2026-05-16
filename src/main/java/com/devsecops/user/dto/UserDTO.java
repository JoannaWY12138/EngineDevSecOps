package com.devsecops.user.dto;

import com.devsecops.common.enums.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 用户DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "用户信息")
public class UserDTO {
    
    @Schema(description = "用户ID")
    private Long id;
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度需在3-50之间")
    @Schema(description = "用户名")
    private String username;
    
    @Schema(description = "邮箱")
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @Schema(description = "真实姓名")
    private String realName;
    
    @Schema(description = "手机号")
    private String phone;
    
    @NotNull(message = "角色不能为空")
    @Schema(description = "用户角色")
    private UserRole role;
    
    @Schema(description = "是否启用")
    private Boolean enabled;
    
    @Schema(description = "创建时间")
    private LocalDateTime createdAt;
    
    @Schema(description = "更新时间")
    private LocalDateTime updatedAt;
}