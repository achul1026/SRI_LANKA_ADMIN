package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTmApiPvsnitem;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmApiPvsnitemRepository {

	private final JPAQueryFactory queryFactory;

	private QTmApiPvsnitem tmApiPvsnitem = QTmApiPvsnitem.tmApiPvsnitem;
}
