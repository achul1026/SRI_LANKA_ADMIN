package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.QTmExmnMng;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlTrfvlRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.QTsMccTrfvlYy;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsMccYyRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTsMccTrfvlYy tsMccTrfvlYy = QTsMccTrfvlYy.tsMccTrfvlYy;
	private QTmExmnMng tmExmnMng =  QTmExmnMng.tmExmnMng;


	public List<TlTrfvlRsltStatisticsDTO.DirectionInfo> getDirectionListByTazCd(TlExmnRsltStatisticsSearchDTO searchDTO){

		StringTemplate tazCode = Expressions.stringTemplate("substring({0}, {1}, {2})", tsMccTrfvlYy.tazCd, 1, searchDTO.getOriginSubstringIdx());

		List<TlTrfvlRsltStatisticsDTO.DirectionInfo> result = queryFactory.select(Projections.bean(
																									TlTrfvlRsltStatisticsDTO.DirectionInfo.class,
																									tsMccTrfvlYy.startlcNm,
																									tsMccTrfvlYy.endlcNm
																							)
																					)
																					.from(tsMccTrfvlYy)
																					.innerJoin(tmExmnMng).on(tsMccTrfvlYy.exmnmngId.eq(tmExmnMng.exmnmngId))
																					.where(tsMccTrfvlYy.statsYy.eq(searchDTO.getSearchDate()).and(tazCode.eq(searchDTO.getSearchCd())).and(tmExmnMng.roadCd.eq(searchDTO.getSearchRoadCd()).and(tmExmnMng.exmnDistance.eq(searchDTO.getSearchExmnDistance()))))
																					.groupBy(tazCode,tsMccTrfvlYy.startlcNm, tsMccTrfvlYy.endlcNm)
																					.fetch();
		return result;

	}
}
