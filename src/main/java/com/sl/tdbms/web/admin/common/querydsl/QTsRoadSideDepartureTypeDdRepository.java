package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSideDepartureTypeDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSideDepartureTypeDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSideDepartureTypeDd tsRoadSideDepartureTypeDd = QTsRoadSideDepartureTypeDd.tsRoadSideDepartureTypeDd;
}