package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelFeeInfoDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelFeeInfoDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelFeeInfoDd tsTravelFeeInfoDd = QTsTravelFeeInfoDd.tsTravelFeeInfoDd;
}
