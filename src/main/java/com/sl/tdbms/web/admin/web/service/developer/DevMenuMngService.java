package com.sl.tdbms.web.admin.web.service.developer;

import java.util.List;

import com.sl.tdbms.web.admin.common.dto.menu.TcMenuMngInfoDTO;
import com.sl.tdbms.web.admin.common.entity.TcAuthGrp;
import com.sl.tdbms.web.admin.common.entity.TcMenuAuth;
import com.sl.tdbms.web.admin.common.entity.TcMenuMng;
import com.sl.tdbms.web.admin.common.querydsl.QTcMenuAuthRepository;
import com.sl.tdbms.web.admin.common.repository.TcAuthGrpRepository;
import com.sl.tdbms.web.admin.common.repository.TcMenuAuthRepository;
import com.sl.tdbms.web.admin.common.repository.TcMenuMngRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;


@Service
public class DevMenuMngService {
	
	@Autowired
	private TcMenuMngRepository tcMenuMngRepository;
	
	@Autowired
	private TcAuthGrpRepository tcAuthGrpRepository;
	
	@Autowired
	private TcMenuAuthRepository tcMenuAuthRepository;
	
	@Autowired
	private QTcMenuAuthRepository qTcMenuAuthRepository;
	
	/**
	  * @Method Name : saveMenu
	  * @작성일 : 2024. 1. 26.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 메뉴 등록
	  * @param tcMenuMng
	  */
	@Transactional
	public void saveMenu(TcMenuMng tcMenuMng) {
		
		tcMenuMngRepository.save(tcMenuMng);
		String menuBaseYn = tcMenuMng.getBscmenuYn() != null ? tcMenuMng.getBscmenuYn() : "N";
		List<TcAuthGrp> authList = tcAuthGrpRepository.findAll();
		if(!CommonUtils.isNull(authList)) {
			for(TcAuthGrp authItem : authList) {
				TcMenuAuth tcMenuAuth = new TcMenuAuth(authItem.getAuthgrpId(), tcMenuMng.getMenuId(), menuBaseYn);
//				String authgrpId = authItem.getAuthgrpId();
//				String menuId = tcMenuMng.getMenuId();
//				
//				tcMenuAuth.setInputYn(menuBaseYn);
//				tcMenuAuth.setSrchYn(menuBaseYn);
//				tcMenuAuth.setUpdtYn(menuBaseYn);;
//				tcMenuAuth.setDelYn(menuBaseYn);
//				tcMenuAuth.setAuthgrpId(authgrpId);
//				tcMenuAuth.setMenuId(menuId);
				
				tcMenuAuthRepository.save(tcMenuAuth);
			}
		}
	}
	
