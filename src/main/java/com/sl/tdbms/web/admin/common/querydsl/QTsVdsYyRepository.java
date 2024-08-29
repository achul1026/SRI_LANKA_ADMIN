package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.CommonCdDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.PeriodSiteDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.StatisticsByPeriodSerachDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TlPeriodRsltStatisticsDTO;
import com.sl.tdbms.web.admin.common.entity.QTcCdGrp;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.entity.QTmInstllcRoad;
import com.sl.tdbms.web.admin.common.entity.QTmVdsInstllc;
import com.sl.tdbms.web.admin.common.entity.QTsVdsYy;
import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTsVdsYyRepository {
	
	final JPAQueryFactory queryFactory;
	
	QTsVdsYy tsVdsYy = QTsVdsYy.tsVdsYy;

	QTmVdsInstllc tmVdsInstllc = QTmVdsInstllc.tmVdsInstllc;

	QTmInstllcRoad tmInstllcRoad =QTmInstllcRoad.tmInstllcRoad;

	QTcCdGrp tcCdGrp = QTcCdGrp.tcCdGrp;

	QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;

	public List<PeriodSiteDTO> getSiteIdList(StatisticsByPeriodSerachDTO searchDTO) {
    	String numberStartDate = CommonUtils.onlyNumbers(searchDTO.getStartDate());
    	String numberEndDate = CommonUtils.onlyNumbers(searchDTO.getEndDate());

        return queryFactory.select(
				Projections.bean(PeriodSiteDTO.class,
					tmVdsInstllc.instllcId.as("instllcId"),
					tmVdsInstllc.instllcNm.as("instllcNm")
				))
				.from(tsVdsYy)
				.innerJoin(tmVdsInstllc).on(tsVdsYy.instllcId.eq(tmVdsInstllc.instllcId))
				.where(
					startDateCondition(numberStartDate)
					.and(endDateCondition(numberEndDate)))
				.groupBy(tmVdsInstllc.instllcId)
				.orderBy(tmVdsInstllc.instllcId.asc())
				.fetch();
	}

	private BooleanExpression startDateCondition(String startDate) {
		return startDate != null && !startDate.isEmpty() ? tsVdsYy.statsYy.goe(startDate) : null;
	}

	private BooleanExpression endDateCondition(String endDate) {
		return endDate != null && !endDate.isEmpty() ? tsVdsYy.statsYy.loe(endDate) : null;
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
				.from(tsVdsYy)
				.innerJoin(tmVdsInstllc).on(tsVdsYy.instllcId.eq(tmVdsInstllc.instllcId))
				.innerJoin(tmInstllcRoad).on(tmVdsInstllc.instllcId.eq(tmInstllcRoad.instllcId))
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
						tsVdsYy.statsYy.as("statsDate"),
						tmVdsInstllc.instllcNm.as("instllcNm"),
						tmInstllcRoad.drctCd.as("drctCd"),
						cdNm.as("cdNm"),
						tsVdsYy.trfvlm.sum().as("trfvlm"),
						Expressions.numberTemplate(Double.class, "ROUND(AVG({0}), 1)", tsVdsYy.avgSpeed).as("avgSpeed")))
				.from(tsVdsYy)
				.innerJoin(tmVdsInstllc).on(tsVdsYy.instllcId.eq(tmVdsInstllc.instllcId))
				.innerJoin(tmInstllcRoad).on(tmVdsInstllc.instllcId.eq(tmInstllcRoad.instllcId))
				.leftJoin(tcCdGrp).on(tcCdGrp.grpCd.eq("TRF_DRCT_CD"))
				.leftJoin(tcCdInfo).on(tcCdInfo.cd.eq(tmInstllcRoad.drctCd)
						.and(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId)))
				.where(
						startDateCondition(numberStartDate)
						.and(endDateCondition(numberEndDate)
						.and(tmVdsInstllc.instllcId.eq(searchDTO.getSearchContent())
						.and(searchDrctCd(searchDTO.getSearchType())
						))))
				.groupBy(tsVdsYy.statsYy,tmVdsInstllc.instllcNm,tmInstllcRoad.drctCd,cdNm)
				.orderBy(tsVdsYy.statsYy.asc())
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
