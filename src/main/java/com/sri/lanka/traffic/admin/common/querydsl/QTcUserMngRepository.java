package com.sri.lanka.traffic.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sri.lanka.traffic.admin.common.dto.common.LoginMngrDTO;
import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.dto.mngr.TcUserAuthMngDTO;
import com.sri.lanka.traffic.admin.common.dto.mngr.TcUserMngDTO;
import com.sri.lanka.traffic.admin.common.entity.QTcAuthGrp;
import com.sri.lanka.traffic.admin.common.entity.QTcAuthMng;
import com.sri.lanka.traffic.admin.common.entity.QTcCdInfo;
import com.sri.lanka.traffic.admin.common.entity.QTcUserMng;
import com.sri.lanka.traffic.admin.common.enums.code.AthrztSttsCd;
//import com.sri.lanka.traffic.admin.common.enums.code.MngrSttsCd;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTcUserMngRepository {

	private final JPAQueryFactory queryFactory;

	private QTcUserMng tcUserMng = QTcUserMng.tcUserMng;

	private QTcAuthMng tcAuthMng = QTcAuthMng.tcAuthMng;

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

		List<TcUserMngDTO> result = queryFactory
				.select(Projections.bean(TcUserMngDTO.class, tcUserMng.usermngId, tcUserMng.userId,
						tcUserMng.userPswd, tcUserMng.userBffltd, tcUserMng.userEmail, tcUserMng.userType,
						tcUserMng.athrztStts, tcUserMng.userNm, tcUserMng.userAuth, tcUserMng.userTel, tcUserMng.registDt,
						tcAuthMng.authNm, bffltdCd.cdNm.as("bffltdNm")))
				.from(tcUserMng).leftJoin(tcAuthMng).on(tcUserMng.authId.eq(tcAuthMng.authId)).leftJoin(bffltdCd)
				.on(tcUserMng.userBffltd.eq(bffltdCd.cd))
				.where(searchOption(searchCommonDTO.getSearchContent()),
						QRepositorySupport.containsKeyword(tcUserMng.userBffltd,
								searchCommonDTO.getSearchTypeCd()),
						QRepositorySupport.toEqExpression(tcUserMng.athrztStts,
								AthrztSttsCd.getEnums(searchCommonDTO.getSearchSttsCd())))
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

		Long count = queryFactory.select(tcUserMng.countDistinct()).from(tcUserMng).leftJoin(tcAuthMng)
				.on(tcUserMng.authId.eq(tcAuthMng.authId)).leftJoin(bffltdCd).on(tcUserMng.userBffltd.eq(bffltdCd.cd))
				.where(searchOption(searchCommonDTO.getSearchContent()),
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
	private BooleanExpression searchOption(String searchContent) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tcUserMng.userId, searchContent)
					.or(QRepositorySupport.containsKeyword(tcUserMng.userNm, searchContent));
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
				.select(Projections.bean(TcUserMngDTO.class, tcUserMng.usermngId, tcUserMng.authId, tcUserMng.userId,
						tcUserMng.userPswd, tcUserMng.userBffltd, tcUserMng.userEmail, tcUserMng.userType,
						tcUserMng.athrztStts, tcUserMng.userNm, tcUserMng.userAuth, tcUserMng.userTel, tcUserMng.registDt,
						tcAuthMng.authNm, bffltdCd.cdNm.as("bffltdNm"), deptCd.cdNm.as("deptNm")))
				.from(tcUserMng).leftJoin(tcAuthMng).on(tcUserMng.authId.eq(tcAuthMng.authId))
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
		LoginMngrDTO result = queryFactory.select(Projections.bean(LoginMngrDTO.class, tcUserMng.usermngId, tcUserMng.authId,
				tcUserMng.userId, tcUserMng.userPswd, tcUserMng.userAuth, tcUserMng.userType,
				tcUserMng.athrztStts, tcUserMng.userNm)).from(tcUserMng).where(tcUserMng.userId.eq(userId))
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
	public List<TcUserAuthMngDTO> getUserInfoByAuthgrpId(String authgrpId, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TcUserAuthMngDTO> result = queryFactory
				.select(Projections.bean(
										TcUserAuthMngDTO.class, tcAuthGrp.authgrpId, tcAuthGrp.authgrpNm
										, bffltdCd.cdNm.as("bffltdNm"), tcUserMng.userId
										, tcUserMng.userNm, tcUserMng.userTel, tcUserMng.userEmail
						))
				.from(tcAuthGrp)
				.innerJoin(tcAuthMng).on(tcAuthGrp.authgrpId.eq(tcAuthMng.authgrpId))
				.innerJoin(tcUserMng).on(tcAuthMng.authId.eq(tcUserMng.authId))
				.leftJoin(bffltdCd).on(tcUserMng.userBffltd.eq(bffltdCd.cd))
				.where(tcAuthGrp.authgrpId.eq(authgrpId),searchOption(searchCommonDTO.getSearchContent()))
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
	public Long getTotalCountByAuthgrpId(String authgrpId, SearchCommonDTO searchCommonDTO) {
		String searchContent = "";
		if (!CommonUtils.isNull(searchCommonDTO)) {
			searchContent = searchCommonDTO.getSearchContent();
		}
		Long count = queryFactory.select(tcUserMng.countDistinct()).from(tcAuthGrp)
				.innerJoin(tcAuthMng).on(tcAuthGrp.authgrpId.eq(tcAuthMng.authgrpId))
				.innerJoin(tcUserMng).on(tcAuthMng.authId.eq(tcUserMng.authId))
 				.leftJoin(bffltdCd).on(tcUserMng.userBffltd.eq(bffltdCd.cd))
				.where(tcAuthGrp.authgrpId.eq(authgrpId),searchOption(searchContent))
				.fetchOne();

		return count;
	}

}
