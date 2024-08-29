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
import com.sl.tdbms.web.admin.common.entity.QTsPersonGenderYy;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsPersonGenderYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsPersonGenderYy tsPersonGenderYy = QTsPersonGenderYy.tsPersonGenderYy;
	
	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;

	public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getPersonalStatusByGenderList(TlExmnRsltStatisticsSearchDTO searchDTO) {
	    StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsPersonGenderYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
	    StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsPersonGenderYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());
	
	    Long totalCnt = queryFactory
	            .select(tsPersonGenderYy.cnt.sum())
	            .from(tsPersonGenderYy)
				.innerJoin(tmExmnMng).on(tsPersonGenderYy.exmnmngId.eq(tmExmnMng.exmnmngId))
		        .where(tsPersonGenderYy.statsYy.eq(searchDTO.getSearchDate())
		            .and(departureTazCode.eq(searchDTO.getSearchCd()))
		            .and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
		            .and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
	            .fetchOne();

        NumberTemplate<Long> percentage = Expressions.numberTemplate(Long.class,
            "ROUND(({0} * 1.0) / {1} * 100)",
            tsPersonGenderYy.cnt.sum(), totalCnt
        );

	    List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory
	        .select(Projections.bean(
	            TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
	            tsPersonGenderYy.gender.as("name"),
	            percentage.as("value")
	        ))
	        .from(tsPersonGenderYy)
			.innerJoin(tmExmnMng).on(tsPersonGenderYy.exmnmngId.eq(tmExmnMng.exmnmngId))
	        .where(tsPersonGenderYy.statsYy.eq(searchDTO.getSearchDate())
	            .and(departureTazCode.eq(searchDTO.getSearchCd()))
	            .and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
	            .and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
	        .groupBy(tsPersonGenderYy.gender)
	        .orderBy(percentage.desc())
	        .fetch();

	    return result;
	}
}
