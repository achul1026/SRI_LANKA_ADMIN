package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserAuthMngDTO;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserMngDTO;
import com.sl.tdbms.web.admin.common.entity.QTcAuthGrp;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.entity.QTcUserMng;
import com.sl.tdbms.web.admin.common.enums.code.AthrztSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;
//import com.sl.tdbms.web.admin.common.enums.code.MngrSttsCd;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcUserMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTcUserMng tcUserMng = QTcUserMng.tcUserMng;

	private QTcCdInfo bffltdCd = QTcCdInfo.tcCdInfo;
	
	private QTcCdInfo deptCd = new QTcCdInfo("deptCd");

	private QTcAuthGrp tcAuthGrp = QTcAuthGrp.tcAuthGrp;
	
	/**
	 * @Method Name : getMngrList
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 관리자 목록 조회
	 * @param commonDTO
	 * @param paging
	 * @return
	 */
	public List<TcUserMngDTO> getTcUserList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		LoginMngrDTO loginUser = LoginMngrUtils.getTcUserMngInfo();
		if (!(loginUser.getUserType() == UserTypeCd.SUPER)
//				&& !(loginUser.getUserBffltd().equals("BFC001"))
				) {
			searchCommonDTO.setSearchTypeCd(loginUser.getUserBffltd());
		}

		List<TcUserMngDTO> result = queryFactory
				.select(Projections.bean(TcUserMngDTO.class, tcUserMng.usermngId, tcUserMng.userId,
						tcUserMng.userPswd, tcUserMng.userBffltd, tcUserMng.userEmail, tcUserMng.userType,
						tcUserMng.athrztStts, tcUserMng.userNm, tcUserMng.userAuth, tcUserMng.userTel, tcUserMng.registDt,
						tcAuthGrp.authgrpNm,
						QRepositorySupport.getCodeInfoNamePath(bffltdCd).as("bffltdNm"), 
						QRepositorySupport.getCodeInfoNamePath(deptCd).as("deptNm")))
				.from(tcUserMng)
				.leftJoin(tcAuthGrp).on(tcUserMng.authgrpId.eq(tcAuthGrp.authgrpId))
				.leftJoin(bffltdCd).on(tcUserMng.userBffltd.eq(bffltdCd.cd))
				.leftJoin(deptCd).on(tcUserMng.userDept.eq(deptCd.cd))
				.where(
						searchOption(searchCommonDTO.getSearchTypeCd(), searchCommonDTO.getSearchContent()),
						QRepositorySupport.toEqExpression(tcUserMng.athrztStts,AthrztSttsCd.getEnums(searchCommonDTO.getSearchSttsCd())))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tcUserMng.registDt.desc()).fetch();

		return result;
	}

	/**
	 * @Method Name : getTotalCount
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 관리자 목록 개수 조회
	 * @param commonDTO
	 * @return
	 */
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {

		Long count = queryFactory.select(tcUserMng.countDistinct()).from(tcUserMng)
				.leftJoin(tcAuthGrp).on(tcUserMng.authgrpId.eq(tcAuthGrp.authgrpId))
				.leftJoin(bffltdCd).on(tcUserMng.userBffltd.eq(bffltdCd.cd))
				.where(searchOption(searchCommonDTO.getSearchTypeCd(), searchCommonDTO.getSearchContent()),
						QRepositorySupport.containsKeyword(tcUserMng.userBffltd,
								searchCommonDTO.getSearchTypeCd()),
						QRepositorySupport.toEqExpression(tcUserMng.athrztStts,
								AthrztSttsCd.getEnums(searchCommonDTO.getSearchSttsCd())))
				.fetchOne();

		return count;
	}

	/**
	 * @Method Name : searchOption
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 검색 조건
	 * @param searchContent
	 * @return
	 */
	private BooleanExpression searchOption(String searchTypeCd, String searchContent) {
		BooleanExpression result = null;
		if (CommonUtils.isNull(searchTypeCd) && !CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tcUserMng.userId, searchContent)
					.or(QRepositorySupport.containsKeyword(tcUserMng.userNm, searchContent));
		} else if(!CommonUtils.isNull(searchTypeCd) && CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tcAuthGrp.bffltdCd, searchTypeCd);
		} else if (!CommonUtils.isNull(searchTypeCd) && !CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tcUserMng.userId, searchContent)
					.and((QRepositorySupport.containsKeyword(tcUserMng.userNm, searchContent))
					.or(QRepositorySupport.containsKeyword(tcAuthGrp.bffltdCd, searchTypeCd)));
		}
		return result;
	}

	/**
	 * @Method Name : getMngrInfo
	 * @작성일 : 2024. 2. 8.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 관리자 상세정보
	 * @param mngrId
	 * @return
	 */
	public TcUserMngDTO getTcUserInfo(String usermngId) {
		TcUserMngDTO result = queryFactory
				.select(Projections.bean(TcUserMngDTO.class, tcUserMng.usermngId, tcUserMng.authgrpId, tcUserMng.userId,
						tcUserMng.userPswd, tcUserMng.userBffltd, tcUserMng.userEmail, tcUserMng.userType,
						tcUserMng.athrztStts, tcUserMng.userNm, tcUserMng.userAuth, tcUserMng.userTel, tcUserMng.registDt,
						tcAuthGrp.authgrpNm,
						QRepositorySupport.getCodeInfoNamePath(bffltdCd).as("bffltdNm"), 
						QRepositorySupport.getCodeInfoNamePath(deptCd).as("deptNm")))
				.from(tcUserMng).leftJoin(tcAuthGrp).on(tcUserMng.authgrpId.eq(tcAuthGrp.authgrpId))
				.leftJoin(bffltdCd).on(tcUserMng.userBffltd.eq(bffltdCd.cd))
				.leftJoin(deptCd).on(tcUserMng.userDept.eq(deptCd.cd))
				.where(tcUserMng.usermngId.eq(usermngId))
				.fetchOne(); 
		return result;
	}

	/**
	 * @Method Name : getTcUserInfoByUserId
	 * @작성일 : 2024. 2. 8.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 관리자 정보 조회 by userId
	 * @param userId
	 * @return
	 */
	public LoginMngrDTO getTcUserInfoByUserId(String userId) {
		LoginMngrDTO result = queryFactory.select(Projections.bean(LoginMngrDTO.class, tcUserMng.usermngId, tcUserMng.authgrpId,
				tcUserMng.userId, tcUserMng.userPswd, tcUserMng.userAuth, tcUserMng.userType, tcUserMng.userBffltd,
				tcUserMng.athrztStts, tcUserMng.userNm, tcUserMng.resetpswdYn ,
				QRepositorySupport.getCodeInfoNamePath(bffltdCd).as("bffltdNm"))).from(tcUserMng)
				.leftJoin(bffltdCd).on(tcUserMng.userBffltd.eq(bffltdCd.cd))
				.where(tcUserMng.userId.eq(userId))
				.fetchOne();
		return result;
	}

	/**
	  * @Method Name : getUserInfoByAuthgrpId
	  * @작성일 : 2024. 4. 12.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 관리자 정보 조회 by authgrpId
	  * @param authGrpId
	  * @return
	  */
	public List<TcUserAuthMngDTO> getUserInfoByAuthgrpId(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TcUserAuthMngDTO> result = queryFactory
				.select(Projections.bean(
										TcUserAuthMngDTO.class, tcAuthGrp.authgrpId, tcAuthGrp.authgrpNm
										, QRepositorySupport.getCodeInfoNamePath(bffltdCd).as("bffltdNm"), tcUserMng.userId
										, tcUserMng.userNm, tcUserMng.userTel, tcUserMng.userEmail
						))
				.from(tcAuthGrp)
				.innerJoin(tcUserMng).on(tcAuthGrp.authgrpId.eq(tcUserMng.authgrpId))
				.leftJoin(bffltdCd).on(tcUserMng.userBffltd.eq(bffltdCd.cd))
				.where(searchOption(searchCommonDTO.getSearchTypeCd(), searchCommonDTO.getSearchContent()),
						QRepositorySupport.containsKeyword(tcUserMng.authgrpId,searchCommonDTO.getSearchType()))
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tcUserMng.registDt.desc())
				.fetch();
		return result;
	}
	
	/**
	  * @Method Name : getTotalCountByAuthgrpId
	  * @작성일 : 2024. 4. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 관리자 목록 개수 조회 by authgrpId
	  * @param authgrpId
	  * @param searchCommonDTO
	  * @return
	  */
	public Long getTotalCountByAuthgrpId(SearchCommonDTO searchCommonDTO) {
//		String searchContent = "";
//		if (!CommonUtils.isNull(searchCommonDTO)) {
//			searchContent = searchCommonDTO.getSearchContent();
//		}
		Long count = queryFactory.select(tcUserMng.countDistinct()).from(tcAuthGrp)
//				.innerJoin(tcAuthMng).on(tcAuthGrp.authgrpId.eq(tcAuthMng.authgrpId))
				.innerJoin(tcUserMng).on(tcAuthGrp.authgrpId.eq(tcUserMng.authgrpId))
 				.leftJoin(bffltdCd).on(tcUserMng.userBffltd.eq(bffltdCd.cd))
				.where(searchOption(searchCommonDTO.getSearchTypeCd(), searchCommonDTO.getSearchContent()),
						QRepositorySupport.containsKeyword(tcUserMng.authgrpId,searchCommonDTO.getSearchType()))
				.fetchOne();

		return count;
	}

}