package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsVhclOnhr;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsVhclOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsVhclOnhr tsVhclOnhr = QTsVhclOnhr.tsVhclOnhr;
}
