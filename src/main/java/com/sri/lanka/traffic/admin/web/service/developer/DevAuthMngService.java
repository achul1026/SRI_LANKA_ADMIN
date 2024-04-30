package com.sri.lanka.traffic.admin.web.service.developer;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sri.lanka.traffic.admin.common.dto.auth.TcAuthMngDetailDTO;
import com.sri.lanka.traffic.admin.common.dto.auth.TcAuthMngDetailDTO.TcMenuAuthInfo;
import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuMngDTO;
import com.sri.lanka.traffic.admin.common.entity.TcAuthGrp;
import com.sri.lanka.traffic.admin.common.entity.TcAuthMng;
import com.sri.lanka.traffic.admin.common.entity.TcMenuAuth;
import com.sri.lanka.traffic.admin.common.entity.TcUserMng;
import com.sri.lanka.traffic.admin.common.querydsl.QTcMenuAuthRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTcMenuMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TcAuthGrpRepository;
import com.sri.lanka.traffic.admin.common.repository.TcAuthMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TcCdInfoRepository;
import com.sri.lanka.traffic.admin.common.repository.TcMenuAuthRepository;
import com.sri.lanka.traffic.admin.common.repository.TcUserMngRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.support.exception.CommonException;
import com.sri.lanka.traffic.admin.support.exception.ErrorCode;

@Service
public class DevAuthMngService {
	
	@Autowired
	private TcUserMngRepository tcUserMngRepository;
	
	@Autowired
	private TcAuthMngRepository tcAuthMngRepository;
	
	@Autowired
	private TcMenuAuthRepository tcMenuAuthRepository;

	@Autowired
	private QTcMenuAuthRepository qTcMenuAuthRepository;
	
	@Autowired
	QTcMenuMngRepository qTcMenuMngRepository;
	
	@Autowired
	TcCdInfoRepository tcCdInfoRepository;
	
	@Autowired
	TcAuthGrpRepository tcAuthGrpRepository;
	
