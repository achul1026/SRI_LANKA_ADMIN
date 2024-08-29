package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.entity.QTcDsdMng;
import com.sl.tdbms.web.admin.common.entity.QTcDsdarMng;
import com.sl.tdbms.web.admin.common.entity.TcDsdMng;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcDsdMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTcDsdMng qTcDsdMng = QTcDsdMng.tcDsdMng;
	private QTcDsdarMng tcDsdarMng = QTcDsdarMng.tcDsdarMng;

	/**
	  * @Method Name : getDsdList
	  * @작성일 : 2024. 8. 14.
	  * @작성자 : SM.KIM
	  * @Method 설명 : tcDsdarMng 테이블에 등록된 dsdId 제외한 목록 조회
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	public List<TcDsdMng> getDsdList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {

		List<TcDsdMng> result = queryFactory.select(Projections.bean(
																	TcDsdMng.class,
																	qTcDsdMng.dsdCd,
																	qTcDsdMng.dsdNm,
																	qTcDsdMng.provinNm,
																	qTcDsdMng.provinCd,
																	qTcDsdMng.districtCd,
																	qTcDsdMng.districtNm,
																	qTcDsdMng.dsdId
																	)
															)
															.from(qTcDsdMng)
															.leftJoin(tcDsdarMng).on(qTcDsdMng.dsdId.eq(tcDsdarMng.dsdId))
															.where(qTcDsdMng.useYn.eq("Y").and(tcDsdarMng.dsdId.isNull()).and(searchOption(searchCommonDTO)))
															.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
															.orderBy(qTcDsdMng.dsdId.asc())
															.fetch();

		return result;
	}

	/**
	  * @Method Name : getTotalCount
	  * @작성일 : 2024. 8. 14.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 
	  * @param searchCommonDTO
	  * @return
	  */
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {
		Long count = queryFactory.select(qTcDsdMng.countDistinct())
									.from(qTcDsdMng)
									.leftJoin(tcDsdarMng).on(qTcDsdMng.dsdId.eq(tcDsdarMng.dsdId))
									.where(qTcDsdMng.useYn.eq("Y").and(tcDsdarMng.dsdId.isNull()).and(searchOption(searchCommonDTO)))
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
			result = QRepositorySupport.containsKeyword(qTcDsdMng.dsdCd, searchCommonDTO.getSearchContent())
						.or(QRepositorySupport.containsKeyword(qTcDsdMng.dsdNm, searchCommonDTO.getSearchContent()))
						.or(QRepositorySupport.containsKeyword(qTcDsdMng.provinNm, searchCommonDTO.getSearchContent()))
						.or(QRepositorySupport.containsKeyword(qTcDsdMng.districtNm, searchCommonDTO.getSearchContent()));
		}
		return result;
	}
}
