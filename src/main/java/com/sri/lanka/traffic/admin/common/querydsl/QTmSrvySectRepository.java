package com.sri.lanka.traffic.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTmSrvySect;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmSrvySectRepository {

	private final JPAQueryFactory queryFactory;

	private QTmSrvySect tmSrvySect = QTmSrvySect.tmSrvySect;
}
