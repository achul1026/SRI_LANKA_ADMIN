package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSideDepartureTypeYy;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSideDepartureTypeYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSideDepartureTypeYy tsRoadSideDepartureTypeYy = QTsRoadSideDepartureTypeYy.tsRoadSideDepartureTypeYy;

//	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;

	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getTypeOfDeparturePointList(TlExmnRsltStatisticsSearchDTO searchDTO){

		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSideDepartureTypeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSideDepartureTypeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(Projections.bean(
																											TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
																											tsRoadSideDepartureTypeYy.departureType.as("name"),
																											tsRoadSideDepartureTypeYy.cnt.sum().as("value")
																											)
																									)
																									.from(tsRoadSideDepartureTypeYy)
//																									.innerJoin(tmExmnMng).on(tsRoadSideDepartureTypeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
																									.where(tsRoadSideDepartureTypeYy.statsYy.eq(searchDTO.getSearchDate())
																									.and(departureTazCode.eq(searchDTO.getSearchCd()))
//																									.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
																									.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
																									.groupBy(tsRoadSideDepartureTypeYy.departureType)
																									.orderBy(tsRoadSideDepartureTypeYy.cnt.sum().desc())
																									.fetch();
		return result;
	}
}
