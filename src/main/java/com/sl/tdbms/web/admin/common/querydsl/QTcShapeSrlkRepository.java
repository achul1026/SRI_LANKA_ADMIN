package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.entity.TcShapeSrlk;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.entity.QTcGnarMng;
import com.sl.tdbms.web.admin.common.entity.QTcShapeSrlk;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QTcShapeSrlkRepository {

	private final JPAQueryFactory queryFactory;

	private QTcShapeSrlk tcShapeSrlk = QTcShapeSrlk.tcShapeSrlk;
	private QTcGnarMng tcGnarMng = QTcGnarMng.tcGnarMng;


	public List<TcShapeSrlk> getTazList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {

		List<TcShapeSrlk> result = queryFactory.select(Projections.bean(
																		TcShapeSrlk.class,
																		tcShapeSrlk.dstrctId,
																		tcShapeSrlk.dstrctCd,
																		tcShapeSrlk.cntrLon,
																		tcShapeSrlk.cntrLat
																		)
																)
																.from(tcShapeSrlk)
																.leftJoin(tcGnarMng).on(tcShapeSrlk.dstrctCd.eq(tcGnarMng.dstrctCd))
																.where(tcGnarMng.dstrctCd.isNull().and(searchOption(searchCommonDTO)))
																.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
																.orderBy(tcShapeSrlk.dstrctId.asc())
																.fetch();

		return result;
	}

	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {
		Long count = queryFactory.select(tcShapeSrlk.countDistinct())
				.from(tcShapeSrlk)
				.leftJoin(tcGnarMng).on(tcShapeSrlk.dstrctCd.eq(tcGnarMng.dstrctCd))
				.where(tcGnarMng.dstrctCd.isNull().and(searchOption(searchCommonDTO)))
				.fetchOne();
		return count;
	}
	
	private BooleanExpression searchOption(SearchCommonDTO searchCommonDTO) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchCommonDTO.getSearchContent())) {
			result = QRepositorySupport.containsKeyword(tcShapeSrlk.dstrctCd, searchCommonDTO.getSearchContent());
		}
		return result;
	}
}
