package com.devsecops.merge.service;

import com.devsecops.branch.entity.Branch;
import com.devsecops.branch.repository.BranchRepository;
import com.devsecops.common.enums.MergeStatus;
import com.devsecops.common.exception.BusinessException;
import com.devsecops.merge.dto.CreateMergeRequest;
import com.devsecops.merge.dto.MergeRequestDTO;
import com.devsecops.merge.entity.MergeRequest;
import com.devsecops.merge.repository.MergeRequestRepository;
import com.devsecops.project.entity.Project;
import com.devsecops.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 合并请求服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MergeRequestService {
    
    private final MergeRequestRepository mergeRequestRepository;
    private final ProjectRepository projectRepository;
    private final BranchRepository branchRepository;
    
    /**
     * 创建合并请求
     */
    @Transactional
    public MergeRequestDTO createMergeRequest(CreateMergeRequest request) {
        // 验证项目是否存在
        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> BusinessException.of("项目不存在"));
        
        // 验证源分支是否存在
        Branch sourceBranch = branchRepository.findById(request.getSourceBranchId())
                .orElseThrow(() -> BusinessException.of("源分支不存在"));
        
        // 验证目标分支是否存在
        Branch targetBranch = branchRepository.findById(request.getTargetBranchId())
                .orElseThrow(() -> BusinessException.of("目标分支不存在"));
        
        // 检查是否已有相同的合并请求
        if (mergeRequestRepository.findBySourceBranchIdAndTargetBranchIdAndStatus(
                request.getSourceBranchId(), request.getTargetBranchId(), MergeStatus.PENDING).isPresent()) {
            throw BusinessException.of("已有相同的合并请求存在");
        }
        
        MergeRequest mergeRequest = new MergeRequest();
        mergeRequest.setProjectId(request.getProjectId());
        mergeRequest.setSourceBranchId(request.getSourceBranchId());
        mergeRequest.setSourceBranchName(sourceBranch.getName());
        mergeRequest.setTargetBranchId(request.getTargetBranchId());
        mergeRequest.setTargetBranchName(targetBranch.getName());
        mergeRequest.setTitle(request.getTitle());
        mergeRequest.setDescription(request.getDescription());
        mergeRequest.setStatus(MergeStatus.PENDING);
        mergeRequest.setCreatorId(request.getCreatorId());
        mergeRequest.setCommitIds(request.getCommitIds());
        
        mergeRequest = mergeRequestRepository.save(mergeRequest);
        log.info("创建合并请求成功: {} -> {}", sourceBranch.getName(), targetBranch.getName());
        
        return convertToDTO(mergeRequest);
    }
    
    /**
     * 审核合并请求
     */
    @Transactional
    public MergeRequestDTO approveMergeRequest(Long id, Long reviewerId) {
        MergeRequest mergeRequest = mergeRequestRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("合并请求不存在"));
        
        if (mergeRequest.getStatus() != MergeStatus.PENDING) {
            throw BusinessException.of("只有待合并状态的请求才能审核");
        }
        
        mergeRequest.setReviewerId(reviewerId);
        mergeRequest.setApprovedTime(LocalDateTime.now());
        mergeRequest = mergeRequestRepository.save(mergeRequest);
        
        log.info("审核合并请求: ID={}, 审核者={}", id, reviewerId);
        return convertToDTO(mergeRequest);
    }
    
    /**
     * 执行合并
     */
    @Transactional
    public MergeRequestDTO executeMerge(Long id) {
        MergeRequest mergeRequest = mergeRequestRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("合并请求不存在"));
        
        if (mergeRequest.getStatus() != MergeStatus.PENDING) {
            throw BusinessException.of("只有待合并状态的请求才能执行合并");
        }
        
        // 更新状态为合并中
        mergeRequest.setStatus(MergeStatus.MERGING);
        mergeRequestRepository.save(mergeRequest);
        
        // 模拟合并操作（实际项目中会调用Git API）
        // 这里假设合并成功
        mergeRequest.setStatus(MergeStatus.SUCCESS);
        mergeRequest.setMergeTime(LocalDateTime.now());
        mergeRequest = mergeRequestRepository.save(mergeRequest);
        
        log.info("合并成功: {} -> {}", 
                mergeRequest.getSourceBranchName(), mergeRequest.getTargetBranchName());
        return convertToDTO(mergeRequest);
    }
    
    /**
     * 合并失败
     */
    @Transactional
    public MergeRequestDTO failMerge(Long id, String errorMessage, String conflictFiles) {
        MergeRequest mergeRequest = mergeRequestRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("合并请求不存在"));
        
        if (errorMessage != null && errorMessage.contains("conflict")) {
            mergeRequest.setStatus(MergeStatus.CONFLICT);
            mergeRequest.setConflictFiles(conflictFiles);
        } else {
            mergeRequest.setStatus(MergeStatus.FAILED);
        }
        
        mergeRequest.setErrorMessage(errorMessage);
        mergeRequest = mergeRequestRepository.save(mergeRequest);
        
        log.info("合并失败: {}", errorMessage);
        return convertToDTO(mergeRequest);
    }
    
    /**
     * 取消合并请求
     */
    @Transactional
    public MergeRequestDTO cancelMergeRequest(Long id) {
        MergeRequest mergeRequest = mergeRequestRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("合并请求不存在"));
        
        if (mergeRequest.getStatus() == MergeStatus.SUCCESS) {
            throw BusinessException.of("已合并的请求不能取消");
        }
        
        mergeRequest.setStatus(MergeStatus.CANCELLED);
        mergeRequest = mergeRequestRepository.save(mergeRequest);
        
        log.info("取消合并请求: ID={}", id);
        return convertToDTO(mergeRequest);
    }
    
    /**
     * 获取合并请求详情
     */
    public MergeRequestDTO getMergeRequestById(Long id) {
        MergeRequest mergeRequest = mergeRequestRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("合并请求不存在"));
        return convertToDTO(mergeRequest);
    }
    
    /**
     * 获取项目的合并请求列表
     */
    public List<MergeRequestDTO> getMergeRequestsByProject(Long projectId) {
        return mergeRequestRepository.findByProjectIdOrderByCreatedAtDesc(projectId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取待审核的合并请求列表
     */
    public List<MergeRequestDTO> getPendingMergeRequests() {
        return mergeRequestRepository.findByStatus(MergeStatus.PENDING).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取用户创建的合并请求列表
     */
    public List<MergeRequestDTO> getMergeRequestsByCreator(Long creatorId) {
        return mergeRequestRepository.findByCreatorIdOrderByCreatedAtDesc(creatorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 转换为DTO
     */
    private MergeRequestDTO convertToDTO(MergeRequest mergeRequest) {
        return new MergeRequestDTO(
                mergeRequest.getId(),
                mergeRequest.getProjectId(),
                mergeRequest.getSourceBranchId(),
                mergeRequest.getSourceBranchName(),
                mergeRequest.getTargetBranchId(),
                mergeRequest.getTargetBranchName(),
                mergeRequest.getTitle(),
                mergeRequest.getDescription(),
                mergeRequest.getStatus(),
                mergeRequest.getCreatorId(),
                mergeRequest.getReviewerId(),
                mergeRequest.getCommitIds(),
                mergeRequest.getConflictFiles(),
                mergeRequest.getMergeTime(),
                mergeRequest.getApprovedTime(),
                mergeRequest.getErrorMessage(),
                mergeRequest.getCreatedAt(),
                mergeRequest.getUpdatedAt()
        );
    }
}