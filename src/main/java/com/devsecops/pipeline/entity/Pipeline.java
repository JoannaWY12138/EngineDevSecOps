package com.devsecops.pipeline.entity;

import com.devsecops.common.entity.BaseEntity;
import com.devsecops.common.enums.PipelineStatus;
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
 * 流水线实体
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pipeline")
public class Pipeline extends BaseEntity {
    
    @Column(name = "project_id", nullable = false)
    private Long projectId;
    
    @Column(name = "name", length = 100)
    private String name;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private PipelineStatus status;
    
    @Column(name = "trigger_user_id", nullable = false)
    private Long triggerUserId;
    
    @Column(name = "branch_name", length = 100)
    private String branchName;
    
    @Column(name = "commit_id", length = 100)
    private String commitId;
    
    @Column(name = "pipeline_number")
    private Integer pipelineNumber;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "duration_seconds")
    private Integer durationSeconds;
    
    @Column(name = "config", columnDefinition = "TEXT")
    private String config;
    
    @Column(name = "error_message", length = 500)
    private String errorMessage;
    
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;
}