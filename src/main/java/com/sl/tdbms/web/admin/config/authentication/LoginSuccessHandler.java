package com.sl.tdbms.web.admin.config.authentication;

import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequiredArgsConstructor
@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	final LocaleResolver localeResolver;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
    	
    	String redirectUrl;
    	//성공시 실패한 세션 아이디 정리
    	authenticationAttributes(request);

    	if (LoginMngrUtils.getTcUserMngInfo().getResetpswdYn().equals("Y")) {
    		// 임시 비밀번호로 로그인 시 보내는 주소
    		redirectUrl = "/login/find/pw/change";
    		getRedirectStrategy().sendRedirect(request,response,redirectUrl);
		} else {
			// 로그인 성공시 보내는 주소
			redirectUrl = "/main";
			getRedirectStrategy().sendRedirect(request,response,redirectUrl);
		}
    }

    protected void authenticationAttributes(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        if(session==null) return;
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}
