package com.backend.kmsproject.repository.dsl;

import com.backend.kmsproject.model.entity.QSubFootballPitchEntity;
import com.backend.kmsproject.model.entity.SubFootballPitchEntity;
import com.backend.kmsproject.request.subfootballpitch.GetListSubFootballPitchRequest;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class SubFootballPitchDslRepository {
    private final QSubFootballPitchEntity subFootballPitch = QSubFootballPitchEntity.subFootballPitchEntity;
    private final JPAQueryFactory queryFactory;

    public List<SubFootballPitchEntity> listSubFootballPitch(GetListSubFootballPitchRequest request) {
        JPAQuery<SubFootballPitchEntity> query = queryFactory.select(subFootballPitch)
                .from(subFootballPitch)
                .where(subFootballPitch.footballPitch.footballPitchId.eq(request.getFootballPitchId()));
        if (StringUtils.hasText(request.getSubFootballPitchName())) {
            query.where(subFootballPitch.subFootballPitchName.containsIgnoreCase(request.getSubFootballPitchName()));
        }
        if (request.getStatus() != null) {
            query.where(subFootballPitch.status.eq(request.getStatus()));
        }
        if (request.getSize() != null && request.getSize() > 0) {
            query.where(subFootballPitch.size.eq(request.getSize()));
        }
        return query.fetch();
    }
}
