package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.sl.tdbms.web.admin.common.dto.common.CommonCdDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.PeriodSiteDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.StatisticsByPeriodSerachDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlPeriodRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.*;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QTsMvmneqYyRepository {

	final JPAQueryFactory queryFactory;
	
	QTsMvmneqYy tsMvmneqYy = QTsMvmneqYy.tsMvmneqYy;

	QTlMvmneqCur tlMvmneqCur = QTlMvmneqCur.tlMvmneqCur;

	QTmInstllcRoad tmInstllcRoad =QTmInstllcRoad.tmInstllcRoad;

	QTcCdGrp tcCdGrp = QTcCdGrp.tcCdGrp;

	QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;
	public List<PeriodSiteDTO> getSiteIdList(StatisticsByPeriodSerachDTO searchDTO) {
		String numberStartDate = CommonUtils.onlyNumbers(searchDTO.getStartDate());
		String numberEndDate = CommonUtils.onlyNumbers(searchDTO.getEndDate());

		return queryFactory.select(
						Projections.bean(PeriodSiteDTO.class,
								tlMvmneqCur.instllcId.as("instllcId"),
								tlMvmneqCur.instllcNm.as("instllcNm")
						))
				.from(tsMvmneqYy)
				.innerJoin(tlMvmneqCur).on(tsMvmneqYy.instllcId.eq(tlMvmneqCur.instllcId))
				.where(
						startDateCondition(numberStartDate)
								.and(endDateCondition(numberEndDate)))
				.groupBy(tlMvmneqCur.instllcId)
				.orderBy(tlMvmneqCur.instllcId.asc())
				.fetch();
	}
	private BooleanExpression startDateCondition(String startDate) {
		return startDate != null && !startDate.isEmpty() ? tsMvmneqYy.statsYy.goe(startDate) : null;
	}

	private BooleanExpression endDateCondition(String endDate) {
		return endDate != null && !endDate.isEmpty() ? tsMvmneqYy.statsYy.loe(endDate) : null;
	}

	private BooleanExpression searchDrctCd(String searchType) {
		return searchType != null && !searchType.isEmpty() ? tmInstllcRoad.drctCd.eq(searchType) : null;
	}

	public List<CommonCdDTO> getDiriectionList(StatisticsByPeriodSerachDTO searchDTO){
		String lang = LocaleContextHolder.getLocale().toString();
		StringExpression cdNm = getCdNm(lang);

		return queryFactory.select(Projections.bean(CommonCdDTO.class,
						tmInstllcRoad.drctCd.as("cd"),
						cdNm.as("cdNm")))
				.from(tsMvmneqYy)
				.innerJoin(tlMvmneqCur).on(tsMvmneqYy.instllcId.eq(tlMvmneqCur.instllcId))
				.innerJoin(tmInstllcRoad).on(tlMvmneqCur.instllcId.eq(tmInstllcRoad.instllcId))
				.leftJoin(tcCdGrp).on(tcCdGrp.grpCd.eq("TRF_DRCT_CD"))
				.leftJoin(tcCdInfo).on(tcCdInfo.cd.eq(tmInstllcRoad.drctCd)
						.and(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId)))
				.groupBy(tmInstllcRoad.drctCd,cdNm)
				.fetch();
	}

	public List<TlPeriodRsltStatisticsDTO.PeriodChartData> getChartData(StatisticsByPeriodSerachDTO searchDTO) {
		String lang = LocaleContextHolder.getLocale().toString();
		StringExpression cdNm = getCdNm(lang);

		String numberStartDate = CommonUtils.onlyNumbers(searchDTO.getStartDate());
		String numberEndDate = CommonUtils.onlyNumbers(searchDTO.getEndDate());

		return queryFactory.select(Projections.bean(TlPeriodRsltStatisticsDTO.PeriodChartData.class,
						tsMvmneqYy.statsYy.as("statsDate"),
						tlMvmneqCur.instllcNm.as("instllcNm"),
						tmInstllcRoad.drctCd.as("drctCd"),
						cdNm.as("cdNm"),
						tsMvmneqYy.trfvlm.sum().as("trfvlm"),
						Expressions.numberTemplate(Double.class, "ROUND(AVG({0}), 1)", tsMvmneqYy.avgSpeed).as("avgSpeed")))
				.from(tsMvmneqYy)
				.innerJoin(tlMvmneqCur).on(tsMvmneqYy.instllcId.eq(tlMvmneqCur.instllcId))
				.innerJoin(tmInstllcRoad).on(tlMvmneqCur.instllcId.eq(tmInstllcRoad.instllcId))
				.leftJoin(tcCdGrp).on(tcCdGrp.grpCd.eq("TRF_DRCT_CD"))
				.leftJoin(tcCdInfo).on(tcCdInfo.cd.eq(tmInstllcRoad.drctCd)
						.and(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId)))
				.where(
						startDateCondition(numberStartDate)
								.and(endDateCondition(numberEndDate)
										.and(tlMvmneqCur.instllcId.eq(searchDTO.getSearchContent())
												.and(searchDrctCd(searchDTO.getSearchType())
												))))
				.groupBy(tsMvmneqYy.statsYy,tlMvmneqCur.instllcNm,tmInstllcRoad.drctCd,cdNm)
				.orderBy(tsMvmneqYy.statsYy.asc())
				.fetch();
	}

	public StringExpression getCdNm(String lang) {
		switch(lang) {
			case "kor":
				return tcCdInfo.cdnmKor;
			case "eng":
				return tcCdInfo.cdnmEng;
			case "sin":
				return tcCdInfo.cdnmSin;
			default:
				return tcCdInfo.cdnmSin;
		}
	}
}
