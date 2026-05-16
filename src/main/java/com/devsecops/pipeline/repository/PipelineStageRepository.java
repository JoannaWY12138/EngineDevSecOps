package com.devsecops.pipeline.repository;

import com.devsecops.common.enums.PipelineStatus;
import com.devsecops.pipeline.entity.PipelineStage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 流水线阶段数据访问层
 */
@Repository
public interface PipelineStageRepository extends JpaRepository<PipelineStage, Long> {
    
    /**
     * 根据流水线ID查询所有阶段（按顺序排序）
     */
    List<PipelineStage> findByPipelineIdOrderByOrderIndex(Long pipelineId);
    
    /**
     * 根据流水线ID和状态查询阶段
     */
    List<PipelineStage> findByPipelineIdAndStatus(Long pipelineId, PipelineStatus status);
    
    /**
     * 获取流水线当前执行阶段
     */
    Optional<PipelineStage> findFirstByPipelineIdAndStatusOrderByOrderIndex(
            Long pipelineId, PipelineStatus status);
    
    /**
     * 获取下一个待执行阶段
     */
    Optional<PipelineStage> findFirstByPipelineIdAndStatusOrderByOrderIndexAsc(
            Long pipelineId, PipelineStatus status);
}