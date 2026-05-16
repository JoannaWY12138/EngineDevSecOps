package com.devsecops.build.repository;

import com.devsecops.build.entity.Build;
import com.devsecops.common.enums.BuildStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 构建数据访问层
 */
@Repository
public interface BuildRepository extends JpaRepository<Build, Long> {
    
    /**
     * 根据项目ID查询构建列表
     */
    List<Build> findByProjectIdOrderByCreatedAtDesc(Long projectId);
    
    /**
     * 根据项目ID和状态查询构建列表
     */
    List<Build> findByProjectIdAndStatus(Long projectId, BuildStatus status);
    
    /**
     * 根据分支ID查询构建列表
     */
    List<Build> findByBranchIdOrderByCreatedAtDesc(Long branchId);
    
    /**
     * 获取项目的下一个构建编号
     */
    @Query("SELECT MAX(b.buildNumber) FROM Build b WHERE b.projectId = :projectId")
    Optional<Integer> findMaxBuildNumberByProjectId(Long projectId);
    
    /**
     * 根据项目ID和构建编号查询
     */
    Optional<Build> findByProjectIdAndBuildNumber(Long projectId, Integer buildNumber);
    
    /**
     * 查询正在运行的构建
     */
    List<Build> findByStatus(BuildStatus status);
}