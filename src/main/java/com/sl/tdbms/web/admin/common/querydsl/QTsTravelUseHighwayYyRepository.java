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
import com.sl.tdbms.web.admin.common.entity.QTsTravelUseHighwayYy;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelUseHighwayYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelUseHighwayYy tsTravelUseHighwayYy = QTsTravelUseHighwayYy.tsTravelUseHighwayYy;

	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;
	
	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getTravelUseHighwayList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelUseHighwayYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelUseHighwayYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(Projections.bean(
			TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
			tsTravelUseHighwayYy.useHighway.as("name"),
			tsTravelUseHighwayYy.cnt.sum().as("value")
			)
		)
		.from(tsTravelUseHighwayYy)
		.innerJoin(tmExmnMng).on(tsTravelUseHighwayYy.exmnmngId.eq(tmExmnMng.exmnmngId))
		.where(tsTravelUseHighwayYy.statsYy.eq(searchDTO.getSearchDate())
		.and(departureTazCode.eq(searchDTO.getSearchCd()))
		.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
		.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
		.groupBy(tsTravelUseHighwayYy.useHighway)
		.orderBy(tsTravelUseHighwayYy.cnt.sum().desc())
		.fetch();
			
		return result;
	}
}
