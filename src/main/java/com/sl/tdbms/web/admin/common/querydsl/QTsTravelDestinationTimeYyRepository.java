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
import com.sl.tdbms.web.admin.common.entity.QTsTravelDestinationTimeYy;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsTravelDestinationTimeYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsTravelDestinationTimeYy tsTravelDestinationTimeYy = QTsTravelDestinationTimeYy.tsTravelDestinationTimeYy;

	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;
	
	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getTravelDestinationTimeList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelDestinationTimeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelDestinationTimeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(tsTravelDestinationTimeYy.statsYy.eq(searchDTO.getSearchDate()));
        builder.and(departureTazCode.eq(searchDTO.getSearchCd()));
        builder.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd()));
        builder.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE));
        
        if (searchDTO.getSearchContent() != null && !"".equals(searchDTO.getSearchContent())) {
            builder.and(tsTravelDestinationTimeYy.tripPurpose.eq(searchDTO.getSearchContent()));
        }

		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(
				Projections.bean(
						TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
						tsTravelDestinationTimeYy.destinationTime.as("name"),
						tsTravelDestinationTimeYy.cnt.sum().as("value")
						)
				)
				.from(tsTravelDestinationTimeYy)
				.innerJoin(tmExmnMng).on(tsTravelDestinationTimeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
				.where(builder)
				.groupBy(tsTravelDestinationTimeYy.destinationTime)
				.orderBy(tsTravelDestinationTimeYy.destinationTime.asc())
				.fetch();
		
		return result;
	}
	
	public List<String> getSearchList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelDestinationTimeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsTravelDestinationTimeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<String> result = queryFactory.select(tsTravelDestinationTimeYy.tripPurpose.as("selectItem"))
											.from(tsTravelDestinationTimeYy)
											.innerJoin(tmExmnMng).on(tsTravelDestinationTimeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
											.where(tsTravelDestinationTimeYy.statsYy.eq(searchDTO.getSearchDate())
													.and(departureTazCode.eq(searchDTO.getSearchCd()))
													.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
													.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
											.groupBy(tsTravelDestinationTimeYy.tripPurpose)
											.orderBy(tsTravelDestinationTimeYy.tripPurpose.desc())
											.fetch();


		return result;
	}
}
