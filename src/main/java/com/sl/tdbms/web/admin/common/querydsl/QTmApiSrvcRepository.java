package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TmApiPvsnitem;
import com.sl.tdbms.web.admin.common.entity.TmApiRqstitem;
import com.sl.tdbms.web.admin.common.entity.TmApiSrvc;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Coalesce;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.datalink.OpenApiDetailDTO;
import com.sl.tdbms.web.admin.common.dto.datalink.OpenApiSearchDTO;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.entity.QTcUserMng;
import com.sl.tdbms.web.admin.common.entity.QTmApiPvsnitem;
import com.sl.tdbms.web.admin.common.entity.QTmApiRqstitem;
import com.sl.tdbms.web.admin.common.entity.QTmApiSrvc;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmApiSrvcRepository {

	private final JPAQueryFactory queryFactory;

	private QTmApiSrvc tmApiSrvc = QTmApiSrvc.tmApiSrvc;
	
	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;
	
	private QTmApiPvsnitem tmApiPvsnitem = QTmApiPvsnitem.tmApiPvsnitem;
    
	private QTmApiRqstitem tmApiRqstitem = QTmApiRqstitem.tmApiRqstitem;
	
	private QTcUserMng tcUserMng = QTcUserMng.tcUserMng;

	/**
	 * @Method Name : getTmApiSrvcList
	 * @작성일 : 2024. 6. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : OpenAPi 목록 조회
	 * @param OpenApiSearchDTO
	 * @return BooleanBuilder
	 */
	public List<TmApiSrvc> getTmApiSrvcList(OpenApiSearchDTO openApiSearchDTO, PagingUtils paging) {
		List<TmApiSrvc> result = queryFactory
				.select(Projections.bean(TmApiSrvc.class, 
						tmApiSrvc.srvcId,
						tmApiSrvc.srvcNm,
						tmApiSrvc.srvcDescr,
						tmApiSrvc.srvcUrl,
						tmApiSrvc.pvsnInst,
						tmApiSrvc.registDt,
						new Coalesce<>(String.class).add(QRepositorySupport.getCodeInfoNamePath(tcCdInfo)).add(tmApiSrvc.srvcClsf).as("srvcClsf")
						))
				.from(tmApiSrvc)
				.leftJoin(tcCdInfo).on(tmApiSrvc.srvcClsf.eq(tcCdInfo.cd)) 
				.where(searchOption(openApiSearchDTO))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tmApiSrvc.registDt.desc()).fetch();
		return result;
	}
	
	/**
	 * @Method Name : searchOption
	 * @작성일 : 2024. 6. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : 검색 조건
	 * @param OpenApiSearchDTO
	 * @return BooleanBuilder
	 */
    private BooleanBuilder searchOption(OpenApiSearchDTO openApiSearchDTO) {
        BooleanBuilder builder = new BooleanBuilder();
        if (openApiSearchDTO != null) {
            if (openApiSearchDTO.getSearchContent() != null && !"".equals(openApiSearchDTO.getSearchContent())) {
            	builder.and(tmApiSrvc.srvcNm.like("%" + openApiSearchDTO.getSearchContent() + "%"));
            }
        }
        return builder;
    }

	/**
	 * @Method getTotalCount
	 * @작성일 : 2024. 06. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : OpenApi목록 카운트 조회
	 * @return
	 */
	public Long getTotalCount(OpenApiSearchDTO openApiSearchDTO) {
		Long count = queryFactory.select(tmApiSrvc.count()).from(tmApiSrvc)
				.where(searchOption(openApiSearchDTO)).fetchOne();
		return count;
	}

	/**
	 * @Method getTotalCount
	 * @작성일 : 2024. 06. 14.
	 * @작성자 : KY.LEE
	 * @Method 설명 : OpenApi 상세 조회
	 * @return
	 */
	public TmApiSrvc getOpenApiDetail(String srvcId) {
		TmApiSrvc result = queryFactory
				.select(Projections.bean(TmApiSrvc.class, 
						tmApiSrvc.srvcId,
						tmApiSrvc.srvcNm,
						tmApiSrvc.srvcDescr,
						tmApiSrvc.srvcUrl,
						tmApiSrvc.pvsnInst,
						tmApiSrvc.registDt,
						new Coalesce<>(String.class).add(QRepositorySupport.getCodeInfoNamePath(tcCdInfo)).add(tmApiSrvc.srvcClsf).as("srvcClsf")
						))
				.from(tmApiSrvc)
				.leftJoin(tcCdInfo).on(tmApiSrvc.srvcClsf.eq(tcCdInfo.cd)) 
				.where(tmApiSrvc.srvcId.eq(srvcId))
				.fetchOne();
		return result;
	}

	public OpenApiDetailDTO findOpenApiDetailBySrvcId(String srvcId) {
        TmApiSrvc tmApiSrvcResult = queryFactory
				.select(Projections.bean(TmApiSrvc.class, 
					tmApiSrvc.srvcId,
					tmApiSrvc.srvcNm,
					tmApiSrvc.srvcClsf,
					tmApiSrvc.srvcDescr,
					tmApiSrvc.srvcUrl,
					tmApiSrvc.pvsnInst,
					tmApiSrvc.registDt,
					tmApiSrvc.mngrId,
					new Coalesce<>(String.class).add(tcUserMng.userNm).add(tmApiSrvc.mngrId).as("mngrNm"),
					new Coalesce<>(String.class).add(QRepositorySupport.getCodeInfoNamePath(tcCdInfo)).add(tmApiSrvc.srvcClsf).as("srvcClsf")
				))
				.from(tmApiSrvc)
				.leftJoin(tcCdInfo).on(tmApiSrvc.srvcClsf.eq(tcCdInfo.cd)) 
				.leftJoin(tcUserMng).on(tmApiSrvc.mngrId.eq(tcUserMng.usermngId)) 
                .where(tmApiSrvc.srvcId.eq(srvcId))
                .fetchOne();

        List<TmApiPvsnitem> tmApiPvsnitemList = queryFactory
                .select(Projections.bean(TmApiPvsnitem.class,
                		tmApiPvsnitem.itemId,
                		tmApiPvsnitem.itemType,
                		tmApiPvsnitem.itemLen,
                		tmApiPvsnitem.itemNm,
                		tmApiPvsnitem.itemDescr,
                		tmApiPvsnitem.itemSqno,
                		tmApiPvsnitem.smplData,
                		tmApiPvsnitem.esntlYn,
                		new Coalesce<>(String.class).add(QRepositorySupport.getCodeInfoNamePath(tcCdInfo)).add(tmApiPvsnitem.itemType).as("itemType")
                ))
                .from(tmApiPvsnitem)
                .leftJoin(tcCdInfo).on(tmApiPvsnitem.itemType.eq(tcCdInfo.cd))
                .where(tmApiPvsnitem.srvcId.eq(srvcId))
                .orderBy(tmApiPvsnitem.itemSqno.asc())
                .fetch();

        List<TmApiRqstitem> tmApiRqstitemList = queryFactory
                .select(Projections.bean(TmApiRqstitem.class,
                		tmApiRqstitem.itemId,
                		tmApiRqstitem.itemType,
                		tmApiRqstitem.itemLen,
                		tmApiRqstitem.itemNm,
                		tmApiRqstitem.itemDescr,
                		tmApiRqstitem.itemSqno,
                		tmApiRqstitem.smplData,
                		tmApiRqstitem.esntlYn,
                		new Coalesce<>(String.class).add(QRepositorySupport.getCodeInfoNamePath(tcCdInfo)).add(tmApiRqstitem.itemType).as("itemType")
                ))
                .from(tmApiRqstitem)
                .leftJoin(tcCdInfo).on(tmApiRqstitem.itemType.eq(tcCdInfo.cd)) 
                .where(tmApiRqstitem.srvcId.eq(srvcId))
                .orderBy(tmApiRqstitem.itemSqno.asc())
                .fetch();

        OpenApiDetailDTO openApiDetailDTO = new OpenApiDetailDTO();
        openApiDetailDTO.setTmApiSrvc(tmApiSrvcResult);
        openApiDetailDTO.setTmApiPvsnitemList(tmApiPvsnitemList);
        openApiDetailDTO.setTmApiRqstitemList(tmApiRqstitemList);

        return openApiDetailDTO;
	}
}
