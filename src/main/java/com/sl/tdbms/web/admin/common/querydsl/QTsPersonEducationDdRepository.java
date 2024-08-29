package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsPersonEducationDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsPersonEducationDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsPersonEducationDd tsPersonEducationDd = QTsPersonEducationDd.tsPersonEducationDd;
}
