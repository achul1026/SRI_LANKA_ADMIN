package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsPersonOccupationDd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsPersonOccupationDdRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsPersonOccupationDd tsPersonOccupationDd = QTsPersonOccupationDd.tsPersonOccupationDd;
}
