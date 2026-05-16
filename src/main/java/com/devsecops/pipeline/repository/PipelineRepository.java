package com.devsecops.pipeline.repository;

import com.devsecops.common.enums.PipelineStatus;
import com.devsecops.pipeline.entity.Pipeline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 流水线数据访问层
 */
@Repository
public interface PipelineRepository extends JpaRepository<Pipeline, Long> {
    
    /**
     * 根据项目ID查询流水线列表
     */
    List<Pipeline> findByProjectIdOrderByCreatedAtDesc(Long projectId);
    
    /**
     * 根据项目ID和状态查询流水线列表
     */
    List<Pipeline> findByProjectIdAndStatus(Long projectId, PipelineStatus status);
    
    /**
     * 获取项目的下一个流水线编号
     */
    @Query("SELECT MAX(p.pipelineNumber) FROM Pipeline p WHERE p.projectId = :projectId")
    Optional<Integer> findMaxPipelineNumberByProjectId(Long projectId);
    
    /**
     * 根据状态查询流水线列表
     */
    List<Pipeline> findByStatus(PipelineStatus status);
    
    /**
     * 查询项目的活跃流水线配置
     */
    List<Pipeline> findByProjectIdAndIsActiveTrue(Long projectId);
}