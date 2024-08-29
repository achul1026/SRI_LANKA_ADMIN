package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.region.RegionDetailDTO;
import com.sl.tdbms.web.admin.common.dto.region.RegionDetailDTO.TazCodeInfo;
import com.sl.tdbms.web.admin.common.dto.region.RegionListDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTcDsdarMng;
import com.sl.tdbms.web.admin.common.entity.QTcShapeSrlk;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcDsdarMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTcDsdarMng tcDsdarMng = QTcDsdarMng.tcDsdarMng;
//	private QTcGnarMng tcGnarMng = QTcGnarMng.tcGnarMng;
	private QTcDsdMng tcDsdMng = QTcDsdMng.tcDsdMng;
	private QTcShapeSrlk tcShapeSrlk = QTcShapeSrlk.tcShapeSrlk;
	
	public int getMaxClsfNo() {
		int clsfNo = queryFactory
				.select(tcDsdarMng.clsfNo.max().coalesce(0))
				.from(tcDsdarMng)
				.fetchOne();
		return clsfNo;
	}

	public List<RegionListDTO> getDsdMngList(SearchCommonDTO searchDTO, PagingUtils paging) {
		List<RegionListDTO> result = queryFactory.select(
				Projections.constructor(RegionListDTO.class,
					tcDsdarMng.dsdId,
					tcDsdMng.provinCd,
					tcDsdMng.provinNm,
					tcDsdMng.districtCd,
					tcDsdMng.districtNm,
					tcDsdMng.dsdCd,
					tcDsdMng.dsdNm
//					tcShapeSrlk.cntrLat,
//					tcShapeSrlk.cntrLon
				)).distinct()
				.from(tcDsdarMng)
				.innerJoin(tcDsdMng).on(tcDsdMng.dsdId.eq(tcDsdarMng.dsdId))
				.innerJoin(tcShapeSrlk).on(tcShapeSrlk.dstrctCd.eq(tcDsdarMng.dstrctCd))
				.where(searchOption(searchDTO.getSearchTypeCd(), searchDTO.getSearchContent()))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tcDsdarMng.dsdId.asc())
				.fetch();
		return result;
	}
	
	public Long getDsdTotalCnt(SearchCommonDTO searchDTO) {
		Long totalCnt = queryFactory.select(tcDsdarMng.dsdId.countDistinct())
				.from(tcDsdarMng)
				.innerJoin(tcDsdMng).on(tcDsdMng.dsdId.eq(tcDsdarMng.dsdId))
				.innerJoin(tcShapeSrlk).on(tcShapeSrlk.dstrctCd.eq(tcDsdarMng.dstrctCd))
				.where(searchOption(searchDTO.getSearchTypeCd(), searchDTO.getSearchContent()))
				.fetchOne();
		return totalCnt != null ? totalCnt : 0L;
	}
	
	/**
	  * @Method Name : searchOption
	  * @작성일 : 2024. 7. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 검색 조건
	  * @param searchTypeCd
	  * @param searchContent
	  * @return
	  */
	private BooleanExpression searchOption(String searchTypeCd, String searchContent) {
		BooleanExpression result = null;
		if (CommonUtils.isNull(searchTypeCd) && !CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tcDsdMng.dsdNm, searchContent)
				.or(QRepositorySupport.containsKeyword(tcDsdMng.provinNm, searchContent))
				.or(QRepositorySupport.containsKeyword(tcDsdMng.districtNm, searchContent));
		} else if(!CommonUtils.isNull(searchTypeCd) && CommonUtils.isNull(searchContent)) {
			result = tcDsdarMng.dsdId.eq(searchTypeCd);
		} else if (!CommonUtils.isNull(searchTypeCd) && !CommonUtils.isNull(searchContent)) {
			result = (QRepositorySupport.containsKeyword(tcDsdMng.dsdNm, searchContent)
					.or(QRepositorySupport.containsKeyword(tcDsdMng.provinNm, searchContent))
					.or(QRepositorySupport.containsKeyword(tcDsdMng.districtNm, searchContent)))
					.and(tcDsdarMng.dsdId.eq(searchTypeCd));
		}
		return result;
	}

	/**
	  * @Method Name : getDsdInfo
	  * @작성일 : 2024. 7. 17.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 
	  * @param dsdId
	  * @return
	  */
	public RegionDetailDTO getDsdarInfo(String dsdId) {
		RegionDetailDTO result = queryFactory.select(Projections.bean(
				RegionDetailDTO.class,
												tcDsdarMng.dsdId,
												tcDsdMng.dsdCd,
												tcDsdMng.provinCd,
												tcDsdMng.provinNm,
												tcDsdMng.dsdNm,
												tcDsdMng.districtNm,
												tcDsdMng.districtCd)).distinct()
	        .from(tcDsdarMng)
	        .leftJoin(tcDsdMng).on(tcDsdMng.dsdId.eq(tcDsdarMng.dsdId))
	        .where(tcDsdarMng.dsdId.eq(dsdId))
	        .fetchOne();

	    if (result != null) {
	        List<TazCodeInfo> tazCodeInfoList = queryFactory.select(Projections.bean(TazCodeInfo.class,
	        		tcShapeSrlk.dstrctId,
	        		tcShapeSrlk.dstrctGis,
	        		tcShapeSrlk.dstrctCd,
	        		tcShapeSrlk.cntrLat,
	        		tcShapeSrlk.cntrLon,
	                tcDsdarMng.distrbCnt
	        		))
	            .from(tcShapeSrlk)
	            .join(tcDsdarMng).on(tcShapeSrlk.dstrctCd.eq(tcDsdarMng.dstrctCd))
	            .where(tcDsdarMng.dsdId.eq(dsdId))
	            .fetch();

	        result.setTazCodeInfoList(tazCodeInfoList);
	    }
		return result;
	}
}
