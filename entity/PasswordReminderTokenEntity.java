package com.go.tokenverification.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "PASSWORD_REMINDER_TOKEN")
public class PasswordReminderTokenEntity {
    @Id
    @GeneratedValue
    @Column(name = "PASSWORD_REMINDER_TOKEN_ID")
    private Long id;

    @Column(name = "TOKEN", columnDefinition = "TEXT")
    private String token;

    @ManyToOne
    @JoinColumn(name = "USER_ID",nullable = false)
    private UserEntity user;
}
