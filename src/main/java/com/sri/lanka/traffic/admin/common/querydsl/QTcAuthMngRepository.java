package com.sri.lanka.traffic.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.dto.auth.TcAuthMngDTO;
import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.entity.QTcAuthMng;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcAuthMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTcAuthMng tcAuthMng = QTcAuthMng.tcAuthMng;

	/**
	 * @Method Name : getAuthList
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 권한 목록 조회
	 * @param commonDTO
	 * @param paging
	 * @return
	 */
	public List<TcAuthMngDTO> getAuthList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {

		List<TcAuthMngDTO> result = queryFactory
				.select(Projections.bean(TcAuthMngDTO.class, tcAuthMng.authId, tcAuthMng.authNm, tcAuthMng.authDescr, tcAuthMng.registDt, tcAuthMng.updtDt))
				.from(tcAuthMng).where(searchOption(searchCommonDTO.getSearchType(), searchCommonDTO.getSearchContent()))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tcAuthMng.registDt.desc()).fetch();

		return result;
	}

	/**
	 * @Method Name : searchCount
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 권한 목록 개수 조회
	 * @param commonDTO
	 * @return
	 */
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {

		Long count = queryFactory.select(tcAuthMng.count()).from(tcAuthMng)
				.where(searchOption(searchCommonDTO.getSearchType(), searchCommonDTO.getSearchContent())).fetchOne();

		return count;
	}

	/**
	 * @Method Name : searchOption
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 검색 조건
	 * @param searchType
	 * @param searchContent
	 * @return
	 */
	private BooleanExpression searchOption(String searchType, String searchContent) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchType) && !CommonUtils.isNull(searchContent)) {

			if ("authNm".equals(searchType)) {
				result = QRepositorySupport.containsKeyword(tcAuthMng.authNm, searchContent);
			} else if ("authDesc".equals(searchType)) {
				result = QRepositorySupport.containsKeyword(tcAuthMng.authDescr, searchContent);
			} else {
				result = QRepositorySupport.containsKeyword(tcAuthMng.authNm, searchContent)
						.or(QRepositorySupport.containsKeyword(tcAuthMng.authDescr, searchContent));
			}
		}
		return result;
	}

}
