package com.sri.lanka.traffic.admin.support.interceptor;

import java.util.List;
import java.util.stream.Stream;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sri.lanka.traffic.admin.common.dto.common.LoginMngrDTO;
import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuAuthDTO;
import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuMngDTO;
import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuMngInfoDTO;
import com.sri.lanka.traffic.admin.common.entity.TcUserMng;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.enums.code.AthrztSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.UserTypeCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTcMenuAuthRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTcMenuMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TcAuthMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TcUserMngRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.LoginMngrUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.NoLoginException;
import com.sri.lanka.traffic.admin.web.service.developer.DevMenuMngService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MenuInterceptor implements HandlerInterceptor {

	@Autowired
	TcUserMngRepository tcUserMngRepository;
	
	@Autowired
	QTcMenuAuthRepository qTcMenuAuthRepository;
	
	@Autowired
	QTcMenuMngRepository qTcMenuMngRepository;
	
	@Autowired
	TcAuthMngRepository tcAuthMngRepository;
	
	@Autowired
	DevMenuMngService menuMngService;
	
    /**
     * @brief 사이드메뉴 설정
     * @return  boolean
     * @throws Exception
     */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response , Object handler) throws Exception {
		log.info("==============Menu Interceptor preHandle Start ===============");
		log.info("Request Url : {}", request.getRequestURL());
		
		LoginMngrDTO loginSessionDTO = LoginMngrUtils.getTcUserMngInfo();
		TcUserMng tcUserMng = tcUserMngRepository.findOneByUsermngIdAndAthrztStts(loginSessionDTO.getUsermngId(), AthrztSttsCd.APPROVAL);
//		TcAuthMng tcAuthMng = tcAuthMngRepository.findOneByAuthId(tcUserMng.getAuthId()); useYn 미사용으로 비활성화
		
		//세션 정보 못가져올때
		if(CommonUtils.isNull(tcUserMng)) {
			//TO DO : NO LOGIN EXCEPTION;
			throw new NoLoginException();
		}
		
		//쿠키값 비교 다국어 처리
		Cookie[] cookies = request.getCookies();
		String lang = "eng";
		for(Cookie cookie : cookies) {
			if("lang".equals(cookie.getName())) {
				lang = cookie.getValue();
				request.setAttribute("lang", lang);
			}
		}
		//최고 관리자인경우
		if(UserTypeCd.SUPER.equals(tcUserMng.getUserType())) {
			TcMenuMngDTO tcMenuMngDTO = new TcMenuMngDTO();
			// 개발자 계정이 아닌경우 사용중인 메뉴만 표시
			if (!tcUserMng.getUserAuth().equals("developer")) tcMenuMngDTO.setUseYn("Y");
			
			//기본 메뉴 체크 없으면 INSERT
			List<TcMenuMngDTO> sideMenuList = qTcMenuMngRepository.getMenuList(tcMenuMngDTO);
			request.setAttribute("sideMenuList", sideMenuList);
		// 권한이 없거나 미사용인 경우
		} else if (CommonUtils.isNull(tcUserMng.getAuthId())) {
			TcMenuMngDTO tcMenuMngDTO = new TcMenuMngDTO();
			tcMenuMngDTO.setBscmenuYn("Y"); // 기본 메뉴만 조회하도록 설정
			tcMenuMngDTO.setUseYn("Y"); // 사용 중인 메뉴만 조회하도록 설정
		    
		    List<TcMenuMngDTO> sideMenuList = qTcMenuMngRepository.getMenuList(tcMenuMngDTO);
		    request.setAttribute("sideMenuList", sideMenuList);
		} else {
			List<TcMenuAuthDTO> sideMenuList = qTcMenuAuthRepository.getSideAuthMenuList(tcUserMng.getAuthId());
			request.setAttribute("sideMenuList", sideMenuList);
		}
		//메뉴 경로 세팅
		if ("GET".equals(request.getMethod())) {
			
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Authority auth = handlerMethod.getMethodAnnotation(Authority.class);
			
			// 3Depth인 경우만 @Authority에서 MenuType을 SAVE, UPDATE, DETAIL로 설정한 후 3Depth 판별하고 있음
			boolean isThreeDepth = Stream.of(MenuType.SAVE, MenuType.UPDATE, MenuType.DETAIL)
										.anyMatch(x -> auth.menuType().equals(x));
			
			String currentUrl = request.getRequestURI();
			char replaceStr = '/';				// URI 구분자
			
			if (isThreeDepth) {
				// 2Depth까지만 DB에 저장되어있어 3Depth일 경우 2Depth Url을 currentUrl로 설정
				currentUrl = currentUrl.substring(0, CommonUtils.findNthIndex(currentUrl, replaceStr, 3));
			}
			
			String currentUrlPath = currentUrl + "/**";
			String parentUrlPath = CommonUtils.getUrlPattrn(request.getRequestURI(), replaceStr, false);
			
			TcMenuMngInfoDTO menuInfo = new TcMenuMngInfoDTO();
			menuInfo = menuMngService.setTcMenuMngInfoDTO(menuInfo, currentUrlPath, parentUrlPath, currentUrl, lang);
			
			// URL PATH DB데이터 조회
			TcMenuMngInfoDTO menuDepthInfo = qTcMenuMngRepository.getMenuInfo(menuInfo);			// 데이터가 존재하는 경우에만
			
			if (menuDepthInfo != null) {
//				menuDepthInfo.setSubMenuNm(parentUrlPath);
				if (isThreeDepth) {
					String currentUri = request.getRequestURI();
					currentUrlPath = CommonUtils.getUrlPattrn(currentUri, replaceStr, isThreeDepth);
					String currentMenuNm = menuDepthInfo.getSubMenuNm() + MenuType.getCodeByName(auth.menuType().toString());
					menuDepthInfo.setMenuActiveLocation("THREE");
					menuDepthInfo.setCurrentMenuNm(currentMenuNm);
					menuDepthInfo.setCurrentMenuUrl(currentUri);
				}
				request.setAttribute("menuDepthInfo", menuDepthInfo);
			}
		}
		return true;
	}

}
