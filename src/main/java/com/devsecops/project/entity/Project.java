package com.devsecops.project.entity;

import com.devsecops.common.entity.BaseEntity;
import com.devsecops.common.enums.ProjectStatus;
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
 * 项目实体
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "project")
public class Project extends BaseEntity {
    
    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;
    
    @Column(name = "description", length = 500)
    private String description;
    
    @Column(name = "git_url", nullable = false, length = 255)
    private String gitUrl;
    
    @Column(name = "default_branch", length = 50)
    private String defaultBranch;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProjectStatus status;
    
    @Column(name = "owner_id", nullable = false)
    private Long ownerId;
    
    @Column(name = "build_tool", length = 50)
    private String buildTool;
    
    @Column(name = "language", length = 50)
    private String language;
}