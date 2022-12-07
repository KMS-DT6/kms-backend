package com.backend.kmsproject.repository.dsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FootballPitchDslRepository {
//    private final QFootballPitchEntity footballPitch;
    private final JPAQueryFactory queryFactory;
}
