package com.sri.lanka.traffic.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTcCdGrp;
import com.sri.lanka.traffic.admin.common.entity.QTcCdInfo;
import com.sri.lanka.traffic.admin.common.entity.TcCdGrp;
import com.sri.lanka.traffic.admin.common.entity.TcCdInfo;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcCdInfoRepository {

	private final JPAQueryFactory queryFactory;

	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;

	private QTcCdGrp tcCdGrp = QTcCdGrp.tcCdGrp;

	public List<TcCdInfo> getTcCdInfoList(TcCdGrp tcCdGrp, PagingUtils paging) {

		List<TcCdInfo> result = queryFactory
				.select(Projections.bean(TcCdInfo.class, tcCdInfo.cdId, tcCdInfo.grpcdId, tcCdInfo.cd, tcCdInfo.cdNm,
						tcCdInfo.cdDescr, tcCdInfo.useYn, tcCdInfo.registId, tcCdInfo.registDt))
				.from(tcCdInfo).where(tcCdInfo.grpcdId.eq(tcCdGrp.getGrpcdId())).offset(paging.getOffsetCount())
				.limit(paging.getLimitCount()).orderBy(tcCdInfo.registDt.desc()).fetch();

		return result;
	}

	public Long searchCount(TcCdGrp tcCdGrp) {

		Long count = queryFactory.select(tcCdInfo.count()).from(tcCdInfo)
				.where(tcCdInfo.grpcdId.eq(tcCdGrp.getGrpcdId())).fetchOne();

		return count;
	}

	public List<TcCdInfo> getTcCdInfoListByGrpCd(String grpCd) {
		List<TcCdInfo> result = queryFactory
				.select(Projections.bean(TcCdInfo.class, tcCdInfo.cdId, tcCdInfo.cd, tcCdInfo.cdNm, tcCdInfo.grpcdId,
						tcCdInfo.useYn))
				.from(tcCdInfo).join(tcCdGrp)
				.on(tcCdGrp.grpCd.eq(grpCd).and(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId)).and(tcCdInfo.useYn.eq("Y")))
				.orderBy(tcCdInfo.registDt.desc()).fetch();
		return result;
	}

	/**
	 * @Method Name : getTcCdInfoListByGrpCdAndCdArr
	 * @작성일 : 2024. 1. 30.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 공통 코드 목록 조회
	 * @param grpCd
	 * @return
	 */
	public List<TcCdInfo> getTcCdInfoListByGrpCdAndCdArr(String grpCd, String[] cdArr) {
		List<TcCdInfo> result = queryFactory
				.select(Projections.bean(TcCdInfo.class, tcCdInfo.cdId, tcCdInfo.cd, tcCdInfo.cdNm, tcCdInfo.grpcdId,
						tcCdInfo.useYn))
				.from(tcCdInfo).join(tcCdGrp).on(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId))
				.where(tcCdGrp.grpCd.eq(grpCd).and(tcCdInfo.cd.in(cdArr).and(tcCdInfo.useYn.eq("Y"))))
				.orderBy(tcCdInfo.registDt.desc()).fetch();
		return result;
	}
}
