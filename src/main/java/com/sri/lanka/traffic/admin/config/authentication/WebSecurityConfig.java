package com.sri.lanka.traffic.admin.config.authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	MessageSource messageSource;

	@Value("${srilanka.auth.role}")
	public String AUTH_ROLE;
	
	@Value("${srilanka.developer.auth.role}")
	public String DEVELOPER_AUTH_ROLE;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable();
        
        http.authorizeRequests()
                .antMatchers("/join","/join/save","/login/find/**","/common/**","/css/**","/js/**","/fonts/**","/images/**").permitAll()
                .antMatchers("/**").hasAnyRole(AUTH_ROLE,DEVELOPER_AUTH_ROLE)
                .anyRequest().authenticated();

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/login/confirm")
                .usernameParameter("userId")
                .passwordParameter("userPswd")
                .defaultSuccessUrl("/main",true)
                .successHandler(successHandler())
                .failureHandler(failureHandler())
                .permitAll();

        http.logout()
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .permitAll();

	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(authenticationService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public AuthenticationSuccessHandler successHandler() {
		return new LoginSuccessHandler();
	}

	@Bean
	public AuthenticationFailureHandler failureHandler() {
		return new LoginFailureHandler(messageSource);
	}
}
