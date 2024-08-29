package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.core.types.Projections;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.QTlSrvyAns;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.QTlSrvyRslt;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QTlSrvyRsltRepository {

	private final JPAQueryFactory queryFactory;

	private QTlSrvyRslt tlSrvyRslt = QTlSrvyRslt.tlSrvyRslt;
	private QTlSrvyAns tlSrvyAns = QTlSrvyAns.tlSrvyAns;


	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getSurveyStatisticsListBySrvyrsltIdListAndType(List<String> srvyrsltIdList, String[] typeArr){

		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(Projections.bean(
																												TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
																												tlSrvyAns.ansCnts.as("name"),
																												tlSrvyAns.ansCnts.count().as("value")
																					))
																					.from(tlSrvyRslt)
																					.innerJoin(tlSrvyAns).on(tlSrvyRslt.srvyrsltId.eq(tlSrvyAns.srvyrsltId).and(tlSrvyAns.ansCnts.isNotNull()))
																					.where(tlSrvyRslt.srvyrsltId.in(srvyrsltIdList)
																					.and(tlSrvyAns.srvyMetadataCd.in(typeArr)))
																					.groupBy(tlSrvyAns.ansCnts)
																					.fetch();

		return result;
	}

}
