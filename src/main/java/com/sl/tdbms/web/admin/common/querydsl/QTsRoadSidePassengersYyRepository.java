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
import com.sl.tdbms.web.admin.common.entity.QTsRoadSidePassengersYy;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSidePassengersYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSidePassengersYy tsRoadSidePassengersYy = QTsRoadSidePassengersYy.tsRoadSidePassengersYy;
//	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;


	public List<TlSrvyRsltStatisticsDTO.PassengerStatisticsInfo> getNumberOfPassengersList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePassengersYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePassengersYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(tsRoadSidePassengersYy.statsYy.eq(searchDTO.getSearchDate()));
        builder.and(tsRoadSidePassengersYy.avgPassengers.ne("0"));
        builder.and(departureTazCode.eq(searchDTO.getSearchCd()));
        builder.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd()));
//        builder.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE));
        
        if (searchDTO.getSearchContent() != null && !"".equals(searchDTO.getSearchContent())) {
            builder.and(tsRoadSidePassengersYy.passageType.eq(searchDTO.getSearchContent()));
        }
        
		List<TlSrvyRsltStatisticsDTO.PassengerStatisticsInfo> result = 
	 			queryFactory.select(Projections.bean(
					TlSrvyRsltStatisticsDTO.PassengerStatisticsInfo.class,
	                tsRoadSidePassengersYy.passageType.as("passageType"),
	                Expressions.asNumber(
	                    new CaseBuilder()
	                        .when(tsRoadSidePassengersYy.avgPassengers.eq("1"))
	                        .then(tsRoadSidePassengersYy.cnt)
	                        .otherwise(0L)
	                        .sum()
	                ).as("passengerOne"),
	                Expressions.asNumber(
	                    new CaseBuilder()
	                        .when(tsRoadSidePassengersYy.avgPassengers.eq("2"))
	                        .then(tsRoadSidePassengersYy.cnt)
	                        .otherwise(0L)
	                        .sum()
	                ).as("passengerTwo"),
	                Expressions.asNumber(
	                    new CaseBuilder()
	                        .when(tsRoadSidePassengersYy.avgPassengers.eq("3"))
	                        .then(tsRoadSidePassengersYy.cnt)
	                        .otherwise(0L)
	                        .sum()
	                ).as("passengerThree"),
	                Expressions.asNumber(
	                    new CaseBuilder()
	                        .when(tsRoadSidePassengersYy.avgPassengers.eq("4"))
	                        .then(tsRoadSidePassengersYy.cnt)
	                        .otherwise(0L)
	                        .sum()
	                ).as("passengerFour"),
	                Expressions.asNumber(
	                    new CaseBuilder()
	                        .when(tsRoadSidePassengersYy.avgPassengers.eq("5"))
	                        .then(tsRoadSidePassengersYy.cnt)
	                        .otherwise(0L)
	                        .sum()
	                ).as("passengerFive"),
	                Expressions.asNumber(
	                    new CaseBuilder()
	                        .when(tsRoadSidePassengersYy.avgPassengers.goe("6"))
	                        .then(tsRoadSidePassengersYy.cnt)
	                        .otherwise(0L)
	                        .sum()
	                ).as("passengerSixMore")))
			.from(tsRoadSidePassengersYy)
//			.innerJoin(tmExmnMng).on(tsRoadSidePassengersYy.exmnmngId.eq(tmExmnMng.exmnmngId))
			.where(builder)
			.groupBy(tsRoadSidePassengersYy.passageType)
			.orderBy(tsRoadSidePassengersYy.passageType.desc()) .fetch(); 
	 	
 		return result;
	}
	

	 public List<String> getSearchList(TlExmnRsltStatisticsSearchDTO searchDTO){
		 StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePassengersYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		 StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePassengersYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		 List<String> result =queryFactory.select(
													tsRoadSidePassengersYy.passageType.as("selectItem"))
											.from(tsRoadSidePassengersYy)
//											.innerJoin(tmExmnMng).on(tsRoadSidePassengersYy.exmnmngId.eq(tmExmnMng.exmnmngId))
											 .where(tsRoadSidePassengersYy.statsYy.eq(searchDTO.getSearchDate())
											 .and(departureTazCode.eq(searchDTO.getSearchCd()))
//											 .and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
											 .and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
											 .groupBy(tsRoadSidePassengersYy.passageType)
											 .orderBy(tsRoadSidePassengersYy.passageType.desc()) .fetch();

	 return result;
	 }
}
