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
import com.sl.tdbms.web.admin.common.entity.QTsTravelDestinationTypeYy;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelDestinationTypeYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelDestinationTypeYy tsTravelDestinationTypeYy = QTsTravelDestinationTypeYy.tsTravelDestinationTypeYy;

	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;
	
	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getTravelDepartureList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelDestinationTypeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelDestinationTypeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(Projections.bean(
			TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
			tsTravelDestinationTypeYy.destinationType.as("name"),
			tsTravelDestinationTypeYy.cnt.sum().as("value")
			)
		)
		.from(tsTravelDestinationTypeYy)
		.innerJoin(tmExmnMng).on(tsTravelDestinationTypeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
		.where(tsTravelDestinationTypeYy.statsYy.eq(searchDTO.getSearchDate())
		.and(departureTazCode.eq(searchDTO.getSearchCd()))
		.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
		.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
		.groupBy(tsTravelDestinationTypeYy.destinationType)
		.orderBy(tsTravelDestinationTypeYy.cnt.sum().desc())
		.fetch();
			
		return result;
	}
}
