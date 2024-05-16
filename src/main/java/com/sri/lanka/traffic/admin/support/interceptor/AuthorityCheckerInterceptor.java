package com.sri.lanka.traffic.admin.support.interceptor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sri.lanka.traffic.admin.common.dto.common.LoginMngrDTO;
import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuMngMenuCheckDTO;
import com.sri.lanka.traffic.admin.common.entity.TcMenuAuth;
import com.sri.lanka.traffic.admin.common.entity.TcUserMng;
import com.sri.lanka.traffic.admin.common.enums.code.AthrztSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.UserTypeCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTcMenuAuthRepository;
import com.sri.lanka.traffic.admin.common.repository.TcUserMngRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.LoginMngrUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.ErrorCode;
import com.sri.lanka.traffic.admin.support.exception.NoLoginException;

@Configuration
public class AuthorityCheckerInterceptor implements HandlerInterceptor {
	
	@Autowired
	TcUserMngRepository tcUserMngRepository;
	
	@Autowired
	QTcMenuAuthRepository qTcMenuAuthMenuRepository;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response , Object handler ) throws Exception {
		
		LoginMngrDTO tcUserMngInfo = LoginMngrUtils.getTcUserMngInfo();
		
		// 개발자 계정일 경우 통과
		if (tcUserMngInfo.getUserAuth().equals("developer") || tcUserMngInfo.getUserType().equals(UserTypeCd.SUPER)) {
			return true;
		}
		
		// 세션 정보를 가져오지 못 했을 때
		if (CommonUtils.isNull(tcUserMngInfo)) {
			throw new NoLoginException(ErrorCode.NOT_FOUND_USER_INFO);
		}
		
		TcUserMng dbTcUserMng = tcUserMngRepository.findOneByUserId(tcUserMngInfo.getUserId());
		
		// 정지된 계정일 경우
		boolean match = Stream.of(AthrztSttsCd.SUSPENDED).anyMatch(x -> dbTcUserMng.getAthrztStts().equals(x));
		if (match) {
			throw new NoLoginException(ErrorCode.MNGR_STTS_NOT_NORMAL);
		}
		
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Authority auth = handlerMethod.getMethodAnnotation(Authority.class);
		
		if (!CommonUtils.isNull(auth)) {
			TcMenuAuth tcMenuAuth = null;
			String url = request.getRequestURI().toString();
			
			//메뉴 사용여부 조회
			switch (auth.authType()) {
			case CREATE:
				tcMenuAuth = new TcMenuAuth(tcUserMngInfo.getAuthgrpId(), "Y", null, null, null);
				break;
			case READ:
				tcMenuAuth = new TcMenuAuth(tcUserMngInfo.getAuthgrpId(), null, "Y", null, null);
				break;
			case UPDATE:
				tcMenuAuth = new TcMenuAuth(tcUserMngInfo.getAuthgrpId(), null, null, "Y", null);
				break;
			case DELETE:
				tcMenuAuth = new TcMenuAuth(tcUserMngInfo.getAuthgrpId(), null, null, null, "Y");
				break;
			case GENERAL:
				return true;
			default:
				break;
			}
			
			List<TcMenuMngMenuCheckDTO> authMenuList = qTcMenuAuthMenuRepository.getAuthMenuListByAuthority(tcMenuAuth);
			
			AntPathMatcher antPathMatcher = new AntPathMatcher();
			
			Optional<TcMenuMngMenuCheckDTO> currentMenuAuthInfo = authMenuList.stream().filter(x -> antPathMatcher.match(x.getUppermenuUrlpttrn(), url)).findFirst();
			if(!currentMenuAuthInfo.isPresent()) {
		    	throw new NoLoginException(ErrorCode.PERMISSION_DENIED);
		    }
//			request.setAttribute("currentMenuAuthInfo", currentMenuAuthInfo.get());
		} else {
			throw new NoLoginException(ErrorCode.NOT_FOUND_USER_INFO);
		}
		
		return true;
	}
}
