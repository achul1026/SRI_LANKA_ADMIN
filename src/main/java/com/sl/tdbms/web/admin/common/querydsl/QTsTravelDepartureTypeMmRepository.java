package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelDepartureTypeMm;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelDepartureTypeMmRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelDepartureTypeMm tsTravelDepartureTypeMm = QTsTravelDepartureTypeMm.tsTravelDepartureTypeMm;
}
