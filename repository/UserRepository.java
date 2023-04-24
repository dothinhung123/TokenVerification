package com.go.tokenverification.repository;

import com.go.tokenverification.entity.UserEntity;
import com.go.tokenverification.model.security.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    Optional<UserEntity> findUserEntityByUsername(String username);

    @Query("select u from UserEntity u where u.username= :username and u.isActive= true")
    Optional<UserEntity> findActiveUserByUsername(@Param("username") String username);

    @Query("select u from UserEntity u where u.username= :username and u.isActive=false")
    Optional<UserEntity> findInactiveUserByUsername(@Param("username") String username);

    @Modifying
    @Query("update UserEntity u set u.isActive=true where u.username = :username and u.isActive=false")
    void activeUserByUsername(@Param("username") String username);
}
