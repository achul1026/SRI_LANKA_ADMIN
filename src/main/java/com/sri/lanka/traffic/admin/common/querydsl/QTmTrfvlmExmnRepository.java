package com.sri.lanka.traffic.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTmTrfvlmExmn;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmTrfvlmExmnRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTmTrfvlmExmn tmTrfvlmExmn = QTmTrfvlmExmn.tmTrfvlmExmn;

}