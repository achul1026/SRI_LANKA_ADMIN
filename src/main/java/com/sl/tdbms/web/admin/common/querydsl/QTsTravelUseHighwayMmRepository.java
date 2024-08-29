package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelUseHighwayMm;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelUseHighwayMmRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelUseHighwayMm tsTravelUseHighwayMm = QTsTravelUseHighwayMm.tsTravelUseHighwayMm;
}
