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
import com.sl.tdbms.web.admin.common.entity.QTmInstllcRoad;
import com.sl.tdbms.web.admin.common.entity.QTmVdsInstllc;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmVdsInstllcRepository {
	
	private final JPAQueryFactory queryFactory;
	
	private QTmInstllcRoad instllcRoad = QTmInstllcRoad.tmInstllcRoad;
	private QTmVdsInstllc vdsInstllc = QTmVdsInstllc.tmVdsInstllc;
	private QTcRoadMng roadMng = QTcRoadMng.tcRoadMng;
	private QTcCdInfo drctCd = QTcCdInfo.tcCdInfo;
	private QTcCdGrp tcCdGrp = QTcCdGrp.tcCdGrp;

	/**
	 * @Method Name : getTmVdsInstllcList
	 * @작성일 : 2024. 7. 25.
	 * @작성자 : KC.KIM
	 * @Method 설명 : VDS 장비 정보 조회
	 */
	public List<FacilitiesDTO> getTmVdsInstllcList() {
		List<FacilitiesDTO> result = queryFactory
				.select(Projections.bean(FacilitiesDTO.class, 
						vdsInstllc.instllcId,
						vdsInstllc.instllcNm,
						vdsInstllc.cameraId,
						roadMng.roadDescr.coalesce("-").as("instllcDescr")
						))	
				.from(vdsInstllc)
				.leftJoin(instllcRoad).on(instllcRoad.instllcId.eq(vdsInstllc.instllcId))
				.leftJoin(roadMng).on(instllcRoad.roadCd.eq(roadMng.roadCd))
				.fetch();
		return result;
	}
	
	/**
	  * @Method Name : getFacilityList
	  * @작성일 : 2024. 7. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : VDS 시설물 목록 조회
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	public List<FacilitiesDTO> getFacilityVdsList(SearchCommonDTO searchCommonDTO, PagingUtils paging, String type) {
		List<FacilitiesDTO> result = queryFactory
				.select(Projections.bean(FacilitiesDTO.class, 
										vdsInstllc.instllcId,
										vdsInstllc.instllcNm,
										vdsInstllc.lat,
										vdsInstllc.cameraId,
										vdsInstllc.lon,
										instllcRoad.roadCd,
//										QRepositorySupport.getCodeInfoNamePath(typeCd).as("eqpmntClsf"),
//										QRepositorySupport.getCodeInfoNamePath(drctCd).as("drctCd"),
										instllcRoad.useYn,
										instllcRoad.registDt
										))
//				.from(vdsInstllc)
//				.leftJoin(instllcRoad).on(instllcRoad.instllcId.eq(vdsInstllc.instllcId))
//				.leftJoin(typeCd).on(typeCd.cd.eq(instllcRoad.eqpmntClsf))
//				.leftJoin(drctCd).on(drctCd.cd.eq(instllcRoad.drctCd))
//				.where(instllcRoad.eqpmntClsf.eq(type),vdsInstllc.cameraId.eq(searchCommonDTO.getSearchContent()))
				.from(instllcRoad)
				.leftJoin(vdsInstllc).on(vdsInstllc.instllcId.eq(instllcRoad.instllcId))
				.where(instllcRoad.eqpmntClsf.eq(type),vdsSearchOption(searchCommonDTO.getSearchContent()))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
				.groupBy(vdsInstllc.instllcId,instllcRoad.roadCd,instllcRoad.useYn,instllcRoad.registDt)
				.orderBy(instllcRoad.registDt.desc())
				.fetch();

		return result;
	}
	
	/**
	  * @Method Name : getFacilityInfo
	  * @작성일 : 2024. 7. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : VDS 시설물 상세 조회
	  * @param instllcId
	  * @return
	  */
	public FacilitiesDTO getFacilityVdsInfo(String instllcId, String grpCd, String typeCd) {
		FacilitiesDTO result = queryFactory
				.select(Projections.bean(FacilitiesDTO.class, 
										vdsInstllc.instllcId,
										vdsInstllc.instllcNm,
										vdsInstllc.instllcDescr,
										vdsInstllc.lat,
										vdsInstllc.cameraId,
										vdsInstllc.registDt,
										vdsInstllc.lon,
										instllcRoad.roadCd,
										instllcRoad.laneCnt,
//										QRepositorySupport.getCodeInfoNamePath(typeCd).as("eqpmntClsf"),
										QRepositorySupport.getCodeInfoNamePath(drctCd).as("drctCd"),
										instllcRoad.useYn
										))
				.from(vdsInstllc)
				.leftJoin(instllcRoad).on(instllcRoad.instllcId.eq(instllcId).and(instllcRoad.eqpmntClsf.eq(typeCd)))
//				.leftJoin(typeCd).on(typeCd.cd.eq(instllcRoad.eqpmntClsf))
				.leftJoin(tcCdGrp).on(tcCdGrp.grpCd.eq(grpCd))
				.leftJoin(drctCd).on(tcCdGrp.grpcdId.eq(drctCd.grpcdId).and(drctCd.cd.eq(instllcRoad.drctCd)))
				.where(vdsInstllc.instllcId.eq(instllcId))
				.fetchOne();
		return result;
	}
	
	/**
	  * @Method Name : getTotalCount
	  * @작성일 : 2024. 7. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : VDS 시설물 개수
	  * @param searchCommonDTO
	  * @return
	  */
	public Long getTotalVdsCount(SearchCommonDTO searchCommonDTO, String type) {
		Long count = queryFactory.select(vdsInstllc.countDistinct())
				.from(instllcRoad)
				.leftJoin(vdsInstllc).on(vdsInstllc.instllcId.eq(instllcRoad.instllcId))
				.where(instllcRoad.eqpmntClsf.eq(type),vdsSearchOption(searchCommonDTO.getSearchContent()))
				.fetchOne();
		return count;
	}
	
	/**
	  * @Method Name : searchOption
	  * @작성일 : 2024. 7. 3.
	  * @작성자 : SM.KIM
	  * @Method 설명 : VDS 검색 조건
	  * @param searchTypeCd
	  * @param searchContent
	  * @return
	  */
	private BooleanExpression vdsSearchOption(String searchContent) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(vdsInstllc.cameraId, searchContent);
		}
		return result;
	}
}