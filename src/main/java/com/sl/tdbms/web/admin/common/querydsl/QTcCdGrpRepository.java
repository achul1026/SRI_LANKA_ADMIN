package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.tccdgrp.TcCdGrpDTO;
import com.sl.tdbms.web.admin.common.entity.QTcCdGrp;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcCdGrpRepository {

	private final JPAQueryFactory queryFactory;

	private QTcCdGrp tcCdGrp = QTcCdGrp.tcCdGrp;
	
	/**
	 * @Method Name : getTcCdGrpList
	 * @작성일 : 2024. 1. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 그룹 코드 목록 조회
	 * @param commonDTO
	 * @param paging
	 * @return
	 */
	public List<TcCdGrpDTO> getTcCdGrpList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TcCdGrpDTO> result = queryFactory
				.select(Projections.bean(TcCdGrpDTO.class, tcCdGrp.grpcdId, tcCdGrp.grpCd, 
						tcCdGrp.grpcdNm,
						tcCdGrp.useYn, tcCdGrp.registId, tcCdGrp.registDt))
				.from(tcCdGrp)
				.where(searchOption(searchCommonDTO.getSearchContent()))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tcCdGrp.registDt.desc()).fetch();

		return result;
	}

	/**
	 * @Method Name : searchCount
	 * @작성일 : 2024. 1. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 그룹 코드 갯수 조회
	 * @param commonDTO
	 * @return
	 */
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {
		Long count = queryFactory.select(tcCdGrp.count()).from(tcCdGrp)
				.where(searchOption(searchCommonDTO.getSearchContent())).fetchOne();
		return count;
	}

	/**
	 * @Method Name : searchOption
	 * @작성일 : 2024. 1. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 검색 조건
	 * @param searchType
	 * @param searchContent
	 * @return
	 */
	private BooleanExpression searchOption(String searchContent) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tcCdGrp.grpCd, searchContent)
					.or(QRepositorySupport.containsKeyword(tcCdGrp.grpcdNm, searchContent));
		}
		return result;
	}
}
