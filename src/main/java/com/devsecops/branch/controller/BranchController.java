package com.devsecops.branch.controller;

import com.devsecops.common.dto.ApiResponse;
import com.devsecops.common.enums.BranchType;
import com.devsecops.branch.dto.BranchDTO;
import com.devsecops.branch.dto.CreateBranchRequest;
import com.devsecops.branch.dto.UpdateBranchRequest;
import com.devsecops.branch.service.BranchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
 * 分支管理控制器
 */
@Tag(name = "分支管理", description = "分支的增删改查和合并接口")
@RestController
@RequestMapping("/branches")
@RequiredArgsConstructor
public class BranchController {
    
    private final BranchService branchService;
    
    @Operation(summary = "创建分支")
    @PostMapping
    public ApiResponse<BranchDTO> createBranch(@Valid @RequestBody CreateBranchRequest request) {
        BranchDTO branch = branchService.createBranch(request);
        return ApiResponse.success("创建分支成功", branch);
    }
    
    @Operation(summary = "更新分支")
    @PutMapping("/{id}")
    public ApiResponse<BranchDTO> updateBranch(
            @PathVariable Long id,
            @RequestBody UpdateBranchRequest request) {
        BranchDTO branch = branchService.updateBranch(id, request);
        return ApiResponse.success("更新分支成功", branch);
    }
    
    @Operation(summary = "删除分支")
    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ApiResponse.success("删除分支成功", null);
    }
    
    @Operation(summary = "合并分支")
    @PostMapping("/{id}/merge")
    public ApiResponse<Void> mergeBranch(
            @PathVariable Long id,
            @RequestParam String targetBranch) {
        branchService.mergeBranch(id, targetBranch);
        return ApiResponse.success("合并分支成功", null);
    }
    
    @Operation(summary = "获取分支详情")
    @GetMapping("/{id}")
    public ApiResponse<BranchDTO> getBranchById(@PathVariable Long id) {
        BranchDTO branch = branchService.getBranchById(id);
        return ApiResponse.success(branch);
    }
    
    @Operation(summary = "获取项目的所有分支")
    @GetMapping("/project/{projectId}")
    public ApiResponse<List<BranchDTO>> getBranchesByProject(
            @PathVariable Long projectId) {
        List<BranchDTO> branches = branchService.getBranchesByProject(projectId);
        return ApiResponse.success(branches);
    }
    
    @Operation(summary = "根据类型获取项目的分支")
    @GetMapping("/project/{projectId}/type")
    public ApiResponse<List<BranchDTO>> getBranchesByProjectAndType(
            @PathVariable Long projectId,
            @RequestParam BranchType type) {
        List<BranchDTO> branches = branchService.getBranchesByProjectAndType(projectId, type);
        return ApiResponse.success(branches);
    }
    
    @Operation(summary = "获取用户创建的分支")
    @GetMapping("/creator/{creatorId}")
    public ApiResponse<List<BranchDTO>> getBranchesByCreator(
            @PathVariable Long creatorId) {
        List<BranchDTO> branches = branchService.getBranchesByCreator(creatorId);
        return ApiResponse.success(branches);
    }
}