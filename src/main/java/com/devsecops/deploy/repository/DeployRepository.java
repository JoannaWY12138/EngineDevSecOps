package com.devsecops.deploy.repository;

import com.devsecops.common.enums.DeployStatus;
import com.devsecops.deploy.entity.Deploy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 发布数据访问层
 */
@Repository
public interface DeployRepository extends JpaRepository<Deploy, Long> {
    
    /**
     * 根据项目ID查询发布列表
     */
    List<Deploy> findByProjectIdOrderByCreatedAtDesc(Long projectId);
    
    /**
     * 根据项目ID和环境查询发布列表
     */
    List<Deploy> findByProjectIdAndEnvironmentOrderByCreatedAtDesc(Long projectId, String environment);
    
    /**
     * 根据构建ID查询发布列表
     */
    List<Deploy> findByBuildId(Long buildId);
    
    /**
     * 获取项目环境的下一个发布编号
     */
    @Query("SELECT MAX(d.deployNumber) FROM Deploy d WHERE d.projectId = :projectId AND d.environment = :environment")
    Optional<Integer> findMaxDeployNumberByProjectIdAndEnvironment(Long projectId, String environment);
    
    /**
     * 根据状态查询发布列表
     */
    List<Deploy> findByStatus(DeployStatus status);
    
    /**
     * 查询项目的最新成功发布
     */
    Optional<Deploy> findFirstByProjectIdAndEnvironmentAndStatusOrderByCreatedAtDesc(
            Long projectId, String environment, DeployStatus status);
}