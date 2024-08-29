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
import com.sl.tdbms.web.admin.common.entity.QTsRoadSideDepartureTimeYy;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSideDepartureTimeYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSideDepartureTimeYy tsRoadSideDepartureTimeYy = QTsRoadSideDepartureTimeYy.tsRoadSideDepartureTimeYy;

//	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;

	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getDepartureTimelList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSideDepartureTimeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSideDepartureTimeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(tsRoadSideDepartureTimeYy.statsYy.eq(searchDTO.getSearchDate()));
        builder.and(departureTazCode.eq(searchDTO.getSearchCd()));
        builder.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd()));
//        builder.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE));
        
        if (searchDTO.getSearchContent() != null && !"".equals(searchDTO.getSearchContent())) {
            builder.and(tsRoadSideDepartureTimeYy.tripPurpose.eq(searchDTO.getSearchContent()));
        }

		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(
				Projections.bean(
						TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
                        tsRoadSideDepartureTimeYy.departureTime.as("name"),
                        tsRoadSideDepartureTimeYy.cnt.sum().as("value")
						)
				)
				.from(tsRoadSideDepartureTimeYy)
//				.innerJoin(tmExmnMng).on(tsRoadSideDepartureTimeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
				.where(builder)
				.groupBy(tsRoadSideDepartureTimeYy.departureTime)
				.orderBy(tsRoadSideDepartureTimeYy.departureTime.asc())
				.fetch();
		
		return result;
	}


	public List<String> getSearchList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSideDepartureTimeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSideDepartureTimeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<String> result = queryFactory.select(tsRoadSideDepartureTimeYy.tripPurpose.as("selectItem"))
											.from(tsRoadSideDepartureTimeYy)
//											.innerJoin(tmExmnMng).on(tsRoadSideDepartureTimeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
											.where(tsRoadSideDepartureTimeYy.statsYy.eq(searchDTO.getSearchDate())
													.and(departureTazCode.eq(searchDTO.getSearchCd()))
//													.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
													.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
											.groupBy(tsRoadSideDepartureTimeYy.tripPurpose)
											.orderBy(tsRoadSideDepartureTimeYy.tripPurpose.desc())
											.fetch();


		return result;
	}
}
