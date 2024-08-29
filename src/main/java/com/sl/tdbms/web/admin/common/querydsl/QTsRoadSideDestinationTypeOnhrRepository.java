package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSideDestinationTypeOnhr;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSideDestinationTypeOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSideDestinationTypeOnhr tsRoadSideDestinationTypeOnhr = QTsRoadSideDestinationTypeOnhr.tsRoadSideDestinationTypeOnhr;
}
