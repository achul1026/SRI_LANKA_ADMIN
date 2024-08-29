package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSidePassageTypeDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSidePassageTypeDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSidePassageTypeDd tsRoadSidePassageTypeDd = QTsRoadSidePassageTypeDd.tsRoadSidePassageTypeDd; 
}
