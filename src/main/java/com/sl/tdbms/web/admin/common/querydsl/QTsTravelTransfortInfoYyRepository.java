package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.QTmExmnMng;
import com.sl.tdbms.web.admin.common.entity.QTsTravelTransfortInfoYy;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelTransfortInfoYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelTransfortInfoYy tsTravelTransfortInfoYy = QTsTravelTransfortInfoYy.tsTravelTransfortInfoYy;

	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;
	
	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getTravelTransfortCntList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelTransfortInfoYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelTransfortInfoYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(Projections.bean(
			TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
	        Expressions.cases()
            .when(tsTravelTransfortInfoYy.transfortCount.eq(0L))
            .then("No transfer")
            .otherwise(tsTravelTransfortInfoYy.transfortCount.stringValue())
            .as("name"),
			tsTravelTransfortInfoYy.cnt.sum().as("value")
			)
		)
		.from(tsTravelTransfortInfoYy)
		.innerJoin(tmExmnMng).on(tsTravelTransfortInfoYy.exmnmngId.eq(tmExmnMng.exmnmngId))
		.where(tsTravelTransfortInfoYy.statsYy.eq(searchDTO.getSearchDate())
		.and(departureTazCode.eq(searchDTO.getSearchCd()))
		.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
		.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
		.groupBy(tsTravelTransfortInfoYy.transfortCount)
		.orderBy(tsTravelTransfortInfoYy.cnt.sum().desc())
		.fetch();
			
		return result;
	}

	public Double getAvgTravelTransfortHour(TlExmnRsltStatisticsSearchDTO searchDTO) {
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelTransfortInfoYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelTransfortInfoYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(tsTravelTransfortInfoYy.statsYy.eq(searchDTO.getSearchDate()));
        builder.and(departureTazCode.eq(searchDTO.getSearchCd()));
        builder.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd()));
        builder.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE));
        
        Double result = queryFactory.select(
                Expressions.numberTemplate(Double.class, "ROUND({0}, 1)", tsTravelTransfortInfoYy.avgTransfortMinutes.avg()).coalesce(0.0)
            )
	    .from(tsTravelTransfortInfoYy)
		.innerJoin(tmExmnMng).on(tsTravelTransfortInfoYy.exmnmngId.eq(tmExmnMng.exmnmngId))
	    .where(builder)
	    .fetchOne();
		
		return result;
	}
}
