package com.backend.kmsproject.repository.jpa;

import com.backend.kmsproject.model.entity.OtherServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtherServiceRepository extends JpaRepository<OtherServiceEntity, Long> {
    @Query("SELECT os FROM OtherServiceEntity os" +
            " LEFT JOIN FETCH os.footballPitch" +
            " WHERE os.otherServiceId = :otherServiceId")
    Optional<OtherServiceEntity> findByOtherId(@Param("otherServiceId") Long otherServiceId);

    Optional<OtherServiceEntity> findByNameAndFootballPitchId(@Param("otherServiceName") String otherServiceName,
                                                              @Param("footballPitchId") Long footballPitchId);
}
