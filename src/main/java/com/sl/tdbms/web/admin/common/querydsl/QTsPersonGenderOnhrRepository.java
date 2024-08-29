package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTsPersonGenderOnhr;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsPersonGenderOnhrRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsPersonGenderOnhr tsPersonGenderOnhr = QTsPersonGenderOnhr.tsPersonGenderOnhr;
}
