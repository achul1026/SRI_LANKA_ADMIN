package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelDestinationTypeOnhr;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelDestinationTypeOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelDestinationTypeOnhr tsTravelDestinationTypeOnhr = QTsTravelDestinationTypeOnhr.tsTravelDestinationTypeOnhr;
}
