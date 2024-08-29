package com.sl.tdbms.web.admin.common.querydsl;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTmExmnDrct;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmExmnDrctRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTmExmnDrct tmExmnDrct = QTmExmnDrct.tmExmnDrct;
	
	/**
	  * @Method Name : deleteByIdArr
	  * @작성일 : 2024. 3. 27.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 방향 목록 삭제
	  * @param drctIdArr
	  */
	public void deleteByIdArr(String[] drctIdArr) {
		queryFactory.delete(tmExmnDrct).where(tmExmnDrct.exmndrctId.in(drctIdArr)).execute();
	}
}
