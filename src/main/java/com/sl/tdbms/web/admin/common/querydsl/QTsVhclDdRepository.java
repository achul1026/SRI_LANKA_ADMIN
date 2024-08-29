package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsVhclDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsVhclDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsVhclDd tsVhclDd = QTsVhclDd.tsVhclDd;
}
