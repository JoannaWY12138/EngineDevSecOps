package com.devsecops.deploy.controller;

import com.devsecops.common.dto.ApiResponse;
import com.devsecops.deploy.dto.DeployDTO;
import com.devsecops.deploy.dto.TriggerDeployRequest;
import com.devsecops.deploy.service.DeployService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 发布管理控制器
 */
@Tag(name = "发布管理", description = "发布的触发、执行和回滚接口")
@RestController
@RequestMapping("/deploys")
@RequiredArgsConstructor
public class DeployController {
    
    private final DeployService deployService;
    
    @Operation(summary = "触发发布")
    @PostMapping("/trigger")
    public ApiResponse<DeployDTO> triggerDeploy(@Valid @RequestBody TriggerDeployRequest request) {
        DeployDTO deploy = deployService.triggerDeploy(request);
        return ApiResponse.success("触发发布成功", deploy);
    }
    
    @Operation(summary = "开始发布")
    @PostMapping("/{id}/start")
    public ApiResponse<DeployDTO> startDeploy(@PathVariable Long id) {
        DeployDTO deploy = deployService.startDeploy(id);
        return ApiResponse.success("开始发布成功", deploy);
    }
    
    @Operation(summary = "完成发布")
    @PostMapping("/{id}/complete")
    public ApiResponse<DeployDTO> completeDeploy(@PathVariable Long id) {
        DeployDTO deploy = deployService.completeDeploy(id);
        return ApiResponse.success("发布完成", deploy);
    }
    
    @Operation(summary = "发布失败")
    @PostMapping("/{id}/fail")
    public ApiResponse<DeployDTO> failDeploy(
            @PathVariable Long id,
            @RequestParam String errorMessage) {
        DeployDTO deploy = deployService.failDeploy(id, errorMessage);
        return ApiResponse.success("发布失败已记录", deploy);
    }
    
    @Operation(summary = "回滚发布")
    @PostMapping("/{id}/rollback")
    public ApiResponse<DeployDTO> rollbackDeploy(@PathVariable Long id) {
        DeployDTO deploy = deployService.rollbackDeploy(id);
        return ApiResponse.success("回滚发布成功", deploy);
    }
    
    @Operation(summary = "取消发布")
    @PostMapping("/{id}/cancel")
    public ApiResponse<DeployDTO> cancelDeploy(@PathVariable Long id) {
        DeployDTO deploy = deployService.cancelDeploy(id);
        return ApiResponse.success("取消发布成功", deploy);
    }
    
    @Operation(summary = "获取发布详情")
    @GetMapping("/{id}")
    public ApiResponse<DeployDTO> getDeployById(@PathVariable Long id) {
        DeployDTO deploy = deployService.getDeployById(id);
        return ApiResponse.success(deploy);
    }
    
    @Operation(summary = "获取项目的发布列表")
    @GetMapping("/project/{projectId}")
    public ApiResponse<List<DeployDTO>> getDeploysByProject(@PathVariable Long projectId) {
        List<DeployDTO> deploys = deployService.getDeploysByProject(projectId);
        return ApiResponse.success(deploys);
    }
    
    @Operation(summary = "获取项目环境的发布列表")
    @GetMapping("/project/{projectId}/environment/{environment}")
    public ApiResponse<List<DeployDTO>> getDeploysByProjectAndEnvironment(
            @PathVariable Long projectId,
            @PathVariable String environment) {
        List<DeployDTO> deploys = deployService.getDeploysByProjectAndEnvironment(projectId, environment);
        return ApiResponse.success(deploys);
    }
    
    @Operation(summary = "获取正在发布的列表")
    @GetMapping("/running")
    public ApiResponse<List<DeployDTO>> getRunningDeploys() {
        List<DeployDTO> deploys = deployService.getRunningDeploys();
        return ApiResponse.success(deploys);
    }
    
    @Operation(summary = "更新发布日志")
    @PutMapping("/{id}/log")
    public ApiResponse<Void> updateDeployLog(
            @PathVariable Long id,
            @RequestBody String log) {
        deployService.updateDeployLog(id, log);
        return ApiResponse.success("更新发布日志成功", null);
    }
}