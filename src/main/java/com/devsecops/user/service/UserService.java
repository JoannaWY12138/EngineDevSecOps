package com.devsecops.user.service;

import com.devsecops.common.enums.UserRole;
import com.devsecops.common.exception.BusinessException;
import com.devsecops.user.dto.CreateUserRequest;
import com.devsecops.user.dto.UpdateUserRequest;
import com.devsecops.user.dto.UserDTO;
import com.devsecops.user.entity.User;
import com.devsecops.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    
    /**
     * 创建用户
     */
    @Transactional
    public UserDTO createUser(CreateUserRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw BusinessException.of("用户名已存在");
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword()); // 实际项目中应加密
        user.setEmail(request.getEmail());
        user.setRealName(request.getRealName());
        user.setPhone(request.getPhone());
        user.setRole(UserRole.valueOf(request.getRole()));
        user.setEnabled(true);
        
        user = userRepository.save(user);
        log.info("创建用户成功: {}", user.getUsername());
        
        return convertToDTO(user);
    }
    
    /**
     * 更新用户
     */
    @Transactional
    public UserDTO updateUser(Long id, UpdateUserRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("用户不存在"));
        
        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }
        if (request.getRealName() != null) {
            user.setRealName(request.getRealName());
        }
        if (request.getPhone() != null) {
            user.setPhone(request.getPhone());
        }
        if (request.getRole() != null) {
            user.setRole(UserRole.valueOf(request.getRole()));
        }
        if (request.getEnabled() != null) {
            user.setEnabled(request.getEnabled());
        }
        
        user = userRepository.save(user);
        log.info("更新用户成功: {}", user.getUsername());
        
        return convertToDTO(user);
    }
    
    /**
     * 删除用户
     */
    @Transactional
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("用户不存在"));
        
        userRepository.delete(user);
        log.info("删除用户成功: {}", user.getUsername());
    }
    
    /**
     * 获取用户详情
     */
    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> BusinessException.of("用户不存在"));
        return convertToDTO(user);
    }
    
    /**
     * 获取所有用户
     */
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * 分页查询用户
     */
    public Page<UserDTO> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    /**
     * 转换为DTO
     */
    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRealName(),
                user.getPhone(),
                user.getRole(),
                user.getEnabled(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}