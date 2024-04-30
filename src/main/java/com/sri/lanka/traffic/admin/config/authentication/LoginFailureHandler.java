package com.sri.lanka.traffic.admin.config.authentication;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.web.servlet.LocaleResolver;

import com.sri.lanka.traffic.admin.common.util.CommonUtils;

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
        if(CommonUtils.isNull((String)request.getParameter("mngrAccountId"))){
            error = messageSource.getMessage("login.fail.id.alert",null,locale);
        }else{
            if(CommonUtils.isNull((String)request.getParameter ("mngrAccountPwd"))){
                error = messageSource.getMessage("login.fail.password.alert",null,locale);
            }else{
            	if(CommonUtils.isNull(error)) {
            		error = messageSource.getMessage("login.fail.error.alert",null,locale);
            	}
            }
        }


        response.setContentType("text/html; charset=UTF-8");
        PrintWriter out = response.getWriter();
        out.println("<script>alert('"+error+"'); location.href='"+request.getContextPath()+"/login';</script>");
        out.flush();
    }
}
