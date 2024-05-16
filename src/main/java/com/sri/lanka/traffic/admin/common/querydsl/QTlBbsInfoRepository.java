package com.sri.lanka.traffic.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.entity.QTcCdInfo;
import com.sri.lanka.traffic.admin.common.entity.QTlBbsInfo;
import com.sri.lanka.traffic.admin.common.entity.TlBbsInfo;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTlBbsInfoRepository {

	private final JPAQueryFactory queryFactory;

	private QTlBbsInfo QtlBbsInfo = QTlBbsInfo.tlBbsInfo;
	
	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;

	/**
	 * @Method Name : getBbsList
	 * @작성일 : 2024. 1. 23.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 공지사항 목록 조회
	 * @param tlBbsInfo
	 * @param paging
	 * @return
	 */
	public List<TlBbsInfo> getBbsList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TlBbsInfo> result = queryFactory
											.select(
													Projections.bean(
															TlBbsInfo.class, 
															QtlBbsInfo.bbsId, 
															tcCdInfo.cdNm.as("bbsType"),
															QtlBbsInfo.bbsTitle,
															QtlBbsInfo.dspyYn, 
															QtlBbsInfo.registId, 
															QtlBbsInfo.registDt)
													)
					.from(QtlBbsInfo)
					.leftJoin(tcCdInfo).on(QtlBbsInfo.bbsType.eq(tcCdInfo.cd))
					.where(searchOption(searchCommonDTO.getSearchType(), searchCommonDTO.getSearchContent()))
					.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(QtlBbsInfo.registDt.desc()).fetch();

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

		Long count = queryFactory.select(QtlBbsInfo.count()).from(QtlBbsInfo)
				.where(searchOption(searchCommonDTO.getSearchType(), searchCommonDTO.getSearchContent()))
				.fetchOne();

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
		if (!CommonUtils.isNull(searchType) && CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(QtlBbsInfo.bbsType, searchType);
		}
		if (CommonUtils.isNull(searchType) && !CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(QtlBbsInfo.bbsTitle, searchContent)
					.or(QRepositorySupport.containsKeyword(QtlBbsInfo.registId, searchContent));
		}
		if (!CommonUtils.isNull(searchType) && !CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(QtlBbsInfo.bbsType, searchType)
					.and(QRepositorySupport.containsKeyword(QtlBbsInfo.bbsTitle, searchContent))
					.or(QRepositorySupport.containsKeyword(QtlBbsInfo.registId, searchContent));
		}
		return result;
	}

}
