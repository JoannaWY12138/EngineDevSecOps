package com.devsecops.pipeline.service;

import com.devsecops.common.enums.PipelineStatus;
import com.devsecops.common.enums.StageType;
import com.devsecops.common.exception.BusinessException;
import com.devsecops.pipeline.dto.PipelineDTO;
import com.devsecops.pipeline.dto.PipelineStageDTO;
import com.devsecops.pipeline.dto.TriggerPipelineRequest;
import com.devsecops.pipeline.entity.Pipeline;
import com.devsecops.pipeline.entity.PipelineStage;
import com.devsecops.pipeline.repository.PipelineRepository;
import com.devsecops.pipeline.repository.PipelineStageRepository;
import com.devsecops.project.entity.Project;
import com.devsecops.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 流水线服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PipelineService {
    
    private final PipelineRepository pipelineRepository;
    private final PipelineStageRepository pipelineStageRepository;
    private final ProjectRepository projectRepository;
    
    /**
     * 触发流水线
     */
    @Transactional
    public PipelineDTO triggerPipeline(TriggerPipelineRequest request) {
        // 验证项目是否存在
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> BusinessException.of("项目不存在"));
        
        // 获取下一个流水线编号
        Integer nextPipelineNumber = pipelineRepository.findMaxPipelineNumberByProjectId(request.getProjectId())
                .map(num -> num + 1)
                .orElse(1);
        
        Pipeline pipeline = new Pipeline();
        pipeline.setProjectId(request.getProjectId());
        pipeline.setName("Pipeline #" + nextPipelineNumber);
        pipeline.setStatus(PipelineStatus.PENDING);
        pipeline.setTriggerUserId(request.getTriggerUserId());
        pipeline.setBranchName(request.getBranchName());
        pipeline.setCommitId(request.getCommitId());
        pipeline.setPipelineNumber(nextPipelineNumber);
        pipeline.setConfig(request.getConfig());
        pipeline.setIsActive(true);
        
        pipeline = pipelineRepository.save(pipeline);
        
        // 创建默认阶段
        createDefaultStages(pipeline.getId());
        
        log.info("触发流水线成功: 项目={}, 流水线编号={}", project.getName(), nextPipelineNumber);
        
        return convertToDTO(pipeline);
    }
    
    /**
     * 创建默认阶段
     */
    private void createDefaultStages(Long pipelineId) {
        List<PipelineStage> stages = new ArrayList<>();
        
        PipelineStage stage1 = new PipelineStage();
        stage1.setPipelineId(pipelineId);
        stage1.setStageType(StageType.CODE_CHECKOUT);
        stage1.setName("代码检出");
        stage1.setOrderIndex(1);
        stage1.setStatus(PipelineStatus.PENDING);
        stages.add(stage1);
        
        PipelineStage stage2 = new PipelineStage();
        stage2.setPipelineId(pipelineId);
        stage2.setStageType(StageType.CODE_SCAN);
        stage2.setName("代码扫描");
        stage2.setOrderIndex(2);
        stage2.setStatus(PipelineStatus.PENDING);
        stages.add(stage2);
        
        PipelineStage stage3 = new PipelineStage();
        stage3.setPipelineId(pipelineId);
        stage3.setStageType(StageType.UNIT_TEST);
        stage3.setName("单元测试");
        stage3.setOrderIndex(3);
        stage3.setStatus(PipelineStatus.PENDING);
        stages.add(stage3);
        
        PipelineStage stage4 = new PipelineStage();
        stage4.setPipelineId(pipelineId);
        stage4.setStageType(StageType.BUILD);
        stage4.setName("构建");
        stage4.setOrderIndex(4);
        stage4.setStatus(PipelineStatus.PENDING);
        stages.add(stage4);
        
        PipelineStage stage5 = new PipelineStage();
        stage5.setPipelineId(pipelineId);
        stage5.setStageType(StageType.DOCKER_BUILD);
        stage5.setName("Docker构建");
        stage5.setOrderIndex(5);
        stage5.setStatus(PipelineStatus.PENDING);
        stages.add(stage5);
        
        PipelineStage stage6 = new PipelineStage();
        stage6.setPipelineId(pipelineId);
        stage6.setStageType(StageType.DEPLOY);
        stage6.setName("部署");
        stage6.setOrderIndex(6);
        stage6.setStatus(PipelineStatus.PENDING);
        stages.add(stage6);
        
        PipelineStage stage7 = new PipelineStage();
        stage7.setPipelineId(pipelineId);
        stage7.setStageType(StageType.NOTIFICATION);
        stage7.setName("通知");
        stage7.setOrderIndex(7);
        stage7.setStatus(PipelineStatus.PENDING);
        stages.add(stage7);
        
        pipelineStageRepository.saveAll(stages);
    }
    
    /**
     * 开始流水线
     */
    @Transactional
    public PipelineDTO startPipeline(Long id) {
        Pipeline pipeline = pipelineRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("流水线不存在"));
        
        if (pipeline.getStatus() != PipelineStatus.PENDING) {
            throw BusinessException.of("只有待执行状态的流水线才能开始");
        }
        
        pipeline.setStatus(PipelineStatus.RUNNING);
        pipeline.setStartTime(LocalDateTime.now());
        pipeline = pipelineRepository.save(pipeline);
        
        // 开始第一个阶段
        PipelineStage firstStage = pipelineStageRepository
                .findFirstByPipelineIdAndStatusOrderByOrderIndexAsc(id, PipelineStatus.PENDING)
                .orElse(null);
        
        if (firstStage != null) {
            firstStage.setStatus(PipelineStatus.RUNNING);
            firstStage.setStartTime(LocalDateTime.now());
            pipelineStageRepository.save(firstStage);
        }
        
        log.info("开始流水线: 流水线编号={}", pipeline.getPipelineNumber());
        return convertToDTO(pipeline);
    }
    
    /**
     * 完成阶段
     */
    @Transactional
    public PipelineStageDTO completeStage(Long stageId) {
        PipelineStage stage = pipelineStageRepository.findById(stageId)
                .orElseThrow(() -> BusinessException.of("阶段不存在"));
        
        if (stage.getStatus() != PipelineStatus.RUNNING) {
            throw BusinessException.of("只有执行中的阶段才能完成");
        }
        
        stage.setStatus(PipelineStatus.SUCCESS);
        stage.setEndTime(LocalDateTime.now());
        
        if (stage.getStartTime() != null && stage.getEndTime() != null) {
            long seconds = java.time.Duration.between(stage.getStartTime(), stage.getEndTime()).getSeconds();
            stage.setDurationSeconds((int) seconds);
        }
        
        stage = pipelineStageRepository.save(stage);
        
        // 检查是否还有下一个阶段
        PipelineStage nextStage = pipelineStageRepository
                .findFirstByPipelineIdAndStatusOrderByOrderIndexAsc(stage.getPipelineId(), PipelineStatus.PENDING)
                .orElse(null);
        
        if (nextStage != null) {
            // 开始下一个阶段
            nextStage.setStatus(PipelineStatus.RUNNING);
            nextStage.setStartTime(LocalDateTime.now());
            pipelineStageRepository.save(nextStage);
        } else {
            // 所有阶段完成，完成流水线
            completePipelineInternal(stage.getPipelineId());
        }
        
        return convertStageToDTO(stage);
    }
    
    /**
     * 内部完成流水线方法
     */
    private void completePipelineInternal(Long pipelineId) {
        Pipeline pipeline = pipelineRepository.findById(pipelineId)
                .orElseThrow(() -> BusinessException.of("流水线不存在"));
        
        pipeline.setStatus(PipelineStatus.SUCCESS);
        pipeline.setEndTime(LocalDateTime.now());
        
        if (pipeline.getStartTime() != null && pipeline.getEndTime() != null) {
            long seconds = java.time.Duration.between(pipeline.getStartTime(), pipeline.getEndTime()).getSeconds();
            pipeline.setDurationSeconds((int) seconds);
        }
        
        pipelineRepository.save(pipeline);
        log.info("流水线完成: 流水线编号={}", pipeline.getPipelineNumber());
    }
    
    /**
     * 阶段失败
     */
    @Transactional
    public PipelineStageDTO failStage(Long stageId, String errorMessage) {
        PipelineStage stage = pipelineStageRepository.findById(stageId)
                .orElseThrow(() -> BusinessException.of("阶段不存在"));
        
        stage.setStatus(PipelineStatus.FAILED);
        stage.setEndTime(LocalDateTime.now());
        stage.setErrorMessage(errorMessage);
        
        if (stage.getStartTime() != null && stage.getEndTime() != null) {
            long seconds = java.time.Duration.between(stage.getStartTime(), stage.getEndTime()).getSeconds();
            stage.setDurationSeconds((int) seconds);
        }
        
        stage = pipelineStageRepository.save(stage);
        
        // 流水线失败
        Pipeline pipeline = pipelineRepository.findById(stage.getPipelineId())
                .orElseThrow(() -> BusinessException.of("流水线不存在"));
        pipeline.setStatus(PipelineStatus.FAILED);
        pipeline.setEndTime(LocalDateTime.now());
        pipeline.setErrorMessage(errorMessage);
        
        if (pipeline.getStartTime() != null && pipeline.getEndTime() != null) {
            long seconds = java.time.Duration.between(pipeline.getStartTime(), pipeline.getEndTime()).getSeconds();
            pipeline.setDurationSeconds((int) seconds);
        }
        
        pipelineRepository.save(pipeline);
        
        log.info("阶段失败: 流水线编号={}, 阶段={}", pipeline.getPipelineNumber(), stage.getName());
        return convertStageToDTO(stage);
    }
    
    /**
     * 取消流水线
     */
    @Transactional
    public PipelineDTO cancelPipeline(Long id) {
        Pipeline pipeline = pipelineRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("流水线不存在"));
        
        if (pipeline.getStatus() == PipelineStatus.SUCCESS || pipeline.getStatus() == PipelineStatus.FAILED) {
            throw BusinessException.of("已完成的流水线不能取消");
        }
        
        pipeline.setStatus(PipelineStatus.CANCELLED);
        pipeline.setEndTime(LocalDateTime.now());
        pipeline = pipelineRepository.save(pipeline);
        
        // 取消所有未完成的阶段
        List<PipelineStage> pendingStages = pipelineStageRepository.findByPipelineIdAndStatus(id, PipelineStatus.PENDING);
        pendingStages.forEach(stage -> stage.setStatus(PipelineStatus.CANCELLED));
        pipelineStageRepository.saveAll(pendingStages);
        
        List<PipelineStage> runningStages = pipelineStageRepository.findByPipelineIdAndStatus(id, PipelineStatus.RUNNING);
        runningStages.forEach(stage -> {
            stage.setStatus(PipelineStatus.CANCELLED);
            stage.setEndTime(LocalDateTime.now());
        });
        pipelineStageRepository.saveAll(runningStages);
        
        log.info("取消流水线: 流水线编号={}", pipeline.getPipelineNumber());
        return convertToDTO(pipeline);
    }
    
    /**
     * 获取流水线详情
     */
    public PipelineDTO getPipelineById(Long id) {
        Pipeline pipeline = pipelineRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("流水线不存在"));
        return convertToDTO(pipeline);
    }
    
    /**
     * 获取流水线的所有阶段
     */
    public List<PipelineStageDTO> getPipelineStages(Long pipelineId) {
        return pipelineStageRepository.findByPipelineIdOrderByOrderIndex(pipelineId).stream()
                .map(this::convertStageToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取项目的流水线列表
     */
    public List<PipelineDTO> getPipelinesByProject(Long projectId) {
        return pipelineRepository.findByProjectIdOrderByCreatedAtDesc(projectId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取正在运行的流水线
     */
    public List<PipelineDTO> getRunningPipelines() {
        return pipelineRepository.findByStatus(PipelineStatus.RUNNING).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 转换为DTO
     */
    private PipelineDTO convertToDTO(Pipeline pipeline) {
        return new PipelineDTO(
                pipeline.getId(),
                pipeline.getProjectId(),
                pipeline.getName(),
                pipeline.getDescription(),
                pipeline.getStatus(),
                pipeline.getTriggerUserId(),
                pipeline.getBranchName(),
                pipeline.getCommitId(),
                pipeline.getPipelineNumber(),
                pipeline.getStartTime(),
                pipeline.getEndTime(),
                pipeline.getDurationSeconds(),
                pipeline.getConfig(),
                pipeline.getErrorMessage(),
                pipeline.getIsActive(),
                pipeline.getCreatedAt(),
                pipeline.getUpdatedAt()
        );
    }
    
    /**
     * 转换阶段为DTO
     */
    private PipelineStageDTO convertStageToDTO(PipelineStage stage) {
        return new PipelineStageDTO(
                stage.getId(),
                stage.getPipelineId(),
                stage.getStageType(),
                stage.getName(),
                stage.getOrderIndex(),
                stage.getStatus(),
                stage.getStartTime(),
                stage.getEndTime(),
                stage.getDurationSeconds(),
                stage.getLog(),
                stage.getErrorMessage(),
                stage.getConfig(),
                stage.getCreatedAt(),
                stage.getUpdatedAt()
        );
    }
}