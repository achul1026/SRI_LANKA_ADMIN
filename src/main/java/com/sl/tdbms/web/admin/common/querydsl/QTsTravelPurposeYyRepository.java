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
import com.sl.tdbms.web.admin.common.entity.QTsTravelPurposeYy;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelPurposeYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelPurposeYy tsTravelPurposeYy = QTsTravelPurposeYy.tsTravelPurposeYy;
	
	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;

	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getTravelDestinationList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelPurposeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelPurposeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(Projections.bean(
			TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
			tsTravelPurposeYy.tripPurpose.as("name"),
			tsTravelPurposeYy.cnt.sum().as("value")
			)
		)
		.from(tsTravelPurposeYy)
		.innerJoin(tmExmnMng).on(tsTravelPurposeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
		.where(tsTravelPurposeYy.statsYy.eq(searchDTO.getSearchDate())
		.and(departureTazCode.eq(searchDTO.getSearchCd()))
		.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
		.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
		.groupBy(tsTravelPurposeYy.tripPurpose)
		.orderBy(tsTravelPurposeYy.cnt.sum().desc())
		.fetch();
		return result;
	}

	public Double getAvgTravelPurposeList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelPurposeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelPurposeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(tsTravelPurposeYy.statsYy.eq(searchDTO.getSearchDate()));
        builder.and(departureTazCode.eq(searchDTO.getSearchCd()));
        builder.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd()));
        builder.and(tsTravelPurposeYy.avgTravelTime.ne(0L));
        builder.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE));
        
        if (searchDTO.getSearchContent() != null && !"".equals(searchDTO.getSearchContent())) {
            builder.and(tsTravelPurposeYy.tripPurpose.eq(searchDTO.getSearchContent()));
        }
        
		Double result = queryFactory.select(
					tsTravelPurposeYy.avgTravelTime.avg().round().coalesce(0.0)
				)
	    .from(tsTravelPurposeYy)
		.innerJoin(tmExmnMng).on(tsTravelPurposeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
	    .where(builder)
	    .fetchOne();
		
		return result;
	}
	
	public List<String> getSearchList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelPurposeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelPurposeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<String> result = queryFactory.select(tsTravelPurposeYy.tripPurpose.as("selectItem"))
											.from(tsTravelPurposeYy)
											.innerJoin(tmExmnMng).on(tsTravelPurposeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
											.where(tsTravelPurposeYy.statsYy.eq(searchDTO.getSearchDate())
													.and(departureTazCode.eq(searchDTO.getSearchCd()))
													.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
													.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
											.groupBy(tsTravelPurposeYy.tripPurpose)
											.orderBy(tsTravelPurposeYy.tripPurpose.desc())
											.fetch();
		return result;
	}

}
