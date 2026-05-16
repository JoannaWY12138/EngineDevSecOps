package com.devsecops.deploy.entity;

import com.devsecops.common.entity.BaseEntity;
import com.devsecops.common.enums.DeployStatus;
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
 * 发布实体
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "deploy")
public class Deploy extends BaseEntity {
    
    @Column(name = "project_id", nullable = false)
    private Long projectId;
    
    @Column(name = "build_id", nullable = false)
    private Long buildId;
    
    @Column(name = "environment", length = 50)
    private String environment;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private DeployStatus status;
    
    @Column(name = "deploy_number")
    private Integer deployNumber;
    
    @Column(name = "trigger_user_id", nullable = false)
    private Long triggerUserId;
    
    @Column(name = "deploy_log", columnDefinition = "TEXT")
    private String deployLog;
    
    @Column(name = "duration_seconds")
    private Integer durationSeconds;
    
    @Column(name = "start_time")
    private LocalDateTime startTime;
    
    @Column(name = "end_time")
    private LocalDateTime endTime;
    
    @Column(name = "target_server", length = 255)
    private String targetServer;
    
    @Column(name = "deploy_path", length = 255)
    private String deployPath;
    
    @Column(name = "version", length = 50)
    private String version;
    
    @Column(name = "rollback_version", length = 50)
    private String rollbackVersion;
    
    @Column(name = "error_message", length = 500)
    private String errorMessage;
}