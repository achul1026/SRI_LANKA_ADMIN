package com.sl.tdbms.web.admin.support.interceptor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.dto.menu.TcMenuMngMenuCheckDTO;
import com.sl.tdbms.web.admin.common.entity.TcMenuAuth;
import com.sl.tdbms.web.admin.common.entity.TcUserMng;
import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.code.AthrztSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.QTcMenuAuthRepository;
import com.sl.tdbms.web.admin.common.repository.TcUserMngRepository;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponseException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.NoLoginException;

@Configuration
public class AuthorityCheckerInterceptor implements HandlerInterceptor {
	
	@Autowired
    TcUserMngRepository tcUserMngRepository;
	
	@Autowired
    QTcMenuAuthRepository qTcMenuAuthMenuRepository;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response , Object handler) throws Exception {
		try  {
			LoginMngrDTO tcUserMngInfo = LoginMngrUtils.getTcUserMngInfo();
			
			// 세션 정보를 가져오지 못 했을 때
			if (CommonUtils.isNull(tcUserMngInfo)
					|| CommonUtils.isNull(tcUserMngInfo.getUserType())
					|| CommonUtils.isNull(tcUserMngInfo.getUserAuth())
					) {
				throw new NoLoginException(ErrorCode.NOT_FOUND_USER_INFO);
			}
			
			// 개발자 계정 또는 슈퍼관리자일 경우 통과
			if (tcUserMngInfo.getUserAuth().equals("developer") || tcUserMngInfo.getUserType().equals(UserTypeCd.SUPER)) {
				request.setAttribute("currentMenuAuthInfo", new TcMenuMngMenuCheckDTO("Y"));
				return true;
			}
			
			// 임시 비밀번호를 발급 받은 경우
			if (tcUserMngInfo.getResetpswdYn().equals("Y")) {
				throw new CommonException(ErrorCode.PERMISSION_DENIED);
			}
			
			TcUserMng dbTcUserMng = tcUserMngRepository.findOneByUserId(tcUserMngInfo.getUserId());
			
			// 정지된 계정일 경우
			boolean match = Stream.of(AthrztSttsCd.SUSPENDED).anyMatch(x -> dbTcUserMng.getAthrztStts().equals(x));
			if (match) {
				throw new CommonException(ErrorCode.MNGR_STTS_NOT_NORMAL);
			}
			
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Authority auth = handlerMethod.getMethodAnnotation(Authority.class);
			
			if (!CommonUtils.isNull(auth)) {
				TcMenuAuth tcMenuAuth = null;
				String url = request.getRequestURI().toString();
				
				//메뉴 사용여부 조회
				for (AuthType authType : auth.authType()) {
					switch (authType) {
						case CREATE:
							tcMenuAuth = new TcMenuAuth(dbTcUserMng.getAuthgrpId(), "Y", null, null, null);
							break;
						case READ:
							tcMenuAuth = new TcMenuAuth(dbTcUserMng.getAuthgrpId(), null, "Y", null, null);
							break;
						case UPDATE:
							tcMenuAuth = new TcMenuAuth(dbTcUserMng.getAuthgrpId(), null, null, "Y", null);
							break;
						case DELETE:
							tcMenuAuth = new TcMenuAuth(dbTcUserMng.getAuthgrpId(), null, null, null, "Y");
							break;
						case GENERAL:
							return true; // GENERAL이 있으면 바로 true 리턴
						default:
							break;
					}
				}
				List<TcMenuMngMenuCheckDTO> authMenuList = qTcMenuAuthMenuRepository.getAuthMenuListByAuthority(tcMenuAuth);
				
				AntPathMatcher antPathMatcher = new AntPathMatcher();
				
				Optional<TcMenuMngMenuCheckDTO> currentMenuAuthInfo = authMenuList.stream().filter(x -> antPathMatcher.match(x.getUppermenuUrlpttrn(), url)).findFirst();
				if(!currentMenuAuthInfo.isPresent()) {
					throw new CommonResponseException(ErrorCode.PERMISSION_DENIED);
				}
				request.setAttribute("currentMenuAuthInfo", currentMenuAuthInfo.get());
			} else {
				throw new NoLoginException(ErrorCode.NOT_FOUND_USER_INFO);
			}
		} catch (CommonException | CommonResponseException e) {
			// Exception 메시지 SET
			response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			String jsonResponse = String.format("{\"message\": \"%s\"}", e.getMessage()); 
			response.getWriter().write(jsonResponse);
			return false;
		}
		
		return true;
	}
}
