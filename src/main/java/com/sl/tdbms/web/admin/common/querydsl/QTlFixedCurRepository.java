package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.facilties.FacilitiesDTO;
import com.sl.tdbms.web.admin.common.entity.QTcCdGrp;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.entity.QTcRoadMng;
import com.sl.tdbms.web.admin.common.entity.QTlFixedCur;
import com.sl.tdbms.web.admin.common.entity.QTmInstllcRoad;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTlFixedCurRepository {
	private final JPAQueryFactory queryFactory;
	
	private QTmInstllcRoad instllcRoad = QTmInstllcRoad.tmInstllcRoad;
	private QTlFixedCur fixedCur = QTlFixedCur.tlFixedCur;
	private QTcRoadMng roadMng = QTcRoadMng.tcRoadMng;
	private QTcCdInfo drctCd = QTcCdInfo.tcCdInfo;
	private QTcCdGrp tcCdGrp = QTcCdGrp.tcCdGrp;

	/**
	 * @Method Name : getTlFixedCurList
	 * @작성일 : 2024. 7. 25.
	 * @작성자 : KC.KIM
	 * @Method 설명 : 고정형 장비 정보 조회
	 */
	public List<FacilitiesDTO> getTlFixedCurList() {
		List<FacilitiesDTO> result = queryFactory
				.select(Projections.bean(FacilitiesDTO.class, 
						fixedCur.instllcId,
						fixedCur.instllcNm,
						roadMng.roadDescr.coalesce("-").as("instllcDescr")
						))	
				.from(fixedCur)
				.leftJoin(instllcRoad).on(instllcRoad.instllcId.eq(fixedCur.instllcId))
				.leftJoin(roadMng).on(instllcRoad.roadCd.eq(roadMng.roadCd))
				.fetch();
		return result;
	}
	
	/**
	  * @Method Name : getFacilityFixedList
	  * @작성일 : 2024. 8. 8.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 고정형 Metro Count 목록 조회
	  * @param searchCommonDTO
	  * @param paging
	  * @param typeCd
	  * @return
	  */
	public List<FacilitiesDTO> getFacilityFixedList(SearchCommonDTO searchCommonDTO, PagingUtils paging, String typeCd) {
		List<FacilitiesDTO> result = queryFactory
				.select(Projections.bean(FacilitiesDTO.class,
										fixedCur.instllcId,
										fixedCur.instllcNm,
										instllcRoad.registDt,
//										instllcRoad.roadCd,
										instllcRoad.useYn
										))
				.from(instllcRoad)
				.leftJoin(fixedCur).on(fixedCur.instllcId.eq(instllcRoad.instllcId))
				.where(instllcRoad.eqpmntClsf.eq(typeCd),fixedSearchOption(searchCommonDTO.getSearchContent()))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
				.groupBy(fixedCur.instllcId,instllcRoad.roadCd,instllcRoad.useYn,instllcRoad.registDt)
				.orderBy(instllcRoad.registDt.desc())
				.fetch();

		return result;
	}

	/**
	  * @Method Name : getTotalFixedCount
	  * @작성일 : 2024. 8. 8.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 고정형 Metro Count 목록 개수
	  * @param searchCommonDTO
	  * @param typeCd
	  * @return
	  */
	public long getTotalFixedCount(SearchCommonDTO searchCommonDTO, String typeCd) {
		long count = queryFactory.select(instllcRoad.countDistinct())
				.from(instllcRoad)
				.leftJoin(fixedCur).on(fixedCur.instllcId.eq(instllcRoad.instllcId))
				.where(instllcRoad.eqpmntClsf.eq(typeCd),fixedSearchOption(searchCommonDTO.getSearchContent()))
				.fetchOne();
		return count;
	}
	
	/**
	  * @Method Name : fixedSearchOption
	  * @작성일 : 2024. 8. 8.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 고정형 Metro Count 검색 조건
	  * @param searchContent
	  * @return
	  */
	private BooleanExpression fixedSearchOption(String searchContent) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(fixedCur.instllcNm, searchContent);
		}
		return result;
	}

	/**
	  * @Method Name : getFacilityFixedInfo
	  * @작성일 : 2024. 8. 9.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 고정형 Metro Count 상세 조회
	  * @param instllcId
	  * @param grpCd
	  * @return
	  */
	public FacilitiesDTO getFacilityFixedInfo(String instllcId, String grpCd, String typeCd) {
		FacilitiesDTO result = queryFactory
				.select(Projections.bean(FacilitiesDTO.class, 
										fixedCur.instllcId,
										fixedCur.instllcNm,
										fixedCur.lat,
										fixedCur.lon,
										instllcRoad.roadCd,
										instllcRoad.laneCnt,
//										QRepositorySupport.getCodeInfoNamePath(typeCd).as("eqpmntClsf"),
										QRepositorySupport.getCodeInfoNamePath(drctCd).as("drctCd"),
										instllcRoad.useYn
										))
				.from(fixedCur)
				.leftJoin(instllcRoad).on(instllcRoad.instllcId.eq(instllcId).and(instllcRoad.eqpmntClsf.eq(typeCd)))
//				.leftJoin(typeCd).on(typeCd.cd.eq(instllcRoad.eqpmntClsf))
				.leftJoin(tcCdGrp).on(tcCdGrp.grpCd.eq(grpCd))
				.leftJoin(drctCd).on(tcCdGrp.grpcdId.eq(drctCd.grpcdId).and(drctCd.cd.eq(instllcRoad.drctCd)))
				.where(fixedCur.instllcId.eq(instllcId))
				.fetchOne();
		
		return result;
	}
}
