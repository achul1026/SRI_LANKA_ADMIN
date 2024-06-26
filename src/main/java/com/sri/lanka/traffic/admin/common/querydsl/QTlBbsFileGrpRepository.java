package com.sri.lanka.traffic.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTlBbsFileGrp;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTlBbsFileGrpRepository {

	private final JPAQueryFactory queryFactory;

	private QTlBbsFileGrp tlBbsFileGrp = QTlBbsFileGrp.tlBbsFileGrp;
}
