package com.example.quiz_boot.modules.user.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.quiz_boot.modules.shared.utils.JwtUtils;
import com.example.quiz_boot.modules.user.dto.request.LoginRequestDto;
import com.example.quiz_boot.modules.user.dto.request.UserCreateDto;
import com.example.quiz_boot.modules.user.dto.response.JwtResponseDto;
import com.example.quiz_boot.modules.user.dto.response.UserResponseDto;
import com.example.quiz_boot.modules.user.exception.DatabaseOperationException;
import com.example.quiz_boot.modules.user.exception.InvalidUserException;
import com.example.quiz_boot.modules.user.mapper.UserMapper;
import com.example.quiz_boot.modules.user.model.Role;
import com.example.quiz_boot.modules.user.model.User;
import com.example.quiz_boot.modules.user.model.UserRole;
import com.example.quiz_boot.modules.user.repository.RoleRepository;
import com.example.quiz_boot.modules.user.repository.UserRepository;
import com.example.quiz_boot.modules.user.repository.UserRoleRepository;
import com.example.quiz_boot.modules.user.validation.UserValidation;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserValidation userValidation;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;

    public AuthService(UserValidation userValidation, UserRepository userRepository,
            RoleRepository roleRepository, UserRoleRepository userRoleRepository,
            PasswordEncoder passwordEncoder, UserMapper userMapper, JwtUtils jwtUtils) {
        this.userValidation = userValidation;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
        this.jwtUtils = jwtUtils;
    }

    /**
     * Registers a new user and returns JWT token
     * 
     * @param userCreateDto DTO containing user registration data
     * @return DTO containing JWT token and user data
     */
    @Transactional
    public JwtResponseDto registerUser(UserCreateDto userCreateDto) {
        // Audit: Log registration attempt
        logger.info("AUDIT: User registration attempt - username: {}",
                userCreateDto != null ? userCreateDto.getUsername() : "null");

        if (userCreateDto == null) {
            logger.warn("AUDIT: Registration failed - null user data");
            throw new InvalidUserException("User registration data cannot be null");
        }

        // Validate user data
        List<String> validationErrors = userValidation.validateUserCreation(userCreateDto);
        if (userValidation.hasErrors(validationErrors)) {
            logger.warn("AUDIT: Registration failed - validation errors for username '{}': {}",
                    userCreateDto.getUsername(), validationErrors);
            throw new InvalidUserException(userValidation.formatErrors(validationErrors));
        }

        try {
            // Create normalized and encoded user data (preserving immutability)
            UserCreateDto normalizedUser = new UserCreateDto(
                    userCreateDto.getUsername().trim().toLowerCase(),
                    userCreateDto.getEmail().trim().toLowerCase(),
                    passwordEncoder.encode(userCreateDto.getPassword()),
                    userCreateDto.getFirstName().trim(),
                    userCreateDto.getLastName().trim(),
                    userCreateDto.getRole() != null ? userCreateDto.getRole().toUpperCase() : "STUDENT");

            // Convert to entity and save
            User newUser = userMapper.toEntity(normalizedUser);
            User savedUser = userRepository.save(newUser);

            // Assign role to user
            String roleName = normalizedUser.getRole();
            Role role = roleRepository.findByName(roleName)
                    .orElseThrow(() -> new InvalidUserException("Invalid role: " + roleName));

            UserRole userRole = new UserRole();
            userRole.setUser(savedUser);
            userRole.setRole(role);
            userRoleRepository.save(userRole);

            // Generate JWT token
            String jwt = jwtUtils.generateJwtToken(savedUser.getUsername());

            // Audit: Log successful registration
            logger.info("AUDIT: User registration successful - ID: {}, username: {}, email: {}, role: {}",
                    savedUser.getId(), savedUser.getUsername(), savedUser.getEmail(), roleName);

            UserResponseDto userResponse = userMapper.toResponseDto(savedUser);
            return JwtResponseDto.of(jwt, userResponse);

        } catch (org.springframework.dao.DataAccessException e) {
            // Audit: Log database operation failure
            logger.error("AUDIT: Database operation failed during user registration - username: {}, error: {}",
                    userCreateDto.getUsername(), e.getMessage());
            throw new DatabaseOperationException("Database operation failed during registration", e);
        } catch (Exception e) {
            // Audit: Log general registration failure
            logger.error("AUDIT: User registration failed - username: {}, error: {}",
                    userCreateDto.getUsername(), e.getMessage());
            throw new InvalidUserException("Registration failed: " + e.getMessage());
        }
    }

    /**
     * Authenticates a user and returns JWT token with user data.
     *
     * @param loginRequest The login request containing username and password
     * @return JWT response with token and user data
     */
    public JwtResponseDto loginUser(LoginRequestDto loginRequest) {
        // Audit: Log login attempt
        logger.info("AUDIT: User login attempt - username: {}",
                loginRequest != null && loginRequest.getUsername() != null ? loginRequest.getUsername() : "null");

        if (loginRequest == null) {
            logger.warn("AUDIT: Login failed - null login request");
            throw new InvalidUserException("Login request cannot be null");
        }

        try {
            // Step 1: Validate credentials
            userValidation.validateLoginCredentials(loginRequest.getUsername(), loginRequest.getPassword());

            // Step 2: Normalize username and fetch user
            String normalizedUsername = loginRequest.getUsername().trim().toLowerCase();
            User user = userRepository.findByUsername(normalizedUsername)
                    .orElseThrow(() -> {
                        logger.warn("AUDIT: Login failed - user not found for username '{}'", normalizedUsername);
                        return new InvalidUserException("Invalid username or password");
                    });

            // Step 3: Verify password
            if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                logger.warn("AUDIT: Login failed - incorrect password for username '{}'", normalizedUsername);
                throw new InvalidUserException("Invalid username or password");
            }

            // Step 4: Generate JWT token
            String jwt = jwtUtils.generateJwtToken(user.getUsername());

            // Step 5: Audit successful login
            logger.info("AUDIT: User login successful - ID: {}, username: {}", user.getId(), user.getUsername());

            UserResponseDto userResponse = userMapper.toResponseDto(user);
            return JwtResponseDto.of(jwt, userResponse);

        } catch (DataAccessException e) {
            logger.error("AUDIT: Database operation failed during user login - username: {}, error: {}",
                    loginRequest.getUsername(), e.getMessage());
            throw new DatabaseOperationException("Database operation failed during login", e);
        } catch (InvalidUserException e) {
            throw e; // Already logged
        } catch (Exception e) {
            logger.error("AUDIT: User login failed - username: {}, error: {}",
                    loginRequest.getUsername(), e.getMessage());
            throw new InvalidUserException("Login failed: " + e.getMessage());
        }
    }

    /**
     * Authenticates a user and returns their data (legacy method for backward
     * compatibility).
     *
     * @param username The username of the user
     * @param password The password of the user
     * @return DTO containing the authenticated user data
     * @deprecated Use {@link #loginUser(LoginRequestDto)} instead
     */
    @Deprecated
    public UserResponseDto loginUser(String username, String password) {
        LoginRequestDto loginRequest = new LoginRequestDto(username, password);
        JwtResponseDto jwtResponse = loginUser(loginRequest);
        return jwtResponse.getUser();
    }
}
