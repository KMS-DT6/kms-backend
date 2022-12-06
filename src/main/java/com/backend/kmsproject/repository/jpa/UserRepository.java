package com.backend.kmsproject.repository.jpa;

import com.backend.kmsproject.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u" +
            " LEFT JOIN FETCH u.role" +
            " LEFT JOIN FETCH u.footballPitch" +
            " LEFT JOIN FETCH u.address" +
            " WHERE u.username = :username")
    Optional<UserEntity> findByUsername(@Param("username") String username);

    @Query("SELECT u FROM UserEntity u" +
            " LEFT JOIN FETCH u.role" +
            " LEFT JOIN FETCH u.footballPitch" +
            " LEFT JOIN FETCH u.address" +
            " WHERE u.userId = :userId")
    Optional<UserEntity> findByUserId(@Param("userId") Long userId);

    @Query("SELECT u FROM UserEntity u" +
            " WHERE u.phoneNumber = :phoneNumber")
    Optional<UserEntity> findByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
