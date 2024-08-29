package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringTemplate;
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

/**
 * The type Q ts mcc trfvl onhr repository.
 */
@Repository
@RequiredArgsConstructor
public class QTsMccTrfvlOnhrRepository {

	final JPAQueryFactory queryFactory;

	QTsMccTrfvlOnhr tsMccTrfvlOnhr = QTsMccTrfvlOnhr.tsMccTrfvlOnhr;

	QTmExmnMng tmExmnMng = QTmExmnMng.tmExmnMng;

	QTmInstllcRoad tmInstllcRoad = QTmInstllcRoad.tmInstllcRoad;

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
								tmExmnMng.roadCd.as("instllcId"),
								tmExmnMng.roadDescr.as("instllcNm")
						))
				.from(tsMccTrfvlOnhr)
				.innerJoin(tmExmnMng).on(tsMccTrfvlOnhr.exmnmngId.eq(tmExmnMng.exmnmngId))
				.where(
					startDateCondition(numberStartDate,numberStartHour)
					.and(endDateCondition(numberEndDate,numberEndHour)))
				.groupBy(tmExmnMng.roadCd, tmExmnMng.roadDescr)
				.orderBy(tmExmnMng.roadCd.asc())
				.fetch();
	}

	/**
	 * methodName : startDateCondition
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : 시작날짜 조건
	 * @param startDate
	 * @param startHour
	 * @return BooleanExpression
	 */
	private BooleanExpression startDateCondition(String startDate, String startHour) {
		BooleanExpression startCondition = null;
		if (startDate != null && !startDate.isEmpty() && startHour != null && !startHour.isEmpty()) {
			StringExpression datePart = tsMccTrfvlOnhr.statsYymmdt.substring(0, 8); // 'yyyyMMdd' 추출
			StringExpression hourPart = tsMccTrfvlOnhr.statsYymmdt.substring(8, 10); // 'HH' 추출

			startCondition = datePart.goe(startDate).and(hourPart.goe(startHour));
		}
		return startCondition;
	}

	/**
	 * methodName : endDateCondition
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description :종료 날짜 조건
	 * @param endDate
	 * @param endHour
	 * @return BooleanExpression
	 */
	private BooleanExpression endDateCondition(String endDate, String endHour) {
		BooleanExpression endCondition = null;
		if (endDate != null && !endDate.isEmpty() && endHour != null && !endHour.isEmpty()) {
			StringExpression datePart = tsMccTrfvlOnhr.statsYymmdt.substring(0, 8); // 'yyyyMMdd' 추출
			StringExpression hourPart = tsMccTrfvlOnhr.statsYymmdt.substring(8, 10); // 'HH' 추출

			endCondition = datePart.loe(endDate).and(hourPart.loe(endHour));
		}
		return endCondition;
	}

	/**
	 * methodName : searchDrctCd
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : 기간별통계 onchange 조건
	 * @param searchType
	 * @return BooleanExpression
	 */
	private BooleanExpression searchDrctCd(String searchType) {
		return searchType != null && !searchType.isEmpty() ? tmInstllcRoad.drctCd.eq(searchType) : null;
	}

	/**
	 * methodName : getDiriectionList
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : site아이디조회
	 * @param searchDTO
	 * @return List<CommonCdDTO>
	 */
	public List<CommonCdDTO> getDiriectionList(StatisticsByPeriodSerachDTO searchDTO){
		String lang = LocaleContextHolder.getLocale().toString();
		StringExpression cdNm = getCdNm(lang);

		return queryFactory.select(Projections.bean(CommonCdDTO.class,
						tmInstllcRoad.drctCd.as("cd"),
						cdNm.as("cdNm")))
				.from(tsMccTrfvlOnhr)
				.innerJoin(tmExmnMng).on(tsMccTrfvlOnhr.exmnmngId.eq(tmExmnMng.exmnmngId))
				.innerJoin(tmInstllcRoad).on(tmInstllcRoad.roadCd.eq(tmExmnMng.roadCd))
				.leftJoin(tcCdGrp).on(tcCdGrp.grpCd.eq("TRF_DRCT_CD"))
				.leftJoin(tcCdInfo).on(tcCdInfo.cd.eq(tmInstllcRoad.drctCd)
						.and(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId)))
				.groupBy(tmInstllcRoad.drctCd,cdNm)
				.fetch();
	}

	/**
	 * methodName : getChartData
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : MCC 기간별 통계 차트데이터 조회
	 * @param searchDTO
	 * @return List<PeriodChartData>
	 */
	public List<TlPeriodRsltStatisticsDTO.PeriodChartData> getChartData(StatisticsByPeriodSerachDTO searchDTO) {
		String lang = LocaleContextHolder.getLocale().toString();
		StringExpression cdNm = getCdNm(lang);

		String numberStartDate = CommonUtils.onlyNumbers(searchDTO.getStartDate());
		String numberEndDate = CommonUtils.onlyNumbers(searchDTO.getEndDate());
		String numberStartHour = CommonUtils.onlyNumbers(searchDTO.getStartHour());
		String numberEndHour = CommonUtils.onlyNumbers(searchDTO.getEndHour());

		StringTemplate statsDate = Expressions.stringTemplate(
				"substring({0}, 9, 2)",
				tsMccTrfvlOnhr.statsYymmdt
		);
		return queryFactory.select(Projections.bean(TlPeriodRsltStatisticsDTO.PeriodChartData.class,
						statsDate.as("statsDate"),
						tmInstllcRoad.roadCd.as("instllcNm"),
						tmInstllcRoad.drctCd.as("drctCd"),
						cdNm.as("cdNm"),
						tsMccTrfvlOnhr.trfvlm.sum().as("trfvlm")))
				.from(tsMccTrfvlOnhr)
				.innerJoin(tmExmnMng).on(tsMccTrfvlOnhr.exmnmngId.eq(tmExmnMng.exmnmngId))
				.innerJoin(tmInstllcRoad).on(tmInstllcRoad.roadCd.eq(tmExmnMng.roadCd))
				.leftJoin(tcCdGrp).on(tcCdGrp.grpCd.eq("TRF_DRCT_CD"))
				.leftJoin(tcCdInfo).on(tcCdInfo.cd.eq(tmInstllcRoad.drctCd)
						.and(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId)))
				.where(
					startDateCondition(numberStartDate,numberStartHour)
					.and(endDateCondition(numberEndDate,numberEndHour)
					.and(tmInstllcRoad.roadCd.eq(searchDTO.getSearchContent())
					.and(searchDrctCd(searchDTO.getSearchType())
				))))
				.groupBy(statsDate,tmInstllcRoad.roadCd,tmInstllcRoad.drctCd,cdNm)
				.orderBy(statsDate.asc())
				.fetch();
	}


	/**
	 * methodName : getCdNm
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : 다국어 처리
	 * @param lang
	 * @return StringExpression
	 */
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
