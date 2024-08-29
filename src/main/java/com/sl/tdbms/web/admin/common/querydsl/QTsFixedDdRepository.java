package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.CommonCdDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.PeriodSiteDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.StatisticsByPeriodSerachDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlPeriodRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.*;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QTsFixedDdRepository {
	
	final JPAQueryFactory queryFactory;

	QTsFixedDd tsFixedDd = QTsFixedDd.tsFixedDd;

	QTlFixedCur tlFixedCur = QTlFixedCur.tlFixedCur;

	QTmInstllcRoad tmInstllcRoad = QTmInstllcRoad.tmInstllcRoad;

	QTcCdGrp tcCdGrp = QTcCdGrp.tcCdGrp;

	QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;


	public List<PeriodSiteDTO> getSiteIdList(StatisticsByPeriodSerachDTO searchDTO) {
		String numberStartDate = CommonUtils.onlyNumbers(searchDTO.getStartDate());
		String numberEndDate = CommonUtils.onlyNumbers(searchDTO.getEndDate());

		return queryFactory.select(
						Projections.bean(PeriodSiteDTO.class,
								tlFixedCur.instllcId.as("instllcId"),
								tlFixedCur.instllcNm.as("instllcNm")
						))
				.from(tsFixedDd)
				.innerJoin(tlFixedCur).on(tsFixedDd.instllcId.eq(tlFixedCur.instllcId))
				.where(
						startDateCondition(numberStartDate)
								.and(endDateCondition(numberEndDate)))
				.groupBy(tlFixedCur.instllcId)
				.orderBy(tlFixedCur.instllcId.asc())
				.fetch();
	}

	private BooleanExpression startDateCondition(String startDate) {
		return startDate != null && !startDate.isEmpty() ? tsFixedDd.statsYymmdd.goe(startDate) : null;
	}

	private BooleanExpression endDateCondition(String endDate) {
		return endDate != null && !endDate.isEmpty() ? tsFixedDd.statsYymmdd.loe(endDate) : null;
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
				.from(tsFixedDd)
				.innerJoin(tlFixedCur).on(tsFixedDd.instllcId.eq(tlFixedCur.instllcId))
				.innerJoin(tmInstllcRoad).on(tlFixedCur.instllcId.eq(tmInstllcRoad.instllcId))
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

		NumberTemplate<Integer> dayOfWeekNumber = Expressions.numberTemplate(
				Integer.class,
				"extract_dow({0}, {1})",
				tsFixedDd.statsYymmdd, "'YYYYMMDD'"
		);

		return queryFactory.select(Projections.bean(TlPeriodRsltStatisticsDTO.PeriodChartData.class,
						dayOfWeekNumber.as("dayOfTheWeek"),
						tlFixedCur.instllcNm.as("instllcNm"),
						tmInstllcRoad.drctCd.as("drctCd"),
						cdNm.as("cdNm"),
						tsFixedDd.trfvlm.sum().as("trfvlm"),
						Expressions.numberTemplate(Double.class, "ROUND(AVG({0}), 1)", tsFixedDd.avgSpeed).as("avgSpeed")))
				.from(tsFixedDd)
				.innerJoin(tlFixedCur).on(tsFixedDd.instllcId.eq(tlFixedCur.instllcId))
				.innerJoin(tmInstllcRoad).on(tlFixedCur.instllcId.eq(tmInstllcRoad.instllcId))
				.leftJoin(tcCdGrp).on(tcCdGrp.grpCd.eq("TRF_DRCT_CD"))
				.leftJoin(tcCdInfo).on(tcCdInfo.cd.eq(tmInstllcRoad.drctCd)
						.and(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId)))
				.where(
						startDateCondition(numberStartDate)
								.and(endDateCondition(numberEndDate)
										.and(tlFixedCur.instllcId.eq(searchDTO.getSearchContent())
												.and(searchDrctCd(searchDTO.getSearchType())
												))))
				.groupBy(dayOfWeekNumber,tlFixedCur.instllcNm,tmInstllcRoad.drctCd,cdNm)
				.orderBy(dayOfWeekNumber.asc())
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
