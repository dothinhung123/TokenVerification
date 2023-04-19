package com.go.tokenverification.repository;

import com.go.tokenverification.entity.EmailConfirmationTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailConfirmationTokenRepository extends JpaRepository<EmailConfirmationTokenEntity, Long> {

    Optional<EmailConfirmationTokenEntity> findByToken(String token);

}
