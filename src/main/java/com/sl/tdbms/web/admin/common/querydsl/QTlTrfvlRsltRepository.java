package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.statistics.TlExmnRsltStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlTrfvlRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.QTlExmnRslt;
import com.sl.tdbms.web.admin.common.entity.QTlTrfvlRslt;
import com.sl.tdbms.web.admin.common.entity.QTmExmnMng;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTlTrfvlRsltRepository {

	private final JPAQueryFactory queryFactory;

	private QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;
	private QTlExmnRslt tlExmnRslt = QTlExmnRslt.tlExmnRslt;
	private QTlTrfvlRslt tlTrfvlRslt = QTlTrfvlRslt.tlTrfvlRslt;

	public List<TlTrfvlRsltStatisticsDTO.DirectionInfo> getDirectionListByDsdCd(TlExmnRsltStatisticsSearchDTO searchDTO){

		// to_char 함수 사용
		StringTemplate formattedDate = Expressions.stringTemplate(
				"to_char({0}, {1})",
				tlTrfvlRslt.exmnstartDt,
				Expressions.constant("yyyy")
		);

		List<TlTrfvlRsltStatisticsDTO.DirectionInfo> result = queryFactory.select(Projections.bean(
																									TlTrfvlRsltStatisticsDTO.DirectionInfo.class,
																									tlTrfvlRslt.startlcNm,
																									tlTrfvlRslt.endlcNm
																									)
																								)
																							.from(tmExmnMng)
															.innerJoin(tlExmnRslt).on(tmExmnMng.exmnmngId.eq(tlExmnRslt.exmnmngId))
															.innerJoin(tlTrfvlRslt).on(tlExmnRslt.exmnrsltId.eq(tlTrfvlRslt.exmnrsltId))
															.where(tmExmnMng.dsdCd.eq(searchDTO.getDsdCd()).and(formattedDate.eq(searchDTO.getSearchDate())))
															.groupBy(tlTrfvlRslt.startlcNm,tlTrfvlRslt.endlcNm)
															.fetch();
		return result;

	}

	public List<TlTrfvlRsltStatisticsDTO.DirectionInfo> getDirectionListByGnCd(TlExmnRsltStatisticsSearchDTO searchDTO){

		// to_char 함수 사용
		StringTemplate formattedDate = Expressions.stringTemplate(
				"to_char({0}, {1})",
				tlTrfvlRslt.exmnstartDt,
				Expressions.constant("yyyy")
		);

		List<TlTrfvlRsltStatisticsDTO.DirectionInfo> result = queryFactory.select(Projections.bean(
																									TlTrfvlRsltStatisticsDTO.DirectionInfo.class,
																									tlTrfvlRslt.startlcNm,
																									tlTrfvlRslt.endlcNm
																									)
																								)
																							.from(tmExmnMng)
															.innerJoin(tlExmnRslt).on(tmExmnMng.exmnmngId.eq(tlExmnRslt.exmnmngId))
															.innerJoin(tlTrfvlRslt).on(tlExmnRslt.exmnrsltId.eq(tlTrfvlRslt.exmnrsltId))
															.where(tmExmnMng.gnCd.eq(searchDTO.getGnCd()).and(formattedDate.eq(searchDTO.getSearchDate())))
															.groupBy(tlTrfvlRslt.startlcNm,tlTrfvlRslt.endlcNm)
															.fetch();
		return result;
	}
}
