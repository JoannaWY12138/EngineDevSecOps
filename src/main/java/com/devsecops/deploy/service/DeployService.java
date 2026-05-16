package com.devsecops.deploy.service;

import com.devsecops.build.entity.Build;
import com.devsecops.build.repository.BuildRepository;
import com.devsecops.common.enums.BuildStatus;
import com.devsecops.common.enums.DeployStatus;
import com.devsecops.common.exception.BusinessException;
import com.devsecops.deploy.dto.DeployDTO;
import com.devsecops.deploy.dto.TriggerDeployRequest;
import com.devsecops.deploy.entity.Deploy;
import com.devsecops.deploy.repository.DeployRepository;
import com.devsecops.project.entity.Project;
import com.devsecops.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 发布服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DeployService {
    
    private final DeployRepository deployRepository;
    private final ProjectRepository projectRepository;
    private final BuildRepository buildRepository;
    
    /**
     * 触发发布
     */
    @Transactional
    public DeployDTO triggerDeploy(TriggerDeployRequest request) {
        // 验证项目是否存在
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> BusinessException.of("项目不存在"));
        
        // 验证构建是否存在且已完成
        Build build = buildRepository.findById(request.getBuildId())
                .orElseThrow(() -> BusinessException.of("构建不存在"));
        
        if (build.getStatus() != BuildStatus.SUCCESS) {
            throw BusinessException.of("只有成功的构建才能发布");
        }
        
        // 获取下一个发布编号
        Integer nextDeployNumber = deployRepository
                .findMaxDeployNumberByProjectIdAndEnvironment(request.getProjectId(), request.getEnvironment())
                .map(num -> num + 1)
                .orElse(1);
        
        // 获取上一个成功的版本用于回滚
        String rollbackVersion = deployRepository
                .findFirstByProjectIdAndEnvironmentAndStatusOrderByCreatedAtDesc(
                        request.getProjectId(), request.getEnvironment(), DeployStatus.SUCCESS)
                .map(Deploy::getVersion)
                .orElse(null);
        
        Deploy deploy = new Deploy();
        deploy.setProjectId(request.getProjectId());
        deploy.setBuildId(request.getBuildId());
        deploy.setEnvironment(request.getEnvironment());
        deploy.setStatus(DeployStatus.PENDING);
        deploy.setDeployNumber(nextDeployNumber);
        deploy.setTriggerUserId(request.getTriggerUserId());
        deploy.setTargetServer(request.getTargetServer());
        deploy.setDeployPath(request.getDeployPath());
        deploy.setVersion(request.getVersion());
        deploy.setRollbackVersion(rollbackVersion);
        
        deploy = deployRepository.save(deploy);
        log.info("触发发布成功: 项目={}, 环境={}, 发布编号={}", 
                project.getName(), request.getEnvironment(), nextDeployNumber);
        
        return convertToDTO(deploy);
    }
    
    /**
     * 开始发布
     */
    @Transactional
    public DeployDTO startDeploy(Long id) {
        Deploy deploy = deployRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("发布不存在"));
        
        if (deploy.getStatus() != DeployStatus.PENDING) {
            throw BusinessException.of("只有待发布状态的发布才能开始");
        }
        
        deploy.setStatus(DeployStatus.DEPLOYING);
        deploy.setStartTime(LocalDateTime.now());
        deploy = deployRepository.save(deploy);
        
        log.info("开始发布: 发布编号={}", deploy.getDeployNumber());
        return convertToDTO(deploy);
    }
    
    /**
     * 完成发布
     */
    @Transactional
    public DeployDTO completeDeploy(Long id) {
        Deploy deploy = deployRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("发布不存在"));
        
        if (deploy.getStatus() != DeployStatus.DEPLOYING) {
            throw BusinessException.of("只有发布中状态的发布才能完成");
        }
        
        deploy.setStatus(DeployStatus.SUCCESS);
        deploy.setEndTime(LocalDateTime.now());
        
        if (deploy.getStartTime() != null && deploy.getEndTime() != null) {
            long seconds = java.time.Duration.between(deploy.getStartTime(), deploy.getEndTime()).getSeconds();
            deploy.setDurationSeconds((int) seconds);
        }
        
        deploy = deployRepository.save(deploy);
        log.info("完成发布: 发布编号={}", deploy.getDeployNumber());
        return convertToDTO(deploy);
    }
    
    /**
     * 发布失败
     */
    @Transactional
    public DeployDTO failDeploy(Long id, String errorMessage) {
        Deploy deploy = deployRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("发布不存在"));
        
        deploy.setStatus(DeployStatus.FAILED);
        deploy.setEndTime(LocalDateTime.now());
        deploy.setErrorMessage(errorMessage);
        
        if (deploy.getStartTime() != null && deploy.getEndTime() != null) {
            long seconds = java.time.Duration.between(deploy.getStartTime(), deploy.getEndTime()).getSeconds();
            deploy.setDurationSeconds((int) seconds);
        }
        
        deploy = deployRepository.save(deploy);
        log.info("发布失败: 发布编号={}, 错误={}", deploy.getDeployNumber(), errorMessage);
        return convertToDTO(deploy);
    }
    
    /**
     * 回滚发布
     */
    @Transactional
    public DeployDTO rollbackDeploy(Long id) {
        Deploy deploy = deployRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("发布不存在"));
        
        if (deploy.getStatus() != DeployStatus.SUCCESS) {
            throw BusinessException.of("只有成功的发布才能回滚");
        }
        
        if (deploy.getRollbackVersion() == null) {
            throw BusinessException.of("没有可回滚的版本");
        }
        
        deploy.setStatus(DeployStatus.ROLLBACK);
        deploy = deployRepository.save(deploy);
        
        log.info("回滚发布: 发布编号={}, 回滚到版本={}", 
                deploy.getDeployNumber(), deploy.getRollbackVersion());
        return convertToDTO(deploy);
    }
    
    /**
     * 取消发布
     */
    @Transactional
    public DeployDTO cancelDeploy(Long id) {
        Deploy deploy = deployRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("发布不存在"));
        
        if (deploy.getStatus() == DeployStatus.SUCCESS || deploy.getStatus() == DeployStatus.FAILED) {
            throw BusinessException.of("已完成的发布不能取消");
        }
        
        deploy.setStatus(DeployStatus.CANCELLED);
        deploy.setEndTime(LocalDateTime.now());
        deploy = deployRepository.save(deploy);
        
        log.info("取消发布: 发布编号={}", deploy.getDeployNumber());
        return convertToDTO(deploy);
    }
    
    /**
     * 获取发布详情
     */
    public DeployDTO getDeployById(Long id) {
        Deploy deploy = deployRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("发布不存在"));
        return convertToDTO(deploy);
    }
    
    /**
     * 获取项目的发布列表
     */
    public List<DeployDTO> getDeploysByProject(Long projectId) {
        return deployRepository.findByProjectIdOrderByCreatedAtDesc(projectId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取项目环境的发布列表
     */
    public List<DeployDTO> getDeploysByProjectAndEnvironment(Long projectId, String environment) {
        return deployRepository.findByProjectIdAndEnvironmentOrderByCreatedAtDesc(projectId, environment).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取正在发布的列表
     */
    public List<DeployDTO> getRunningDeploys() {
        return deployRepository.findByStatus(DeployStatus.DEPLOYING).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 更新发布日志
     */
    @Transactional
    public void updateDeployLog(Long id, String log) {
        Deploy deploy = deployRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("发布不存在"));
        deploy.setDeployLog(log);
        deployRepository.save(deploy);
    }
    
    /**
     * 转换为DTO
     */
    private DeployDTO convertToDTO(Deploy deploy) {
        return new DeployDTO(
                deploy.getId(),
                deploy.getProjectId(),
                deploy.getBuildId(),
                deploy.getEnvironment(),
                deploy.getStatus(),
                deploy.getDeployNumber(),
                deploy.getTriggerUserId(),
                deploy.getDeployLog(),
                deploy.getDurationSeconds(),
                deploy.getStartTime(),
                deploy.getEndTime(),
                deploy.getTargetServer(),
                deploy.getDeployPath(),
                deploy.getVersion(),
                deploy.getRollbackVersion(),
                deploy.getErrorMessage(),
                deploy.getCreatedAt(),
                deploy.getUpdatedAt()
        );
    }
}