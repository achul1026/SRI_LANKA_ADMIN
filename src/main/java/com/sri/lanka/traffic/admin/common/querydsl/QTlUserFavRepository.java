package com.sri.lanka.traffic.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTlUserFav;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTlUserFavRepository {

	private final JPAQueryFactory queryFactory;

	private QTlUserFav tlUserFav = QTlUserFav.tlUserFav;
}
