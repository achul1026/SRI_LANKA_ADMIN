package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsVhclFtnmin;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsVhclFtnminRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsVhclFtnmin tsVhclFtnmin = QTsVhclFtnmin.tsVhclFtnmin;
}
