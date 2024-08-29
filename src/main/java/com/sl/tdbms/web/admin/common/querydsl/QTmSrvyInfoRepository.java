package com.sl.tdbms.web.admin.common.querydsl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.invst.TmSrvyInfoDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.entity.QTmExmnMng;
import com.sl.tdbms.web.admin.common.entity.QTmSrvyInfo;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmSrvyInfoRepository {

	private final JPAQueryFactory queryFactory;

	private QTmSrvyInfo tmSrvyInfo = QTmSrvyInfo.tmSrvyInfo;
	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;
	
	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;
	
	/**
	  * @Method Name : getSurveyList
	  * @작성일 : 2024. 5. 2.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 목록 조회
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	public List<TmSrvyInfoDTO> getSurveyList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TmSrvyInfoDTO> result = queryFactory.select(Projections.bean(
															TmSrvyInfoDTO.class, 
															tmSrvyInfo.srvyId,
															tmSrvyInfo.srvyTitle,
															tmSrvyInfo.srvyType,
															tmSrvyInfo.cstmYn,
															QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("srvyTypeNm"),
															tmSrvyInfo.startDt,
															tmSrvyInfo.endDt,
															tmSrvyInfo.registId,
															tmSrvyInfo.registDt
															)
														)
												.from(tmSrvyInfo)
												.leftJoin(tcCdInfo).on(tmSrvyInfo.srvyType.eq(tcCdInfo.cd))
												.where(searchOption(searchCommonDTO))
												.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
												.orderBy(tmSrvyInfo.registDt.desc()).fetch();

		return result;
	}
	/**
	  * @Method Name : getSurveyListForStatistics
	  * @작성일 : 2024. 5. 2.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 목록 조회 (통계 검색용)
	  * @param searchDTO
	  * @return
	  */
	public List<TmSrvyInfoDTO> getSurveyListForStatistics(TlExmnRsltStatisticsSearchDTO searchDTO) {
		/*StringTemplate surveyStartDt = Expressions.stringTemplate(
				"to_char({0}, {1})",
				tmExmnMng.startDt,
				Expressions.constant("yyyy")
		);

		StringTemplate surveyEbdDt = Expressions.stringTemplate(
				"to_char({0}, {1})",
				tmExmnMng.endDt,
				Expressions.constant("yyyy")
		);*/
		List<TmSrvyInfoDTO> result = queryFactory.select(Projections.bean(
															TmSrvyInfoDTO.class,
															tmSrvyInfo.srvyId,
															tmSrvyInfo.srvyTitle,
															tmSrvyInfo.registDt
															)
														)
												.distinct()
												.from(tmSrvyInfo)
												.innerJoin(tmExmnMng).on(tmSrvyInfo.srvyId.eq(tmExmnMng.srvyId))
												.where(tmExmnMng.exmnType.eq(searchDTO.getExmnTypeCd()))
												.fetch().stream().sorted((x, y) -> y.getRegistDt().compareTo(x.getRegistDt()))
												.collect(Collectors.toList());

		return result;
	}
	
	/**
	  * @Method Name : getTotalCount
	  * @작성일 : 2024. 5. 2.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 목록 개수 
	  * @param searchCommonDTO
	  * @return
	  */
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {

		Long count = queryFactory.select(tmSrvyInfo.count())
								.from(tmSrvyInfo)
								.where(searchOption(searchCommonDTO))
								.fetchOne();

		return count;
	}
	
	
	/**
	  * @Method Name : getSurveyInfo
	  * @작성일 : 2024. 5. 2.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 양식 상세정보
	  * @param srvyId
	  * @return
	  */
	public TmSrvyInfoDTO getSurveyInfo(String srvyId) {
		TmSrvyInfoDTO result = queryFactory.select(Projections.bean(
															TmSrvyInfoDTO.class, 
															tmSrvyInfo.srvyId,
															tmSrvyInfo.srvyTitle,
															tmSrvyInfo.srvyType,
															tmSrvyInfo.cstmYn,
															QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("srvyTypeNm"),
															tmSrvyInfo.startDt,
															tmSrvyInfo.endDt,
															tmSrvyInfo.registId
															)
														)
												.from(tmSrvyInfo)
												.leftJoin(tcCdInfo).on(tmSrvyInfo.srvyType.eq(tcCdInfo.cd))
												.where(tmSrvyInfo.srvyId.eq(srvyId))
												.fetchOne();

		return result;
	}
	
	/**
	  * @Method Name : searchOption
	  * @작성일 : 2024. 5. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 설문 양식 검색 조건
	  * @param searchCommonDTO
	  * @return
	  */
	private BooleanExpression searchOption(SearchCommonDTO searchCommonDTO) {
		BooleanExpression result = null;
		
		if (searchCommonDTO.getSearchTypeCd() != null && !searchCommonDTO.getSearchTypeCd().isBlank()) {
			result = QRepositorySupport.toEqExpression(tmSrvyInfo.srvyType, searchCommonDTO.getSearchTypeCd());
		}

		if (searchCommonDTO.getSearchContent() != null && !searchCommonDTO.getSearchContent().isBlank()) {
			BooleanExpression keywordExpression = QRepositorySupport.containsKeyword(tmSrvyInfo.srvyTitle, searchCommonDTO.getSearchContent());
			result = (result != null) ? result.and(keywordExpression) : keywordExpression;
		}

		if (searchCommonDTO.getStartDate() != null) {
			LocalDateTime startDateTime = CommonUtils.getStartOfDay(searchCommonDTO.getStartDate()); 
			BooleanExpression startExpression = QRepositorySupport.toGoeExpression(tmSrvyInfo.registDt, startDateTime);
			result = (result != null) ? result.and(startExpression) : startExpression;
		}

		if (searchCommonDTO.getEndDate() != null) {
			LocalDateTime endDateTime = CommonUtils.getEndOfDay(searchCommonDTO.getEndDate()); 
			BooleanExpression endExpression = QRepositorySupport.toLoeExpression(tmSrvyInfo.registDt, endDateTime);
			result = (result != null) ? result.and(endExpression) : endExpression;
		}
		return result;
		
	}
}
