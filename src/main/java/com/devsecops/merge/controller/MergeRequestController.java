package com.devsecops.merge.controller;

import com.devsecops.common.dto.ApiResponse;
import com.devsecops.merge.dto.CreateMergeRequest;
import com.devsecops.merge.dto.MergeRequestDTO;
import com.devsecops.merge.service.MergeRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 合并请求管理控制器
 */
@Tag(name = "代码合并", description = "代码合并请求的创建、审核和执行接口")
@RestController
@RequestMapping("/merge-requests")
@RequiredArgsConstructor
public class MergeRequestController {
    
    private final MergeRequestService mergeRequestService;
    
    @Operation(summary = "创建合并请求")
    @PostMapping
    public ApiResponse<MergeRequestDTO> createMergeRequest(@Valid @RequestBody CreateMergeRequest request) {
        MergeRequestDTO mergeRequest = mergeRequestService.createMergeRequest(request);
        return ApiResponse.success("创建合并请求成功", mergeRequest);
    }
    
    @Operation(summary = "审核合并请求")
    @PostMapping("/{id}/approve")
    public ApiResponse<MergeRequestDTO> approveMergeRequest(
            @PathVariable Long id,
            @RequestParam Long reviewerId) {
        MergeRequestDTO mergeRequest = mergeRequestService.approveMergeRequest(id, reviewerId);
        return ApiResponse.success("审核成功", mergeRequest);
    }
    
    @Operation(summary = "执行合并")
    @PostMapping("/{id}/merge")
    public ApiResponse<MergeRequestDTO> executeMerge(@PathVariable Long id) {
        MergeRequestDTO mergeRequest = mergeRequestService.executeMerge(id);
        return ApiResponse.success("合并成功", mergeRequest);
    }
    
    @Operation(summary = "合并失败")
    @PostMapping("/{id}/fail")
    public ApiResponse<MergeRequestDTO> failMerge(
            @PathVariable Long id,
            @RequestParam String errorMessage,
            @RequestParam(required = false) String conflictFiles) {
        MergeRequestDTO mergeRequest = mergeRequestService.failMerge(id, errorMessage, conflictFiles);
        return ApiResponse.success("合并失败已记录", mergeRequest);
    }
    
    @Operation(summary = "取消合并请求")
    @DeleteMapping("/{id}")
    public ApiResponse<MergeRequestDTO> cancelMergeRequest(@PathVariable Long id) {
        MergeRequestDTO mergeRequest = mergeRequestService.cancelMergeRequest(id);
        return ApiResponse.success("取消合并请求成功", mergeRequest);
    }
    
    @Operation(summary = "获取合并请求详情")
    @GetMapping("/{id}")
    public ApiResponse<MergeRequestDTO> getMergeRequestById(@PathVariable Long id) {
        MergeRequestDTO mergeRequest = mergeRequestService.getMergeRequestById(id);
        return ApiResponse.success(mergeRequest);
    }
    
    @Operation(summary = "获取项目的合并请求列表")
    @GetMapping("/project/{projectId}")
    public ApiResponse<List<MergeRequestDTO>> getMergeRequestsByProject(@PathVariable Long projectId) {
        List<MergeRequestDTO> mergeRequests = mergeRequestService.getMergeRequestsByProject(projectId);
        return ApiResponse.success(mergeRequests);
    }
    
    @Operation(summary = "获取待审核的合并请求列表")
    @GetMapping("/pending")
    public ApiResponse<List<MergeRequestDTO>> getPendingMergeRequests() {
        List<MergeRequestDTO> mergeRequests = mergeRequestService.getPendingMergeRequests();
        return ApiResponse.success(mergeRequests);
    }
    
    @Operation(summary = "获取用户创建的合并请求列表")
    @GetMapping("/creator/{creatorId}")
    public ApiResponse<List<MergeRequestDTO>> getMergeRequestsByCreator(@PathVariable Long creatorId) {
        List<MergeRequestDTO> mergeRequests = mergeRequestService.getMergeRequestsByCreator(creatorId);
        return ApiResponse.success(mergeRequests);
    }
}