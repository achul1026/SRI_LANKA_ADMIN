package com.sri.lanka.traffic.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTlTrfvlRslt;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTlTrfvlRsltRepository {

	private final JPAQueryFactory queryFactory;

	private QTlTrfvlRslt tlTrfvlRslt = QTlTrfvlRslt.tlTrfvlRslt;
}
