package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelUseHighwayDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelUseHighwayDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelUseHighwayDd tsTravelUseHighwayDd = QTsTravelUseHighwayDd.tsTravelUseHighwayDd;
}
