package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSideDestinationTypeYy;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSideDestinationTypeYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSideDestinationTypeYy tsRoadSideDestinationTypeYy = QTsRoadSideDestinationTypeYy.tsRoadSideDestinationTypeYy;
//	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;


	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getTypeOfArrivalPointList(TlExmnRsltStatisticsSearchDTO searchDTO){

		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSideDestinationTypeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSideDestinationTypeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(Projections.bean(
																											TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
																											tsRoadSideDestinationTypeYy.destinationType.as("name"),
																											tsRoadSideDestinationTypeYy.cnt.sum().as("value")
																											)
																									)
																									.from(tsRoadSideDestinationTypeYy)
//																									.innerJoin(tmExmnMng).on(tsRoadSideDestinationTypeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
																									.where(tsRoadSideDestinationTypeYy.statsYy.eq(searchDTO.getSearchDate())
																									.and(departureTazCode.eq(searchDTO.getSearchCd()))
//																									.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
																									.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
																									.groupBy(tsRoadSideDestinationTypeYy.destinationType)
																									.orderBy(tsRoadSideDestinationTypeYy.cnt.sum().desc())
																									.fetch();
		return result;
	}
}
