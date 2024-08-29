package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelDepartureTimeMm;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelDepartureTimeMmRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelDepartureTimeMm tsTravelDepartureTimeMm = QTsTravelDepartureTimeMm.tsTravelDepartureTimeMm;
}
