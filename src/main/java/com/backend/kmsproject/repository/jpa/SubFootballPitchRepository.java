package com.backend.kmsproject.repository.jpa;

import com.backend.kmsproject.model.entity.SubFootballPitchEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SubFootballPitchRepository extends JpaRepository<SubFootballPitchEntity, Long> {
    @Query("SELECT sfp FROM SubFootballPitchEntity sfp" +
            " LEFT JOIN FETCH sfp.footballPitch fp" +
            " WHERE lower(sfp.subFootballPitchName) = lower(:subFootballPitchName)" +
            " AND fp.footballPitchId = :footballPitchId")
    Optional<SubFootballPitchEntity> findByFootballPitchIdAndName(@Param("subFootballPitchName") String subFootballPitchName,
                                                                  @Param("footballPitchId") Long footballPitchId);
}