package com.example.quiz_boot.modules.user.model;

import com.example.quiz_boot.shared.Base.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "users", indexes = {
                @jakarta.persistence.Index(name = "idx_username", columnList = "username"),
                @jakarta.persistence.Index(name = "idx_email", columnList = "email")
})
public class User extends BaseEntity {
        @Column(nullable = false, unique = true)
        private String username;

        @Column(nullable = false, unique = true)
        private String email;

        @Column(nullable = false)
        private String password;

        @Column(nullable = false)
        private String firstName;

        @Column(nullable = false)
        private String lastName;

        @Column(nullable = false)
        private boolean isActive;
}
