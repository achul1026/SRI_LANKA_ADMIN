package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSideDepartureTimeDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSideDepartureTimeDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSideDepartureTimeDd tsRoadSideDepartureTimeDd = QTsRoadSideDepartureTimeDd.tsRoadSideDepartureTimeDd;
}
