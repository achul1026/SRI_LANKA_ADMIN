package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTmSrvyQstn;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmSrvyQstnRepository {

	private final JPAQueryFactory queryFactory;

	private QTmSrvyQstn tmSrvyQstn = QTmSrvyQstn.tmSrvyQstn;
	
	
	/**
	  * @Method Name : deleteByIdArr
	  * @작성일 : 2024. 5. 7.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 질문 정보 삭제
	  * @param qstnIdArr
	  */
	public void deleteByIdArr(String[] qstnIdArr) {
		queryFactory.delete(tmSrvyQstn).where(tmSrvyQstn.qstnId.in(qstnIdArr)).execute();
	}
	
	/**
	  * @Method Name : getQstnIdArrBySectIdArr
	  * @작성일 : 2024. 5. 7.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 질문 ID 정보 조회
	  * @param sectIdArr
	  * @return
	  */
	public String[] getQstnIdArrBySectIdArr(String[] sectIdArr) {

		List<String> result = queryFactory.select(tmSrvyQstn.qstnId)
									.from(tmSrvyQstn)
									.where(tmSrvyQstn.sectId.in(sectIdArr))
									.fetch();

		return result.toArray(String[]::new);
	}
}
