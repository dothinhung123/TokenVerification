package com.go.tokenverification.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.time.LocalDateTime;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor

@Accessors(chain = true)
public class EmailConfirmationTokenEntity {
    @Column(name="EMAIL_CONFIRMATION_TOKEN")
    private String emailConfirmationToken;

    @Column(name="EMAIL_CONFIRMATION_TOKEN_GENERATION_TIME")
    private LocalDateTime emailConfirmationTokenGeneratedAt;

    @Column(name="EMAIL_CONFIRMATION_EXPIRE")
    private LocalDateTime emailConfirmationExpire;
}
