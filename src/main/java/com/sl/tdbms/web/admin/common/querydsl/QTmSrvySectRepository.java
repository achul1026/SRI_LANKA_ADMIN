package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TmSrvySect;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.entity.QTmSrvySect;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmSrvySectRepository {

	private final JPAQueryFactory queryFactory;

	private QTmSrvySect tmSrvySect = QTmSrvySect.tmSrvySect;
	
	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;
	
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
	
	
	public List<TmSrvySect> getSrvySectList(String srvyId){
		List<TmSrvySect> result = queryFactory.select(Projections.bean(
																		TmSrvySect.class,
																		tmSrvySect.sectId, 
																		tmSrvySect.srvyId, 
																		tmSrvySect.sectTitle, 
																		tmSrvySect.sectSubtitle, 
																		tmSrvySect.sectType,
																		QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("sectTypeNm"),
																		tmSrvySect.sectSqno 
																		)
														)
														.from(tmSrvySect)
														.leftJoin(tcCdInfo).on(tmSrvySect.sectType.eq(tcCdInfo.cd))
														.where(tmSrvySect.srvyId.eq(srvyId))
														.orderBy(tmSrvySect.sectSqno.asc())
														.fetch();
		return result;
	}
}
