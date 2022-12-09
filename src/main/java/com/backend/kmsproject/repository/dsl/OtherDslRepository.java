package com.backend.kmsproject.repository.dsl;

import com.backend.kmsproject.model.entity.OtherServiceEntity;
import com.backend.kmsproject.model.entity.QOtherServiceEntity;
import com.backend.kmsproject.request.otherservice.GetListOtherServiceRequest;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OtherDslRepository {
    private final QOtherServiceEntity otherService = QOtherServiceEntity.otherServiceEntity;
    private final JPAQueryFactory queryFactory;

    public List<OtherServiceEntity> getListOtherService(GetListOtherServiceRequest request) {
        JPAQuery<OtherServiceEntity> query = queryFactory.select(otherService)
                .from(otherService)
                .where(otherService.footballPitch.footballPitchId.eq(request.getFootballPitchId()));
        if (StringUtils.hasText(request.getOtherServiceName())) {
            query.where(otherService.otherServiceName.containsIgnoreCase(request.getOtherServiceName()));
        }
        if (request.getStatus() != null) {
            query.where(otherService.status.eq(request.getStatus()));
        }
        if (request.getFrom() != null && request.getFrom() > 0) {
            query.where(otherService.pricePerOne.goe(request.getFrom())
                    .or(otherService.pricePerHour.goe(request.getFrom())));
        }
        if (request.getTo() != null && request.getTo() > 0) {
            query.where(otherService.pricePerOne.loe(request.getTo())
                    .or(otherService.pricePerHour.loe(request.getTo())));
        }
        return query.fetch();
    }
}
