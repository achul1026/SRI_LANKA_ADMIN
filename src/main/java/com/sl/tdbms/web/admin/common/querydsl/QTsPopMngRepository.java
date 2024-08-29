package com.sl.tdbms.web.admin.common.querydsl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.dataregistmng.PopulationSearchDTO;
import com.sl.tdbms.web.admin.common.dto.dataregistmng.TsPopMngDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.TsPopulationStatisticsSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.entity.QTsPopFile;
import com.sl.tdbms.web.admin.common.entity.QTsPopMng;
import com.sl.tdbms.web.admin.common.entity.TsPopMng;
import com.sl.tdbms.web.admin.common.enums.code.PopStatsTypeCd;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class QTsPopMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTsPopMng tsPopMng = QTsPopMng.tsPopMng;
	private QTsPopFile tsPopFile = QTsPopFile.tsPopFile;

	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;
	
	
	/**
	 * @Method Name : getTsPopMngList
	 * @작성일 : 2024. 6. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 인구데이터 통계 부모 목록 조회
	 * @param paging
	 * @param populationSearchDTO
	 * @return List<TsPopMngDTO> 
	 */
	public List<TsPopMngDTO> getTsPopMngList(PopulationSearchDTO populationSearchDTO, PagingUtils paging) {
		List<TsPopMngDTO> result = queryFactory
				.select(Projections.bean(TsPopMngDTO.class, 
						tsPopMng.popmngId,
						tsPopFile.fileId,
						tsPopMng.popmngTitle,
						tsPopMng.popmngType,
						tsPopMng.popmngCnts,
						tsPopMng.userBffltd,
						tsPopMng.registDt,
						tsPopMng.registId,
						QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("userBffltdNm")
						))
				.from(tsPopMng)
				.leftJoin(tcCdInfo).on(tsPopMng.userBffltd.eq(tcCdInfo.cd))
				.leftJoin(tsPopFile).on(tsPopMng.tsPopFile.eq(tsPopFile))
				.where(searchOption(populationSearchDTO))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tsPopMng.registDt.desc()).fetch();
		return result;
	}
	
	/**
	 * @Method Name : getTotalCount
	 * @작성일 : 2024. 6. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 목록 카운트 조회
	 * @param populationSearchDTO
	 * @return BooleanExpression
	 */
	public Long getTotalCount(PopulationSearchDTO populationSearchDTO) {
		Long count = queryFactory.select(tsPopMng.count()).from(tsPopMng)
				.where(searchOption(populationSearchDTO)).fetchOne();
		return count;
	}
	
	/**
	 * @Method Name : getTsPopMngList
	 * @작성일 : 2024. 6. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 인구데이터 통계 단건 조회
	 * @param popmngId
	 * @return
	 */
	public TsPopMngDTO getTsPopMngDetail(String popmngId) {
		TsPopMngDTO result = queryFactory
	            .select(Projections.bean(TsPopMngDTO.class,
	                    tsPopMng.popmngId,
	                    tsPopMng.tsPopFile.fileId,
	                    tsPopMng.tsPopFile.orgFileNm,
	                    tsPopMng.popmngTitle,
	                    tsPopMng.popmngType,
	                    tsPopMng.popmngCnts,
	                    tsPopMng.userBffltd,
	                    tsPopMng.startYear,
	                    tsPopMng.endYear,
	                    tsPopMng.registDt,
	                    tsPopMng.registId,
	                    QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("userBffltdNm")
	                    ))
	            .from(tsPopMng)
	            .leftJoin(tcCdInfo).on(tsPopMng.userBffltd.eq(tcCdInfo.cd))
	            .where(tsPopMng.popmngId.eq(popmngId))
	            .fetchOne();
		return result;
	}
	
	/**
	 * @Method Name : searchOption
	 * @작성일 : 2024. 6. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 검색 조건
	 * @param populationSearchDTO
	 * @return BooleanBuilder
	 */
    private BooleanBuilder searchOption(PopulationSearchDTO populationSearchDTO) {
        BooleanBuilder builder = new BooleanBuilder();
        if (populationSearchDTO != null) {
            if (populationSearchDTO.getPopStatTypeCd() != null) {
                builder.and(tsPopMng.popmngType.eq(populationSearchDTO.getPopStatTypeCd()));
            }
            if (populationSearchDTO.getBffltdCd() != null &&!"".equals(populationSearchDTO.getBffltdCd())) {
                builder.and(tsPopMng.userBffltd.eq(populationSearchDTO.getBffltdCd()));
            }
            if (populationSearchDTO.getSearchContent() != null && !"".equals(populationSearchDTO.getSearchContent())) {
            	builder.and(tsPopMng.popmngTitle.like("%" + populationSearchDTO.getSearchContent() + "%"));
            }
        }
        return builder;
    }
    
	/**
	 * @Method Name : findMinMaxYear
	 * @작성일 : 2024. 6. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 인구통계 데이터 연도 조회
	 */
    public Map<String, String> findMinMaxYear() {
        Tuple result = queryFactory
            .select(tsPopMng.startYear.min(), 
            		tsPopMng.endYear.max())
            .from(tsPopMng)
            .fetchOne();

        Map<String, String> resultMap = new HashMap<>();
        resultMap.put("startYear", result.get(tsPopMng.startYear.min()));
        resultMap.put("endYear", result.get(tsPopMng.endYear.max()));
        return resultMap;
    }

	/**
	 * @Method Name : getTsPopMngStatsList
	 * @작성일 : 2024. 6. 28.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 인구 통계데이터 상세 통계 목록
	 */
	public List<TsPopMng> getTsPopMngStatsList(TsPopulationStatisticsSearchDTO searchDTO) {
        return queryFactory.selectFrom(tsPopMng)
        		.where(
                		eqPopStatTypeCd(searchDTO.getPopStatTypeCd()),
                		eqUserBffltd(searchDTO.getBffltdCd()),
                		betweenYears(searchDTO.getSearchYear())
                )
                .fetch();
	}

    private BooleanExpression eqPopStatTypeCd(PopStatsTypeCd popStatTypeCd) {
        return popStatTypeCd != null ? tsPopMng.popmngType.eq(popStatTypeCd) : null;
    }

    private BooleanExpression eqUserBffltd(String userBffltd) {
        return !CommonUtils.isNull(userBffltd) ? tsPopMng.userBffltd.eq(userBffltd) : null;
    }

    private BooleanExpression betweenYears(String searchYear) {
        if (!CommonUtils.isNull(searchYear)) {
            return tsPopMng.startYear.loe(searchYear)
                    .and(tsPopMng.endYear.goe(searchYear));
        } else {
            return null;
        }
    }
}

