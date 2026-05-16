package com.devsecops.project.service;

import com.devsecops.common.enums.ProjectStatus;
import com.devsecops.common.exception.BusinessException;
import com.devsecops.project.dto.CreateProjectRequest;
import com.devsecops.project.dto.ProjectDTO;
import com.devsecops.project.dto.UpdateProjectRequest;
import com.devsecops.project.entity.Project;
import com.devsecops.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    
    private final ProjectRepository projectRepository;
    
    /**
     * 创建项目
     */
    @Transactional
    public ProjectDTO createProject(CreateProjectRequest request) {
        // 检查项目名称是否已存在
        if (projectRepository.existsByName(request.getName())) {
            throw BusinessException.of("项目名称已存在");
        }
        
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setGitUrl(request.getGitUrl());
        project.setDefaultBranch(request.getDefaultBranch());
        project.setOwnerId(request.getOwnerId());
        project.setBuildTool(request.getBuildTool());
        project.setLanguage(request.getLanguage());
        project.setStatus(ProjectStatus.ACTIVE);
        
        project = projectRepository.save(project);
        log.info("创建项目成功: {}", project.getName());
        
        return convertToDTO(project);
    }
    
    /**
     * 更新项目
     */
    @Transactional
    public ProjectDTO updateProject(Long id, UpdateProjectRequest request) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("项目不存在"));
        
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }
        if (request.getGitUrl() != null) {
            project.setGitUrl(request.getGitUrl());
        }
        if (request.getDefaultBranch() != null) {
            project.setDefaultBranch(request.getDefaultBranch());
        }
        if (request.getOwnerId() != null) {
            project.setOwnerId(request.getOwnerId());
        }
        if (request.getBuildTool() != null) {
            project.setBuildTool(request.getBuildTool());
        }
        if (request.getLanguage() != null) {
            project.setLanguage(request.getLanguage());
        }
        if (request.getStatus() != null) {
            project.setStatus(ProjectStatus.valueOf(request.getStatus()));
        }
        
        project = projectRepository.save(project);
        log.info("更新项目成功: {}", project.getName());
        
        return convertToDTO(project);
    }
    
    /**
     * 删除项目（归档）
     */
    @Transactional
    public void archiveProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("项目不存在"));
        
        project.setStatus(ProjectStatus.ARCHIVED);
        projectRepository.save(project);
        log.info("归档项目成功: {}", project.getName());
    }
    
    /**
     * 恢复项目
     */
    @Transactional
    public void restoreProject(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("项目不存在"));
        
        project.setStatus(ProjectStatus.ACTIVE);
        projectRepository.save(project);
        log.info("恢复项目成功: {}", project.getName());
    }
    
    /**
     * 获取项目详情
     */
    public ProjectDTO getProjectById(Long id) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("项目不存在"));
        return convertToDTO(project);
    }
    
    /**
     * 获取所有活跃项目
     */
    public List<ProjectDTO> getAllActiveProjects() {
        return projectRepository.findByStatus(ProjectStatus.ACTIVE).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 分页查询项目
     */
    public Page<ProjectDTO> getProjects(Pageable pageable) {
        return projectRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    /**
     * 根据负责人查询项目
     */
    public List<ProjectDTO> getProjectsByOwner(Long ownerId) {
        return projectRepository.findByOwnerId(ownerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 转换为DTO
     */
    private ProjectDTO convertToDTO(Project project) {
        return new ProjectDTO(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getGitUrl(),
                project.getDefaultBranch(),
                project.getStatus(),
                project.getOwnerId(),
                project.getBuildTool(),
                project.getLanguage(),
                project.getCreatedAt(),
                project.getUpdatedAt()
        );
    }
}