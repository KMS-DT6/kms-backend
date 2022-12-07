package com.backend.kmsproject.repository.jpa;

import com.backend.kmsproject.model.entity.FootballPitchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FootballPitchRepository extends JpaRepository<FootballPitchEntity, Long> {
    @Query("SELECT fp FROM FootballPitchEntity fp" +
            " LEFT JOIN FETCH fp.address" +
            " WHERE fp.footballPitchId = :footballPitchId")
    Optional<FootballPitchEntity> findByFootballPitchId(@Param("footballPitchId") Long footballPitchId);

    @Query("SELECT fp FROM FootballPitchEntity fp" +
            " LEFT JOIN FETCH fp.address a" +
            " WHERE fp.footballPitchName = :footballPitchName" +
            " AND a.address = :address")
    Optional<FootballPitchEntity> findByNameAndAddress(@Param("footballPitchName") String footballPitchName, @Param("address") String address);
}
