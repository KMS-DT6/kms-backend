package com.backend.kmsproject.repository.dsl;

import com.backend.kmsproject.model.entity.FootballPitchEntity;
import com.backend.kmsproject.model.entity.QFootballPitchEntity;
import com.backend.kmsproject.request.footballpitch.GetListFootballPitchRequest;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FootballPitchDslRepository {
    private final QFootballPitchEntity footballPitch = QFootballPitchEntity.footballPitchEntity;
    private final JPAQueryFactory queryFactory;

    public List<FootballPitchEntity> listFootballPitch(GetListFootballPitchRequest request) {
        JPAQuery<FootballPitchEntity> query = queryFactory.select(footballPitch)
                .from(footballPitch);
        if (StringUtils.hasText(request.getFootballPitchName())) {
            query.where(footballPitch.footballPitchName.containsIgnoreCase(request.getFootballPitchName()));
        }
        if (StringUtils.hasText(request.getDistrict())) {
            query.where(footballPitch.address.district.containsIgnoreCase(request.getDistrict()));
        }
        if (StringUtils.hasText(request.getCity())) {
            query.where(footballPitch.address.city.containsIgnoreCase(request.getCity()));
        }
        query.leftJoin(footballPitch.address).fetch();
        return query.fetch();
    }
}
