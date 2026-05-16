package com.devsecops.branch.service;

import com.devsecops.common.enums.BranchType;
import com.devsecops.common.exception.BusinessException;
import com.devsecops.branch.dto.BranchDTO;
import com.devsecops.branch.dto.CreateBranchRequest;
import com.devsecops.branch.dto.UpdateBranchRequest;
import com.devsecops.branch.entity.Branch;
import com.devsecops.branch.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 分支服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class BranchService {
    
    private final BranchRepository branchRepository;
    
    /**
     * 创建分支
     */
    @Transactional
    public BranchDTO createBranch(CreateBranchRequest request) {
        // 检查分支是否已存在
        if (branchRepository.existsByProjectIdAndName(request.getProjectId(), request.getName())) {
            throw BusinessException.of("分支名称已存在");
        }
        
        Branch branch = new Branch();
        branch.setName(request.getName());
        branch.setProjectId(request.getProjectId());
        branch.setType(BranchType.valueOf(request.getType()));
        branch.setSourceBranch(request.getSourceBranch());
        branch.setCreatorId(request.getCreatorId());
        branch.setDescription(request.getDescription());
        branch.setIsProtected(request.getIsProtected());
        branch.setIsDeleted(false);
        
        branch = branchRepository.save(branch);
        log.info("创建分支成功: {} (项目ID: {})", branch.getName(), branch.getProjectId());
        
        return convertToDTO(branch);
    }
    
    /**
     * 更新分支
     */
    @Transactional
    public BranchDTO updateBranch(Long id, UpdateBranchRequest request) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("分支不存在"));
        
        if (branch.getIsDeleted()) {
            throw BusinessException.of("分支已删除，无法更新");
        }
        
        if (request.getDescription() != null) {
            branch.setDescription(request.getDescription());
        }
        if (request.getCommitId() != null) {
            branch.setCommitId(request.getCommitId());
        }
        if (request.getIsProtected() != null) {
            branch.setIsProtected(request.getIsProtected());
        }
        
        branch = branchRepository.save(branch);
        log.info("更新分支成功: {}", branch.getName());
        
        return convertToDTO(branch);
    }
    
    /**
     * 删除分支
     */
    @Transactional
    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("分支不存在"));
        
        if (branch.getIsProtected()) {
            throw BusinessException.of("受保护的分支不能删除");
        }
        
        branch.setIsDeleted(true);
        branchRepository.save(branch);
        log.info("删除分支成功: {}", branch.getName());
    }
    
    /**
     * 合并分支（将当前分支合并到目标分支）
     */
    @Transactional
    public void mergeBranch(Long id, String targetBranchName) {
        Branch sourceBranch = branchRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("源分支不存在"));
        
        Branch targetBranch = branchRepository.findByProjectIdAndName(
                sourceBranch.getProjectId(), targetBranchName)
                .orElseThrow(() -> BusinessException.of("目标分支不存在"));
        
        // 设置目标分支信息
        sourceBranch.setTargetBranch(targetBranchName);
        branchRepository.save(sourceBranch);
        
        log.info("合并分支 {} 到 {}", sourceBranch.getName(), targetBranchName);
    }
    
    /**
     * 获取分支详情
     */
    public BranchDTO getBranchById(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("分支不存在"));
        return convertToDTO(branch);
    }
    
    /**
     * 获取项目的所有分支
     */
    public List<BranchDTO> getBranchesByProject(Long projectId) {
        return branchRepository.findByProjectIdAndIsDeletedFalse(projectId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 根据类型获取项目的分支
     */
    public List<BranchDTO> getBranchesByProjectAndType(Long projectId, BranchType type) {
        return branchRepository.findByProjectIdAndTypeAndIsDeletedFalse(projectId, type).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取用户创建的分支
     */
    public List<BranchDTO> getBranchesByCreator(Long creatorId) {
        return branchRepository.findByCreatorIdAndIsDeletedFalse(creatorId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 转换为DTO
     */
    private BranchDTO convertToDTO(Branch branch) {
        return new BranchDTO(
                branch.getId(),
                branch.getName(),
                branch.getProjectId(),
                branch.getType(),
                branch.getSourceBranch(),
                branch.getTargetBranch(),
                branch.getCreatorId(),
                branch.getDescription(),
                branch.getCommitId(),
                branch.getIsProtected(),
                branch.getIsDeleted(),
                branch.getCreatedAt(),
                branch.getUpdatedAt()
        );
    }
}