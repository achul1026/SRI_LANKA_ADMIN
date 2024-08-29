package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelTransfortInfoOnhr;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelTransfortInfoOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelTransfortInfoOnhr tsTravelTransfortInfoOnhr = QTsTravelTransfortInfoOnhr.tsTravelTransfortInfoOnhr;
}
