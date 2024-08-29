package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.gis.SurveyGISDetailDTO;
import com.sl.tdbms.web.admin.common.dto.invst.TmExmnMngDTO;
import com.sl.tdbms.web.admin.common.dto.invst.TmExmnScheduleDTO;
import com.sl.tdbms.web.admin.common.entity.*;
import com.sl.tdbms.web.admin.common.enums.code.ExmnScheduleSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * The type Q tm exmn mng repository.
 */
@Repository
@RequiredArgsConstructor
public class QTmExmnMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTmExmnMng tmExmnMng 	= QTmExmnMng.tmExmnMng;
	private QTcUserMng tcUserMng 	= QTcUserMng.tcUserMng;
	private QTmSrvyInfo tmSrvyInfo 	= QTmSrvyInfo.tmSrvyInfo;
	private QTlExmnRslt tlExmnRslt	= QTlExmnRslt.tlExmnRslt;
	private QTlTrfvlRslt tlTrfvlRslt = QTlTrfvlRslt.tlTrfvlRslt;
	private QTlSrvyRslt tlSrvyRslt = QTlSrvyRslt.tlSrvyRslt;
	private QTcCdInfo  tcCdInfo 	= QTcCdInfo.tcCdInfo;
	private QTmExmnPollster tmExmnPollster = QTmExmnPollster.tmExmnPollster;

	/**
	 * methodName : getInvstList
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description :
	 *
	 * @param searchCommonDTO
	 * @param paging
	 * @return
	 * @Method Name : getInvstList
	 * @Method 설명 : 조사 목록 조회
	 * @작성일 : 2024. 2. 5.
	 * @작성자 : NK.KIM
	 */
	public List<TmExmnMngDTO> getInvstList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		// 해당 소속만 출력
		BooleanBuilder whereClause = new BooleanBuilder(searchOption(searchCommonDTO));
	    if (userInfo.getUserType() != UserTypeCd.SUPER) whereClause.and(tcUserMng.userBffltd.eq(userInfo.getUserBffltd()));
		
		List<TmExmnMngDTO> result = queryFactory.select(Projections.bean(
															TmExmnMngDTO.class, 
															tmExmnMng.exmnmngId, 
															tmExmnMng.usermngId,
															QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("mngrBffltd"),
															tcUserMng.userNm.coalesce("-").as("userNm") ,
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
												.where(whereClause)
												.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
												.orderBy(tmExmnMng.registDt.desc()).fetch();

		return result;
	}

	/**
	 * methodName : getInvstScheduleList
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description :
	 *
	 * @param searchCommonDTO
	 * @param paging
	 * @return
	 * @Method Name : getInvstScheduleList
	 * @Method 설명 : 조사 스케쥴 목록 조회
	 * @작성일 : 2024. 2. 5.
	 * @작성자 : NK.KIM
	 */
	public List<TmExmnScheduleDTO> getInvstScheduleList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		LocalDateTime currentDate = LocalDateTime.now();
		
		// 해당 소속만 출력
		BooleanBuilder whereClause = new BooleanBuilder(tmExmnMng.sttsCd.ne(ExmnSttsCd.INVEST_WRITING).and(scheduleSearchOption(searchCommonDTO)));
	    if (userInfo.getUserType() != UserTypeCd.SUPER) whereClause.and(tcUserMng.userBffltd.eq(userInfo.getUserBffltd()));
		
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
//																		new CaseBuilder()
//																		.when(tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(tmExmnMng.startDt.goe(LocalDateTime.now()).and(tmExmnPollster.pollsterId.count().eq(tmExmnMng.exmnNop.longValue())))).then(ExmnScheduleSttsCd.NOT_YET_PROGRESS.getCode())
//																		.when(tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(tmExmnMng.startDt.lt(LocalDateTime.now()).and(tmExmnPollster.pollsterId.count().eq(tmExmnMng.exmnNop.longValue())))).then(ExmnScheduleSttsCd.NOT_PROGRESS.getCode())
//																		.when(tmExmnPollster.pollsterId.count().lt(tmExmnMng.exmnNop)).then(ExmnScheduleSttsCd.NOT_YET_INVESTIGATOR.getCode())
//																		.when(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE).and(tmExmnPollster.pollsterId.count().eq(tmExmnMng.exmnNop.longValue()))).then(ExmnScheduleSttsCd.PROGRESS_COMPLETE.getCode())
//																		.when(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_PROGRESS).and(tmExmnPollster.pollsterId.count().eq(tmExmnMng.exmnNop.longValue()))).then(ExmnScheduleSttsCd.PROGRESSING.getCode())
																		new CaseBuilder()
												                        .when(tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(tmExmnMng.startDt.goe(currentDate).and(tmExmnPollster.pollsterId.count().gt(0L)))).then(ExmnScheduleSttsCd.NOT_YET_PROGRESS.getCode())
												                        .when(tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(tmExmnMng.startDt.lt(currentDate).and(tmExmnPollster.pollsterId.count().gt(0L)))).then(ExmnScheduleSttsCd.NOT_PROGRESS.getCode())
												                        .when(tmExmnPollster.pollsterId.count().eq(0L)).then(ExmnScheduleSttsCd.NOT_YET_INVESTIGATOR.getCode())
												                        .when(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE).and(tmExmnPollster.pollsterId.count().gt(0L))).then(ExmnScheduleSttsCd.PROGRESS_COMPLETE.getCode())
												                        .when(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_PROGRESS).and(tmExmnPollster.pollsterId.count().gt(0L))).then(ExmnScheduleSttsCd.PROGRESSING.getCode())
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
												.where(whereClause)
												.groupBy(tmExmnMng.exmnmngId)
												.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
												.orderBy(tmExmnMng.registDt.desc()).fetch();

		//조사 완료 / 조사 진행 시 조사 불가 사유 체크
		if(!CommonUtils.isListNull(result)){
			result.forEach(x -> {
									if("traffic".equals(x.getExmnType().getType())
										&& (x.getSttsCd().equals(ExmnSttsCd.INVEST_PROGRESS) || x.getSttsCd().equals(ExmnSttsCd.INVEST_COMPLETE))){
										String lcchgRsnChkVal = "N";
										Long lcchgRsnChkCnt = queryFactory.select(tlExmnRslt.countDistinct())
																			.from(tlExmnRslt)
																			.leftJoin(tlTrfvlRslt).on(tlExmnRslt.exmnrsltId.eq(tlTrfvlRslt.exmnrsltId))
																			.where(tlExmnRslt.exmnmngId.eq(x.getExmnmngId()).and(tlTrfvlRslt.lcchgRsn.isNotNull()))
																			.fetchOne();
										if(lcchgRsnChkCnt > 0) lcchgRsnChkVal = "Y";
										x.setLcchgRsnChkVal(lcchgRsnChkVal);
									}
								}
							);
		}

		return result;
	}

	/**
	 * methodName : getInvstScheduleListByNotSttsCd
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description :
	 *
	 * @param exmnSttsCd
	 * @return
	 * @Method Name : getInvstScheduleList
	 * @Method 설명 : 조사 스케쥴 목록 조회
	 * @작성일 : 2024. 2. 5.
	 * @작성자 : NK.KIM
	 */
	public List<TmExmnMngDTO> getInvstScheduleListByNotSttsCd(ExmnSttsCd exmnSttsCd) {
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		// 해당 소속만 출력
		BooleanBuilder whereClause = new BooleanBuilder(tmExmnMng.sttsCd.ne(exmnSttsCd));
	    if (userInfo.getUserType() != UserTypeCd.SUPER) whereClause.and(tcUserMng.userBffltd.eq(userInfo.getUserBffltd()));
		
		List<TmExmnMngDTO> result = queryFactory.select(Projections.bean(
																		TmExmnMngDTO.class, 
																		tmExmnMng.exmnmngId, 
																		tmExmnMng.usermngId,
																		QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("mngrBffltd"), 
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
																		.where(whereClause)
																		.orderBy(
														                        Expressions.numberTemplate(Integer.class, "age({0}, {1})",
														                                tmExmnMng.startDt, tmExmnMng.endDt).asc(),
														                        tmExmnMng.registDt.desc()
														                )
																		.fetch();
		
		return result;
	}

	/**
	 * methodName : getInvstInfo
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description :
	 *
	 * @param exmmngId
	 * @return exmn mng dto
	 * @Method Name : getInvstInfo
	 * @Method 설명 : 조사 목록 조회
	 * @작성일 : 2024. 2. 5.
	 * @작성자 : NK.KIM
	 */
	public TmExmnMngDTO getInvstInfo(String exmmngId) {
		
		TmExmnMngDTO result = queryFactory.select(Projections.bean(
													TmExmnMngDTO.class, 
													tmExmnMng.exmnmngId, 
													tmExmnMng.usermngId,
													tmSrvyInfo.srvyId,
													tmSrvyInfo.srvyTitle,
													tmSrvyInfo.cstmYn,
													QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("mngrBffltd"), 
													tcUserMng.userNm.coalesce("-").as("userNm"),
													tmExmnMng.exmnNm,
													tmExmnMng.exmnLc, 
													tmExmnMng.colrCd, 
													tmExmnMng.exmnType,
													tmExmnMng.exmnpicId, 
													tmExmnMng.laneCnt, 
													tmExmnMng.startDt,
													tmExmnMng.endDt,
													tmExmnMng.roadCd,
													tmExmnMng.exmnDistance,
													tmExmnMng.roadDescr,
													tmExmnMng.exmnDiv,
													tmExmnMng.exmnNop,
													tmExmnMng.partcptCd,
													tmExmnMng.goalCnt,
													tmExmnMng.lat,
													tmExmnMng.lon,
													tmExmnMng.exmnRange,
													tmExmnMng.sttsCd,
													tmExmnMng.dsdCd,
													tmExmnMng.tazCd,
													tmExmnMng.gnCd,
													tmExmnMng.cordonLine,
													tmExmnMng.tollBooth,
													tmExmnMng.screenLine,
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
	 * methodName : getSurveyGisDataDetail
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 조사 gis 상세 정보
	 *
	 * @param exmmngId
	 * @return survey gis detail dto
	 */
	public SurveyGISDetailDTO getSurveyGisDataDetail(String exmmngId) {

		SurveyGISDetailDTO result = queryFactory.select(Projections.bean(
																		SurveyGISDetailDTO.class,
																		tmExmnMng.exmnmngId,
																		tmExmnMng.usermngId,
																		tcUserMng.userNm.coalesce("-").as("userNm"),
																		tmExmnMng.exmnNm,
																		tmExmnMng.exmnLc,
																		tmExmnMng.colrCd,
																		tmExmnMng.exmnType,
																		tmExmnMng.exmnpicId,
																		tmExmnMng.laneCnt,
																		tmExmnMng.startDt,
																		tmExmnMng.endDt,
																		tmExmnMng.roadDescr,
																		tmExmnMng.exmnDiv,
																		tmExmnMng.exmnNop,
																		tmExmnMng.partcptCd,
																		tmExmnMng.goalCnt,
																		tmExmnMng.lat,
																		tmExmnMng.lon,
																		tmExmnMng.exmnRange,
																		tmExmnMng.sttsCd,
																		tmExmnMng.dsdCd,
																		tmExmnMng.tazCd,
																		tmExmnMng.gnCd,
																		tmExmnMng.cordonLine,
																		tmExmnMng.tollBooth,
																		tmExmnMng.screenLine,
																		tmExmnMng.registId)
																)
																.from(tmExmnMng)
																.leftJoin(tcUserMng).on(tmExmnMng.usermngId.eq(tcUserMng.usermngId))
																.where(tmExmnMng.exmnmngId.eq(exmmngId))
																.fetchOne();

		Long count = queryFactory.select(tlSrvyRslt.countDistinct())
									.from(tlExmnRslt)
									.leftJoin(tlSrvyRslt).on(tlExmnRslt.exmnrsltId.eq(tlSrvyRslt.exmnrsltId))
									.where(tlExmnRslt.exmnmngId.eq(exmmngId))
									.fetchOne();

		result.setRealExmnCnt(count);

		return result;
	}

	/**
	 * methodName : getTotalCount
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description :
	 *
	 * @param searchCommonDTO
	 * @return
	 * @Method Name : getTotalCount
	 * @Method 설명 : 조사 목록 개수 조회
	 * @작성일 : 2024. 2. 5.
	 * @작성자 : NK.KIM
	 */
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		// 해당 소속만 출력
		BooleanBuilder whereClause = new BooleanBuilder(searchOption(searchCommonDTO));
	    if (userInfo.getUserType() != UserTypeCd.SUPER) whereClause.and(tcUserMng.userBffltd.eq(userInfo.getUserBffltd()));

		Long count = queryFactory.select(tmExmnMng.countDistinct())
								.from(tmExmnMng)
								.where(whereClause)
								.leftJoin(tcUserMng).on(tmExmnMng.usermngId.eq(tcUserMng.usermngId))
								.fetchOne();

		return count;
	}

	/**
	 * methodName : getInvstScheduleTotalCount
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description :
	 *
	 * @param searchCommonDTO
	 * @return
	 * @Method Name : getInvstScheduleTotalCount
	 * @Method 설명 : 조사 스케쥴 목록 개수 조회
	 * @작성일 : 2024. 2. 5.
	 * @작성자 : NK.KIM
	 */
	public Long getInvstScheduleTotalCount(SearchCommonDTO searchCommonDTO) {
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		// 해당 소속만 출력
		BooleanBuilder whereClause = new BooleanBuilder(tmExmnMng.sttsCd.ne(ExmnSttsCd.INVEST_WRITING).and(scheduleSearchOption(searchCommonDTO)));
	    if (userInfo.getUserType() != UserTypeCd.SUPER) whereClause.and(tcUserMng.userBffltd.eq(userInfo.getUserBffltd()));
		
		Long count = queryFactory.select(tmExmnMng.countDistinct())
									.from(tmExmnMng)
									.leftJoin(tcUserMng).on(tmExmnMng.usermngId.eq(tcUserMng.usermngId))
									.leftJoin(tmExmnPollster).on(tmExmnMng.exmnmngId.eq(tmExmnPollster.exmnmngId))
									.where(whereClause)
									.fetchOne();
		
		return count;
	}

	/**
	 * methodName : getInvstScheduleDetailList
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description :
	 *
	 * @param paramDate
	 * @return
	 * @Method Name : getInvstScheduleDetailList
	 * @Method 설명 : 스케쥴 상세 목록
	 * @작성일 : 2024. 3. 29.
	 * @작성자 : NK.KIM
	 */
	public List<TmExmnMng> getInvstScheduleDetailList(String paramDate) {
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		LocalDateTime startDt = LocalDateTime.parse(paramDate + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LocalDateTime endDt = LocalDateTime.parse(paramDate + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		
		// 해당 소속만 출력
		BooleanBuilder whereClause = new BooleanBuilder(tmExmnMng.startDt.loe(startDt));
		whereClause.and(tmExmnMng.endDt.goe(endDt).and(tmExmnMng.sttsCd.ne(ExmnSttsCd.INVEST_WRITING)));
	    if (userInfo.getUserType() != UserTypeCd.SUPER) whereClause.and(tcUserMng.userBffltd.eq(userInfo.getUserBffltd()));
        
		List<TmExmnMng> result = queryFactory.select(tmExmnMng)
												.from(tmExmnMng)
												.leftJoin(tcUserMng).on(tmExmnMng.usermngId.eq(tcUserMng.usermngId))
												.where(whereClause)
												.orderBy(
								                        Expressions.numberTemplate(Integer.class, "age({0}, {1})",
								                                tmExmnMng.startDt, tmExmnMng.endDt).asc(),
								                        tmExmnMng.registDt.desc()
								                )
												.fetch();
		return result;
	}

	/**
	 * methodName : getInvstScheduleDetailTotalCount
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description :
	 *
	 * @param paramDate
	 * @return
	 * @Method Name : getInvstScheduleDetailTotalCnt
	 * @Method 설명 : 스케쥴 상세 목록 개수
	 * @작성일 : 2024. 4. 8.
	 * @작성자 : NK.KIM
	 */
	public int getInvstScheduleDetailTotalCount(String paramDate) {
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		LocalDateTime startDt = LocalDateTime.parse(paramDate + "T23:59:59", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		LocalDateTime endDt = LocalDateTime.parse(paramDate + "T00:00:00", DateTimeFormatter.ISO_LOCAL_DATE_TIME);
		
		// 해당 소속만 출력
		BooleanBuilder whereClause = new BooleanBuilder(tmExmnMng.startDt.loe(startDt));
		whereClause.and(tmExmnMng.endDt.goe(endDt).and(tmExmnMng.sttsCd.ne(ExmnSttsCd.INVEST_WRITING)));
	    if (userInfo.getUserType() != UserTypeCd.SUPER) whereClause.and(tcUserMng.userBffltd.eq(userInfo.getUserBffltd()));
			    
		Long result = queryFactory.select(tmExmnMng.countDistinct())
											.from(tmExmnMng)
											.leftJoin(tcUserMng).on(tmExmnMng.usermngId.eq(tcUserMng.usermngId))
											.where(whereClause)
											.fetchOne();
		return result.intValue();
	}


	/**
	 * methodName : getInvstScheduler
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description :
	 *
	 * @param exmnTypeCdArray
	 * @return
	 * @Method Name : searchOption
	 * @Method 설명 : 설문조사 상태 값 스케쥴러 데이터
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : NK.KIM
	 */
	public List<TmExmnMng> getInvstScheduler(ExmnTypeCd[] exmnTypeCdArray){
		LocalDateTime currentDate = LocalDateTime.now();
		//조사가 시작된 정보 및 오늘날짜 현재 시간 이전 데이터 호출
		List<TmExmnMng> result = queryFactory.selectFrom(tmExmnMng)
											.where(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_PROGRESS)
											.and(tmExmnMng.endDt.loe(currentDate))
											.and(tmExmnMng.exmnType.in(exmnTypeCdArray)))
											.fetch();
		return result;
	}

	/**
	 * methodName : getSurveyYears
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description :
	 *
	 * @param exmnTypeCd
	 * @return list
	 */
	public List<String> getSurveyYears(ExmnTypeCd exmnTypeCd){
		StringTemplate surveyStartDt = Expressions.stringTemplate(
				"to_char({0}, {1})",
				tmExmnMng.startDt,
				Expressions.constant("yyyy")
		);
		List<String> result = queryFactory.select(Projections.bean(
																	String.class,
																	surveyStartDt
																	)
												)
											.from(tmExmnMng)
											.where(tmExmnMng.exmnType.eq(exmnTypeCd)
											.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE)))
											.groupBy(surveyStartDt)
											.fetch();

		return result;
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
//	        	sttsCdExpression = tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).eq(tmExmnMng.exmnNop.longValue()).and(tmExmnMng.startDt.goe(currentDate)));
//	        	sttsCdExpression = tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(tmExmnMng.endDt.goe(currentDate).and(tmExmnPollster.pollsterId.count().ne(0L)).or(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_WRITE_COMPLETED).and(tmExmnPollster.pollsterId.count().ne(0L))));
	        	sttsCdExpression = tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).gt(0L).and(tmExmnMng.startDt.goe(currentDate))); 
	        } else if(ExmnScheduleSttsCd.NOT_PROGRESS.equals(exmnScheduleSttsCd)) {
	        	//미진행
	        	sttsCdExpression = 
//	        			tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).eq(tmExmnMng.exmnNop.longValue()).and(tmExmnMng.startDt.lt(currentDate)));
//	        			tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(tmExmnMng.endDt.lt(currentDate).and(tmExmnPollster.pollsterId.count().ne(0L)));
	        			tmExmnMng.sttsCd.notIn(ExmnSttsCd.INVEST_COMPLETE,ExmnSttsCd.INVEST_PROGRESS).and(queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).gt(0L).and(tmExmnMng.startDt.lt(currentDate)));
	        } else if(ExmnScheduleSttsCd.NOT_YET_INVESTIGATOR.equals(exmnScheduleSttsCd)) {
	        	//조사원 미등록
	        	sttsCdExpression = 
//	        			queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).lt(tmExmnMng.exmnNop.longValue());
//	        			tmExmnPollster.pollsterId.count().eq(0L);
	        			queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).eq(0L);
	        } else if(ExmnScheduleSttsCd.PROGRESSING.equals(exmnScheduleSttsCd)) {
	        	//진행중
	        	sttsCdExpression = 
//	        			tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_PROGRESS).and(queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).eq(tmExmnMng.exmnNop.longValue()));
//	        			tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_PROGRESS).and(tmExmnPollster.pollsterId.count().ne(0L));
	        			tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_PROGRESS).and(queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).gt(0L));
	        } else if(ExmnScheduleSttsCd.PROGRESS_COMPLETE.equals(exmnScheduleSttsCd)) {
	        	//진행 완료
	        	sttsCdExpression = 
//	        			tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE).and(queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).eq(tmExmnMng.exmnNop.longValue()));
//	        			tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE).and(tmExmnPollster.pollsterId.count().ne(0L));
	        			tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE).and(queryFactory.select(tmExmnPollster.pollsterId.countDistinct()).from(tmExmnPollster).where(tmExmnPollster.exmnmngId.eq(tmExmnMng.exmnmngId)).gt(0L));
	        }
	        
			result = (result != null) ? result.and(sttsCdExpression) : sttsCdExpression;
	    }
	    if (searchCommonDTO.getSearchContent() != null) {
	    	BooleanExpression sttsCdExpression = QRepositorySupport.containsKeyword(tmExmnMng.exmnNm, searchCommonDTO.getSearchContent());
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
