package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.QTsRoadSidePurposeYy;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSidePurposeYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSidePurposeYy tsRoadSidePurposeYy = QTsRoadSidePurposeYy.tsRoadSidePurposeYy;
//	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;


	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getPurposeOfTravelList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePurposeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePurposeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(Projections.bean(
																											TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
																											tsRoadSidePurposeYy.tripPurpose.as("name"),
																											tsRoadSidePurposeYy.cnt.sum().as("value")
																											)
																									)
																									.from(tsRoadSidePurposeYy)
//																									.innerJoin(tmExmnMng).on(tsRoadSidePurposeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
																									.where(tsRoadSidePurposeYy.statsYy.eq(searchDTO.getSearchDate())
																									.and(departureTazCode.eq(searchDTO.getSearchCd()))
//																									.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
																									.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
																									.groupBy(tsRoadSidePurposeYy.tripPurpose)
																									.orderBy(tsRoadSidePurposeYy.cnt.sum().desc())
																									.fetch();
		return result;
	}

	public List<TlSrvyRsltStatisticsDTO.PurposeStatisticsInfo> getHourTransportationlList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePurposeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePurposeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(tsRoadSidePurposeYy.statsYy.eq(searchDTO.getSearchDate()));
        builder.and(departureTazCode.eq(searchDTO.getSearchCd()));
        builder.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd()));
//        builder.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE));
        
        if (searchDTO.getSearchContent() != null && !"".equals(searchDTO.getSearchContent())) {
            builder.and(tsRoadSidePurposeYy.tripPurpose.eq(searchDTO.getSearchContent()));
        }
		
		List<TlSrvyRsltStatisticsDTO.PurposeStatisticsInfo> result = queryFactory.select(Projections.bean(
				TlSrvyRsltStatisticsDTO.PurposeStatisticsInfo.class,
				tsRoadSidePurposeYy.tripPurpose.as("tripPurpose"),
                new CaseBuilder()
                        .when(tsRoadSidePurposeYy.travelTime.between(0, 15)).then(tsRoadSidePurposeYy.cnt)
                        .otherwise(0L).sum().as("minGroup0To15"),
                new CaseBuilder()
                        .when(tsRoadSidePurposeYy.travelTime.between(16, 30)).then(tsRoadSidePurposeYy.cnt)
                        .otherwise(0L).sum().as("minGroup16To30"),
                new CaseBuilder()
                        .when(tsRoadSidePurposeYy.travelTime.between(31, 45)).then(tsRoadSidePurposeYy.cnt)
                        .otherwise(0L).sum().as("minGroup31To45"),
                new CaseBuilder()
                        .when(tsRoadSidePurposeYy.travelTime.between(46, 60)).then(tsRoadSidePurposeYy.cnt)
                        .otherwise(0L).sum().as("minGroup46To60"),
                new CaseBuilder()
                        .when(tsRoadSidePurposeYy.travelTime.between(61, 90)).then(tsRoadSidePurposeYy.cnt)
                        .otherwise(0L).sum().as("minGroup61To90"),
                new CaseBuilder()
                        .when(tsRoadSidePurposeYy.travelTime.between(91, 120)).then(tsRoadSidePurposeYy.cnt)
                        .otherwise(0L).sum().as("minGroup91To120"),
                new CaseBuilder()
                        .when(tsRoadSidePurposeYy.travelTime.between(121, 150)).then(tsRoadSidePurposeYy.cnt)
                        .otherwise(0L).sum().as("minGroup121To150"),
                new CaseBuilder()
                        .when(tsRoadSidePurposeYy.travelTime.between(151, 180)).then(tsRoadSidePurposeYy.cnt)
                        .otherwise(0L).sum().as("minGroup151To180"),
                new CaseBuilder()
                        .when(tsRoadSidePurposeYy.travelTime.gt(180)).then(tsRoadSidePurposeYy.cnt)
                        .otherwise(0L).sum().as("minGroup180Above")
        ))
			.from(tsRoadSidePurposeYy)
//			.innerJoin(tmExmnMng).on(tsRoadSidePurposeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
			.where(builder)
			.groupBy(tsRoadSidePurposeYy.tripPurpose)
			.orderBy(tsRoadSidePurposeYy.tripPurpose.asc())
			.fetch();
		return result;
	}

	public List<String> getSearchList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePurposeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePurposeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<String> result =queryFactory.select(
										tsRoadSidePurposeYy.tripPurpose.as("selectItem"))
										.from(tsRoadSidePurposeYy)
//										.innerJoin(tmExmnMng).on(tsRoadSidePurposeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
										.where(tsRoadSidePurposeYy.statsYy.eq(searchDTO.getSearchDate())
												.and(departureTazCode.eq(searchDTO.getSearchCd()))
//												.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
												.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
										.groupBy(tsRoadSidePurposeYy.tripPurpose)
										.orderBy(tsRoadSidePurposeYy.tripPurpose.desc()) .fetch();

		return result;
	}
}
