package com.devsecops.pipeline.controller;

import com.devsecops.common.dto.ApiResponse;
import com.devsecops.pipeline.dto.PipelineDTO;
import com.devsecops.pipeline.dto.PipelineStageDTO;
import com.devsecops.pipeline.dto.TriggerPipelineRequest;
import com.devsecops.pipeline.service.PipelineService;
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
 * 流水线管理控制器
 */
@Tag(name = "流水线管理", description = "流水线的触发、执行和管理接口")
@RestController
@RequestMapping("/pipelines")
@RequiredArgsConstructor
public class PipelineController {
    
    private final PipelineService pipelineService;
    
    @Operation(summary = "触发流水线")
    @PostMapping("/trigger")
    public ApiResponse<PipelineDTO> triggerPipeline(@Valid @RequestBody TriggerPipelineRequest request) {
        PipelineDTO pipeline = pipelineService.triggerPipeline(request);
        return ApiResponse.success("触发流水线成功", pipeline);
    }
    
    @Operation(summary = "开始流水线")
    @PostMapping("/{id}/start")
    public ApiResponse<PipelineDTO> startPipeline(@PathVariable Long id) {
        PipelineDTO pipeline = pipelineService.startPipeline(id);
        return ApiResponse.success("开始流水线成功", pipeline);
    }
    
    @Operation(summary = "完成阶段")
    @PostMapping("/stages/{stageId}/complete")
    public ApiResponse<PipelineStageDTO> completeStage(@PathVariable Long stageId) {
        PipelineStageDTO stage = pipelineService.completeStage(stageId);
        return ApiResponse.success("阶段完成", stage);
    }
    
    @Operation(summary = "阶段失败")
    @PostMapping("/stages/{stageId}/fail")
    public ApiResponse<PipelineStageDTO> failStage(
            @PathVariable Long stageId,
            @RequestParam String errorMessage) {
        PipelineStageDTO stage = pipelineService.failStage(stageId, errorMessage);
        return ApiResponse.success("阶段失败已记录", stage);
    }
    
    @Operation(summary = "取消流水线")
    @DeleteMapping("/{id}")
    public ApiResponse<PipelineDTO> cancelPipeline(@PathVariable Long id) {
        PipelineDTO pipeline = pipelineService.cancelPipeline(id);
        return ApiResponse.success("取消流水线成功", pipeline);
    }
    
    @Operation(summary = "获取流水线详情")
    @GetMapping("/{id}")
    public ApiResponse<PipelineDTO> getPipelineById(@PathVariable Long id) {
        PipelineDTO pipeline = pipelineService.getPipelineById(id);
        return ApiResponse.success(pipeline);
    }
    
    @Operation(summary = "获取流水线的所有阶段")
    @GetMapping("/{id}/stages")
    public ApiResponse<List<PipelineStageDTO>> getPipelineStages(@PathVariable Long id) {
        List<PipelineStageDTO> stages = pipelineService.getPipelineStages(id);
        return ApiResponse.success(stages);
    }
    
    @Operation(summary = "获取项目的流水线列表")
    @GetMapping("/project/{projectId}")
    public ApiResponse<List<PipelineDTO>> getPipelinesByProject(@PathVariable Long projectId) {
        List<PipelineDTO> pipelines = pipelineService.getPipelinesByProject(projectId);
        return ApiResponse.success(pipelines);
    }
    
    @Operation(summary = "获取正在运行的流水线")
    @GetMapping("/running")
    public ApiResponse<List<PipelineDTO>> getRunningPipelines() {
        List<PipelineDTO> pipelines = pipelineService.getRunningPipelines();
        return ApiResponse.success(pipelines);
    }
}