package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelTransfortHourDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelTransfortHourDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelTransfortHourDd tsTravelTransfortHourDd = QTsTravelTransfortHourDd.tsTravelTransfortHourDd;

}
