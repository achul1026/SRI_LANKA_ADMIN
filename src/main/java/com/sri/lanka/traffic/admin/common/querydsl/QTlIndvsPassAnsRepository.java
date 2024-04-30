package com.sri.lanka.traffic.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTlIndvsPassAns;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTlIndvsPassAnsRepository {

	private final JPAQueryFactory queryFactory;

	private QTlIndvsPassAns tlIndvsPassAns = QTlIndvsPassAns.tlIndvsPassAns;
}
