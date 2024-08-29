package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSideDepartureTypeMm;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSideDepartureTypeMmRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSideDepartureTypeMm tsRoadSideDepartureTypeMm = QTsRoadSideDepartureTypeMm.tsRoadSideDepartureTypeMm;
}
