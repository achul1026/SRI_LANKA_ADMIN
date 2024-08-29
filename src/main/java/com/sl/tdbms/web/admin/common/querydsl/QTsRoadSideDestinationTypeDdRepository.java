package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSideDestinationTypeDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSideDestinationTypeDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSideDestinationTypeDd tsRoadSideDestinationTypeDd = QTsRoadSideDestinationTypeDd.tsRoadSideDestinationTypeDd;
}
