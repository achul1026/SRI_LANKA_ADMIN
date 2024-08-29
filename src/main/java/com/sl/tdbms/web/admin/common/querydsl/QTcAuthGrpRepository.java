package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.auth.TcAuthGrpDTO;
import com.sl.tdbms.web.admin.common.dto.bffltd.BffltdAuthGrpDTO;
import com.sl.tdbms.web.admin.common.dto.bffltd.BffltdAuthGrpDTO.AuthGrpInfo;
import com.sl.tdbms.web.admin.common.dto.bffltd.BffltdAuthInfoDTO;
import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.menu.TcAuthGrpMenuDTO;
import com.sl.tdbms.web.admin.common.entity.QTcAuthGrp;
import com.sl.tdbms.web.admin.common.entity.QTcCdGrp;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcAuthGrpRepository {

	private final JPAQueryFactory queryFactory;

	private QTcAuthGrp tcAuthGrp = QTcAuthGrp.tcAuthGrp;
	
	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;
	
	private QTcCdGrp tcCdGrp = QTcCdGrp.tcCdGrp;
	
//	private QTcMenuAuth tcMenuAuth = QTcMenuAuth.tcMenuAuth;
//	
//	private QTcMenuMng tcMenuMng = QTcMenuMng.tcMenuMng;
//
//	private QTcMenuMng parentTcMenuMng = new QTcMenuMng("parentTcMenuMng");

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
		LoginMngrDTO loginUser = LoginMngrUtils.getTcUserMngInfo();
		if (!(loginUser.getUserType() == UserTypeCd.SUPER)
//				&& !(loginUser.getUserBffltd().equals("BFC001"))
				) {
			searchCommonDTO.setSearchTypeCd(loginUser.getUserBffltd());
		}
		
		List<TcAuthGrpDTO> result = queryFactory.select(Projections.bean(TcAuthGrpDTO.class
																		,tcAuthGrp.authgrpId
																		,tcAuthGrp.authgrpNm
																		,tcAuthGrp.authgrpDescr
																		,QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("bffltdNm")))
												.from(tcAuthGrp)
												.where(searchOption(searchCommonDTO.getSearchContent()),
														QRepositorySupport.containsKeyword(tcAuthGrp.bffltdCd,searchCommonDTO.getSearchTypeCd()),
														tcAuthGrp.bscauthYn.eq("N"))
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
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {
		LoginMngrDTO loginUser = LoginMngrUtils.getTcUserMngInfo();
		if (!(loginUser.getUserType() == UserTypeCd.SUPER)
//				&& !(loginUser.getUserBffltd().equals("BFC001"))
				) {
			searchCommonDTO.setSearchTypeCd(loginUser.getUserBffltd());
		}
		
		Long count = queryFactory.select(tcAuthGrp.count())
									.from(tcAuthGrp)
									.where(searchOption(searchCommonDTO.getSearchContent()),
											QRepositorySupport.containsKeyword(tcAuthGrp.bffltdCd,searchCommonDTO.getSearchTypeCd()),
											tcAuthGrp.bscauthYn.eq("N"))
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
						, QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("bffltdNm")))
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
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		// 해당 소속만 출력
		BooleanBuilder whereClause = new BooleanBuilder();
	    if (userInfo.getUserType() != UserTypeCd.SUPER) whereClause.and(tcCdInfo.cd.eq(userInfo.getUserBffltd()));
		
		List<BffltdAuthGrpDTO> result = queryFactory.select(Projections.bean(BffltdAuthGrpDTO.class
																		   , tcCdInfo.cd.as("bffltdCd")
																		   , QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("bffltdNm")))
												.from(tcCdInfo)
												.join(tcCdGrp).on(tcCdGrp.grpCd.eq(grpCd)
														.and(tcCdGrp.grpcdId.eq(tcCdInfo.grpcdId))
														.and(tcCdInfo.useYn.eq("Y")))
												.where(whereClause)
												.orderBy(tcCdInfo.cd.asc())
												.fetch();
		if (!CommonUtils.isNull(result)) {
			result.stream().filter(x -> !CommonUtils.isNull(x))
										.forEach(bffltdList -> bffltdList.setSubAuthGrpList(
												queryFactory.select(Projections.bean(AuthGrpInfo.class
																					, tcAuthGrp.authgrpId
																					, tcAuthGrp.authgrpNm))
												.from(tcAuthGrp)
												.where(tcAuthGrp.bffltdCd.eq(bffltdList.getBffltdCd()).and(tcAuthGrp.bscauthYn.eq("N")))
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
	public List<TcAuthGrpMenuDTO> getAuthGrpByUserBffltd(String userBffltd) {
		
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		// 해당 소속만 출력
		BooleanBuilder whereClause = new BooleanBuilder();
	    if (userInfo.getUserType() != UserTypeCd.SUPER) whereClause.and(tcAuthGrp.bffltdCd.eq(userBffltd));
		
		List<TcAuthGrpMenuDTO> result = queryFactory.select(Projections.bean(TcAuthGrpMenuDTO.class
																			, tcAuthGrp.authgrpId
																			, tcAuthGrp.authgrpNm
																			, tcAuthGrp.authgrpDescr
																			))
												    .from(tcAuthGrp)
												    .where(whereClause.and(tcAuthGrp.bscauthYn.eq("N")))
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
	 * @Method Name : searchOption
	 * @작성일 : 2024. 4. 22.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 검색 조건
	 * @param searchType
	 * @param searchContent
	 * @return
	 */
	private BooleanExpression searchOption(String searchContent) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tcAuthGrp.authgrpNm, searchContent)
					.or(QRepositorySupport.containsKeyword(tcAuthGrp.authgrpDescr, searchContent));
		}
		return result;
	}
	
	/**
	  * @Method Name : getAuthGrpNameByBfflt
	  * @작성일 : 2024. 6. 21.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 유저 소속에 따른 권한 이름 조회
	  * @return
	  */
	public List<BffltdAuthInfoDTO> getAuthGrpListByBfflt(String userBffltd){
		
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		// 일반 관리자일 경우 해당 소속 권한만 조회
		BooleanBuilder whereClause = new BooleanBuilder();
		if (userInfo.getUserType() != UserTypeCd.SUPER) whereClause = whereClause.and(tcAuthGrp.bffltdCd.eq(userBffltd));
		
	    List<BffltdAuthInfoDTO> result = queryFactory.select(
	    								Projections.bean(
	    												BffltdAuthInfoDTO.class, 
	    												tcAuthGrp.authgrpId, 
	    												Expressions.stringTemplate("CONCAT({0}, ' - ', {1})", 
	    									                    QRepositorySupport.getCodeInfoNamePath(tcCdInfo), 
	    									                    tcAuthGrp.authgrpNm).as("authgrpNm")
														))
	    								.from(tcAuthGrp)
    									.leftJoin(tcCdInfo).on(tcAuthGrp.bffltdCd.eq(tcCdInfo.cd))
	    								.where(whereClause.and(tcAuthGrp.bscauthYn.eq("N")))
	    								.fetch();
		return result;
	}

}
