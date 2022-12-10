package com.backend.kmsproject.repository.jpa;

import com.backend.kmsproject.model.entity.FootballPitchEntity;
import com.backend.kmsproject.model.entity.SubFootballPitchEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubFootballPitchRepository extends JpaRepository<SubFootballPitchEntity, Long> {
        @Query("SELECT sfp FROM SubFootballPitchEntity sfp" +

                        " WHERE sfp.subFootBallPitchId = :subFootballPitchId")
        Optional<SubFootballPitchEntity> findBySubFootballPitchId(@Param("subFootballPitchId") Long subFootballPitchId);

        @Query("SELECT sfp FROM SubFootballPitchEntity sfp" +

                        " WHERE sfp.footballPitch.footballPitchId = :footballPitchId")
        List<SubFootballPitchEntity> getListSubFootballPitch(@Param("footballPitchId") Long footballPitchId);

}