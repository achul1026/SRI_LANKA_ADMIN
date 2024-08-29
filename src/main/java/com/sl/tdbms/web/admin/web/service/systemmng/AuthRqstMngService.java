package com.sl.tdbms.web.admin.web.service.systemmng;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.sl.tdbms.web.admin.common.dto.auth.TcAuthGrpDetailDTO;
import com.sl.tdbms.web.admin.common.dto.menu.TcMenuMngDTO;
import com.sl.tdbms.web.admin.common.entity.TcAuthGrp;
import com.sl.tdbms.web.admin.common.entity.TcMenuAuth;
import com.sl.tdbms.web.admin.common.entity.TcUserMng;
import com.sl.tdbms.web.admin.common.entity.TmAuthRqst;
import com.sl.tdbms.web.admin.common.enums.code.RqstSttsCd;
import com.sl.tdbms.web.admin.common.querydsl.QTcMenuAuthRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcMenuMngRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sl.tdbms.web.admin.common.repository.TcAuthGrpRepository;
import com.sl.tdbms.web.admin.common.repository.TcCdInfoRepository;
import com.sl.tdbms.web.admin.common.repository.TcMenuAuthRepository;
import com.sl.tdbms.web.admin.common.repository.TcUserMngRepository;
import com.sl.tdbms.web.admin.common.repository.TmAuthRqstRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponseException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;


@Service
public class AuthRqstMngService {
	
	@Autowired
    TcUserMngRepository tcUserMngRepository;
	
	@Autowired
    TmAuthRqstRepository tmAuthRqstRepository;
	
	@Autowired
    TcAuthGrpRepository tcAuthGrpRepository;
	
	@Autowired
    TcMenuAuthRepository tcMenuAuthRepository;

	@Autowired
    QTcMenuAuthRepository qTcMenuAuthRepository;
	
	@Autowired
    QTcMenuMngRepository qTcMenuMngRepository;
	
	@Autowired
    TcCdInfoRepository tcCdInfoRepository;
	
