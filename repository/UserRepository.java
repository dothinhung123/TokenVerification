package com.go.tokenverification.repository;

import com.go.tokenverification.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    public Optional<UserEntity> findUserEntityByUsername(String username);

}
