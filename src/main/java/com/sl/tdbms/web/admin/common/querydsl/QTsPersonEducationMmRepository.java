package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsPersonEducationMm;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsPersonEducationMmRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsPersonEducationMm tsPersonEducationMm = QTsPersonEducationMm.tsPersonEducationMm;
}
