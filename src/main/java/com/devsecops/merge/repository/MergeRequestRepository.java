package com.devsecops.merge.repository;

import com.devsecops.common.enums.MergeStatus;
import com.devsecops.merge.entity.MergeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 合并请求数据访问层
 */
@Repository
public interface MergeRequestRepository extends JpaRepository<MergeRequest, Long> {
    
    /**
     * 根据项目ID查询合并请求列表
     */
    List<MergeRequest> findByProjectIdOrderByCreatedAtDesc(Long projectId);
    
    /**
     * 根据项目ID和状态查询合并请求列表
     */
    List<MergeRequest> findByProjectIdAndStatus(Long projectId, MergeStatus status);
    
    /**
     * 根据创建者查询合并请求列表
     */
    List<MergeRequest> findByCreatorIdOrderByCreatedAtDesc(Long creatorId);
    
    /**
     * 根据审核者查询合并请求列表
     */
    List<MergeRequest> findByReviewerIdOrderByCreatedAtDesc(Long reviewerId);
    
    /**
     * 根据源分支和目标分支查询合并请求
     */
    Optional<MergeRequest> findBySourceBranchIdAndTargetBranchIdAndStatus(
            Long sourceBranchId, Long targetBranchId, MergeStatus status);
    
    /**
     * 查询待审核的合并请求
     */
    List<MergeRequest> findByStatus(MergeStatus status);
}