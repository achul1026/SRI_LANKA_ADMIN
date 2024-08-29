package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelPurposeOnhr;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelPurposeOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelPurposeOnhr tsTravelPurposeOnhr = QTsTravelPurposeOnhr.tsTravelPurposeOnhr;
}
