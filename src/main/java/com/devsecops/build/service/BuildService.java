package com.devsecops.build.service;

import com.devsecops.build.dto.BuildDTO;
import com.devsecops.build.dto.TriggerBuildRequest;
import com.devsecops.build.entity.Build;
import com.devsecops.build.repository.BuildRepository;
import com.devsecops.branch.entity.Branch;
import com.devsecops.branch.repository.BranchRepository;
import com.devsecops.common.enums.BuildStatus;
import com.devsecops.common.exception.BusinessException;
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
 * 构建服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BuildService {
    
    private final BuildRepository buildRepository;
    private final ProjectRepository projectRepository;
    private final BranchRepository branchRepository;
    
    /**
     * 触发构建
     */
    @Transactional
    public BuildDTO triggerBuild(TriggerBuildRequest request) {
        // 验证项目是否存在
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> BusinessException.of("项目不存在"));
        
        // 验证分支是否存在
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> BusinessException.of("分支不存在"));
        
        // 获取下一个构建编号
        Integer nextBuildNumber = buildRepository.findMaxBuildNumberByProjectId(request.getProjectId())
                .map(num -> num + 1)
                .orElse(1);
        
        Build build = new Build();
        build.setProjectId(request.getProjectId());
        build.setBranchId(request.getBranchId());
        build.setBranchName(branch.getName());
        build.setCommitId(request.getCommitId());
        build.setStatus(BuildStatus.PENDING);
        build.setBuildNumber(nextBuildNumber);
        build.setTriggerUserId(request.getTriggerUserId());
        build.setBuildTool(request.getBuildTool() != null ? request.getBuildTool() : project.getBuildTool());
        
        build = buildRepository.save(build);
        log.info("触发构建成功: 项目={}, 分支={}, 构建编号={}", 
                project.getName(), branch.getName(), nextBuildNumber);
        
        return convertToDTO(build);
    }
    
    /**
     * 开始构建
     */
    @Transactional
    public BuildDTO startBuild(Long id) {
        Build build = buildRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("构建不存在"));
        
        if (build.getStatus() != BuildStatus.PENDING) {
            throw BusinessException.of("只有待构建状态的构建才能开始");
        }
        
        build.setStatus(BuildStatus.RUNNING);
        build.setStartTime(LocalDateTime.now());
        build = buildRepository.save(build);
        
        log.info("开始构建: 构建编号={}", build.getBuildNumber());
        return convertToDTO(build);
    }
    
    /**
     * 完成构建
     */
    @Transactional
    public BuildDTO completeBuild(Long id, String artifactPath) {
        Build build = buildRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("构建不存在"));
        
        if (build.getStatus() != BuildStatus.RUNNING) {
            throw BusinessException.of("只有构建中状态的构建才能完成");
        }
        
        build.setStatus(BuildStatus.SUCCESS);
        build.setEndTime(LocalDateTime.now());
        build.setArtifactPath(artifactPath);
        
        if (build.getStartTime() != null && build.getEndTime() != null) {
            long seconds = java.time.Duration.between(build.getStartTime(), build.getEndTime()).getSeconds();
            build.setDurationSeconds((int) seconds);
        }
        
        build = buildRepository.save(build);
        log.info("完成构建: 构建编号={}", build.getBuildNumber());
        return convertToDTO(build);
    }
    
    /**
     * 构建失败
     */
    @Transactional
    public BuildDTO failBuild(Long id, String errorMessage) {
        Build build = buildRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("构建不存在"));
        
        build.setStatus(BuildStatus.FAILED);
        build.setEndTime(LocalDateTime.now());
        build.setErrorMessage(errorMessage);
        
        if (build.getStartTime() != null && build.getEndTime() != null) {
            long seconds = java.time.Duration.between(build.getStartTime(), build.getEndTime()).getSeconds();
            build.setDurationSeconds((int) seconds);
        }
        
        build = buildRepository.save(build);
        log.info("构建失败: 构建编号={}, 错误={}", build.getBuildNumber(), errorMessage);
        return convertToDTO(build);
    }
    
    /**
     * 取消构建
     */
    @Transactional
    public BuildDTO cancelBuild(Long id) {
        Build build = buildRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("构建不存在"));
        
        if (build.getStatus() == BuildStatus.SUCCESS || build.getStatus() == BuildStatus.FAILED) {
            throw BusinessException.of("已完成的构建不能取消");
        }
        
        build.setStatus(BuildStatus.CANCELLED);
        build.setEndTime(LocalDateTime.now());
        build = buildRepository.save(build);
        
        log.info("取消构建: 构建编号={}", build.getBuildNumber());
        return convertToDTO(build);
    }
    
    /**
     * 获取构建详情
     */
    public BuildDTO getBuildById(Long id) {
        Build build = buildRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("构建不存在"));
        return convertToDTO(build);
    }
    
    /**
     * 获取项目的构建列表
     */
    public List<BuildDTO> getBuildsByProject(Long projectId) {
        return buildRepository.findByProjectIdOrderByCreatedAtDesc(projectId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取分支的构建列表
     */
    public List<BuildDTO> getBuildsByBranch(Long branchId) {
        return buildRepository.findByBranchIdOrderByCreatedAtDesc(branchId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取正在运行的构建
     */
    public List<BuildDTO> getRunningBuilds() {
        return buildRepository.findByStatus(BuildStatus.RUNNING).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 更新构建日志
     */
    @Transactional
    public void updateBuildLog(Long id, String log) {
        Build build = buildRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("构建不存在"));
        build.setBuildLog(log);
        buildRepository.save(build);
    }
    
    /**
     * 转换为DTO
     */
    private BuildDTO convertToDTO(Build build) {
        return new BuildDTO(
                build.getId(),
                build.getProjectId(),
                build.getBranchId(),
                build.getBranchName(),
                build.getCommitId(),
                build.getStatus(),
                build.getBuildNumber(),
                build.getTriggerUserId(),
                build.getBuildLog(),
                build.getDurationSeconds(),
                build.getStartTime(),
                build.getEndTime(),
                build.getBuildTool(),
                build.getArtifactPath(),
                build.getErrorMessage(),
                build.getCreatedAt(),
                build.getUpdatedAt()
        );
    }
}