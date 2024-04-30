package com.sri.lanka.traffic.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTmSrvyMng;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmSrvyMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTmSrvyMng tmSrvyMng = QTmSrvyMng.tmSrvyMng;
}
