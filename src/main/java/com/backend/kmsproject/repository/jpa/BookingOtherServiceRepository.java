package com.backend.kmsproject.repository.jpa;

import com.backend.kmsproject.model.entity.BookingOtherServiceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingOtherServiceRepository extends JpaRepository<BookingOtherServiceEntity, Long> {
    @Query("SELECT bo FROM BookingOtherServiceEntity bo" +
            " LEFT JOIN FETCH bo.otherService" +
            " LEFT JOIN FETCH bo.booking" +
            " WHERE bo.booking.bookingId = :bookingId")
    List<BookingOtherServiceEntity> findByBookingId(@Param("bookingId") Long bookingId);
}
