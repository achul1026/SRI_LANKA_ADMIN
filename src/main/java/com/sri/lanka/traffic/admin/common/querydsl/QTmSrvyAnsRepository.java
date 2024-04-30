package com.sri.lanka.traffic.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTmSrvyAns;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmSrvyAnsRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTmSrvyAns tmSrvyAns = QTmSrvyAns.tmSrvyAns;
	
	/**
	  * @Method Name : deleteByIdArr
	  * @작성일 : 2024. 3. 27.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 답변 목록 삭제
	  * @param ansIdArr
	  */
	public void deleteByIdArr(String[] ansIdArr) {
		queryFactory.delete(tmSrvyAns).where(tmSrvyAns.ansId.in(ansIdArr)).execute();
	}
}
