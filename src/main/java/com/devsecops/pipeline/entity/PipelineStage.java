package com.devsecops.pipeline.entity;

import com.devsecops.common.entity.BaseEntity;
import com.devsecops.common.enums.PipelineStatus;
import com.devsecops.common.enums.StageType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 流水线阶段实体
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pipeline_stage")
public class PipelineStage extends BaseEntity {
    
    @Column(name = "pipeline_id", nullable = false)
    private Long pipelineId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "stage_type", nullable = false, length = 20)
    private StageType stageType;
    
    @Column(name = "name", length = 100)
    private String name;
    
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PipelineStatus status;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "duration_seconds")
    private Integer durationSeconds;
    
    @Column(name = "log", columnDefinition = "TEXT")
    private String log;
    
    @Column(name = "error_message", length = 500)
    private String errorMessage;
    
    @Column(name = "config", columnDefinition = "TEXT")
    private String config;
}