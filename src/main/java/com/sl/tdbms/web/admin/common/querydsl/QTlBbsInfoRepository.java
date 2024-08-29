package com.sl.tdbms.web.admin.common.querydsl;

import java.util.List;

import com.sl.tdbms.web.admin.common.entity.TlBbsInfo;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sl.tdbms.web.admin.common.dto.bbs.BbsInfoFile;
import com.sl.tdbms.web.admin.common.dto.bbs.BbsInfoFile.Files;
import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.entity.QTcCdInfo;
import com.sl.tdbms.web.admin.common.entity.QTcUserMng;
import com.sl.tdbms.web.admin.common.entity.QTlBbsFile;
import com.sl.tdbms.web.admin.common.entity.QTlBbsFileGrp;
import com.sl.tdbms.web.admin.common.entity.QTlBbsInfo;
import com.sl.tdbms.web.admin.common.enums.code.BbsTypeCd;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class QTlBbsInfoRepository {

	private final JPAQueryFactory queryFactory;

	private QTlBbsInfo tlBbsInfo = QTlBbsInfo.tlBbsInfo;
	
	private QTcCdInfo tcCdInfo = QTcCdInfo.tcCdInfo;
	
	private QTcUserMng bbsUser = QTcUserMng.tcUserMng;
	
	private QTlBbsFile tlBbsFile = QTlBbsFile.tlBbsFile;
	
	private QTlBbsFileGrp tlBbsFileGrp = QTlBbsFileGrp.tlBbsFileGrp;
	
	/**
	 * @Method Name : getBbsList
	 * @작성일 : 2024. 1. 23.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 공지사항 목록 조회
	 * @param tlBbsInfo
	 * @param paging
	 * @return
	 */
	public List<TlBbsInfo> getBbsList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		BooleanBuilder builder = new BooleanBuilder();

	    // 검색 조건 추가
	    builder.and(searchOption(searchCommonDTO.getSearchType(), searchCommonDTO.getSearchContent()));
	    if (userInfo.getUserType() != UserTypeCd.SUPER) builder.and(bbsUser.userBffltd.eq(userInfo.getUserBffltd()));

	    // searchType이 null인 경우에만 bbsType 조건 추가
	    if (CommonUtils.isNull(searchCommonDTO.getSearchType())) {
	        builder.and(tlBbsInfo.bbsType.ne(BbsTypeCd.ALARM.getCode()));
	    }
		
		List<TlBbsInfo> result = queryFactory
											.select(
													Projections.bean(
															TlBbsInfo.class, 
															tlBbsInfo.bbsId, 
															QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("bbsType"),
															tlBbsInfo.bbsTitle,
															tlBbsInfo.dspyYn, 
															tlBbsInfo.registId, 
															tlBbsInfo.registDt)
													)
					.from(tlBbsInfo)
					.leftJoin(tcCdInfo).on(tlBbsInfo.bbsType.eq(tcCdInfo.cd))
					.join(bbsUser).on(bbsUser.userId.eq(tlBbsInfo.registId))
					.where(builder)
					.offset(paging.getOffsetCount()).limit(paging.getLimitCount()).orderBy(tlBbsInfo.registDt.desc()).fetch();

		return result;
	}

	/**
	 * @Method Name : getTotalCount
	 * @작성일 : 2024. 1. 23.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 공지사항 갯수 조회
	 * @return
	 */
	public Long getTotalCount(SearchCommonDTO searchCommonDTO) {
		
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		BooleanBuilder builder = new BooleanBuilder(searchOption(searchCommonDTO.getSearchType(), searchCommonDTO.getSearchContent()));

	    // 해당 소속만 출력
	    if (userInfo.getUserType() != UserTypeCd.SUPER) builder.and(bbsUser.userBffltd.eq(userInfo.getUserBffltd()));

	    // searchType이 null인 경우에만 bbsType 조건 추가
	    if (CommonUtils.isNull(searchCommonDTO.getSearchType())) {
	        builder.and(tlBbsInfo.bbsType.ne(BbsTypeCd.ALARM.getCode()));
	    }

		Long count = queryFactory.select(tlBbsInfo.count()).from(tlBbsInfo)
				.join(bbsUser).on(bbsUser.userId.eq(tlBbsInfo.registId))
				.where(builder)
				.fetchOne();

		return count;
	}

	/**
	 * @Method Name : searchOption
	 * @작성일 : 2024. 1. 23.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 검색 조건
	 * @param searchType
	 * @param searchContent
	 * @return
	 */
	private BooleanExpression searchOption(String searchType, String searchContent) {
		BooleanExpression result = null;
		if (!CommonUtils.isNull(searchType) && CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tlBbsInfo.bbsType, searchType);
		}
		if (CommonUtils.isNull(searchType) && !CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tlBbsInfo.bbsTitle, searchContent)
					.or(QRepositorySupport.containsKeyword(tlBbsInfo.registId, searchContent));
		}
		if (!CommonUtils.isNull(searchType) && !CommonUtils.isNull(searchContent)) {
			result = QRepositorySupport.containsKeyword(tlBbsInfo.bbsType, searchType)
					.and(QRepositorySupport.containsKeyword(tlBbsInfo.bbsTitle, searchContent))
					.or(QRepositorySupport.containsKeyword(tlBbsInfo.registId, searchContent));
		}
		return result;
	}

	/**
	  * @Method Name : getBbsInfo
	  * @작성일 : 2024. 6. 21.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 게시글 조회
	  * @param bbsId
	  * @return
	  */
	public BbsInfoFile getBbsInfo(String bbsId) {
		BbsInfoFile result = queryFactory.select(Projections.bean(BbsInfoFile.class,
																tlBbsInfo.bbsId,
																tlBbsInfo.bbsTitle,
																tcCdInfo.cd.as("bbsType"),
																QRepositorySupport.getCodeInfoNamePath(tcCdInfo).as("bbsTypeNm"),
																tlBbsInfo.bbsCnts,
																tlBbsInfo.dspyYn,
																tlBbsInfo.registId,
																tlBbsInfo.registDt
																))
							.from(tlBbsInfo)
							.where(tlBbsInfo.bbsId.eq(bbsId))
							.leftJoin(tcCdInfo).on(tcCdInfo.cd.eq(tlBbsInfo.bbsType))
							.fetchOne();
		if (!CommonUtils.isNull(result)) {
			result.setSubFiles(
											queryFactory.select(Projections.bean(Files.class
																				, tlBbsFile.fileId
																				, tlBbsFile.orgFilenm))
											.from(tlBbsFileGrp)
											.leftJoin(tlBbsFile).on(tlBbsFile.filegrpId.eq(tlBbsFileGrp.filegrpId))
											.where(tlBbsFileGrp.bbsId.eq(bbsId))
											.fetch()
											);
			
		}
		return result;
	}

}
