package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TcFaqMng;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.entity.QTcFaqMng;
import com.sl.tdbms.web.admin.common.entity.QTcUserMng;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcFaqMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTcFaqMng tcFaqMng = QTcFaqMng.tcFaqMng;
	
	private QTcUserMng tcUserMng = QTcUserMng.tcUserMng;
	
	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;
	
	
	/**
	  * @Method Name : getFaqList
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : FAQ 목록 조회
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	public List<TcFaqMng> getFaqList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		BooleanBuilder builder = new BooleanBuilder();

	    // 검색 조건 추가
	    builder.and(searchOption(searchCommonDTO.getSearchType(), searchCommonDTO.getSearchContent()));
	    if (userInfo.getUserType() != UserTypeCd.SUPER) builder.and(tcUserMng.userBffltd.eq(userInfo.getUserBffltd()));

		List<TcFaqMng> result = queryFactory
											.select(
													Projections.bean(
															TcFaqMng.class, 
															tcFaqMng.faqId, 
															QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("faqType"),
															tcFaqMng.faqQstn,
															tcFaqMng.dspyYn, 
															tcFaqMng.registId, 
															tcFaqMng.registDt)
													)
					.from(tcFaqMng)
					.leftJoin(tcCdInfo).on(tcFaqMng.faqType.eq(tcCdInfo.cd))
					.join(tcUserMng).on(tcUserMng.userId.eq(tcFaqMng.registId))
					.where(builder)
					.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tcFaqMng.registDt.desc()).fetch();

		return result;
	}

	/**
	  * @Method Name : getTotalCount
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : FAQ 갯수 조회
	  * @param searchCommonDTO
	  * @return
	  */
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {
		
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		BooleanBuilder builder = new BooleanBuilder(searchOption(searchCommonDTO.getSearchType(), searchCommonDTO.getSearchContent()));

	    // 해당 소속만 출력
	    if (userInfo.getUserType() != UserTypeCd.SUPER) builder.and(tcUserMng.userBffltd.eq(userInfo.getUserBffltd()));

		Long count = queryFactory.select(tcFaqMng.count()).from(tcFaqMng)
				.join(tcUserMng).on(tcUserMng.userId.eq(tcFaqMng.registId))
				.where(builder)
				.fetchOne();

		return count;
	}

	/**
	  * @Method Name : searchOption
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 검색 조건
	  * @param searchType
	  * @param searchContent
	  * @return
	  */
	private BooleanExpression searchOption(String searchType, String searchContent) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchType) && CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tcFaqMng.faqType, searchType);
		}
		if (CommonUtils.isNull(searchType) && !CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tcFaqMng.faqQstn, searchContent)
					.or(QRepositorySupport.containsKeyword(tcFaqMng.registId, searchContent));
		}
		if (!CommonUtils.isNull(searchType) && !CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tcFaqMng.faqType, searchType)
					.and(QRepositorySupport.containsKeyword(tcFaqMng.faqQstn, searchContent))
					.or(QRepositorySupport.containsKeyword(tcFaqMng.registId, searchContent));
		}
		return result;
	}

	/**
	  * @Method Name : getFaqMng
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : FAQ 상세 조회
	  * @param faqId
	  * @return
	  */
	public TcFaqMng getFaqMng(String faqId) {
		TcFaqMng result = queryFactory.select(Projections.bean(	
																TcFaqMng.class,
																tcFaqMng.faqId, 
																QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("faqType"),
																tcFaqMng.faqQstn,
																tcFaqMng.faqAns,
																tcFaqMng.dspyYn, 
																tcFaqMng.registId, 
																tcFaqMng.registDt))
							.from(tcFaqMng)
							.where(tcFaqMng.faqId.eq(faqId))
							.leftJoin(tcCdInfo).on(tcCdInfo.cd.eq(tcFaqMng.faqType))
							.fetchOne();
		return result;
	}

}
