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
import com.sl.tdbms.web.admin.common.entity.QTsTravelDepartureTimeYy;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelDepartureTimeYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelDepartureTimeYy tsTravelDepartureTimeYy = QTsTravelDepartureTimeYy.tsTravelDepartureTimeYy;

	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;
	
	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getTravelDepartureTimeList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelDepartureTimeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelDepartureTimeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(tsTravelDepartureTimeYy.statsYy.eq(searchDTO.getSearchDate()));
        builder.and(departureTazCode.eq(searchDTO.getSearchCd()));
        builder.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd()));
        builder.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE));
		
        if (searchDTO.getSearchContent() != null && !"".equals(searchDTO.getSearchContent())) {
            builder.and(tsTravelDepartureTimeYy.tripPurpose.eq(searchDTO.getSearchContent()));
        }
		
		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(Projections.bean(
			TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
			tsTravelDepartureTimeYy.departureTime.as("name"),
			tsTravelDepartureTimeYy.cnt.sum().as("value")
			)
		)
		.from(tsTravelDepartureTimeYy)
		.innerJoin(tmExmnMng).on(tsTravelDepartureTimeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
		.where(builder)
		.groupBy(tsTravelDepartureTimeYy.departureTime)
		.orderBy(tsTravelDepartureTimeYy.cnt.sum().desc())
		.fetch();
			
		return result;
	}

	public List<String> getSearchList(TlExmnRsltStatisticsSearchDTO searchDTO) {
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelDepartureTimeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelDepartureTimeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<String> result = queryFactory.select(tsTravelDepartureTimeYy.tripPurpose.as("selectItem"))
											.from(tsTravelDepartureTimeYy)
											.innerJoin(tmExmnMng).on(tsTravelDepartureTimeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
											.where(tsTravelDepartureTimeYy.statsYy.eq(searchDTO.getSearchDate())
													.and(departureTazCode.eq(searchDTO.getSearchCd()))
													.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
													.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
											.groupBy(tsTravelDepartureTimeYy.tripPurpose)
											.orderBy(tsTravelDepartureTimeYy.tripPurpose.desc())
											.fetch();
		return result;
	}
}
