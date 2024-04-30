package com.sri.lanka.traffic.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTcJuncMng;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcJuncMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTcJuncMng tcJuncMng = QTcJuncMng.tcJuncMng;

}
