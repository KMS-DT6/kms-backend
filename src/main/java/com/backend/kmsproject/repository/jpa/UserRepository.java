package com.backend.kmsproject.repository.jpa;

import com.backend.kmsproject.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    @Query("SELECT u FROM UserEntity u" +
            " LEFT JOIN FETCH u.role r" +
            " LEFT JOIN FETCH u.footballPitch fp" +
            " WHERE u.username = :username")
    Optional<UserEntity> findByUsername(String username);
}
