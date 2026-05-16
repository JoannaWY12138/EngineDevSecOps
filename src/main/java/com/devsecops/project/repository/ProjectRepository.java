package com.devsecops.project.repository;

import com.devsecops.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * 项目数据访问层
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    
    /**
     * 根据项目名称查询
     */
    Optional<Project> findByName(String name);
    
    /**
     * 检查项目名称是否存在
     */
    boolean existsByName(String name);
    
    /**
     * 根据负责人查询项目列表
     */
    List<Project> findByOwnerId(Long ownerId);
    
    /**
     * 根据状态查询项目列表
     */
    List<Project> findByStatus(com.devsecops.common.enums.ProjectStatus status);
}