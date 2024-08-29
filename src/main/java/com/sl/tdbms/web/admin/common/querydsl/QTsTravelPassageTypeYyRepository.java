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
import com.sl.tdbms.web.admin.common.entity.QTsTravelPassageTypeYy;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelPassageTypeYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelPassageTypeYy qTsTravelPassageTypeYy = QTsTravelPassageTypeYy.tsTravelPassageTypeYy;

	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;
	
	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getTravelModeTransportationList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", qTsTravelPassageTypeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", qTsTravelPassageTypeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(Projections.bean(
			TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
			qTsTravelPassageTypeYy.passageType.as("name"),
			qTsTravelPassageTypeYy.cnt.sum().as("value")
			)
		)
		.from(qTsTravelPassageTypeYy)
		.innerJoin(tmExmnMng).on(qTsTravelPassageTypeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
		.where(qTsTravelPassageTypeYy.statsYy.eq(searchDTO.getSearchDate())
		.and(departureTazCode.eq(searchDTO.getSearchCd()))
		.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
		.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
		.groupBy(qTsTravelPassageTypeYy.passageType)
		.orderBy(qTsTravelPassageTypeYy.cnt.sum().desc())
		.fetch();
			
		return result;
	}

	public Double getAvgTravelModeTransportationList(TlExmnRsltStatisticsSearchDTO searchDTO) {
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", qTsTravelPassageTypeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", qTsTravelPassageTypeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(qTsTravelPassageTypeYy.statsYy.eq(searchDTO.getSearchDate()));
        builder.and(departureTazCode.eq(searchDTO.getSearchCd()));
        builder.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd()));
        builder.and(qTsTravelPassageTypeYy.avgPassengers.ne(0L));
        builder.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE));
        
        if (searchDTO.getSearchContent() != null && !"".equals(searchDTO.getSearchContent())) {
            builder.and(qTsTravelPassageTypeYy.passageType.eq(searchDTO.getSearchContent()));
        }
        
		Double result = queryFactory.select(
				qTsTravelPassageTypeYy.avgPassengers.avg().round().coalesce(0.0)
				)
	    .from(qTsTravelPassageTypeYy)
		.innerJoin(tmExmnMng).on(qTsTravelPassageTypeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
	    .where(builder)
	    .fetchOne();
		
		return result;
	}
	
	
	public List<String> getSearchList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", qTsTravelPassageTypeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", qTsTravelPassageTypeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<String> result = queryFactory.select(qTsTravelPassageTypeYy.passageType.as("selectItem"))
											.from(qTsTravelPassageTypeYy)
											.innerJoin(tmExmnMng).on(qTsTravelPassageTypeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
											.where(qTsTravelPassageTypeYy.statsYy.eq(searchDTO.getSearchDate())
													.and(departureTazCode.eq(searchDTO.getSearchCd()))
													.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
													.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
											.groupBy(qTsTravelPassageTypeYy.passageType)
											.orderBy(qTsTravelPassageTypeYy.passageType.desc())
											.fetch();
		return result;
	}
}
