package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.region.RegionDetailDTO;
import com.sl.tdbms.web.admin.common.dto.region.RegionDetailDTO.GnCodeInfo;
import com.sl.tdbms.web.admin.common.dto.region.RegionListDTO;
import com.sl.tdbms.web.admin.common.entity.QTcGnMng;
import com.sl.tdbms.web.admin.common.entity.QTcGnarMng;
import com.sl.tdbms.web.admin.common.entity.QTcShapeSrlk;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcGnarMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTcGnarMng tcGnarMng = QTcGnarMng.tcGnarMng;
	
	private QTcGnMng tcGnMng = QTcGnMng.tcGnMng;

	private QTcShapeSrlk tcShapeSrlk = QTcShapeSrlk.tcShapeSrlk;
	
	public int getMaxClsfNo() {
		int clsfNo = queryFactory
				.select(tcGnarMng.clsfNo.max().coalesce(0))
				.from(tcGnarMng)
				.fetchOne();
		return clsfNo;
	}

	public List<RegionListDTO> getGnMngList(SearchCommonDTO searchDTO, PagingUtils paging) {
		List<RegionListDTO> result = queryFactory.select(
				Projections.bean(RegionListDTO.class,
					tcGnarMng.dstrctCd,
					tcGnMng.provinNm,
					tcGnMng.districtNm,
					tcGnMng.dsdNm,
					tcShapeSrlk.cntrLat,
					tcShapeSrlk.cntrLon
				)).distinct()
				.from(tcGnarMng)
				.innerJoin(tcGnMng).on(tcGnMng.gnId.eq(tcGnarMng.gnId))
				.innerJoin(tcShapeSrlk).on(tcShapeSrlk.dstrctCd.eq(tcGnarMng.dstrctCd))
				.where(searchOption(searchDTO.getSearchTypeCd(),searchDTO.getSearchContent()))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tcGnarMng.dstrctCd.asc())
				.fetch();
		return result;
	}

	public long getGnTotalCnt(SearchCommonDTO searchDTO) {
		Long totalCnt = queryFactory.select(tcGnarMng.dstrctCd.countDistinct())
				.from(tcGnarMng)
				.innerJoin(tcShapeSrlk).on(tcShapeSrlk.dstrctCd.eq(tcGnarMng.dstrctCd))
				.innerJoin(tcGnMng).on(tcGnMng.gnId.eq(tcGnarMng.gnId))
				.where(searchOption(searchDTO.getSearchTypeCd(),searchDTO.getSearchContent()))
				.fetchOne();
		return totalCnt != null ? totalCnt : 0L;
	}
	
	private BooleanExpression searchOption(String searchTypeCd, String searchContent) {
		BooleanExpression result = null;
		if (CommonUtils.isNull(searchTypeCd) && !CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tcGnMng.dsdNm, searchContent)
				.or(QRepositorySupport.containsKeyword(tcGnMng.provinNm, searchContent))
				.or(QRepositorySupport.containsKeyword(tcGnMng.districtNm, searchContent));
		} else if(!CommonUtils.isNull(searchTypeCd) && CommonUtils.isNull(searchContent)) {
			result = tcGnarMng.dstrctCd.eq(searchTypeCd);
		} else if (!CommonUtils.isNull(searchTypeCd) && !CommonUtils.isNull(searchContent)) {
			result = (QRepositorySupport.containsKeyword(tcGnMng.dsdNm, searchContent)
					.or(QRepositorySupport.containsKeyword(tcGnMng.provinNm, searchContent))
					.or(QRepositorySupport.containsKeyword(tcGnMng.districtNm, searchContent)))
					.and(tcGnarMng.dstrctCd.eq(searchTypeCd));
		}
		return result;
	}

	public RegionDetailDTO getGnarInfo(String dstrctCd) {
		RegionDetailDTO result = queryFactory.select(Projections.bean(
														RegionDetailDTO.class,
														tcShapeSrlk.dstrctCd,
														tcShapeSrlk.cntrLat,
														tcShapeSrlk.cntrLon)).distinct()
											.from(tcGnarMng)
											.innerJoin(tcShapeSrlk).on(tcShapeSrlk.dstrctCd.eq(tcGnarMng.dstrctCd))
											.where(tcGnarMng.dstrctCd.eq(dstrctCd))
											.fetchOne();
		
		if (result != null) {
			List<GnCodeInfo> gnCodeInfoList = queryFactory.select(Projections.bean(GnCodeInfo.class,
															tcGnMng.gnId,
															tcGnMng.gnNm,
															tcGnMng.provinNm,
															tcGnMng.districtNm,
															tcGnMng.dsdNm,
															tcGnarMng.distrbCnt
															))
												.from(tcGnarMng)
												.innerJoin(tcGnMng).on(tcGnMng.gnId.eq(tcGnarMng.gnId))
												.where(tcGnarMng.dstrctCd.eq(dstrctCd))
												.fetch();
			result.setGnCodeInfoList(gnCodeInfoList);
		}
		return result;
	}
}
