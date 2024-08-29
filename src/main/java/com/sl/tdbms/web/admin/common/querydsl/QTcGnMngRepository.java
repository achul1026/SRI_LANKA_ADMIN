package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TcGnMng;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.entity.QTcGnMng;
import com.sl.tdbms.web.admin.common.entity.QTcGnarMng;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcGnMngRepository {

	private final JPAQueryFactory queryFactory;
	
	private QTcGnMng tcGnMng = QTcGnMng.tcGnMng; 
	private QTcGnarMng tcGnarMng = QTcGnarMng.tcGnarMng;
	
	
	public List<TcGnMng> getDsdLocationList(){
		
		List<TcGnMng> result = queryFactory.select(Projections.bean(
																	TcGnMng.class,
																	tcGnMng.gnId, 
																	tcGnMng.provinCd, 
																	tcGnMng.provinNm, 
																	tcGnMng.districtCd, 
																	tcGnMng.districtNm,
																	tcGnMng.dsdCd, 
																	tcGnMng.dsdNm,
																	tcGnMng.gnCd,
																	tcGnMng.gnNm,
																	tcGnMng.useYn 
																	)
													)
										.from(tcGnMng)
										.where(tcGnMng.useYn.eq("Y"))
										.orderBy(tcGnMng.provinCd.asc(),tcGnMng.districtCd.asc(),tcGnMng.dsdCd.asc())
										.fetch();

		return result;
	}


	/**
	  * @Method Name : getGnList
	  * @작성일 : 2024. 8. 19.
	  * @작성자 : SM.KIM
	  * @Method 설명 : tcGnarMng 테이블에 등록된 gnId 제외한 목록 조회
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	public List<TcGnMng> getGnList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TcGnMng> result = queryFactory.select(Projections.bean(
																	TcGnMng.class,
																	tcGnMng.gnId, 
																	tcGnMng.provinCd, 
																	tcGnMng.provinNm, 
																	tcGnMng.districtCd, 
																	tcGnMng.districtNm,
																	tcGnMng.dsdCd, 
																	tcGnMng.dsdNm,
																	tcGnMng.gnCd,
																	tcGnMng.gnNm,
																	tcGnMng.useYn 
																	)
															)
		.from(tcGnMng)
		.leftJoin(tcGnarMng).on(tcGnMng.gnId.eq(tcGnarMng.gnId))
		.where(tcGnMng.useYn.eq("Y").and(searchOption(searchCommonDTO))
//				.and(tcGnarMng.gnId.isNull())
				)
		.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
		.orderBy(tcGnMng.provinCd.asc(),tcGnMng.districtCd.asc(),tcGnMng.dsdCd.asc())
		.fetch();
		
		return result;
	}


	/**
	  * @Method Name : getTotalCount
	  * @작성일 : 2024. 8. 19.
	  * @작성자 : SM.KIM
	  * @Method 설명 : tcGnarMng 테이블에 등록된 gnId 제외한 목록 개수
	  * @param searchCommonDTO
	  * @return
	  */
	public long getTotalCount(SearchCommonDTO searchCommonDTO) {
		long count = queryFactory.select(tcGnMng.countDistinct())
				.from(tcGnMng)
				.leftJoin(tcGnarMng).on(tcGnMng.gnId.eq(tcGnarMng.gnId))
				.where(tcGnMng.useYn.eq("Y").and(searchOption(searchCommonDTO))
//						.and(tcGnarMng.gnId.isNull())
						)
				.fetchOne();
		return count;
	}
	
	/**
	  * @Method Name : searchOption
	  * @작성일 : 2024. 8. 27.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 검색 조건
	  * @param searchCommonDTO
	  * @return
	  */
	private BooleanExpression searchOption(SearchCommonDTO searchCommonDTO) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchCommonDTO.getSearchContent())) {
			result = QRepositorySupport.containsKeyword(tcGnMng.gnId, searchCommonDTO.getSearchContent())
						.or(QRepositorySupport.containsKeyword(tcGnMng.dsdNm, searchCommonDTO.getSearchContent()))
						.or(QRepositorySupport.containsKeyword(tcGnMng.provinNm, searchCommonDTO.getSearchContent()))
						.or(QRepositorySupport.containsKeyword(tcGnMng.districtNm, searchCommonDTO.getSearchContent()));
		}
		return result;
	}
}
