package com.backend.kmsproject.repository.dsl;

import com.backend.kmsproject.common.enums.KmsRole;
import com.backend.kmsproject.model.entity.QUserEntity;
import com.backend.kmsproject.model.entity.UserEntity;
import com.backend.kmsproject.request.footballpitchadmin.GetListFootballPitchAdminRequest;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.beans.Expression;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserDslRepository {
    private final QUserEntity user = QUserEntity.userEntity;
    private final JPAQueryFactory queryFactory;

    public List<UserEntity> listFootballPitchAdmin(GetListFootballPitchAdminRequest request) {
        JPAQuery<UserEntity> query = queryFactory.select(user)
                .from(user)
                .where(user.role.roleId.eq(KmsRole.FOOTBALL_PITCH_ROLE.getRoleId()));
        if (request.getFootballPitchId() != null && request.getFootballPitchId() > 0) {
            query.where(user.footballPitch.footballPitchId.eq(request.getFootballPitchId()));
        }
        if (StringUtils.hasText(request.getContextSearch())) {
            query.where(user.username.containsIgnoreCase(request.getContextSearch())
                    .or(user.firstName.concat(" ").concat(user.lastName)
                            .containsIgnoreCase(request.getContextSearch()))
                    .or(user.phoneNumber.containsIgnoreCase(request.getContextSearch())));
        }
        return query.fetch();
    }
}