	/**
	  * @Method Name : setUserAuth
	  * @작성일 : 2024. 4. 18.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 요청 권한 관리 승인 또는 반려
	  * @param authgrpId
	  * @param authrqstId
	  * @param isApproval
	  */
	@Transactional
	public void setUserAuth(String authgrpId, String userId, String authrqstId, boolean isApproval) {
		try {
			if (isApproval) {
				TcUserMng userMng = tcUserMngRepository.findOneByUserId(userId);
				userMng.setAuthgrpId(authgrpId);
				tcUserMngRepository.save(userMng);
			}
			
			TmAuthRqst tmAuthRqst = tmAuthRqstRepository.findById(authrqstId).get();
			
			RqstSttsCd status = isApproval ? RqstSttsCd.APPROVAL : RqstSttsCd.REJECT;
			tmAuthRqst.setRqstStts(status);
			tmAuthRqst.setUserId(LoginMngrUtils.getUserId());
			tmAuthRqst.setAthrztDt(LocalDateTime.now());
			tmAuthRqstRepository.save(tmAuthRqst);
		} catch (Exception e) {
			String resMsg = CommonUtils.getMessage("authRqst.authRqstDetail.approval.process.fail");
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
	}
	
	/**
	  * @Method Name : setAuthgrp
	  * @작성일 : 2024. 5. 2.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 권한 수정 권한 설정
	  * @param authgrpId
	  * @param tcAuthGrp
	  * @return
	  */
	public TcAuthGrp setAuthgrp(String authgrpId, TcAuthGrp tcAuthGrp) {
		String resMsg = CommonUtils.getMessage("authRqst.authority.notExist");
		TcAuthGrp newAuthGrp = tcAuthGrpRepository.findById(authgrpId).orElseThrow(() -> new CommonException(ErrorCode.EMPTY_DATA, resMsg));
		
		if (!CommonUtils.isNull(tcAuthGrp.getAuthgrpNm())) newAuthGrp.setAuthgrpNm(tcAuthGrp.getAuthgrpNm());
		if (!CommonUtils.isNull(tcAuthGrp.getAuthgrpDescr())) newAuthGrp.setAuthgrpDescr(tcAuthGrp.getAuthgrpDescr());
//		if (!CommonUtils.isNull(tcAuthGrp.getBffltdCd())) newAuthGrp.setBffltdCd(tcAuthGrp.getBffltdCd());
		
		return newAuthGrp;
	}
	
	/**
	  * @Method Name : saveAuthByAuthGrp
	  * @작성일 : 2024. 4. 16.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 권한 그룹 등록에 따른 권한 등록
	  * @param tcAuthGrp
	  */
	@Transactional
	public void saveAuthGrp(TcAuthGrp tcAuthGrp) {
		
		try {
			TcMenuMngDTO tcMenuMngDTO = new TcMenuMngDTO();
			tcMenuMngDTO.setUseYn("Y");
//			tcMenuMngDTO.setBscmenuYn("N");
			List<TcMenuMngDTO> menuList = qTcMenuMngRepository.getMenuList(tcMenuMngDTO);
			
			List<TcMenuAuth> tcMenuAuthList = menuList.stream()
				    .flatMap(menu -> {
				        String authYn = "N";
				        String baseSrchYn = "N";
				        // 기본 메뉴의 읽기 권한을 설정
				        if ("Y".equals(menu.getBscmenuYn())) baseSrchYn = "Y";
				        return Stream.concat(
				            Stream.of(new TcMenuAuth(tcAuthGrp.getAuthgrpId(), menu.getMenuId(), baseSrchYn, authYn)), // 메인 메뉴에 대한 TcMenuAuth 객체 생성
				            menu.getSubMenuList().stream() // 서브 메뉴 리스트 접근
				                .map(subMenu -> new TcMenuAuth(tcAuthGrp.getAuthgrpId(), subMenu.getMenuId(), authYn)) // 서브 메뉴에 대한 TcMenuAuth 객체 생성
				        );
				    })
				    .collect(Collectors.toList());
			
			if (tcAuthGrp.getBscauthYn()!="Y") tcAuthGrp.setBscauthYn("N");
			tcAuthGrp.setTcMenuAuthList(tcMenuAuthList);
			
			tcAuthGrpRepository.save(tcAuthGrp);
			if(!CommonUtils.isNull(tcAuthGrp.getTcMenuAuthList())) {
				tcAuthGrp.getTcMenuAuthList().forEach(x -> x.setAuthgrpId(tcAuthGrp.getAuthgrpId()));
				tcMenuAuthRepository.saveAll(tcAuthGrp.getTcMenuAuthList());
			}
			
		} catch (Exception e) {
//			e.printStackTrace();
			String resMsg = CommonUtils.getMessage("authRqst.authSetting.authority.save.fail");
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
	}
	
	/**
	 * @Method Name : updateAuth
	 * @작성일 : 2024. 1. 24.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 권한 수정
	 * @param tcAuthMng
	 */
	@Transactional
	public void updateAuth(TcAuthGrp tcAuthGrp) throws CommonException {
		try {
			tcMenuAuthRepository.saveAll(tcAuthGrp.getTcMenuAuthList());
		}catch (Exception e) {
			String resMsg = CommonUtils.getMessage("authRqst.authSetting.authority.update.fail");
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
	}
	
	/**
	  * @Method Name : deleteAuth
	  * @작성일 : 2024. 1. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 권한 삭제
	  * @param authgrpId
	  */
	@Transactional
	public void deleteAuth(String authgrpId) throws CommonException {
		String resMsg = CommonUtils.getMessage("authRqst.authSetting.authority.delete.fail");;
		try {
			List<TcUserMng> mngrList = tcUserMngRepository.findAllByAuthgrpId(authgrpId);
			if(!CommonUtils.isListNull(mngrList)) {
				resMsg = CommonUtils.getMessage("authRqst.authSetting.authority.delete.fail.applied");
				throw new CommonException(ErrorCode.ENTITY_DELETE_FAILED, resMsg);
			}
//				mngrList.forEach(x -> x.setAuthgrpId(null));
			tcUserMngRepository.saveAll(mngrList);
			tcMenuAuthRepository.deleteAllByAuthgrpId(authgrpId);
			tcAuthGrpRepository.deleteById(authgrpId);
		} catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_DELETE_FAILED, resMsg);
		}
	}
	
	/**
	  * @Method Name : getAuthInfo
	  * @작성일 : 2024. 1. 24.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 권한 상세 조회
	  * @param authgrpId
	  * @return
	  */
	@Transactional
	public TcAuthGrpDetailDTO getAuthInfo(String authgrpId) {
		TcAuthGrpDetailDTO tcAuthMngDetailDTO = new TcAuthGrpDetailDTO();
		Optional<TcAuthGrp> authInfo = tcAuthGrpRepository.findById(authgrpId); 
		if(!authInfo.isPresent()) {
			String resMsg = CommonUtils.getMessage("authRqst.authority.notExist");
			throw new CommonException(ErrorCode.EMPTY_DATA, resMsg);
		}
		List<TcAuthGrpDetailDTO.TcMenuAuthInfo> authMenuList = qTcMenuAuthRepository.getAuthMenuList(authgrpId);

		tcAuthMngDetailDTO.setTcAuthGrp(authInfo.get());
		tcAuthMngDetailDTO.setAuthMenuList(authMenuList);

		return tcAuthMngDetailDTO;
	}

	/**
	  * @Method Name : checkDuplicateAuth
	  * @작성일 : 2024. 6. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 권한 이름 중복검사
	  * @param tcAuthGrp
	  */
	public void checkDuplicateAuth(String authgrpId, String authgrpNm) {
		// 권한 이름 중복 검사
		Optional<TcAuthGrp> existAuth = tcAuthGrpRepository.findOneByAuthgrpNm(authgrpId, authgrpNm);
		if (existAuth.isPresent()) {
			String resMsg = CommonUtils.getMessage("authRqst.authSetting.duplication.name");
			throw new CommonResponseException(ErrorCode.DUPLICATION_DATA, resMsg);
		}
		
	}
	
}
