package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSidePassageTypeMm;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSidePassageTypeMmRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSidePassageTypeMm tsRoadSidePassageTypeMm = QTsRoadSidePassageTypeMm.tsRoadSidePassageTypeMm; 
	
}
