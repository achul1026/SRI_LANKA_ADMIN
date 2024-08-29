package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTmExmnMng;
import com.sl.tdbms.web.admin.common.entity.QTmExmnPollster;
import com.sl.tdbms.web.admin.common.entity.TmExmnPollster;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

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
	public List<TmExmnPollster> getPollsterListByInvstDate(LocalDateTime startDt, LocalDateTime endDt, String pollsterTel, String pollsterEmail) {
		List<TmExmnPollster> result = queryFactory.select(tmExmnPollster)
												.from(tmExmnMng)
												.innerJoin(tmExmnPollster).on(tmExmnMng.exmnmngId.eq(tmExmnPollster.exmnmngId))
												.where(tmExmnPollster.pollsterId.isNotNull()
												        .and(tmExmnMng.startDt.between(startDt, endDt).or(tmExmnMng.endDt.between(startDt, endDt)))
												        .and(QRepositorySupport.toEqExpression(tmExmnPollster.pollsterTel, pollsterTel)
												        		.or(QRepositorySupport.toEqExpression(tmExmnPollster.pollsterEmail,pollsterEmail)))
																.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_PROGRESS))
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
