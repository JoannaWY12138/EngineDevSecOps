package com.devsecops.project.controller;

import com.devsecops.common.dto.ApiResponse;
import com.devsecops.project.dto.CreateProjectRequest;
import com.devsecops.project.dto.ProjectDTO;
import com.devsecops.project.dto.UpdateProjectRequest;
import com.devsecops.project.service.ProjectService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 项目管理控制器
 */
@Tag(name = "项目管理", description = "项目的增删改查接口")
@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {
    
    private final ProjectService projectService;
    
    @Operation(summary = "创建项目")
    @PostMapping
    public ApiResponse<ProjectDTO> createProject(@Valid @RequestBody CreateProjectRequest request) {
        ProjectDTO project = projectService.createProject(request);
        return ApiResponse.success("创建项目成功", project);
    }
    
    @Operation(summary = "更新项目")
    @PutMapping("/{id}")
    public ApiResponse<ProjectDTO> updateProject(
            @PathVariable Long id,
            @RequestBody UpdateProjectRequest request) {
        ProjectDTO project = projectService.updateProject(id, request);
        return ApiResponse.success("更新项目成功", project);
    }
    
    @Operation(summary = "归档项目")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> archiveProject(@PathVariable Long id) {
        projectService.archiveProject(id);
        return ApiResponse.success("归档项目成功", null);
    }
    
    @Operation(summary = "恢复项目")
    @PostMapping("/{id}/restore")
    public ApiResponse<Void> restoreProject(@PathVariable Long id) {
        projectService.restoreProject(id);
        return ApiResponse.success("恢复项目成功", null);
    }
    
    @Operation(summary = "获取项目详情")
    @GetMapping("/{id}")
    public ApiResponse<ProjectDTO> getProjectById(@PathVariable Long id) {
        ProjectDTO project = projectService.getProjectById(id);
        return ApiResponse.success(project);
    }
    
    @Operation(summary = "获取所有活跃项目")
    @GetMapping("/active")
    public ApiResponse<List<ProjectDTO>> getAllActiveProjects() {
        List<ProjectDTO> projects = projectService.getAllActiveProjects();
        return ApiResponse.success(projects);
    }
    
    @Operation(summary = "分页查询项目")
    @GetMapping("/page")
    public ApiResponse<Page<ProjectDTO>> getProjectsPage(
            @PageableDefault Pageable pageable) {
        Page<ProjectDTO> projects = projectService.getProjects(pageable);
        return ApiResponse.success(projects);
    }
    
    @Operation(summary = "根据负责人查询项目")
    @GetMapping("/owner/{ownerId}")
    public ApiResponse<List<ProjectDTO>> getProjectsByOwner(
            @PathVariable Long ownerId) {
        List<ProjectDTO> projects = projectService.getProjectsByOwner(ownerId);
        return ApiResponse.success(projects);
    }
}