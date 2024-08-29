package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTmApiKeysrvc;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmApiKeysrvcRepository {

	private final JPAQueryFactory queryFactory;

	private QTmApiKeysrvc tmApiKeysrvc = QTmApiKeysrvc.tmApiKeysrvc;
}
