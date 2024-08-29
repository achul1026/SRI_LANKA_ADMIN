package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelDestinationTimeOnhr;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelDestinationTimeOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelDestinationTimeOnhr tsTravelDestinationTimeOnhr = QTsTravelDestinationTimeOnhr.tsTravelDestinationTimeOnhr;
}