	/**
	  * @Method Name : updateMenu
	  * @작성일 : 2024. 1. 29.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 메뉴 수정
	  * @param menuId
	  * @param tcMenuMng
	  */
	@Transactional
	public void updateMenu(String menuId, TcMenuMng tcMenuMng) {
		
		TcMenuMng originTcMenuMng = tcMenuMngRepository.findById(menuId).get();
		List<TcMenuMng> originSubTcMenuMng = tcMenuMngRepository.findAllByUppermenuCd(originTcMenuMng.getMenuCd());
		String menuCd = originTcMenuMng.getMenuCd();
		
		//상위 메뉴 기본 메뉴 값이 변경 되었을때
		//메뉴 권한 값 수정
		String originBscmenuYn = originTcMenuMng.getBscmenuYn();
		String menuBaseYn = tcMenuMng.getBscmenuYn();
		
		if(!CommonUtils.isNull(tcMenuMng.getMenuCd())) originTcMenuMng.setMenuCd(tcMenuMng.getMenuCd()); 
//		if(!CommonUtils.isNull(tcMenuMng.getMenunmEng())) originTcMenuMng.setMenunmEng(tcMenuMng.getMenunmEng()); 
//		if(!CommonUtils.isNull(tcMenuMng.getMenunmKor())) originTcMenuMng.setMenunmKor(tcMenuMng.getMenunmKor()); 
//		if(!CommonUtils.isNull(tcMenuMng.getMenunmSin())) originTcMenuMng.setMenunmSin(tcMenuMng.getMenunmSin()); 
		if(!CommonUtils.isNull(tcMenuMng.getUppermenuUrlpttrn())) originTcMenuMng.setUppermenuUrlpttrn(tcMenuMng.getUppermenuUrlpttrn()); 
		if(!CommonUtils.isNull(tcMenuMng.getMenuUrlpttrn())) originTcMenuMng.setMenuUrlpttrn(tcMenuMng.getMenuUrlpttrn()); 
//		if(!CommonUtils.isNull(tcMenuMng.getMenudescrEng())) originTcMenuMng.setMenudescrEng(tcMenuMng.getMenudescrEng());
//		if(!CommonUtils.isNull(tcMenuMng.getMenudescrKor())) originTcMenuMng.setMenudescrKor(tcMenuMng.getMenudescrKor());
//		if(!CommonUtils.isNull(tcMenuMng.getMenudescrSin())) originTcMenuMng.setMenudescrSin(tcMenuMng.getMenudescrSin());
		if(!CommonUtils.isNull(tcMenuMng.getMenuSqno())) originTcMenuMng.setMenuSqno(tcMenuMng.getMenuSqno());
		if(!CommonUtils.isNull(tcMenuMng.getBscmenuYn())) originTcMenuMng.setBscmenuYn(tcMenuMng.getBscmenuYn());
		
		if (!CommonUtils.isNull(originSubTcMenuMng)) {
			for (TcMenuMng subMenu : originSubTcMenuMng) {
				subMenu.setUppermenuCd(tcMenuMng.getMenuCd());
				subMenu.setUpdtId(LoginMngrUtils.getUserId());
				tcMenuMngRepository.save(subMenu);
			}
		}
		
		originTcMenuMng.setUpdtId(LoginMngrUtils.getUserId());
		originTcMenuMng.setUseYn(tcMenuMng.getUseYn());
		tcMenuMngRepository.save(originTcMenuMng);
		
		if(CommonUtils.isNull(originTcMenuMng.getUppermenuCd()) && !originBscmenuYn.equals(menuBaseYn)) {
			//상위메뉴 , 하위메뉴 menuId 
			String[] menuIdArr = tcMenuMngRepository.findAllByUppermenuCdOrMenuCd(menuCd,menuCd).stream().map(TcMenuMng::getMenuCd).toArray(String[]::new);
			List<TcMenuAuth> authMenuList = qTcMenuAuthRepository.getAuthMenuListByMenuIdArr(menuIdArr);
			if(!CommonUtils.isNull(authMenuList)) authMenuList.forEach(x -> {
																				x.setSrchYn(menuBaseYn); x.setInputYn(menuBaseYn); 
																				x.setUpdtYn(menuBaseYn); x.setDelYn(menuBaseYn); 
																			}
																		);
			tcMenuAuthRepository.saveAll(authMenuList);
		}
	}
	
	/**
	  * @Method Name : setTcMenuMngInfoDTO
	  * @작성일 : 2024. 3. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 메뉴 정보 세팅
	  * @param tcMenuMngInfoDTO
	  * @param currentUrlPath
	  * @param parentUrlPath
	  * @param currentUrl
	  * @param lang
	  * @return
	  */
	public TcMenuMngInfoDTO setTcMenuMngInfoDTO(TcMenuMngInfoDTO tcMenuMngInfoDTO, String currentUrlPath, String parentUrlPath, String currentUrl) {
		tcMenuMngInfoDTO.setCurrentMenuUrl(currentUrlPath);
		tcMenuMngInfoDTO.setUppermenuUrlpttrn(parentUrlPath);
		tcMenuMngInfoDTO.setMenuUrlpttrn(currentUrl);
//		tcMenuMngInfoDTO.setLang(lang);
		return tcMenuMngInfoDTO;
	}
	
}
