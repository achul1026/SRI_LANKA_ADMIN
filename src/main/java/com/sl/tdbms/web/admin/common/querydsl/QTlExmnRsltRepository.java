package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.invst.TmExmnPollsterDTO;
import com.sl.tdbms.web.admin.common.entity.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QTlExmnRsltRepository {

	private final JPAQueryFactory queryFactory;

	private QTlExmnRslt tlExmnRslt = QTlExmnRslt.tlExmnRslt;
	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;

	private QTlSrvyRslt tlSrvyRslt = QTlSrvyRslt.tlSrvyRslt;
	private QTlSrvyAns tlSrvyAns = QTlSrvyAns.tlSrvyAns;
	private QTmExmnPollster tmExmnPollster = QTmExmnPollster.tmExmnPollster;
	private QTlTrfvlRslt tlTrfvlRslt = QTlTrfvlRslt.tlTrfvlRslt;
	
	
	/**
	  * @Method Name : getSruveyCompletedCnt
	  * @작성일 : 2024. 5. 17.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 조사 달성 개수
	  * @param exmnmngId
	  * @return
	  */
	public int getSruveyCompletedCnt(String exmnmngId,String startlcNm,String endlcNm) {
		
		Long count = queryFactory.select(tlSrvyRslt.countDistinct())
									.from(tlExmnRslt)
									.leftJoin(tlSrvyRslt).on(tlExmnRslt.exmnrsltId.eq(tlSrvyRslt.exmnrsltId)
															.and(QRepositorySupport.toEqExpression(tlSrvyRslt.startlcNm, startlcNm))
															.and(QRepositorySupport.toEqExpression(tlSrvyRslt.endlcNm, endlcNm))	
															)
									.where(tlExmnRslt.exmnmngId.eq(exmnmngId).and(tlSrvyRslt.srvyrsltId.isNotNull()))
									.fetchOne();
		return count.intValue();
	}

	public List<String> getSrvyrsltIdList(String exmnmngId){

		List<String> result = queryFactory.select(tlSrvyRslt.srvyrsltId).distinct()
										.from(tlSrvyRslt)
										.leftJoin(tlExmnRslt).on(tlExmnRslt.exmnmngId.eq(exmnmngId))
										.leftJoin(tlSrvyRslt).on(tlExmnRslt.exmnrsltId.eq(tlSrvyRslt.exmnrsltId))
										.leftJoin(tlSrvyAns).on(tlSrvyRslt.srvyrsltId.eq(tlSrvyAns.srvyrsltId))
										.where(tlSrvyAns.isNotNull())
										.fetch();
 		return result;
	}

	/**
	  * @Method Name : getTrafficPollsterList
	  * @작성일 : 2024. 8. 6.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 교통 조사 조사원 목록 조회
	  * @param exmnmngId
	  * @return
	  */
	public List<TmExmnPollsterDTO> getTrafficPollsterList(String exmnmngId) {
		
		List<TmExmnPollsterDTO> result = queryFactory.select(Projections.bean(TmExmnPollsterDTO.class,
														tmExmnPollster.pollsterId,
														tmExmnPollster.exmnmngId,
														tmExmnPollster.mngrId,
														tmExmnPollster.pollsterNm,
														tmExmnPollster.pollsterEmail,
														tmExmnPollster.pollsterType,
														tmExmnPollster.pollsterTel,
														tmExmnPollster.pollsterBrdt,
														new CaseBuilder()
														.when(tlTrfvlRslt.trfvlmexmnId.isNotNull()).then("Y").otherwise("N").as("pollsterYn")
														))
											.from(tmExmnMng)
											.innerJoin(tmExmnPollster).on(tmExmnMng.exmnmngId.eq(tmExmnPollster.exmnmngId))
											.leftJoin(tlExmnRslt).on(tmExmnMng.exmnmngId.eq(tlExmnRslt.exmnmngId))
											.leftJoin(tlTrfvlRslt).on(tlExmnRslt.exmnrsltId.eq(tlTrfvlRslt.exmnrsltId)
													.and(tmExmnPollster.pollsterTel.eq(tlTrfvlRslt.pollsterTel)))
											.where(tmExmnMng.exmnmngId.eq(exmnmngId))
											.distinct()
											.fetch();
		return result;
	}

	/**
	  * @Method Name : getSurveyPollsterList
	  * @작성일 : 2024. 8. 6.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 설문 조사 조사원 목록 조회
	  * @param exmnmngId
	  * @return
	  */
	public List<TmExmnPollsterDTO> getSurveyPollsterList(String exmnmngId) {
		List<TmExmnPollsterDTO> result = queryFactory.select(Projections.bean(TmExmnPollsterDTO.class,
														tmExmnPollster.pollsterId,
														tmExmnPollster.exmnmngId,
														tmExmnPollster.mngrId,
														tmExmnPollster.pollsterNm,
														tmExmnPollster.pollsterEmail,
														tmExmnPollster.pollsterType,
														tmExmnPollster.pollsterTel,
														tmExmnPollster.pollsterBrdt,
														new CaseBuilder()
														.when(tlSrvyRslt.srvyrsltId.isNotNull()).then("Y").otherwise("N").as("pollsterYn")
														))
											.from(tmExmnMng)
											.innerJoin(tmExmnPollster).on(tmExmnMng.exmnmngId.eq(tmExmnPollster.exmnmngId))
											.leftJoin(tlExmnRslt).on(tmExmnMng.exmnmngId.eq(tlExmnRslt.exmnmngId))
											.leftJoin(tlSrvyRslt).on(tlExmnRslt.exmnrsltId.eq(tlSrvyRslt.exmnrsltId)
														.and(tmExmnPollster.pollsterTel.eq(tlSrvyRslt.pollsterTel)))
											.where(tmExmnMng.exmnmngId.eq(exmnmngId))
											.distinct()
											.fetch();
		return result;
	}
	
	/**
	  * @Method Name : getFinishPollsterCnt
	  * @작성일 : 2024. 8. 7.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 교통 조사 완료한 조사원 수 조회
	  * @param exmnmngId
	  * @return
	  */
	public long getTrafficRegistExmnPollsterCnt(String exmnmngId) {
		long result = queryFactory.select(tlTrfvlRslt.trfvlmexmnId.count())
				.from(tmExmnMng)
				.innerJoin(tmExmnPollster).on(tmExmnMng.exmnmngId.eq(tmExmnPollster.exmnmngId))
				.leftJoin(tlExmnRslt).on(tmExmnMng.exmnmngId.eq(tlExmnRslt.exmnmngId))
				.leftJoin(tlTrfvlRslt).on(tlExmnRslt.exmnrsltId.eq(tlTrfvlRslt.exmnrsltId)
						.and(tmExmnPollster.pollsterTel.eq(tlTrfvlRslt.pollsterTel)))
				.where(tmExmnMng.exmnmngId.eq(exmnmngId)
						.and(tlTrfvlRslt.trfvlmexmnId.isNotNull()))
				.distinct()
				.fetchOne();
		return result;
	}

	/**
	  * @Method Name : getSurveyRegistExmnPollsterCnt
	  * @작성일 : 2024. 8. 7.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 설문 조사 완료한 조사원 수 조회
	  * @param exmnmngId
	  * @return
	  */
	public long getSurveyRegistExmnPollsterCnt(String exmnmngId) {
		long result = queryFactory.select(tlSrvyRslt.srvyrsltId.count())
				.from(tmExmnMng)
				.innerJoin(tmExmnPollster).on(tmExmnMng.exmnmngId.eq(tmExmnPollster.exmnmngId))
				.leftJoin(tlExmnRslt).on(tmExmnMng.exmnmngId.eq(tlExmnRslt.exmnmngId))
				.leftJoin(tlSrvyRslt).on(tlExmnRslt.exmnrsltId.eq(tlSrvyRslt.exmnrsltId)
						.and(tmExmnPollster.pollsterTel.eq(tlSrvyRslt.pollsterTel)))
				.where(tmExmnMng.exmnmngId.eq(exmnmngId)
						.and(tlSrvyRslt.srvyrsltId.isNotNull()))
				.distinct()
				.fetchOne();
		return result;
	}
	
}
