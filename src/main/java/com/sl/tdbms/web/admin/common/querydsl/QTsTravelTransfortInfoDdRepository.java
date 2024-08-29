package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelTransfortInfoDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelTransfortInfoDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelTransfortInfoDd tsTravelTransfortInfoDd = QTsTravelTransfortInfoDd.tsTravelTransfortInfoDd;
}
