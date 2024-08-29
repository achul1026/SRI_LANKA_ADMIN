package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSideDepartureTypeOnhr;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSideDepartureTypeOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSideDepartureTypeOnhr tsRoadSideDepartureTypeOnhr = QTsRoadSideDepartureTypeOnhr.tsRoadSideDepartureTypeOnhr;
}
