package com.sri.lanka.traffic.admin.common.querydsl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvyInfoDTO;
import com.sri.lanka.traffic.admin.common.entity.QTcCdInfo;
import com.sri.lanka.traffic.admin.common.entity.QTmSrvyAns;
import com.sri.lanka.traffic.admin.common.entity.QTmSrvyInfo;
import com.sri.lanka.traffic.admin.common.entity.QTmSrvyQstn;
import com.sri.lanka.traffic.admin.common.entity.QTmSrvySect;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmSrvyInfoRepository {

	private final JPAQueryFactory queryFactory;

	private QTmSrvyInfo tmSrvyInfo = QTmSrvyInfo.tmSrvyInfo;
	
	private QTmSrvySect tmSrvySect = QTmSrvySect.tmSrvySect;
	
	private QTmSrvyQstn tmSrvyQstn = QTmSrvyQstn.tmSrvyQstn;
	
	private QTmSrvyAns tmSrvyAns = QTmSrvyAns.tmSrvyAns;
	
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
															tcCdInfo.cdNm.as("srvyTypeNm"),
															tmSrvyInfo.startDt,
															tmSrvyInfo.endDt,
															tmSrvyInfo.registId
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
															tcCdInfo.cdNm.as("srvyTypeNm"),
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
			BooleanExpression startExpression = QRepositorySupport.toGoeExpression(tmSrvyInfo.startDt, startDateTime);
			result = (result != null) ? result.and(startExpression) : startExpression;
		}

		if (searchCommonDTO.getEndDate() != null) {
			LocalDateTime endDateTime = CommonUtils.getEndOfDay(searchCommonDTO.getEndDate()); 
			BooleanExpression endExpression = QRepositorySupport.toLoeExpression(tmSrvyInfo.endDt, endDateTime);
			result = (result != null) ? result.and(endExpression) : endExpression;
		}
		return result;
		
	}
}
