package com.example.quiz_boot.modules.user.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.quiz_boot.modules.user.model.Role;
import com.example.quiz_boot.modules.user.repository.RoleRepository;

/**
 * Initializes default roles in the database
 */
@Component
public class RoleDataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRole("STUDENT", "Student role with basic quiz-taking permissions");
        initializeRole("TEACHER", "Teacher role with quiz management permissions");
        initializeRole("ADMIN", "Administrator role with full system access");
    }

    private void initializeRole(String roleName, String description) {
        if (roleRepository.findByName(roleName).isEmpty()) {
            Role role = new Role();
            role.setName(roleName);
            role.setDescription(description);
            roleRepository.save(role);
        }
    }
}
