package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsPersonAgeMm;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsPersonAgeMmRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsPersonAgeMm tsPersonAgeMm = QTsPersonAgeMm.tsPersonAgeMm;
}
