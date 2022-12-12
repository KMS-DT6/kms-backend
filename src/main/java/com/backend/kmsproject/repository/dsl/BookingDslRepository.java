package com.backend.kmsproject.repository.dsl;

import com.backend.kmsproject.common.enums.KmsRole;
import com.backend.kmsproject.model.entity.*;
import com.backend.kmsproject.request.booking.GetListBookingRequest;
import com.backend.kmsproject.request.myaccount.GetListHistoryBookingRequest;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BookingDslRepository {
    private final QBookingEntity booking = QBookingEntity.bookingEntity;
    private final QUserEntity user = QUserEntity.userEntity;
    private final QFootballPitchEntity footballPitch = QFootballPitchEntity.footballPitchEntity;
    private final JPAQueryFactory queryFactory;

    public List<BookingEntity> listBookingByUserId(GetListHistoryBookingRequest request, Long userId) {
        JPAQuery<BookingEntity> query = queryFactory.select(booking)
                .from(booking)
                .innerJoin(user).on(booking.customer.userId.eq(user.userId))
                .innerJoin(footballPitch).on(booking.subFootballPitch.footballPitch
                        .footballPitchId.eq(footballPitch.footballPitchId))
                .where(user.userId.eq(userId))
                .where(user.role.roleId.eq(KmsRole.CUSTOMER_ROLE.getRoleId()));
        if (StringUtils.hasText(request.getFootballPitchName())) {
            query.where(footballPitch.footballPitchName.containsIgnoreCase(request.getFootballPitchName()));
        }
        if (StringUtils.hasText(request.getFromDate()) && StringUtils.hasText(request.getToDate())
                && LocalDate.parse(request.getFromDate()).isBefore(LocalDate.parse(request.getToDate()))) {
            query.where(booking.bookDay.between(LocalDate.parse(request.getFromDate()),
                    LocalDate.parse(request.getToDate())));
        }
        if (request.getStatus() != null) {
            query.where(booking.status.eq(request.getStatus()));
        }
        if (request.getIsPaid() != null) {
            query.where(booking.isPaid.eq(request.getIsPaid()));
        }
        return query.fetch();
    }

    public List<BookingEntity> listBookingByFootBallPitch(GetListBookingRequest request, Long footballPitchId){
        JPAQuery<BookingEntity> query = queryFactory.select(booking)
                .from(booking)
                .innerJoin(user).on(booking.customer.userId.eq(user.userId))
                .innerJoin(footballPitch).on(booking.subFootballPitch.footballPitch
                        .footballPitchId.eq(footballPitch.footballPitchId))
                .where(footballPitch.footballPitchId.eq(footballPitchId));
        if (request.getSubFootballPitchId() != null) {
            query.where(booking.subFootballPitch.subFootBallPitchId.eq(request.getSubFootballPitchId()));
        }
        if (StringUtils.hasText(request.getFromDate()) && StringUtils.hasText(request.getToDate())
                && LocalDate.parse(request.getFromDate()).isBefore(LocalDate.parse(request.getToDate()))) {
            query.where(booking.bookDay.between(LocalDate.parse(request.getFromDate()),
                    LocalDate.parse(request.getToDate())));
        }
        if (request.getStatus() != null) {
            query.where(booking.status.eq(request.getStatus()));
        }
        if (request.getIsPaid() != null) {
            query.where(booking.isPaid.eq(request.getIsPaid()));
        }
        return query.fetch();
    }
}
