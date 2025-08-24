package com.example.quiz_boot.modules.user.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.quiz_boot.modules.user.dto.request.UserCreateDto;
import com.example.quiz_boot.modules.user.dto.response.UserResponseDto;
import com.example.quiz_boot.modules.user.exception.DatabaseOperationException;
import com.example.quiz_boot.modules.user.exception.InvalidUserException;
import com.example.quiz_boot.modules.user.mapper.UserMapper;
import com.example.quiz_boot.modules.user.model.User;
import com.example.quiz_boot.modules.user.repository.UserRepository;
import com.example.quiz_boot.modules.user.validation.UserValidation;

import jakarta.transaction.Transactional;

@Service
public class AuthService {
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserValidation userValidation;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public AuthService(UserValidation userValidation, UserRepository userRepository, PasswordEncoder passwordEncoder,
            UserMapper userMapper) {
        this.userValidation = userValidation;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * Registers a new user
     * 
     * @param userCreateDto DTO containing user registration data
     * @return DTO containing the registered user data
     */
    @Transactional
    public UserResponseDto registerUser(UserCreateDto userCreateDto) {
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
                    userCreateDto.getLastName().trim());

            // Convert to entity and save
            User newUser = userMapper.toEntity(normalizedUser);
            User savedUser = userRepository.save(newUser);

            // Audit: Log successful registration
            logger.info("AUDIT: User registration successful - ID: {}, username: {}, email: {}",
                    savedUser.getId(), savedUser.getUsername(), savedUser.getEmail());

            return userMapper.toResponseDto(savedUser);

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
     * Authenticates a user and returns their data.
     *
     * @param username The username of the user
     * @param password The password of the user
     * @return DTO containing the authenticated user data
     */
    public UserResponseDto loginUser(String username, String password) {
        // Audit: Log login attempt
        logger.info("AUDIT: User login attempt - username: {}", username != null ? username : "null");

        try {
            // Step 1: Validate credentials
            userValidation.validateLoginCredentials(username, password);

            // Step 2: Normalize username and fetch user
            String normalizedUsername = username.trim().toLowerCase();
            User user = userRepository.findByUsername(normalizedUsername)
                    .orElseThrow(() -> {
                        logger.warn("AUDIT: Login failed - user not found for username '{}'", normalizedUsername);
                        return new InvalidUserException("Invalid username or password");
                    });

            // Step 3: Verify password
            if (!passwordEncoder.matches(password, user.getPassword())) {
                logger.warn("AUDIT: Login failed - incorrect password for username '{}'", normalizedUsername);
                throw new InvalidUserException("Invalid username or password");
            }

            // Step 4: Audit successful login
            logger.info("AUDIT: User login successful - ID: {}, username: {}", user.getId(), user.getUsername());
            return userMapper.toResponseDto(user);

        } catch (DataAccessException e) {
            logger.error("AUDIT: Database operation failed during user login - username: {}, error: {}",
                    username, e.getMessage());
            throw new DatabaseOperationException("Database operation failed during login", e);
        } catch (InvalidUserException e) {
            throw e; // Already logged
        } catch (Exception e) {
            logger.error("AUDIT: User login failed - username: {}, error: {}", username, e.getMessage());
            throw new InvalidUserException("Login failed: " + e.getMessage());
        }
    }
}
