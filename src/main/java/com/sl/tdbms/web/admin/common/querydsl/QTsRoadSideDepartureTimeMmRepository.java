package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSideDepartureTimeMm;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSideDepartureTimeMmRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSideDepartureTimeMm tsRoadSideDepartureTimeMm = QTsRoadSideDepartureTimeMm.tsRoadSideDepartureTimeMm;
}
