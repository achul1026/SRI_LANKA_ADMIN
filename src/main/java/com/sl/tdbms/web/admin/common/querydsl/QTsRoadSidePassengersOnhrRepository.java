package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QTsRoadSidePassengersOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	//private QTsRoadSidePassengersOnhr tsRoadSidePassengersOnhr = QTsRoadSidePassengersOnhr.tsRoadSidePassengersOnhr;
}
