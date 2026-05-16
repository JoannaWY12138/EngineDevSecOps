package com.devsecops.merge.entity;

import com.devsecops.common.entity.BaseEntity;
import com.devsecops.common.enums.MergeStatus;
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
 * 代码合并请求实体
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "merge_request")
public class MergeRequest extends BaseEntity {
    
    @Column(name = "project_id", nullable = false)
    private Long projectId;
    
    @Column(name = "source_branch_id", nullable = false)
    private Long sourceBranchId;
    
    @Column(name = "source_branch_name", length = 100)
    private String sourceBranchName;
    
    @Column(name = "target_branch_id", nullable = false)
    private Long targetBranchId;
    
    @Column(name = "target_branch_name", length = 100)
    private String targetBranchName;
    
    @Column(name = "title", length = 200)
    private String title;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MergeStatus status;
    
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;
    
    @Column(name = "reviewer_id")
    private Long reviewerId;
    
    @Column(name = "commit_ids", length = 500)
    private String commitIds;
    
    @Column(name = "conflict_files", columnDefinition = "TEXT")
    private String conflictFiles;
    
    @Column(name = "merge_time")
    private LocalDateTime mergeTime;
    
    @Column(name = "approved_time")
    private LocalDateTime approvedTime;
    
    @Column(name = "error_message", length = 500)
    private String errorMessage;
}