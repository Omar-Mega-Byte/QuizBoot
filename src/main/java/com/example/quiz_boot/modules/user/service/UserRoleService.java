package com.example.quiz_boot.modules.user.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.quiz_boot.modules.user.model.UserRole;
import com.example.quiz_boot.modules.user.repository.UserRoleRepository;

/**
 * Service for user role operations
 */
@Service
public class UserRoleService {

    @Autowired
    private UserRoleRepository userRoleRepository;

    /**
     * Get roles for a user by user ID
     */
    public Set<String> getUserRoles(Long userId) {
        List<UserRole> userRoles = userRoleRepository.findByUserIdWithRole(userId);
        return userRoles.stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toSet());
    }

    /**
     * Get roles for a user by username
     */
    public Set<String> getUserRoles(String username) {
        List<UserRole> userRoles = userRoleRepository.findByUsernameWithRole(username);
        return userRoles.stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toSet());
    }

    /**
     * Check if user has a specific role
     */
    public boolean hasRole(String username, String roleName) {
        return getUserRoles(username).contains(roleName);
    }
}
