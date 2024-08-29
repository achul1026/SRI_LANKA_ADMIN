package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringTemplate;
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
public class QTsMvmneqOnhrRepository {

	final JPAQueryFactory queryFactory;
	
	QTsMvmneqOnhr tsMvmneqOnhr = QTsMvmneqOnhr.tsMvmneqOnhr;

	QTlMvmneqCur tlMvmneqCur = QTlMvmneqCur.tlMvmneqCur;

	QTmInstllcRoad tmInstllcRoad =QTmInstllcRoad.tmInstllcRoad;

	QTcCdGrp tcCdGrp = QTcCdGrp.tcCdGrp;

	QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;

	/**
	 * methodName : getSiteIdList
	 * author : Peo.Lee
	 * date : 2024-08-19
	 * description : SiteId 목록 조회
	 * @param searchDTO
	 * @return List<PeriodSiteDTO>
	 */
	public List<PeriodSiteDTO> getSiteIdList(StatisticsByPeriodSerachDTO searchDTO) {
		String numberStartDate = CommonUtils.onlyNumbers(searchDTO.getStartDate());
		String numberEndDate = CommonUtils.onlyNumbers(searchDTO.getEndDate());
		String numberStartHour = CommonUtils.onlyNumbers(searchDTO.getStartHour());
		String numberEndHour = CommonUtils.onlyNumbers(searchDTO.getEndHour());

		return queryFactory.select(
						Projections.bean(PeriodSiteDTO.class,
								tlMvmneqCur.instllcId.as("instllcId"),
								tlMvmneqCur.instllcNm.as("instllcNm")
						))
				.from(tsMvmneqOnhr)
				.innerJoin(tlMvmneqCur).on(tsMvmneqOnhr.instllcId.eq(tlMvmneqCur.instllcId))
				.where(
						startDateCondition(numberStartDate,numberStartHour)
								.and(endDateCondition(numberEndDate,numberEndHour)))
				.groupBy(tlMvmneqCur.instllcId)
				.orderBy(tlMvmneqCur.instllcId.asc())
				.fetch();
	}

	private BooleanExpression startDateCondition(String startDate, String startHour) {
		BooleanExpression startCondition = null;
		if (startDate != null && !startDate.isEmpty() && startHour != null && !startHour.isEmpty()) {
			StringExpression datePart = tsMvmneqOnhr.statsYymmdt.substring(0, 8); // 'yyyyMMdd' 추출
			StringExpression hourPart = tsMvmneqOnhr.statsYymmdt.substring(8, 10); // 'HH' 추출

			startCondition = datePart.goe(startDate).and(hourPart.goe(startHour));
		}
		return startCondition;
	}

	private BooleanExpression endDateCondition(String endDate, String endHour) {
		BooleanExpression endCondition = null;
		if (endDate != null && !endDate.isEmpty() && endHour != null && !endHour.isEmpty()) {
			StringExpression datePart = tsMvmneqOnhr.statsYymmdt.substring(0, 8); // 'yyyyMMdd' 추출
			StringExpression hourPart = tsMvmneqOnhr.statsYymmdt.substring(8, 10); // 'HH' 추출

			endCondition = datePart.loe(endDate).and(hourPart.loe(endHour));
		}
		return endCondition;
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
				.from(tsMvmneqOnhr)
				.innerJoin(tlMvmneqCur).on(tsMvmneqOnhr.instllcId.eq(tlMvmneqCur.instllcId))
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
		String numberStartHour = CommonUtils.onlyNumbers(searchDTO.getStartHour());
		String numberEndHour = CommonUtils.onlyNumbers(searchDTO.getEndHour());

		StringTemplate statsDate = Expressions.stringTemplate(
				"substring({0}, 9, 2)",
				tsMvmneqOnhr.statsYymmdt
		);
		return queryFactory.select(Projections.bean(TlPeriodRsltStatisticsDTO.PeriodChartData.class,
						statsDate.as("statsDate"),
						tlMvmneqCur.instllcNm.as("instllcNm"),
						tmInstllcRoad.drctCd.as("drctCd"),
						cdNm.as("cdNm"),
						tsMvmneqOnhr.trfvlm.sum().as("trfvlm"),
						Expressions.numberTemplate(Double.class, "ROUND(AVG({0}), 1)", tsMvmneqOnhr.avgSpeed).as("avgSpeed")))
				.from(tsMvmneqOnhr)
				.innerJoin(tlMvmneqCur).on(tsMvmneqOnhr.instllcId.eq(tlMvmneqCur.instllcId))
				.innerJoin(tmInstllcRoad).on(tlMvmneqCur.instllcId.eq(tmInstllcRoad.instllcId))
				.leftJoin(tcCdGrp).on(tcCdGrp.grpCd.eq("TRF_DRCT_CD"))
				.leftJoin(tcCdInfo).on(tcCdInfo.cd.eq(tmInstllcRoad.drctCd)
						.and(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId)))
				.where(
						startDateCondition(numberStartDate,numberStartHour)
								.and(endDateCondition(numberEndDate,numberEndHour)
										.and(tlMvmneqCur.instllcId.eq(searchDTO.getSearchContent())
												.and(searchDrctCd(searchDTO.getSearchType())
												))))
				.groupBy(statsDate,tlMvmneqCur.instllcNm,tmInstllcRoad.drctCd,cdNm)
				.orderBy(statsDate.asc())
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
