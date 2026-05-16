package com.devsecops.branch.repository;

import com.devsecops.branch.entity.Branch;
import com.devsecops.common.enums.BranchType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 分支数据访问层
 */
@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    
    /**
     * 根据项目ID和分支名称查询
     */
    Optional<Branch> findByProjectIdAndName(Long projectId, String name);
    
    /**
     * 检查项目下分支名称是否存在
     */
    boolean existsByProjectIdAndName(Long projectId, String name);
    
    /**
     * 根据项目ID查询所有分支
     */
    List<Branch> findByProjectIdAndIsDeletedFalse(Long projectId);
    
    /**
     * 根据项目ID和分支类型查询
     */
    List<Branch> findByProjectIdAndTypeAndIsDeletedFalse(Long projectId, BranchType type);
    
    /**
     * 根据创建者查询分支
     */
    List<Branch> findByCreatorIdAndIsDeletedFalse(Long creatorId);
}