package com.devsecops.build.controller;

import com.devsecops.build.dto.BuildDTO;
import com.devsecops.build.dto.TriggerBuildRequest;
import com.devsecops.build.service.BuildService;
import com.devsecops.common.dto.ApiResponse;
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
 * 构建管理控制器
 */
@Tag(name = "构建管理", description = "构建的触发、执行和管理接口")
@RestController
@RequestMapping("/builds")
@RequiredArgsConstructor
public class BuildController {
    
    private final BuildService buildService;
    
    @Operation(summary = "触发构建")
    @PostMapping("/trigger")
    public ApiResponse<BuildDTO> triggerBuild(@Valid @RequestBody TriggerBuildRequest request) {
        BuildDTO build = buildService.triggerBuild(request);
        return ApiResponse.success("触发构建成功", build);
    }
    
    @Operation(summary = "开始构建")
    @PostMapping("/{id}/start")
    public ApiResponse<BuildDTO> startBuild(@PathVariable Long id) {
        BuildDTO build = buildService.startBuild(id);
        return ApiResponse.success("开始构建成功", build);
    }
    
    @Operation(summary = "完成构建")
    @PostMapping("/{id}/complete")
    public ApiResponse<BuildDTO> completeBuild(
            @PathVariable Long id,
            @RequestParam(required = false) String artifactPath) {
        BuildDTO build = buildService.completeBuild(id, artifactPath);
        return ApiResponse.success("构建完成", build);
    }
    
    @Operation(summary = "构建失败")
    @PostMapping("/{id}/fail")
    public ApiResponse<BuildDTO> failBuild(
            @PathVariable Long id,
            @RequestParam String errorMessage) {
        BuildDTO build = buildService.failBuild(id, errorMessage);
        return ApiResponse.success("构建失败已记录", build);
    }
    
    @Operation(summary = "取消构建")
    @PostMapping("/{id}/cancel")
    public ApiResponse<BuildDTO> cancelBuild(@PathVariable Long id) {
        BuildDTO build = buildService.cancelBuild(id);
        return ApiResponse.success("取消构建成功", build);
    }
    
    @Operation(summary = "获取构建详情")
    @GetMapping("/{id}")
    public ApiResponse<BuildDTO> getBuildById(@PathVariable Long id) {
        BuildDTO build = buildService.getBuildById(id);
        return ApiResponse.success(build);
    }
    
    @Operation(summary = "获取项目的构建列表")
    @GetMapping("/project/{projectId}")
    public ApiResponse<List<BuildDTO>> getBuildsByProject(@PathVariable Long projectId) {
        List<BuildDTO> builds = buildService.getBuildsByProject(projectId);
        return ApiResponse.success(builds);
    }
    
    @Operation(summary = "获取分支的构建列表")
    @GetMapping("/branch/{branchId}")
    public ApiResponse<List<BuildDTO>> getBuildsByBranch(@PathVariable Long branchId) {
        List<BuildDTO> builds = buildService.getBuildsByBranch(branchId);
        return ApiResponse.success(builds);
    }
    
    @Operation(summary = "获取正在运行的构建")
    @GetMapping("/running")
    public ApiResponse<List<BuildDTO>> getRunningBuilds() {
        List<BuildDTO> builds = buildService.getRunningBuilds();
        return ApiResponse.success(builds);
    }
    
    @Operation(summary = "更新构建日志")
    @PutMapping("/{id}/log")
    public ApiResponse<Void> updateBuildLog(
            @PathVariable Long id,
            @RequestBody String log) {
        buildService.updateBuildLog(id, log);
        return ApiResponse.success("更新构建日志成功", null);
    }
}