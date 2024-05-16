package com.sri.lanka.traffic.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.dto.auth.TcAuthGrpDTO;
import com.sri.lanka.traffic.admin.common.dto.bffltd.BffltdAuthGrpDTO;
import com.sri.lanka.traffic.admin.common.dto.bffltd.BffltdAuthGrpDTO.AuthGrpInfo;
import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.dto.menu.TcAuthGrpMenuDTO;
import com.sri.lanka.traffic.admin.common.entity.QTcAuthGrp;
import com.sri.lanka.traffic.admin.common.entity.QTcCdGrp;
import com.sri.lanka.traffic.admin.common.entity.QTcCdInfo;
import com.sri.lanka.traffic.admin.common.entity.QTcMenuAuth;
import com.sri.lanka.traffic.admin.common.entity.QTcMenuMng;
import com.sri.lanka.traffic.admin.common.entity.TcUserMng;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcAuthGrpRepository {

	private final JPAQueryFactory queryFactory;

	private QTcAuthGrp tcAuthGrp = QTcAuthGrp.tcAuthGrp;
	
	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;
	
	private QTcCdGrp tcCdGrp = QTcCdGrp.tcCdGrp;
	
	private QTcMenuAuth tcMenuAuth = QTcMenuAuth.tcMenuAuth;
	
	private QTcMenuMng tcMenuMng = QTcMenuMng.tcMenuMng;

	private QTcMenuMng parentTcMenuMng = new QTcMenuMng("parentTcMenuMng");

	/**
	  * @Method Name : getAuthList
	  * @작성일 : 2024. 4. 11.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 그룹 권한 목록 조회
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	public List<TcAuthGrpDTO> getAuthList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TcAuthGrpDTO> result = queryFactory.select(Projections.bean(TcAuthGrpDTO.class
																		,tcAuthGrp.authgrpId
																		,tcAuthGrp.authgrpNm
																		,tcAuthGrp.authgrpDescr
																		,tcCdInfo.cdNm.as("bffltdNm")))
												.from(tcAuthGrp)
												.leftJoin(tcCdInfo).on(tcAuthGrp.bffltdCd.eq(tcCdInfo.cd))
												.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tcAuthGrp.registDt.desc())
												.fetch();
		return result;
	}

	/**
	  * @Method Name : getTotalCount
	  * @작성일 : 2024. 4. 11.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 그룹 목록 개수 조회
	  * @param searchCommonDTO
	  * @return
	  */
	public long getTotalCount(SearchCommonDTO searchCommonDTO) {
		Long count = queryFactory.select(tcAuthGrp.count())
									.from(tcAuthGrp)
									.fetchOne();
		return count;
	}

	/**
	  * @Method Name : getAuthInfo
	  * @작성일 : 2024. 4. 11.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 권한 그룹 정보 조회
	  * @param authgrpId
	  * @return
	  */
	public TcAuthGrpDTO getAuthInfo(String authgrpId) {
		TcAuthGrpDTO result = queryFactory
				.select(Projections.bean(TcAuthGrpDTO.class, tcAuthGrp.authgrpId, tcAuthGrp.authgrpNm, tcAuthGrp.authgrpDescr
						, tcCdInfo.cdNm.as("bffltdNm")))
				.from(tcAuthGrp).leftJoin(tcCdInfo).on(tcAuthGrp.bffltdCd.eq(tcCdInfo.cd))
				.where(tcAuthGrp.authgrpId.eq(authgrpId))
				.fetchOne();
		return result;
	}
	
	/**
	  * @Method Name : getBffltdAuthGrpList
	  * @작성일 : 2024. 4. 12.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 소속에 따른 소속 목록과 그룹 유형 목록 조회
	  * @return
	  */
	public List<BffltdAuthGrpDTO> getBffltdAuthGrpList(String grpCd){
		List<BffltdAuthGrpDTO> result = queryFactory.select(Projections.bean(BffltdAuthGrpDTO.class
																		   , tcCdInfo.cd.as("bffltdCd")
																		   , tcCdInfo.cdNm.as("bffltdNm")))
												.from(tcCdInfo)
												.join(tcCdGrp).on(tcCdGrp.grpCd.eq(grpCd)
														.and(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId))
														.and(tcCdInfo.useYn.eq("Y")))
												.orderBy(tcCdInfo.cdNm.asc())
												.fetch();
		if (!CommonUtils.isNull(result)) {
			result.stream().filter(x -> !CommonUtils.isNull(x))
										.forEach(bffltdList -> bffltdList.setSubAuthGrpList(
												queryFactory.select(Projections.bean(AuthGrpInfo.class
																					, tcAuthGrp.authgrpId
																					, tcAuthGrp.authgrpNm))
												.from(tcAuthGrp)
												.where(tcAuthGrp.bffltdCd.eq(bffltdList.getBffltdCd()).and(tcAuthGrp.bscauth_yn.eq("N")))
												.fetch()
												));
		}
				
		return result;
	}

	/**
	  * @Method Name : getAuthGrpMenuList
	  * @작성일 : 2024. 4. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 그룹 유형에 따른 메뉴 권한 목록 조회
	  * @param userId
	  * @return
	  */
	public List<TcAuthGrpMenuDTO> getAuthGrpMenuList(TcUserMng tcUserMng) {
		List<TcAuthGrpMenuDTO> result = queryFactory.select(Projections.bean(TcAuthGrpMenuDTO.class
																			, tcAuthGrp.authgrpId
																			, tcAuthGrp.authgrpNm
																			, tcAuthGrp.authgrpDescr
																			))
												    .from(tcAuthGrp)
												    .where(tcAuthGrp.bffltdCd.eq(tcUserMng.getUserBffltd()).and(tcAuthGrp.bscauth_yn.eq("N")))
												    .fetch();

//	    if (result != null) {
//	    	for(TcAuthGrpMenuDTO authGrp : result) {
//	            List<TcMenuAuthDTO> subMenus = queryFactory.select(Projections.bean(TcMenuAuthDTO.class 
//	            													, parentTcMenuMng.menuCd
//	            													, parentTcMenuMng.menuNm
//											                       ))
//										                    .from(parentTcMenuMng)
//										                    .leftJoin(tcMenuAuth)
//										                    .on(parentTcMenuMng.menuId.eq(tcMenuAuth.menuId).and(tcMenuAuth.authId.eq(authGrp.getAuthId())))
//										                    .where(parentTcMenuMng.uppermenuCd.isNull()
//										                    		.and(parentTcMenuMng.useYn.eq("Y")).and(tcMenuAuth.srchYn.eq("Y")))
//										                    .orderBy(parentTcMenuMng.menuSqno.asc())
//										                    .fetch();
//	
//	            authGrp.setSubMenuAuthList(subMenus);
//	            for (TcMenuAuthDTO menu : authGrp.getSubMenuAuthList()) {
//	                List<SubAuthMenuInfo> subMenuInfos = queryFactory.select(Projections.bean(SubAuthMenuInfo.class
//	                																		, tcMenuMng.menuNm
//	                																		))
//											                        .from(tcMenuMng)
//											                        .join(tcMenuAuth)
//											                        .on(tcMenuMng.menuId.eq(tcMenuAuth.menuId)
//											                            .and(tcMenuAuth.authId.eq(authGrp.getAuthId())))
//											                        .where(tcMenuMng.uppermenuCd.eq(menu.getMenuCd())
//											                            .and(tcMenuMng.useYn.eq("Y"))
//											                            .and(tcMenuAuth.srchYn.eq("Y")))
//											                        .orderBy(tcMenuMng.menuSqno.asc())
//											                        .fetch();
//	
//	                menu.setSubMenuList(subMenuInfos);
//            }}
//	    }
		return result;
	}
	
	/**
	 * @Method Name : getAuthRqstList
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 요청 권한 목록 조회
	 * @param commonDTO
	 * @param paging
	 * @return
	 */
	public List<TcAuthGrpDTO> getAuthRqstList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {

		List<TcAuthGrpDTO> result = queryFactory
				.select(Projections.bean(TcAuthGrpDTO.class, tcAuthGrp.authgrpId, tcAuthGrp.authgrpNm
										, tcAuthGrp.authgrpDescr, tcAuthGrp.registDt, tcAuthGrp.updtDt
										, tcCdInfo.cdNm.as("bffltdNm")))
				.from(tcAuthGrp).where(searchOption(searchCommonDTO.getSearchType(), searchCommonDTO.getSearchContent()))
				.leftJoin(tcCdInfo).on(tcAuthGrp.bffltdCd.eq(tcCdInfo.cd))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tcAuthGrp.registDt.desc()).fetch();

		return result;
	}
	
	/**
	 * @Method Name : getTotalCountByAuthRqst
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 요청 권한 목록 개수 조회
	 * @param commonDTO
	 * @return
	 */
	public Long getTotalCountByAuthRqst(SearchCommonDTO searchCommonDTO) {

		Long count = queryFactory.select(tcAuthGrp.count()).from(tcAuthGrp)
				.where(searchOption(searchCommonDTO.getSearchType(), searchCommonDTO.getSearchContent())).fetchOne();

		return count;
	}
	
	/**
	 * @Method Name : searchOption
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 검색 조건
	 * @param searchType
	 * @param searchContent
	 * @return
	 */
	private BooleanExpression searchOption(String searchType, String searchContent) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchType) && !CommonUtils.isNull(searchContent)) {

			if ("authgrpNm".equals(searchType)) {
				result = QRepositorySupport.containsKeyword(tcAuthGrp.authgrpNm, searchContent);
			} else if ("authgrpDescr".equals(searchType)) {
				result = QRepositorySupport.containsKeyword(tcAuthGrp.authgrpDescr, searchContent);
			} else {
				result = QRepositorySupport.containsKeyword(tcAuthGrp.authgrpNm, searchContent)
						.or(QRepositorySupport.containsKeyword(tcAuthGrp.authgrpDescr, searchContent));
			}
		}
		return result;
	}




}
