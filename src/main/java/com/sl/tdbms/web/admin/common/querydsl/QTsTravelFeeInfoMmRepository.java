package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsTravelFeeInfoMm;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelFeeInfoMmRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelFeeInfoMm tsTravelFeeInfoMm = QTsTravelFeeInfoMm.tsTravelFeeInfoMm;
}
