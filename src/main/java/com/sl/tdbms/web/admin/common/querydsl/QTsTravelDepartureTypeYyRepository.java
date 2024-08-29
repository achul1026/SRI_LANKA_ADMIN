package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.QTmExmnMng;
import com.sl.tdbms.web.admin.common.entity.QTsTravelDepartureTypeYy;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelDepartureTypeYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelDepartureTypeYy tsTravelDepartureTypeYy = QTsTravelDepartureTypeYy.tsTravelDepartureTypeYy;

	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;
	
	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getTravelPurposeList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelDepartureTypeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelDepartureTypeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(Projections.bean(
			TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
			tsTravelDepartureTypeYy.departureType.as("name"),
			tsTravelDepartureTypeYy.cnt.sum().as("value")
			)
		)
		.from(tsTravelDepartureTypeYy)
		.innerJoin(tmExmnMng).on(tsTravelDepartureTypeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
		.where(tsTravelDepartureTypeYy.statsYy.eq(searchDTO.getSearchDate())
		.and(departureTazCode.eq(searchDTO.getSearchCd()))
		.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
		.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
		.groupBy(tsTravelDepartureTypeYy.departureType)
		.orderBy(tsTravelDepartureTypeYy.cnt.sum().desc())
		.fetch();
			
		return result;
	}
}
