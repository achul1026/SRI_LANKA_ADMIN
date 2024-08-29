package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSideDepartureTimeOnhr;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSideDepartureTimeOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSideDepartureTimeOnhr tsRoadSideDepartureTimeOnhr = QTsRoadSideDepartureTimeOnhr.tsRoadSideDepartureTimeOnhr;
}
