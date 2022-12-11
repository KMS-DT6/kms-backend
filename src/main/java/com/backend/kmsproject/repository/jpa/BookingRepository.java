package com.backend.kmsproject.repository.jpa;

import com.backend.kmsproject.model.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingRepository extends JpaRepository<BookingEntity,Long> {
}
