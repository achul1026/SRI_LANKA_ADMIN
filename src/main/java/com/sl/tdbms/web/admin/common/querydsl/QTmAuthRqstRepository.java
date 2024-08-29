package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserRqstAuthDTO;
import com.sl.tdbms.web.admin.common.entity.QTcAuthGrp;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.entity.QTcUserMng;
import com.sl.tdbms.web.admin.common.entity.QTmAuthRqst;
import com.sl.tdbms.web.admin.common.enums.code.RqstSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTmAuthRqstRepository {

	private final JPAQueryFactory queryFactory;

	private QTmAuthRqst tmAuthRqst = QTmAuthRqst.tmAuthRqst;
	
	private QTcAuthGrp tcAuthGrp = QTcAuthGrp.tcAuthGrp;
	
	private QTcUserMng tcUserMng = QTcUserMng.tcUserMng;

	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;
	
	/**
	  * @Method Name : getRqstAuthList
	  * @작성일 : 2024. 4. 17.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 요청 권한 관리 목록 조회
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	public List<TcUserRqstAuthDTO> getRqstAuthList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		BooleanBuilder whereClause = new BooleanBuilder(searchOption(searchCommonDTO.getSearchTypeCd(), searchCommonDTO.getSearchContent()));
		// 해당 소속만 출력
	    if (userInfo.getUserType() != UserTypeCd.SUPER) whereClause.and(tcCdInfo.cd.eq(userInfo.getUserBffltd()));
	    if (!CommonUtils.isNull(searchCommonDTO.getSearchSttsCd())) whereClause.and(tmAuthRqst.rqstStts.eq(RqstSttsCd.getEnums(searchCommonDTO.getSearchSttsCd())));
		
		List<TcUserRqstAuthDTO> result = queryFactory
				.select(Projections.bean(TcUserRqstAuthDTO.class, tcUserMng.usermngId
						, QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("bffltdNm"), tcUserMng.userId, tcUserMng.userNm
						, tcUserMng.userTel, tcUserMng.userEmail, tmAuthRqst.authrqstId, tmAuthRqst.authgrpNm, tmAuthRqst.rqstStts, tmAuthRqst.rqstDt
						))
				.from(tmAuthRqst)
	            .leftJoin(tcUserMng).on(tcUserMng.usermngId.eq(tmAuthRqst.usermngId))
	            .leftJoin(tcCdInfo).on(tcUserMng.userBffltd.eq(tcCdInfo.cd))
	            .where(whereClause)
				.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tmAuthRqst.rqstDt.desc()).fetch();
		return result;
	}
	
	/**
	  * @Method Name : getRqstAuthInfo
	  * @작성일 : 2024. 4. 17.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 요청 권한 관리 정보 조회
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	public TcUserRqstAuthDTO getRqstAuthInfo(String authrqstId) {
		TcUserRqstAuthDTO result = queryFactory
				.select(Projections.bean(TcUserRqstAuthDTO.class, tcUserMng.usermngId
						, QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("bffltdNm"), tcAuthGrp.authgrpNm, tcUserMng.userId, tcUserMng.userNm
						, tcUserMng.userTel, tcUserMng.userEmail, tmAuthRqst.rqstStts, tmAuthRqst.rqstRsn, tcAuthGrp.authgrpId
						))
				.from(tmAuthRqst)
	            .leftJoin(tcUserMng).on(tcUserMng.usermngId.eq(tmAuthRqst.usermngId))
	            .leftJoin(tcCdInfo).on(tcUserMng.userBffltd.eq(tcCdInfo.cd))
	            .leftJoin(tcAuthGrp).on(tcAuthGrp.authgrpId.eq(tmAuthRqst.authgrpId))
	            .where(tmAuthRqst.authrqstId.eq(authrqstId))
				.fetchOne();
		return result;
	}
	
	/**
	  * @Method Name : getTotalCount
	  * @작성일 : 2024. 4. 17.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 요청 권한 관리 개수 조회
	  * @param searchCommonDTO
	  * @return
	  */
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {
		
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		// 기본 조건
	    BooleanExpression baseCondition = searchOption(searchCommonDTO.getSearchTypeCd(), searchCommonDTO.getSearchContent());
		
		// 해당 소속만 출력
		BooleanBuilder whereClause = new BooleanBuilder(baseCondition);
	    if (userInfo.getUserType() != UserTypeCd.SUPER) whereClause.and(tcCdInfo.cd.eq(userInfo.getUserBffltd()));
	    if (!CommonUtils.isNull(searchCommonDTO.getSearchSttsCd())) whereClause.and(tmAuthRqst.rqstStts.eq(RqstSttsCd.getEnums(searchCommonDTO.getSearchSttsCd())));
		
		Long count = queryFactory.select(tmAuthRqst.count()).from(tmAuthRqst)
	            .leftJoin(tcUserMng).on(tcUserMng.usermngId.eq(tmAuthRqst.usermngId))
	            .leftJoin(tcCdInfo).on(tcUserMng.userBffltd.eq(tcCdInfo.cd))
	            .leftJoin(tcAuthGrp).on(tcAuthGrp.authgrpId.eq(tmAuthRqst.authgrpId))
				.where(whereClause)
				.fetchOne();
		return count;
	}
	
	/**
	  * @Method Name : searchOption
	  * @작성일 : 2024. 4. 17.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 요청 권한 관리 검색 조건
	  * @param searchCommonDTO
	  * @return
	  */
	private BooleanExpression searchOption(String searchTypeCd, String searchContent) {
	    BooleanExpression result = null;
	    if (!CommonUtils.isNull(searchTypeCd) && !CommonUtils.isNull(searchContent)) {
	        result = QRepositorySupport.containsKeyword(tmAuthRqst.authgrpId, searchTypeCd)
	                    .and(QRepositorySupport.containsKeyword(tcUserMng.userId, searchContent)
	                    .or(QRepositorySupport.containsKeyword(tcUserMng.userNm, searchContent)));
	    } else if (!CommonUtils.isNull(searchTypeCd)) {
	        result = QRepositorySupport.containsKeyword(tmAuthRqst.authgrpId, searchTypeCd);
	    } else if (!CommonUtils.isNull(searchContent)) {
	        result = QRepositorySupport.containsKeyword(tcUserMng.userId, searchContent)
	                    .or(QRepositorySupport.containsKeyword(tcUserMng.userNm, searchContent));
	    }
	    return result;
	}

	/**
	  * @Method Name : waitingApprovalRequest
	  * @작성일 : 2024. 4. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 권한 요청 유무 확인
	  * @param userId
	  * @return
	  */
	public RqstSttsCd waitingApprovalRequest(String usermngId) {
		RqstSttsCd exists = queryFactory
		        .select(tmAuthRqst.rqstStts)
		        .from(tmAuthRqst)
		        .where(tmAuthRqst.usermngId.eq(usermngId), tmAuthRqst.rqstStts.eq(RqstSttsCd.WAITING))
		        .fetchOne();
	    return exists;
	}

}
