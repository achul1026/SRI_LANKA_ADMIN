package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTlApiPvsn;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTlApiPvsnRepository {

	private final JPAQueryFactory queryFactory;

	private QTlApiPvsn tlApiPvsn = QTlApiPvsn.tlApiPvsn;
}
