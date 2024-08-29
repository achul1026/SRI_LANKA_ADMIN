package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelDepartureTypeDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelDepartureTypeDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelDepartureTypeDd tsTravelDepartureTypeDd = QTsTravelDepartureTypeDd.tsTravelDepartureTypeDd;
}
