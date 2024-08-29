package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelDepartureTypeOnhr;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelDepartureTypeOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelDepartureTypeOnhr tsTravelDepartureTypeOnhr = QTsTravelDepartureTypeOnhr.tsTravelDepartureTypeOnhr;
}
