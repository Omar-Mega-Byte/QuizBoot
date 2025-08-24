package com.example.quiz_boot.modules.user.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.example.quiz_boot.modules.user.dto.request.UserCreateDto;
import com.example.quiz_boot.modules.user.dto.request.UserUpdateDto;
import com.example.quiz_boot.modules.user.repository.UserRepository;

/**
 * Ultimate User Validation Class
 * Provides comprehensive validation for all user-related operations
 * Includes business rules, security validations, and data integrity checks
 */
@Component
public class UserValidation {

    @Autowired
    private UserRepository userRepository;

    // Regex patterns for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");

    private static final Pattern USERNAME_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9._-]{3,20}$");

    private static final Pattern PASSWORD_PATTERN = Pattern.compile(
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{8,}$");

    private static final Pattern NAME_PATTERN = Pattern.compile(
            "^[a-zA-Z\\s]{2,30}$");

    // Forbidden usernames for security
    private static final List<String> FORBIDDEN_USERNAMES = List.of(
            "admin", "administrator", "root", "superuser", "moderator", "mod",
            "user", "test", "guest", "demo", "api", "system", "null", "undefined");

    /**
     * Validates UserCreateDto for user registration
     * 
     * @param dto The user creation data
     * @return List of validation errors (empty if valid)
     */
    public List<String> validateUserCreation(UserCreateDto dto) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("User data cannot be null");
            return errors;
        }

        // Validate username
        errors.addAll(validateUsername(dto.getUsername(), true));

        // Validate email
        errors.addAll(validateEmail(dto.getEmail(), true));

        // Validate password
        errors.addAll(validatePassword(dto.getPassword()));

        // Validate names
        errors.addAll(validateFirstName(dto.getFirstName()));
        errors.addAll(validateLastName(dto.getLastName()));

        return errors;
    }

    /**
     * Validates UserUpdateDto for user profile updates
     * 
     * @param dto           The user update data
     * @param currentUserId The ID of the user being updated
     * @return List of validation errors (empty if valid)
     */
    public List<String> validateUserUpdate(UserUpdateDto dto, Long currentUserId) {
        List<String> errors = new ArrayList<>();

        if (dto == null) {
            errors.add("Update data cannot be null");
            return errors;
        }

        // Validate email if provided
        if (dto.getEmail() != null) {
            errors.addAll(validateEmail(dto.getEmail(), true));

            // Check if email is already taken by another user
            if (userRepository.existsByEmailAndIdNot(dto.getEmail(), currentUserId)) {
                errors.add("Email is already taken by another user");
            }
        }

        // Validate names if provided
        if (dto.getFirstName() != null) {
            errors.addAll(validateFirstName(dto.getFirstName()));
        }

        if (dto.getLastName() != null) {
            errors.addAll(validateLastName(dto.getLastName()));
        }

        return errors;
    }

    /**
     * Validates username with comprehensive rules
     */
    public List<String> validateUsername(String username, boolean checkUniqueness) {
        List<String> errors = new ArrayList<>();

        if (username == null || username.trim().isEmpty()) {
            errors.add("Username is required");
            return errors;
        }

        username = username.trim().toLowerCase();

        // Length validation
        if (username.length() < 3) {
            errors.add("Username must be at least 3 characters long");
        }
        if (username.length() > 20) {
            errors.add("Username cannot exceed 20 characters");
        }

        // Pattern validation
        if (!USERNAME_PATTERN.matcher(username).matches()) {
            errors.add("Username can only contain letters, numbers, dots, underscores, and hyphens");
        }

        // Cannot start or end with special characters
        if (username.startsWith(".") || username.startsWith("_") || username.startsWith("-") ||
                username.endsWith(".") || username.endsWith("_") || username.endsWith("-")) {
            errors.add("Username cannot start or end with special characters");
        }

        // No consecutive special characters
        if (username.contains("..") || username.contains("__") || username.contains("--")) {
            errors.add("Username cannot contain consecutive special characters");
        }

        // Forbidden usernames
        if (FORBIDDEN_USERNAMES.contains(username)) {
            errors.add("This username is not allowed");
        }

        // Uniqueness check
        if (checkUniqueness && userRepository.existsByUsername(username)) {
            errors.add("Username is already taken");
        }

        return errors;
    }

    /**
     * Validates email with format and uniqueness checks
     */
    public List<String> validateEmail(String email, boolean checkUniqueness) {
        List<String> errors = new ArrayList<>();

        if (email == null || email.trim().isEmpty()) {
            errors.add("Email is required");
            return errors;
        }

        email = email.trim().toLowerCase();

        // Length validation
        if (email.length() > 100) {
            errors.add("Email cannot exceed 100 characters");
        }

        // Format validation
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            errors.add("Invalid email format");
        }

        // Domain validation
        String[] parts = email.split("@");
        if (parts.length == 2) {
            String domain = parts[1];
            if (domain.startsWith(".") || domain.endsWith(".") || domain.contains("..")) {
                errors.add("Invalid email domain");
            }
        }

        // Uniqueness check
        if (checkUniqueness && userRepository.existsByEmail(email)) {
            errors.add("Email is already registered");
        }

        return errors;
    }

    /**
     * Validates login credentials
     */
    public List<String> validateLoginCredentials(String username, String password) {
        List<String> errors = new ArrayList<>();

        if (username == null || username.trim().isEmpty()) {
            errors.add("Username is required");
        }

        if (password == null || password.isEmpty()) {
            errors.add("Password is required");
        }

        return errors;
    }

    /**
     * Validates password with security requirements
     */
    public List<String> validatePassword(String password) {
        List<String> errors = new ArrayList<>();

        if (password == null || password.isEmpty()) {
            errors.add("Password is required");
            return errors;
        }

        // Length validation
        if (password.length() < 8) {
            errors.add("Password must be at least 8 characters long");
        }
        if (password.length() > 128) {
            errors.add("Password cannot exceed 128 characters");
        }

        // Complexity validation
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            errors.add(
                    "Password must contain at least one uppercase letter, one lowercase letter, one number, and one special character (@$!%*?&_)");
        }

        // Common password check
        if (isCommonPassword(password)) {
            errors.add("Password is too common, please choose a more secure password");
        }

        // No whitespace
        if (password.contains(" ")) {
            errors.add("Password cannot contain spaces");
        }

        return errors;
    }

    /**
     * Validates first name
     */
    public List<String> validateFirstName(String firstName) {
        List<String> errors = new ArrayList<>();

        if (firstName == null || firstName.trim().isEmpty()) {
            errors.add("First name is required");
            return errors;
        }

        firstName = firstName.trim();

        if (firstName.length() < 2) {
            errors.add("First name must be at least 2 characters long");
        }
        if (firstName.length() > 30) {
            errors.add("First name cannot exceed 30 characters");
        }

        if (!NAME_PATTERN.matcher(firstName).matches()) {
            errors.add("First name can only contain letters and spaces");
        }

        return errors;
    }

    /**
     * Validates last name
     */
    public List<String> validateLastName(String lastName) {
        List<String> errors = new ArrayList<>();

        if (lastName == null || lastName.trim().isEmpty()) {
            errors.add("Last name is required");
            return errors;
        }

        lastName = lastName.trim();

        if (lastName.length() < 2) {
            errors.add("Last name must be at least 2 characters long");
        }
        if (lastName.length() > 30) {
            errors.add("Last name cannot exceed 30 characters");
        }

        if (!NAME_PATTERN.matcher(lastName).matches()) {
            errors.add("Last name can only contain letters and spaces");
        }

        return errors;
    }

    /**
     * Validates password confirmation for password changes
     */
    public List<String> validatePasswordConfirmation(String password, String confirmPassword) {
        List<String> errors = new ArrayList<>();

        if (confirmPassword == null || confirmPassword.isEmpty()) {
            errors.add("Password confirmation is required");
            return errors;
        }

        if (!password.equals(confirmPassword)) {
            errors.add("Passwords do not match");
        }

        return errors;
    }

    /**
     * Checks if the validation has any errors
     */
    public boolean hasErrors(List<String> errors) {
        return errors != null && !errors.isEmpty();
    }

    /**
     * Formats validation errors into a single string
     */
    public String formatErrors(List<String> errors) {
        if (errors == null || errors.isEmpty()) {
            return "";
        }
        return String.join("; ", errors);
    }

    /**
     * Checks if password is in common passwords list
     */
    private boolean isCommonPassword(String password) {
        List<String> commonPasswords = List.of(
                "password", "123456", "password123", "admin", "qwerty",
                "letmein", "welcome", "monkey", "dragon", "pass",
                "Password1", "123456789", "12345678", "1234567890");
        return commonPasswords.contains(password);
    }

    /**
     * Advanced email validation with disposable email detection
     */
    public boolean isDisposableEmail(String email) {
        if (email == null)
            return false;

        String domain = email.split("@")[1].toLowerCase();
        List<String> disposableDomains = List.of(
                "10minutemail.com", "guerrillamail.com", "mailinator.com",
                "tempmail.org", "throwaway.email", "temp-mail.org");

        return disposableDomains.contains(domain);
    }

    /**
     * Validates if username contains inappropriate content
     */
    public boolean containsInappropriateContent(String username) {
        if (username == null)
            return false;

        List<String> inappropriateWords = List.of(
                "spam", "fake", "bot", "test123", "delete");

        String lowerUsername = username.toLowerCase();
        return inappropriateWords.stream().anyMatch(lowerUsername::contains);
    }
}
