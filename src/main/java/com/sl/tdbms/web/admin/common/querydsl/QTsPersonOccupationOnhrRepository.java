package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsPersonOccupationOnhr;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsPersonOccupationOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsPersonOccupationOnhr tsPersonOccupationOnhr = QTsPersonOccupationOnhr.tsPersonOccupationOnhr;
}
