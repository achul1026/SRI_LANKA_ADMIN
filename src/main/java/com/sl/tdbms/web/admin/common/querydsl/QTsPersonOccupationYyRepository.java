package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlSrvyRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.QTmExmnMng;
import com.sl.tdbms.web.admin.common.entity.QTsPersonOccupationYy;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsPersonOccupationYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsPersonOccupationYy tsPersonOccupationYy = QTsPersonOccupationYy.tsPersonOccupationYy;
	
	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;

	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getPersonalStatusByOccupationList(TlExmnRsltStatisticsSearchDTO searchDTO) {
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsPersonOccupationYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsPersonOccupationYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

	    Long totalCnt = queryFactory
	            .select(tsPersonOccupationYy.cnt.sum())
	            .from(tsPersonOccupationYy)
				.innerJoin(tmExmnMng).on(tsPersonOccupationYy.exmnmngId.eq(tmExmnMng.exmnmngId))
	            .where(tsPersonOccupationYy.statsYy.eq(searchDTO.getSearchDate())
		            .and(departureTazCode.eq(searchDTO.getSearchCd()))
		            .and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
		            .and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
	            .fetchOne();

        NumberTemplate<Long> percentage = Expressions.numberTemplate(Long.class,
            "ROUND(({0} * 1.0) / {1} * 100)",
            tsPersonOccupationYy.cnt.sum(), totalCnt
        );
        
        StringTemplate occupationCase = Expressions.stringTemplate(
            "CASE WHEN {0} = 'Y' THEN 'Other' ELSE {1} END",
            tsPersonOccupationYy.etcYn, tsPersonOccupationYy.occupation
        );
		
		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(Projections.bean(
			TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
			occupationCase.as("name"),
			percentage.as("value")
			)
		)
		.from(tsPersonOccupationYy)
		.innerJoin(tmExmnMng).on(tsPersonOccupationYy.exmnmngId.eq(tmExmnMng.exmnmngId))
		.where(tsPersonOccupationYy.statsYy.eq(searchDTO.getSearchDate())
		.and(departureTazCode.eq(searchDTO.getSearchCd()))
		.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
		.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
		.groupBy(occupationCase,tsPersonOccupationYy.etcYn)
		.orderBy(tsPersonOccupationYy.cnt.sum().desc())
		.fetch();
			
		return result;
	}
}
