package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelPurposeMm;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelPurposeMmRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelPurposeMm tsTravelPurposeMm = QTsTravelPurposeMm.tsTravelPurposeMm;
}
