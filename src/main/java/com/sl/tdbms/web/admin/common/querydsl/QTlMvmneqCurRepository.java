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
import com.sl.tdbms.web.admin.common.entity.QTlMvmneqCur;
import com.sl.tdbms.web.admin.common.entity.QTlMvmneqLog;
import com.sl.tdbms.web.admin.common.entity.QTmInstllcRoad;
import com.sl.tdbms.web.admin.common.entity.TlMvmneqLog;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTlMvmneqCurRepository {
	private final JPAQueryFactory queryFactory;
	
	private QTmInstllcRoad instllcRoad = QTmInstllcRoad.tmInstllcRoad;
	private QTlMvmneqCur mvmneqCur = QTlMvmneqCur.tlMvmneqCur;
	private QTlMvmneqLog mvmneqLog = QTlMvmneqLog.tlMvmneqLog; 
	private QTcRoadMng roadMng = QTcRoadMng.tcRoadMng;
	private QTcCdInfo drctCd = QTcCdInfo.tcCdInfo;
	private QTcCdGrp tcCdGrp = QTcCdGrp.tcCdGrp;

	/**
	 * @Method Name : getTlMvmneqCurList
	 * @작성일 : 2024. 7. 25.
	 * @작성자 : KC.KIM
	 * @Method 설명 : 이동형 장비 정보 조회
	 */
	public List<FacilitiesDTO> getTlMvmneqCurList() {
		List<FacilitiesDTO> result = queryFactory
				.select(Projections.bean(FacilitiesDTO.class, 
						mvmneqCur.instllcId,
						mvmneqCur.instllcNm,
						roadMng.roadDescr.coalesce("-").as("instllcDescr")
						))	
				.from(mvmneqCur)
				.leftJoin(instllcRoad).on(instllcRoad.instllcId.eq(mvmneqCur.instllcId))
				.leftJoin(roadMng).on(instllcRoad.roadCd.eq(roadMng.roadCd))
				.fetch();
		return result;
	}
	
	/**
	 * @Method Name : getFacilityPortableList
	 * @작성일 : 2024. 8. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 이동형 Metro Count 목록 조회
	 * @param searchCommonDTO
	 * @param paging
	 * @param typeCd
	 * @return
	 */
	public List<FacilitiesDTO> getFacilityPortableList(SearchCommonDTO searchCommonDTO, PagingUtils paging,
			String typeCd) {
		List<FacilitiesDTO> result = queryFactory
				.select(Projections.bean(FacilitiesDTO.class, 
//										mvmneqCur.eqpmntId,
										mvmneqCur.instllcId,
										mvmneqCur.instllcNm,
										mvmneqCur.lat,
										mvmneqCur.lon,
										instllcRoad.roadCd,
										instllcRoad.useYn,
										instllcRoad.registDt
										))
				.from(instllcRoad)
				.leftJoin(mvmneqCur).on(mvmneqCur.instllcId.eq(instllcRoad.instllcId))
				.where(instllcRoad.eqpmntClsf.eq(typeCd),portableSearchOption(searchCommonDTO.getSearchContent()))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
				.groupBy(mvmneqCur.instllcId,instllcRoad.roadCd,instllcRoad.useYn,instllcRoad.registDt)
				.orderBy(instllcRoad.registDt.desc())
				.fetch();

		return result;
	}

	/**
	 * @Method Name : portableSearchOption
	 * @작성일 : 2024. 8. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 이동형 Metro Count 검색 조건
	 * @param searchContent
	 * @return
	 */
	private BooleanExpression portableSearchOption(String searchContent) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(mvmneqCur.instllcNm, searchContent);
		}
		return result;
	}

	/**
	 * @Method Name : getTotalPortableCount
	 * @작성일 : 2024. 8. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 이동형 Metro Count 목록 개수
	 * @param searchCommonDTO
	 * @param typeCd
	 * @return
	 */
	public long getTotalPortableCount(SearchCommonDTO searchCommonDTO, String typeCd) {
		long count = queryFactory.select(instllcRoad.countDistinct())
				.from(instllcRoad)
				.leftJoin(mvmneqCur).on(mvmneqCur.instllcId.eq(instllcRoad.instllcId))
				.where(instllcRoad.eqpmntClsf.eq(typeCd),portableSearchOption(searchCommonDTO.getSearchContent()))
				.fetchOne();
		return count;
	}

	/**
	  * @Method Name : getFacilityPortableInfo
	  * @작성일 : 2024. 8. 13.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 이동형 Metro Count 상세 조회
	  * @param instllcId
	  * @param grpCd
	  * @param typeCd
	  * @return
	  */
	public FacilitiesDTO getFacilityPortableInfo(String instllcId, String grpCd, String typeCd) {
		FacilitiesDTO result = queryFactory
				.select(Projections.bean(FacilitiesDTO.class, 
										mvmneqCur.instllcId,
										mvmneqCur.instllcNm,
//										mvmneqLog.instllcDescr,
//										mvmneqLog.eqpmntId,
//										mvmneqLog.clctDt,
										mvmneqCur.lat,
										mvmneqCur.lon,
										instllcRoad.roadCd,
										instllcRoad.laneCnt,
//										QRepositorySupport.getCodeInfoNamePath(typeCd).as("eqpmntClsf"),
										QRepositorySupport.getCodeInfoNamePath(drctCd).as("drctCd"),
										instllcRoad.useYn,
										instllcRoad.updtDt
										))
				.from(mvmneqCur)
				.leftJoin(instllcRoad).on(instllcRoad.instllcId.eq(instllcId).and(instllcRoad.eqpmntClsf.eq(typeCd)))
				.leftJoin(tcCdGrp).on(tcCdGrp.grpCd.eq(grpCd))
				.leftJoin(drctCd).on(tcCdGrp.grpcdId.eq(drctCd.grpcdId).and(drctCd.cd.eq(instllcRoad.drctCd)))
//				.leftJoin(typeCd).on(typeCd.cd.eq(instllcRoad.eqpmntClsf))
				.where(mvmneqCur.instllcId.eq(instllcId))
//				.orderBy(mvmneqLog.clctDt.desc())
//				.limit(1)
				.fetchOne();
		return result;
	}

	/**
	  * @Method Name : getMvmneqLogList
	  * @작성일 : 2024. 8. 13.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 이동형 Metro Count Log 목록 조회
	  * @param instllcId
	  * @param paging
	  * @return
	  */
	public List<TlMvmneqLog> getMvmneqLogList(String instllcId, PagingUtils paging) {
		List<TlMvmneqLog> result = queryFactory
				.select(Projections.bean(TlMvmneqLog.class, 
						mvmneqLog.clctDt,
						mvmneqLog.instllcNm,
						mvmneqLog.lat,
						mvmneqLog.lon
						))
				.from(mvmneqLog)
				.where(mvmneqLog.instllcId.eq(instllcId))
				//상세 페이지에서 상세에 해당하는 로그는 제외하기 위해 offset + 1
				.offset(paging.getOffsetCount()+1).limit(paging.getLimitCount())
				.orderBy(mvmneqLog.clctDt.desc())
				.fetch();
		return result;
	}

	/**
	  * @Method Name : getTotalMvmneqLogCount
	  * @작성일 : 2024. 8. 13.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 이동형 Metro Count Log 목록 개수
	  * @param instllcId
	  * @return
	  */
	public long getTotalMvmneqLogCount(String instllcId) {
		long count = queryFactory.select(mvmneqLog.countDistinct())
				.from(mvmneqLog)
				.where(mvmneqLog.instllcId.eq(instllcId))
				.fetchOne();
		return count;
	}
}
