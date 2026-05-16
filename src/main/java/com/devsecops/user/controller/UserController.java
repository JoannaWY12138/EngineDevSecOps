package com.devsecops.user.controller;

import com.devsecops.common.dto.ApiResponse;
import com.devsecops.user.dto.CreateUserRequest;
import com.devsecops.user.dto.UpdateUserRequest;
import com.devsecops.user.dto.UserDTO;
import com.devsecops.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 用户管理控制器
 */
@Tag(name = "用户管理", description = "用户的增删改查接口")
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    @Operation(summary = "创建用户")
    @PostMapping
    public ApiResponse<UserDTO> createUser(@Valid @RequestBody CreateUserRequest request) {
        UserDTO user = userService.createUser(request);
        return ApiResponse.success("创建用户成功", user);
    }
    
    @Operation(summary = "更新用户")
    @PutMapping("/{id}")
    public ApiResponse<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody UpdateUserRequest request) {
        UserDTO user = userService.updateUser(id, request);
        return ApiResponse.success("更新用户成功", user);
    }
    
    @Operation(summary = "删除用户")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ApiResponse.success("删除用户成功", null);
    }
    
    @Operation(summary = "获取用户详情")
    @GetMapping("/{id}")
    public ApiResponse<UserDTO> getUserById(@PathVariable Long id) {
        UserDTO user = userService.getUserById(id);
        return ApiResponse.success(user);
    }
    
    @Operation(summary = "获取所有用户")
    @GetMapping
    public ApiResponse<List<UserDTO>> getAllUsers() {
        List<UserDTO> users = userService.getAllUsers();
        return ApiResponse.success(users);
    }
    
    @Operation(summary = "分页查询用户")
    @GetMapping("/page")
    public ApiResponse<Page<UserDTO>> getUsersPage(
            @PageableDefault Pageable pageable) {
        Page<UserDTO> users = userService.getUsers(pageable);
        return ApiResponse.success(users);
    }
}