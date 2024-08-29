package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TcCdGrp;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.tccdgrp.TcCdInfoDTO;
import com.sl.tdbms.web.admin.common.entity.QTcCdGrp;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcCdInfoRepository {

	private final JPAQueryFactory queryFactory;

	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;

	private QTcCdGrp tcCdGrp = QTcCdGrp.tcCdGrp;
	
	public List<TcCdInfoDTO> getTcCdInfoList(TcCdGrp tcCdGrp, PagingUtils paging) {
		List<TcCdInfoDTO> result = queryFactory
				.select(Projections.bean(TcCdInfoDTO.class, tcCdInfo.cdId, tcCdInfo.grpcdId, tcCdInfo.cd, 
//						QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("cdNm"),
						tcCdInfo.cdnmEng, tcCdInfo.cdnmKor, tcCdInfo.cdnmSin, 
//						tcCdInfo.cdDescr, 
						tcCdInfo.useYn, tcCdInfo.registId, tcCdInfo.registDt))
				.from(tcCdInfo).where(tcCdInfo.grpcdId.eq(tcCdGrp.getGrpcdId())).offset(paging.getOffsetCount())
				.limit(paging.getLimitCount()).orderBy(tcCdInfo.registDt.desc()).fetch();
		return result;
	}

	public Long searchCount(TcCdGrp tcCdGrp) {
		Long count = queryFactory.select(tcCdInfo.count()).from(tcCdInfo)
				.where(tcCdInfo.grpcdId.eq(tcCdGrp.getGrpcdId())).fetchOne();
		return count;
	}

	public List<TcCdInfoDTO> getTcCdInfoListByGrpCd(String grpCd) {
		List<TcCdInfoDTO> result = queryFactory
				.select(Projections.bean(TcCdInfoDTO.class, tcCdInfo.cdId, tcCdInfo.cd, tcCdInfo.grpcdId,
						QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("cdNm"), tcCdInfo.useYn))
				.from(tcCdInfo).join(tcCdGrp)
				.on(tcCdGrp.grpCd.eq(grpCd).and(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId)).and(tcCdInfo.useYn.eq("Y")))
				.orderBy(tcCdInfo.registDt.desc()).fetch();
		return result;
	}
	
	/**
	  * @Method Name : getTcCdInfoListNotCd
	  * @작성일 : 2024. 6. 19.
	  * @작성자 : SM.KIM
	  * @Method 설명 : cd 제외 코드 목록 조회
	  * @param cd
	  * @return
	  */
	public List<TcCdInfoDTO> getTcCdInfoListNotCd(String grpCd,String cd) {
		List<TcCdInfoDTO> result = queryFactory
				.select(Projections.bean(TcCdInfoDTO.class, tcCdInfo.cdId, tcCdInfo.cd, tcCdInfo.grpcdId,
						QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("cdNm"), tcCdInfo.useYn))
				.from(tcCdInfo)
				.leftJoin(tcCdGrp).on(tcCdGrp.grpCd.eq(grpCd))
				.where(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId).and(tcCdInfo.cd.ne(cd)).and(tcCdInfo.useYn.eq("Y")))
				.orderBy(tcCdInfo.registDt.asc()).fetch();
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
	public List<TcCdInfoDTO> getTcCdInfoListByGrpCdAndCdArr(String grpCd, String[] cdArr) {
		List<TcCdInfoDTO> result = queryFactory
				.select(Projections.bean(TcCdInfoDTO.class, tcCdInfo.cdId, tcCdInfo.cd, tcCdInfo.grpcdId,
						QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("cdNm"), tcCdInfo.useYn))
				.from(tcCdInfo).join(tcCdGrp).on(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId))
				.where(tcCdGrp.grpCd.eq(grpCd).and(tcCdInfo.cd.in(cdArr).and(tcCdInfo.useYn.eq("Y"))))
				.orderBy(tcCdInfo.registDt.desc()).fetch();
		return result;
	}

	/**
	  * @Method Name : getCdInfoById
	  * @작성일 : 2024. 6. 4.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 
	  * @param cdId
	  * @return
	  */
	public TcCdInfoDTO getCdInfoById(String cdId) {
		TcCdInfoDTO result = queryFactory
				.select(Projections.bean(TcCdInfoDTO.class, tcCdInfo.cdId, tcCdInfo.cd, tcCdInfo.grpcdId,
						QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("cdNm"), tcCdInfo.useYn))
				.from(tcCdInfo)
				.where(tcCdInfo.cdId.eq(cdId))
				.orderBy(tcCdInfo.registDt.desc()).fetchOne();
		return result;
	}
	
	
}
