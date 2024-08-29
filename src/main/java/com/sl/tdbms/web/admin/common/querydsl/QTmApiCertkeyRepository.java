package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.datalink.OpenApiRequestListDto;
import com.sl.tdbms.web.admin.common.entity.QTmApiCertkey;
import com.sl.tdbms.web.admin.common.entity.QTmApiKeysrvc;
import com.sl.tdbms.web.admin.common.entity.QTmApiSrvc;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmApiCertkeyRepository {

	private final JPAQueryFactory queryFactory;

	QTmApiCertkey tmApiCertkey = QTmApiCertkey.tmApiCertkey;
	QTmApiKeysrvc tmApiKeysrvc = QTmApiKeysrvc.tmApiKeysrvc;
	QTmApiSrvc tmApiSrvc = QTmApiSrvc.tmApiSrvc;

	/**
	 * Gets api cert key request list.
	 *
	 * @param searchCommonDTO the search common dto
	 * @param paging          the paging
	 * @return the api cert key request list
	 */
	public List<OpenApiRequestListDto> getApiCertKeyRequestList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {

		return queryFactory.select(
						Projections.bean(
								OpenApiRequestListDto.class,
								tmApiCertkey.certkeyId,
								tmApiCertkey.email,
								tmApiCertkey.apiKey,
								tmApiCertkey.endYmd,
								tmApiCertkey.sttsCd,
								tmApiCertkey.rqstDt,
								tmApiCertkey.athrztDt,
								tmApiCertkey.athrztId,
								tmApiCertkey.aplyRsn
//								tmApiSrvc.srvcNm,
						)
				)
				.from(tmApiCertkey)
				//.innerJoin(tmApiKeysrvc).on(tmApiCertkey.certkeyId.eq(tmApiKeysrvc.certkeyId))
				//.innerJoin(tmApiSrvc).on(tmApiSrvc.srvcId.eq(tmApiKeysrvc.srvcId))
				.where(searchOption(searchCommonDTO))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
				.orderBy(tmApiCertkey.rqstDt.desc()).fetch();
	}

	/**
	 * Gets total count.
	 *
	 * @param searchCommonDTO the search common dto
	 * @return the total count
	 */
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {

		Long count = queryFactory.select(tmApiCertkey.count())
				.from(tmApiCertkey)
				//.innerJoin(tmApiKeysrvc).on(tmApiCertkey.certkeyId.eq(tmApiKeysrvc.certkeyId))
				//.innerJoin(tmApiSrvc).on(tmApiSrvc.srvcId.eq(tmApiKeysrvc.srvcId))
				.where(searchOption(searchCommonDTO))
				.fetchOne();

		return count;
	}

	/**
	 * Get new api cert key string.
	 *
	 * @return the string
	 */
	public String getNewApiCertId(){
		StringTemplate uniqueIdTemplate = Expressions.stringTemplate(
				"TO_CHAR(NOW(), 'YYYYMMDD') || LPAD(CAST(nextval('api_id_seq') AS TEXT), 6, '0')"
		);
		return queryFactory.select(uniqueIdTemplate)
				.fetchOne();
	}

	private BooleanExpression searchOption(SearchCommonDTO searchCommonDTO) {
		BooleanExpression result = null;

		if (searchCommonDTO.getSearchTypeCd() != null && !searchCommonDTO.getSearchTypeCd().isBlank()) {
			result = QRepositorySupport.toEqExpression(tmApiCertkey.sttsCd, searchCommonDTO.getSearchTypeCd());
		}

		if (searchCommonDTO.getSearchContent() != null && !searchCommonDTO.getSearchContent().isBlank()) {
			BooleanExpression keywordExpression = QRepositorySupport.containsKeyword(tmApiCertkey.email, searchCommonDTO.getSearchContent());
			result = (result != null) ? result.and(keywordExpression) : keywordExpression;
		}
		return result;

	}

	public List<OpenApiRequestListDto> getApiSrvcList(String certkeyId) {
		return queryFactory.select(
						Projections.bean(
								OpenApiRequestListDto.class,
								tmApiCertkey.certkeyId,
								tmApiCertkey.email,
								tmApiCertkey.apiKey,
								tmApiCertkey.endYmd,
								tmApiCertkey.sttsCd,
								tmApiCertkey.rqstDt,
								tmApiCertkey.athrztDt,
								tmApiCertkey.athrztId,
								tmApiCertkey.aplyRsn,
								tmApiSrvc.srvcId,
								tmApiSrvc.srvcNm,
								tmApiSrvc.srvcUrl,
								tmApiSrvc.srvcClsf,
								tmApiSrvc.srvcDescr,
								tmApiSrvc.pvsnInst
						)
				)
				.from(tmApiCertkey)
				.innerJoin(tmApiKeysrvc).on(tmApiCertkey.certkeyId.eq(tmApiKeysrvc.certkeyId))
				.innerJoin(tmApiSrvc).on(tmApiSrvc.srvcId.eq(tmApiKeysrvc.srvcId))
				.where(tmApiCertkey.certkeyId.eq(certkeyId).and(tmApiSrvc.useYn.eq("Y")))
				.orderBy(tmApiCertkey.rqstDt.desc()).fetch();
	}

	public Long getSrvcKeyConnectCount(String certkeyId , SearchCommonDTO searchCommonDTO) {
		return queryFactory.select(
				tmApiSrvc.countDistinct()
				)
			    .from(tmApiSrvc)
			    .leftJoin(tmApiKeysrvc).on(tmApiKeysrvc.srvcId.eq(tmApiSrvc.srvcId))
	            .where(apiSrvcListOption(certkeyId,searchCommonDTO))
				.fetchOne();
	}
	
	public List<OpenApiRequestListDto> getSrvcKeyConnectList(String certkeyId , SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		return queryFactory.select(
		        Projections.bean(
		                OpenApiRequestListDto.class,
		                tmApiSrvc.srvcId,
		                tmApiSrvc.srvcNm,
		                tmApiSrvc.srvcUrl,
		                tmApiSrvc.srvcClsf,
		                tmApiSrvc.srvcDescr,
		                tmApiSrvc.pvsnInst
		        )
		    )
			.distinct()
		    .from(tmApiSrvc)
		    .leftJoin(tmApiKeysrvc).on(tmApiKeysrvc.srvcId.eq(tmApiSrvc.srvcId))
		    .where(apiSrvcListOption(certkeyId,searchCommonDTO))
			.offset(paging.getOffsetCount()).limit(paging.getLimitCount())
		    .orderBy(tmApiSrvc.srvcId.desc())
		    .fetch();
	}
	
	public BooleanBuilder apiSrvcListOption(String certkeyId , SearchCommonDTO searchCommonDTO) {
	    BooleanBuilder builder = new BooleanBuilder();
	    builder.and(tmApiSrvc.useYn.eq("Y"))
        .and(tmApiKeysrvc.certkeyId.isNull().or(tmApiKeysrvc.certkeyId.ne(certkeyId)));
		
		if (!CommonUtils.isNull(searchCommonDTO.getSearchContent())) {
			builder.and(tmApiSrvc.srvcNm.like("%" + searchCommonDTO.getSearchContent() + "%"));
		}
		
		if (!CommonUtils.isNull(searchCommonDTO.getSearchType())) {
			builder.and(tmApiSrvc.srvcClsf.eq(searchCommonDTO.getSearchType()));
		}
	    return builder;
	}

}
