package com.sl.tdbms.web.admin.support.interceptor;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.navercorp.lucy.security.xss.servletfilter.XssEscapeServletFilter;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Autowired
	MenuInterceptor menuInterceptor;

	@Autowired
	AuthorityCheckerInterceptor authorityCheckerInterceptor;

	/**
	 * Locale Resolver
	 *
	 * @return the locale resolver
	 */
	@Bean
	public LocaleResolver localeResolver() {
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(new Locale("eng"));
		return localeResolver;
	}

	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor() {
		LocaleChangeInterceptor interceptor = new LocaleChangeInterceptor();
		interceptor.setParamName("lang");
		return interceptor;
	}
	
	@Bean
	public MessageSource messageSource(@Value("${srilanka.messages.basename}") String basename,
			@Value("${srilanka.messages.encoding}") String encoding) {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasenames(basename.split(","));
		messageSource.setDefaultEncoding(encoding);
		return messageSource;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(localeChangeInterceptor());

		// 권한 체크
		// TODO::DB 동기화 작업 후 주석 해제
		registry.addInterceptor(authorityCheckerInterceptor).addPathPatterns("/**").excludePathPatterns("/")
				.excludePathPatterns("/login").excludePathPatterns("/login/confirm").excludePathPatterns("/login/**")
				.excludePathPatterns("/changeLocale")
				.excludePathPatterns("/login/find/**").excludePathPatterns("/join").excludePathPatterns("/join/save")
				.excludePathPatterns("/common/**").excludePathPatterns("/error").excludePathPatterns("/css/**")
				.excludePathPatterns("/fonts/**").excludePathPatterns("/images/**").excludePathPatterns("/js/**")
				.excludePathPatterns("/excel/**");

		// 사이드 메뉴관련 추가
		registry.addInterceptor(menuInterceptor).addPathPatterns("/**").excludePathPatterns("/")
				.excludePathPatterns("/login").excludePathPatterns("/login/confirm").excludePathPatterns("/login/**")
				.excludePathPatterns("/changeLocale")
				.excludePathPatterns("/login/find/**").excludePathPatterns("/join").excludePathPatterns("/join/save")
				.excludePathPatterns("/common/**").excludePathPatterns("/error").excludePathPatterns("/css/**")
				.excludePathPatterns("/fonts/**").excludePathPatterns("/images/**").excludePathPatterns("/js/**")
				.excludePathPatterns("/excel/**");
	}

	/**
	 * XSS Filter
	 *
	 * @return the filter registration bean
	 */
	@Bean
	public FilterRegistrationBean<XssEscapeServletFilter> filterRegistrationBean() {
		FilterRegistrationBean<XssEscapeServletFilter> filterRegistration = new FilterRegistrationBean<>();
		filterRegistration.setFilter(new XssEscapeServletFilter());
		filterRegistration.setOrder(1);
		filterRegistration.addUrlPatterns("/*");
		return filterRegistration;
	}

}