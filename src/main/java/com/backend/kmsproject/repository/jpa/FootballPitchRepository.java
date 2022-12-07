package com.backend.kmsproject.repository.jpa;

import com.backend.kmsproject.model.entity.FootballPitchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FootballPitchRepository extends JpaRepository<FootballPitchEntity, Long> {
}
