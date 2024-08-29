package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelUseHighwayOnhr;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelUseHighwayOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelUseHighwayOnhr tsTravelUseHighwayOnhr = QTsTravelUseHighwayOnhr.tsTravelUseHighwayOnhr;
}