	/**
	  * @Method Name : saveAuth
	  * @작성일 : 2024. 1. 24.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 권한 등록
	  * @param tcAuthMng
	  */
	@Transactional
	public void saveAuth(TcAuthMng tcAuthMng) {
		
		try {
			tcAuthMngRepository.save(tcAuthMng);
			if(!CommonUtils.isNull(tcAuthMng.getTcMenuAuthList())) {
//				List<TcMenuMng> defaultMenuList = tcMenuMngRepository.findAllByBscmenuYn("Y");
//				if(!defaultMenuList.isEmpty()) {
//					List<TcMenuAuth> defaultAuthMenuList = defaultMenuList.stream().map(x -> new TcMenuAuth(tcAuthMng.getAuthId(),x.getMenuId(),"Y")).collect(Collectors.toList());
//					tcAuthMng.getTcMenuAuthList().addAll(defaultAuthMenuList);
//				}
				tcAuthMng.getTcMenuAuthList().forEach(x -> x.setAuthId(tcAuthMng.getAuthId()));
				tcMenuAuthRepository.saveAll(tcAuthMng.getTcMenuAuthList());
			}
		}catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "Auth save failed");
		}
	}
	
	/**
	  * @Method Name : saveAuthByAuthGrp
	  * @작성일 : 2024. 4. 16.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 권한 그룹 등록에 따른 권한 등록
	  * @param tcAuthGrp
	  */
	@Transactional
	public void saveAuthByAuthGrp(TcAuthGrp tcAuthGrp, TcAuthMng authMng) {
		
		try {
			String authgrpId = null;
			String authId = null;
			String authNm = null;
			//TODO:: authDecsr 권한 메뉴 목록으로 변경되면 삭제
			String authDescr = null;
			
			if (!CommonUtils.isNull(tcAuthGrp)) {
				authgrpId = tcAuthGrp.getAuthgrpId();
				authNm = tcAuthGrp.getAuthgrpNm();
				authDescr = null;
//						tcCdInfoRepository.findByCd(tcAuthGrp.getBffltdCd()).getCdNm();
			} else if (!CommonUtils.isNull(authMng)) {
				authId = authMng.getAuthId();
				authNm = authMng.getAuthNm();
				authDescr = authMng.getAuthDescr();
			}
			
			TcMenuMngDTO tcMenuMngDTO = new TcMenuMngDTO();
			tcMenuMngDTO.setUseYn("Y");
			tcMenuMngDTO.setBscmenuYn("N");
			List<TcMenuMngDTO> menuList = qTcMenuMngRepository.getMenuList(tcMenuMngDTO);
			
			
			TcAuthMng tcAuthMng = new TcAuthMng();
			if (!CommonUtils.isNull(authId)) tcAuthMng.setAuthId(authId);
			tcAuthMng.setAuthgrpId(authgrpId);
			tcAuthMng.setAuthNm(authNm);
			//TODO:: 권한 설명은 권한 메뉴 리스트로 변경
			tcAuthMng.setAuthDescr(authDescr);
			
			List<TcMenuAuth> tcMenuAuthList = menuList.stream()
				    .flatMap(menu -> Stream.concat(
				        Stream.of(new TcMenuAuth(tcAuthMng.getAuthId(), menu.getMenuId(), "N")), // 메인 메뉴에 대한 TcMenuAuth 객체 생성
				        menu.getSubMenuList().stream() // 서브 메뉴 리스트 접근
				            .map(subMenu -> new TcMenuAuth(tcAuthMng.getAuthId(), subMenu.getMenuId(), "N")) // 서브 메뉴에 대한 TcMenuAuth 객체 생성
				    ))
				    .collect(Collectors.toList());
			
			tcAuthMng.setTcMenuAuthList(tcMenuAuthList);
			
			saveAuth(tcAuthMng);
			
			if (!CommonUtils.isNull(tcAuthGrp)) tcAuthGrpRepository.save(tcAuthGrp);
			
		} catch (Exception e) {
			e.printStackTrace();
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "Authority save failed.");
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
	public void updateAuth(TcAuthMng tcAuthMng) {
		try {
			tcAuthMngRepository.save(tcAuthMng);
			tcMenuAuthRepository.saveAll(tcAuthMng.getTcMenuAuthList());
		}catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "Authority or Menu Authority save failed.");
		}
	}
	
	/**
	  * @Method Name : deleteAuth
	  * @작성일 : 2024. 1. 25.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 권한 삭제
	  * @param authId
	  */
	@Transactional
	public void deleteAuth(String authId) {
		
		try {
			List<TcUserMng> mngrList = tcUserMngRepository.findAllByAuthId(authId);
			if(!CommonUtils.isNull(mngrList)) mngrList.forEach(x -> x.setAuthId(null));
			tcUserMngRepository.saveAll(mngrList);
			tcMenuAuthRepository.deleteAllByAuthId(authId);
			tcAuthMngRepository.deleteById(authId);
		} catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "User or Authority or Menu Authority save failed.");
		}
	}
	
	/**
	  * @Method Name : getAuthInfo
	  * @작성일 : 2024. 1. 24.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 권한 상세 조회
	  * @param authId
	  * @return
	  */
	@Transactional
	public TcAuthMngDetailDTO getAuthInfo(String authId) {
		TcAuthMngDetailDTO tcAuthMngDetailDTO = new TcAuthMngDetailDTO();
		Optional<TcAuthMng> authInfo = tcAuthMngRepository.findById(authId); 
		if(!authInfo.isPresent()) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Authority is not present");
		}
		List<TcMenuAuthInfo> authMenuList = qTcMenuAuthRepository.getAuthMenuList(authId);

		tcAuthMngDetailDTO.setTcAuthMng(authInfo.get());
		tcAuthMngDetailDTO.setAuthMenuList(authMenuList);

		return tcAuthMngDetailDTO;
	}
}
