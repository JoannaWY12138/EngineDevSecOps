package com.devsecops.branch.entity;

import com.devsecops.common.entity.BaseEntity;
import com.devsecops.common.enums.BranchType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 分支实体
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "branch")
public class Branch extends BaseEntity {
    
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @Column(name = "project_id", nullable = false)
    private Long projectId;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, length = 20)
    private BranchType type;
    
    @Column(name = "source_branch", length = 100)
    private String sourceBranch;
    
    @Column(name = "target_branch", length = 100)
    private String targetBranch;
    
    @Column(name = "creator_id", nullable = false)
    private Long creatorId;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "commit_id", length = 100)
    private String commitId;
    
    @Column(name = "is_protected", nullable = false)
    private Boolean isProtected = false;
    
    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;
}