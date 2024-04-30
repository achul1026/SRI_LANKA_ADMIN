package com.sri.lanka.traffic.admin.common.querydsl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTmExmnMng;
import com.sri.lanka.traffic.admin.common.entity.QTmExmnPollster;
import com.sri.lanka.traffic.admin.common.entity.TmExmnPollster;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmExmnPollsterRepository {

	private final JPAQueryFactory queryFactory;

	private QTmExmnPollster tmExmnPollster = QTmExmnPollster.tmExmnPollster;
	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;
	
	/**
	  * @Method Name : getPollsterListByInvstDate
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 같은 기간내에 조사원 목록
	  * @param startDt
	  * @param endDt
	  * @return
	  */
	public List<TmExmnPollster> getPollsterListByInvstDate(LocalDateTime startDt,LocalDateTime endDt,String pollsterTel, String pollsterEmail) {
		List<TmExmnPollster> result = queryFactory.select(tmExmnPollster)
												.from(tmExmnMng)
												.innerJoin(tmExmnPollster).on(tmExmnMng.exmnmngId.eq(tmExmnPollster.exmnmngId))
												.where(tmExmnPollster.pollsterId.isNotNull()
												        .and(tmExmnMng.startDt.between(startDt, endDt).or(tmExmnMng.endDt.between(startDt, endDt)))
												        .and(QRepositorySupport.toEqExpression(tmExmnPollster.pollsterTel, pollsterTel)
												        		.or(QRepositorySupport.toEqExpression(tmExmnPollster.pollsterEmail,pollsterEmail)))
												        )
												.fetch();
		return result;
	}
	
	
	/**
	  * @Method Name : deleteByIdArr
	  * @작성일 : 2024. 3. 27.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사원 목록 삭제
	  * @param pollsterIdArr
	  */
	public void deleteByIdArr(String[] pollsterIdArr) {
		queryFactory.delete(tmExmnPollster).where(tmExmnPollster.pollsterId.in(pollsterIdArr)).execute();
	}
}
