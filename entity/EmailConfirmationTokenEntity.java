package com.go.tokenverification.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Entity
@Table(name = "EMAIL_CONFIRMATION_TOKEN")
public class EmailConfirmationTokenEntity {

    @Id
    @GeneratedValue
    @Column(name = "EMAIL_CONFIRMATION_TOKEN_ID")
    private Long id;

    @Column(name="TOKEN",columnDefinition = "TEXT")
    private String token;

    @ManyToOne
    @JoinColumn(name = "USER_ID",nullable = false)
    private UserEntity user;

}
