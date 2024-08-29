package com.sl.tdbms.web.admin.config.authentication;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.FlashMap;
import org.springframework.web.servlet.FlashMapManager;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.support.SessionFlashMapManager;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

public class LoginFailureHandler implements AuthenticationFailureHandler {

    MessageSource messageSource;

    @Autowired
    LocaleResolver localeResolver;

    public LoginFailureHandler(MessageSource messageSource){
        this.messageSource = messageSource;
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String error = exception.getMessage();
        Locale locale = localeResolver.resolveLocale(request);
        //아이디 입력 안했을때
        if(CommonUtils.isNull((String)request.getParameter("userId"))){
            error = messageSource.getMessage("login.fail.id.alert",null,locale);
        }else{
            if(CommonUtils.isNull((String)request.getParameter ("userPswd"))){
                error = messageSource.getMessage("login.fail.password.alert",null,locale);
            }else{
            	//if(CommonUtils.isNull(error)) {
            	error = messageSource.getMessage("login.fail.error.alert",null,locale);
            	//}
            }
        }

        // Flash Attribute를 사용하여 에러 메시지 전달
        FlashMap flashMap = new FlashMap();
        flashMap.put("loginFailMsg", error);
        
        FlashMapManager flashMapManager = new SessionFlashMapManager();
        flashMapManager.saveOutputFlashMap(flashMap, request, response);
        
        // 로그인 페이지로 리다이렉트
        response.sendRedirect(request.getContextPath() + "/login");
        
//        response.setContentType("text/html; charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        out.println("<script>new MsgModalBuilder().init().alertBody('"+error+"').footer(4,"+CommonUtils.getMessage("common.button.confirm")+",function(button, modal){modal.close();}).open(); location.href='"+request.getContextPath()+"/login';</script>");
//        out.flush();
    }
}
