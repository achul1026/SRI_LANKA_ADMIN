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
import com.sl.tdbms.web.admin.common.entity.QTsRoadSidePassageTypeYy;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsRoadSidePassageTypeYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsRoadSidePassageTypeYy tsRoadSidePassageTypeYy = QTsRoadSidePassageTypeYy.tsRoadSidePassageTypeYy;

//	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;

    public List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> getModeOfTransportationList(TlExmnRsltStatisticsSearchDTO searchDTO) {
        StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePassageTypeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
        StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePassageTypeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(tsRoadSidePassageTypeYy.statsYy.eq(searchDTO.getSearchDate()));
        builder.and(departureTazCode.eq(searchDTO.getSearchCd()));
        builder.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd()));
//        builder.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE));
        
        if (searchDTO.getSearchContent() != null && !"".equals(searchDTO.getSearchContent())) {
            builder.and(tsRoadSidePassageTypeYy.tripPurpose.eq(searchDTO.getSearchContent()));
        }

        List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = queryFactory.select(
                Projections.bean(
                        TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo.class,
                        tsRoadSidePassageTypeYy.passageType.as("name"),
                        tsRoadSidePassageTypeYy.cnt.sum().as("value")
                ))
                .from(tsRoadSidePassageTypeYy)
//                .innerJoin(tmExmnMng).on(tsRoadSidePassageTypeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
                .where(builder)
                .groupBy(tsRoadSidePassageTypeYy.passageType)
                .orderBy(tsRoadSidePassageTypeYy.cnt.sum().desc())
                .fetch();

        return result;
    }

	public List<String> getSearchList(TlExmnRsltStatisticsSearchDTO searchDTO){
		StringTemplate departureTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePassageTypeYy.departureTazCode, 1, searchDTO.getOriginSubstringIdx());
		StringTemplate destinationTazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsRoadSidePassageTypeYy.destinationTazCode, 1, searchDTO.getDestinationSubstringIdx());

		List<String> result = queryFactory.select(
													tsRoadSidePassageTypeYy.tripPurpose.as("selectItem"))
											.from(tsRoadSidePassageTypeYy)
//											.innerJoin(tmExmnMng).on(tsRoadSidePassageTypeYy.exmnmngId.eq(tmExmnMng.exmnmngId))
											.where(tsRoadSidePassageTypeYy.statsYy.eq(searchDTO.getSearchDate())
											.and(departureTazCode.eq(searchDTO.getSearchCd()))
//											.and(tmExmnMng.sttsCd.eq(ExmnSttsCd.INVEST_COMPLETE))
											.and(destinationTazCode.eq(searchDTO.getDestinationSearchCd())))
											.groupBy(tsRoadSidePassageTypeYy.tripPurpose)
											.orderBy(tsRoadSidePassageTypeYy.tripPurpose.desc()).fetch();

		return result;
	}
}
