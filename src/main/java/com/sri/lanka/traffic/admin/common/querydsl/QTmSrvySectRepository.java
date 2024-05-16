package com.sri.lanka.traffic.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.entity.QTmSrvySect;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmSrvySectRepository {

	private final JPAQueryFactory queryFactory;

	private QTmSrvySect tmSrvySect = QTmSrvySect.tmSrvySect;
	
	/**
	  * @Method Name : deleteByIdArr
	  * @작성일 : 2024. 5. 7.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 부문 삭제
	  * @param qstnIdArr
	  */
	public void deleteByIdArr(String[] sectIdArr) {
		queryFactory.delete(tmSrvySect).where(tmSrvySect.sectId.in(sectIdArr)).execute();
	}
}
