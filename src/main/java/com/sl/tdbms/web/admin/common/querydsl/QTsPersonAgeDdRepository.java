package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsPersonAgeDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsPersonAgeDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsPersonAgeDd tsPersonAgeDd = QTsPersonAgeDd.tsPersonAgeDd;
}
