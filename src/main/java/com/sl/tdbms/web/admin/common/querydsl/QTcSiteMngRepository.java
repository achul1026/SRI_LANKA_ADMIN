package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TcSiteMng;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.entity.QTcSiteMng;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcSiteMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTcSiteMng tcSiteMng = QTcSiteMng.tcSiteMng;

	/**
	 * @Method Name : getSiteList
	 * @작성일 : 2024. 1. 23.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 공지사항 목록 조회
	 * @param searchCommonDTO
	 * @param paging
	 * @return
	 */
	public List<TcSiteMng> getSiteList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TcSiteMng> result = queryFactory
				.select(Projections.bean(TcSiteMng.class, tcSiteMng.siteId, tcSiteMng.siteNm,
						tcSiteMng.siteclsfCd, tcSiteMng.siteUrl, tcSiteMng.dspyYn, tcSiteMng.registId,
						tcSiteMng.registDt))
				.from(tcSiteMng)
				.where(searchOption(searchCommonDTO.getSearchType(), searchCommonDTO.getSearchContent()))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tcSiteMng.registDt.desc())
				.fetch();

		return result;
	}

	/**
	 * @Method Name : getTotalCount
	 * @작성일 : 2024. 1. 23.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 공지사항 갯수 조회
	 * @return
	 */
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {

		Long count = queryFactory.select(tcSiteMng.count()).from(tcSiteMng)
				.where(searchOption(searchCommonDTO.getSearchType(), searchCommonDTO.getSearchContent())).fetchOne();

		return count;
	}

	/**
	 * @Method Name : searchOption
	 * @작성일 : 2024. 1. 23.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 검색 조건
	 * @param searchType
	 * @param searchContent
	 * @return
	 */
	private BooleanExpression searchOption(String searchType, String searchContent) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchType) && !CommonUtils.isNull(searchContent)) {
			if ("siteNm".equals(searchType)) {
				result = QRepositorySupport.containsKeyword(tcSiteMng.siteNm, searchContent);
			} else if ("registId".equals(searchType)) {
				result = QRepositorySupport.containsKeyword(tcSiteMng.registId, searchContent);
			} else if ("siteclsfCd".equals(searchType)) {
				result = QRepositorySupport.containsKeyword(tcSiteMng.siteclsfCd, searchContent);
			} else {
				result = QRepositorySupport.containsKeyword(tcSiteMng.siteNm, searchContent)
						.or(QRepositorySupport.containsKeyword(tcSiteMng.registId, searchContent)
								.or(QRepositorySupport.containsKeyword(tcSiteMng.siteclsfCd, searchContent)));
			}
		}
		return result;
	}

}
