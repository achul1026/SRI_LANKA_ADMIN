package com.sri.lanka.traffic.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTmExmnGrp;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmExmnGrpRepository {

	private final JPAQueryFactory queryFactory;

	private QTmExmnGrp tmExmnGrp = QTmExmnGrp.tmExmnGrp;
}
