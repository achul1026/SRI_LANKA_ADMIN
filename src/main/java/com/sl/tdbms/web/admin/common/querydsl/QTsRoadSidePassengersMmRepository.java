package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QTsRoadSidePassengersMmRepository {

	private final JPAQueryFactory queryFactory;
	
	//private QTsRoadSidePassengersMm tsRoadSidePassengersMm = QTsRoadSidePassengersMm.tsRoadSidePassengersMm;
}
