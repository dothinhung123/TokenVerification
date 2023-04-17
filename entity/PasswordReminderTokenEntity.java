package com.go.tokenverification.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordReminderTokenEntity {
    @Column(name = "PASSWORD_REMINDER_TOKEN")
    private String passwordReminderToken;

    @Column(name = "PASSWORD_REMINDER_TOKEN_GENERATION_TIME")
    private LocalDateTime passwordReminderTokenGeneratedAt;

    @Column(name="PASSWORD_REMINDER_EXPIRE")
    private int passwordReminderExpire;

}
