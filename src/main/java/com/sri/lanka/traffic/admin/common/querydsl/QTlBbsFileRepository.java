package com.sri.lanka.traffic.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTlBbsFile;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTlBbsFileRepository {

	private final JPAQueryFactory queryFactory;

	private QTlBbsFile tlBbsFile = QTlBbsFile.tlBbsFile;
}
