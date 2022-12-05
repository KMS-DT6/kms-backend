package com.backend.kmsproject.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
public class DslQueryConfig {
    @Bean
    public JPAQueryFactory jpaQueryBuilder(EntityManager manager) {
        return new JPAQueryFactory(manager);
    }
}
