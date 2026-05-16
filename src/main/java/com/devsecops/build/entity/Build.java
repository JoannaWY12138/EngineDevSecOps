package com.devsecops.build.entity;

import com.devsecops.common.entity.BaseEntity;
import com.devsecops.common.enums.BuildStatus;
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
 * 构建实体
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "build")
public class Build extends BaseEntity {
    
    @Column(name = "project_id", nullable = false)
    private Long projectId;
    
    @Column(name = "branch_id", nullable = false)
    private Long branchId;
    
    @Column(name = "branch_name", length = 100)
    private String branchName;
    
    @Column(name = "commit_id", length = 100)
    private String commitId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BuildStatus status;
    
    @Column(name = "build_number")
    private Integer buildNumber;
    
    @Column(name = "trigger_user_id", nullable = false)
    private Long triggerUserId;
    
    @Column(name = "build_log", columnDefinition = "TEXT")
    private String buildLog;
    
    @Column(name = "duration_seconds")
    private Integer durationSeconds;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "build_tool", length = 50)
    private String buildTool;
    
    @Column(name = "artifact_path", length = 255)
    private String artifactPath;
    
    @Column(name = "error_message", length = 500)
    private String errorMessage;
}