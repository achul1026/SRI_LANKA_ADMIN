package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelTransfortHourOnhr;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class QTsTravelTransfortHourOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelTransfortHourOnhr tsTravelTransfortHourOnhr = QTsTravelTransfortHourOnhr.tsTravelTransfortHourOnhr;

}
