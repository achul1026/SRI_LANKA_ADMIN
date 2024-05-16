package com.sri.lanka.traffic.admin.common.querydsl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnMngDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnScheduleDTO;
import com.sri.lanka.traffic.admin.common.entity.QTcCdInfo;
import com.sri.lanka.traffic.admin.common.entity.QTcUserMng;
import com.sri.lanka.traffic.admin.common.entity.QTmExmnMng;
import com.sri.lanka.traffic.admin.common.entity.QTmExmnPollster;
import com.sri.lanka.traffic.admin.common.entity.QTmSrvyInfo;
import com.sri.lanka.traffic.admin.common.entity.TmExmnMng;
import com.sri.lanka.traffic.admin.common.enums.code.ExmnScheduleSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.ExmnSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.ExmnTypeCd;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmExmnMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTmExmnMng tmExmnMng 	= QTmExmnMng.tmExmnMng;
	private QTcUserMng tcUserMng 	= QTcUserMng.tcUserMng;
	private QTmSrvyInfo tmSrvyInfo 	= QTmSrvyInfo.tmSrvyInfo; 
	private QTcCdInfo  tcCdInfo 	= QTcCdInfo.tcCdInfo;
	private QTmExmnPollster tmExmnPollster = QTmExmnPollster.tmExmnPollster;

	/**
	 * @Method Name : getInvstList
	 * @작성일 : 2024. 2. 5.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 조사 목록 조회
	 * @param searchCommonDTO
	 * @param paging
	 * @return
	 */
	public List<TmExmnMngDTO> getInvstList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {

		List<TmExmnMngDTO> result = queryFactory.select(Projections.bean(
															TmExmnMngDTO.class, 
															tmExmnMng.exmnmngId, 
															tmExmnMng.usermngId,
															tcCdInfo.cdNm.as("mngrBffltd"), 
															tmExmnMng.exmnpicId, 
															tmExmnMng.exmnNm, 
															tmExmnMng.exmnType,
															tmExmnMng.sttsCd, 
															tmExmnMng.exmnpicId, 
															tmExmnMng.startDt, 
															tmExmnMng.endDt,
															tmExmnMng.registId,
															tmExmnMng.exmnLc)
														)
												.from(tmExmnMng)
												.leftJoin(tcUserMng).on(tmExmnMng.usermngId.eq(tcUserMng.usermngId))
												.leftJoin(tcCdInfo).on(tcUserMng.userBffltd.eq(tcCdInfo.cd))
												.where(searchOption(searchCommonDTO))
												.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
												.orderBy(tmExmnMng.registDt.desc()).fetch();

		return result;
	}
	
	/**
	 * @Method Name : getInvstScheduleList
	 * @작성일 : 2024. 2. 5.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 조사 스케쥴 목록 조회
	 * @param searchCommonDTO
	 * @param paging
	 * @return
	 */
	public List<TmExmnScheduleDTO> getInvstScheduleList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		
		List<TmExmnScheduleDTO> result = queryFactory.select(Projections.bean(
																		TmExmnScheduleDTO.class, 
																		tmExmnMng.exmnmngId, 
																		tmExmnMng.usermngId,
																		tmExmnMng.exmnpicId, 
																		tmExmnMng.exmnNm, 
																		tmExmnMng.roadCd, 
																		tmExmnMng.exmnType,
																		tmExmnMng.colrCd,
																		tmExmnMng.sttsCd, 
												                        new CaseBuilder()
												                        .when(tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(tmExmnMng.startDt.goe(LocalDateTime.now()).and(tmExmnPollster.pollsterId.count().eq(tmExmnMng.exmnNop.longValue())))).then(ExmnScheduleSttsCd.NOT_YET_PROGRESS.getCode())
												                        .when(tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(tmExmnMng.startDt.lt(LocalDateTime.now()).and(tmExmnPollster.pollsterId.count().eq(tmExmnMng.exmnNop.longValue())))).then(ExmnScheduleSttsCd.NOT_PROGRESS.getCode())
												                        .when(tmExmnPollster.pollsterId.count().lt(tmExmnMng.exmnNop)).then(ExmnScheduleSttsCd.NOT_YET_INVESTIGATOR.getCode())
												                        .when(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE).and(tmExmnPollster.pollsterId.count().eq(tmExmnMng.exmnNop.longValue()))).then(ExmnScheduleSttsCd.PROGRESS_COMPLETE.getCode())
												                        .when(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_PROGRESS).and(tmExmnPollster.pollsterId.count().eq(tmExmnMng.exmnNop.longValue()))).then(ExmnScheduleSttsCd.PROGRESSING.getCode())
												                        .otherwise(ExmnScheduleSttsCd.NO_MATCH_TYPE.getCode()).as("exmnScheduleSttsCd"),
																		tmExmnMng.exmnpicId,
																		tmExmnMng.startDt, 
																		tmExmnMng.endDt,
																		tmExmnMng.registId,
																		tmExmnMng.exmnLc)
																		)
												.from(tmExmnMng)
												.leftJoin(tcUserMng).on(tmExmnMng.usermngId.eq(tcUserMng.usermngId))
												.leftJoin(tmExmnPollster).on(tmExmnMng.exmnmngId.eq(tmExmnPollster.exmnmngId))
												.where(tmExmnMng.sttsCd.ne(ExmnSttsCd.INVEST_WRITING).and(scheduleSearchOption(searchCommonDTO)))
												.groupBy(tmExmnMng.exmnmngId)
												.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
												.orderBy(tmExmnMng.registDt.desc()).fetch();
		
		return result;
	}
	
	/**
	 * @Method Name : getInvstScheduleList
	 * @작성일 : 2024. 2. 5.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 조사 스케쥴 목록 조회
	 * @param searchCommonDTO
	 * @param paging
	 * @return
	 */
	public List<TmExmnMngDTO> getInvstScheduleListByNotSttsCd(ExmnSttsCd exmnSttsCd) {
		
		List<TmExmnMngDTO> result = queryFactory.select(Projections.bean(
																		TmExmnMngDTO.class, 
																		tmExmnMng.exmnmngId, 
																		tmExmnMng.usermngId,
																		tcCdInfo.cdNm.as("mngrBffltd"), 
																		tmExmnMng.exmnpicId, 
																		tmExmnMng.exmnNm, 
																		tmExmnMng.roadCd, 
																		tmExmnMng.exmnType,
																		tmExmnMng.colrCd,
																		tmExmnMng.sttsCd, 
																		tmExmnMng.exmnpicId, 
																		tmExmnMng.startDt, 
																		tmExmnMng.endDt,
																		tmExmnMng.registId,
																		tmExmnMng.exmnLc)
																		)
																		.from(tmExmnMng)
																		.leftJoin(tcUserMng).on(tmExmnMng.usermngId.eq(tcUserMng.usermngId))
																		.leftJoin(tcCdInfo).on(tcUserMng.userBffltd.eq(tcCdInfo.cd))
																		.where(tmExmnMng.sttsCd.ne(exmnSttsCd))
																		.orderBy(
														                        Expressions.numberTemplate(Integer.class, "age({0}, {1})",
														                                tmExmnMng.startDt, tmExmnMng.endDt).asc(),
														                        tmExmnMng.registDt.desc()
														                )
																		.fetch();
		
		return result;
	}
	
	/**
	 * @Method Name : getInvstInfo
	 * @작성일 : 2024. 2. 5.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 조사 목록 조회
	 * @param exmmngId
	 * @return
	 */
	public TmExmnMngDTO getInvstInfo(String exmmngId) {
		
		TmExmnMngDTO result = queryFactory.select(Projections.bean(
													TmExmnMngDTO.class, 
													tmExmnMng.exmnmngId, 
													tmExmnMng.usermngId,
													tmSrvyInfo.srvyId,
													tmSrvyInfo.srvyTitle,
													tcCdInfo.cdNm.as("mngrBffltd"), 
													tcUserMng.userNm, 
													tmExmnMng.exmnNm, 
													tmExmnMng.exmnLc, 
													tmExmnMng.colrCd, 
													tmExmnMng.exmnType,
													tmExmnMng.exmnpicId, 
													tmExmnMng.laneCnt, 
													tmExmnMng.startDt, 
													tmExmnMng.endDt,
													tmExmnMng.exmnDiv,
													tmExmnMng.exmnNop,
													tmExmnMng.partcptCd,
													tmExmnMng.goalCnt,
													tmExmnMng.lat,
													tmExmnMng.lon,
													tmExmnMng.exmnRange,
													tmExmnMng.sttsCd,
													tmExmnMng.registId)
												)
											.from(tmExmnMng)
											.leftJoin(tcUserMng).on(tmExmnMng.usermngId.eq(tcUserMng.usermngId))
											.leftJoin(tcCdInfo).on(tcUserMng.userBffltd.eq(tcCdInfo.cd))
											.leftJoin(tmSrvyInfo).on(tmExmnMng.srvyId.eq(tmSrvyInfo.srvyId))
											.where(tmExmnMng.exmnmngId.eq(exmmngId))
											.fetchOne();
		
		return result;
	}

	/**
	 * @Method Name : getTotalCount
	 * @작성일 : 2024. 2. 5.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 조사 목록 개수 조회
	 * @param searchCommonDTO
	 * @return
	 */
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {

		Long count = queryFactory.select(tmExmnMng.countDistinct())
								.from(tmExmnMng)
								.where(searchOption(searchCommonDTO))
								.leftJoin(tcUserMng).on(tmExmnMng.usermngId.eq(tcUserMng.usermngId))
								.fetchOne();

		return count;
	}
	
	/**
	 * @Method Name : getInvstScheduleTotalCount
	 * @작성일 : 2024. 2. 5.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 조사 스케쥴 목록 개수 조회
	 * @param searchCommonDTO
	 * @return
	 */
	public Long getInvstScheduleTotalCount(SearchCommonDTO searchCommonDTO) {
		
		Long count = queryFactory.select(tmExmnMng.countDistinct())
									.from(tmExmnMng)
									.leftJoin(tcUserMng).on(tmExmnMng.usermngId.eq(tcUserMng.usermngId))
									.leftJoin(tmExmnPollster).on(tmExmnMng.exmnmngId.eq(tmExmnPollster.exmnmngId))
									.where(tmExmnMng.sttsCd.ne(ExmnSttsCd.INVEST_WRITING).and(scheduleSearchOption(searchCommonDTO)))
									.fetchOne();
		
		return count;
	}
	/**
	  * @Method Name : getInvstScheduleDetailList
	  * @작성일 : 2024. 3. 29.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 스케쥴 상세 목록
	  * @param paramDate
	  * @return
	  */
	public List<TmExmnMng> getInvstScheduleDetailList(String paramDate) {
		
		LocalDateTime startDt = LocalDateTime.parse(paramDate + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LocalDateTime endDt = LocalDateTime.parse(paramDate + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        
		List<TmExmnMng> result = queryFactory.select(tmExmnMng)
												.from(tmExmnMng)
												.where(tmExmnMng.startDt.loe(startDt),tmExmnMng.endDt.goe(endDt)
												.and(tmExmnMng.sttsCd.ne(ExmnSttsCd.INVEST_WRITING)))
												.orderBy(
								                        Expressions.numberTemplate(Integer.class, "age({0}, {1})",
								                                tmExmnMng.startDt, tmExmnMng.endDt).asc(),
								                        tmExmnMng.registDt.desc()
								                )
												.fetch();
		return result;
	}
	
	/**
	  * @Method Name : getInvstScheduleDetailTotalCnt
	  * @작성일 : 2024. 4. 8.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 스케쥴 상세 목록 개수
	  * @param paramDate
	  * @return
	  */
	public int getInvstScheduleDetailTotalCount(String paramDate) {
		
		LocalDateTime startDt = LocalDateTime.parse(paramDate + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LocalDateTime endDt = LocalDateTime.parse(paramDate + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		
		Long result = queryFactory.select(tmExmnMng.countDistinct())
											.from(tmExmnMng)
											.where(tmExmnMng.startDt.loe(startDt),tmExmnMng.endDt.goe(endDt)
													.and(tmExmnMng.sttsCd.ne(ExmnSttsCd.INVEST_WRITING)))
											.fetchOne();
		return result.intValue();
	}
	
	
	/**
	 * @Method Name : searchOption
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 검색 조건
	 * @param searchCommonDTO
	 * @return
	 */
	private BooleanExpression scheduleSearchOption(SearchCommonDTO searchCommonDTO) {

		BooleanExpression result = null;

		if (ExmnTypeCd.getEnums(searchCommonDTO.getSearchTypeCd()) != null) {
			result = QRepositorySupport.toEqExpression(tmExmnMng.exmnType, ExmnTypeCd.getEnums(searchCommonDTO.getSearchTypeCd()));
		}
	    if (ExmnScheduleSttsCd.getEnums(searchCommonDTO.getSearchSttsCd()) != null) {
	        BooleanExpression sttsCdExpression = null;
	        ExmnScheduleSttsCd exmnScheduleSttsCd = ExmnScheduleSttsCd.getEnums(searchCommonDTO.getSearchSttsCd());
	        LocalDateTime currentDate = LocalDateTime.now();
	        
	        if(ExmnScheduleSttsCd.NOT_YET_PROGRESS.equals(exmnScheduleSttsCd)) {
	        	//진행 예정
	        	sttsCdExpression = tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).eq(tmExmnMng.exmnNop.longValue()).and(tmExmnMng.startDt.goe(currentDate))); 
	        }else if(ExmnScheduleSttsCd.NOT_PROGRESS.equals(exmnScheduleSttsCd)) {
	        	//미진행
	        	sttsCdExpression = tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).eq(tmExmnMng.exmnNop.longValue()).and(tmExmnMng.startDt.lt(currentDate)));
	        }else if(ExmnScheduleSttsCd.NOT_YET_INVESTIGATOR.equals(exmnScheduleSttsCd)) {
	        	//조사원 미등록
	        	sttsCdExpression = queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).lt(tmExmnMng.exmnNop.longValue());
	        }else if(ExmnScheduleSttsCd.PROGRESSING.equals(exmnScheduleSttsCd)) {
	        	//진행중
	        	sttsCdExpression = tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_PROGRESS).and(queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).eq(tmExmnMng.exmnNop.longValue()));
	        }else if(ExmnScheduleSttsCd.PROGRESS_COMPLETE.equals(exmnScheduleSttsCd)) {
	        	//진행 완료
	        	sttsCdExpression = tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE).and(queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).eq(tmExmnMng.exmnNop.longValue()));
	        }
	        
			result = (result != null) ? result.and(sttsCdExpression) : sttsCdExpression;
	    }

		return result;
	}

	private BooleanExpression searchOption(SearchCommonDTO searchCommonDTO) {

		BooleanExpression result = null;

		if (ExmnTypeCd.getEnums(searchCommonDTO.getSearchTypeCd()) != null) {
			result = QRepositorySupport.toEqExpression(tmExmnMng.exmnType, ExmnTypeCd.getEnums(searchCommonDTO.getSearchTypeCd()));
		}

		if (searchCommonDTO.getSearchContent() != null && !searchCommonDTO.getSearchContent().isBlank()) {
			BooleanExpression keywordExpression = QRepositorySupport.containsKeyword(tmExmnMng.exmnNm, searchCommonDTO.getSearchContent());
			result = (result != null) ? result.and(keywordExpression) : keywordExpression;
		}

		if (searchCommonDTO.getStartDate() != null) {
			LocalDateTime startDateTime = CommonUtils.getStartOfDay(searchCommonDTO.getStartDate()); 
			BooleanExpression startExpression = QRepositorySupport.toGoeExpression(tmExmnMng.startDt, startDateTime);
			result = (result != null) ? result.and(startExpression) : startExpression;
		}

		if (searchCommonDTO.getEndDate() != null) {
			LocalDateTime endDateTime = CommonUtils.getEndOfDay(searchCommonDTO.getEndDate()); 
			BooleanExpression endExpression = QRepositorySupport.toLoeExpression(tmExmnMng.endDt, endDateTime);
			result = (result != null) ? result.and(endExpression) : endExpression;
		}
		return result;
	}
}
