package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelTransfortHourMm;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelTransfortHourMmRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelTransfortHourMm tsTravelTransfortHourMm = QTsTravelTransfortHourMm.tsTravelTransfortHourMm;

}
