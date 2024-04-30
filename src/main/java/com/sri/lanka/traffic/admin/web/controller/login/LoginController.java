package com.sri.lanka.traffic.admin.web.controller.login;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sri.lanka.traffic.admin.common.dto.mngr.TcUserMngDTO;
import com.sri.lanka.traffic.admin.common.entity.TcCdInfo;
import com.sri.lanka.traffic.admin.common.entity.TcUserMng;
import com.sri.lanka.traffic.admin.common.enums.GroupCode;
import com.sri.lanka.traffic.admin.common.querydsl.QTcCdInfoRepository;
import com.sri.lanka.traffic.admin.common.repository.TcUserMngRepository;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.web.service.systemmng.TcUserMngService;


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
		return "views/login/login";
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
		List<TcCdInfo> bffltdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode());
		List<TcCdInfo> deptList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.DEPT_CD.getCode());
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
	public @ResponseBody CommonResponse<?> join(@Valid TcUserMng tcUserMng, BindingResult bindingResult) {
		
		Map<String, Object> result = new HashMap<String, Object>();
		// validation
		if(bindingResult.hasErrors()) {
			// 에러 발생 시
			FieldError error = bindingResult.getFieldError();
			Object[] arguments = error.getArguments();
			
			String errorMsg = messageSource.getMessage(error.getDefaultMessage(), arguments, LocaleContextHolder.getLocale());
			String msgTrgt = error.getField();
			
			Boolean userIdDuplicate = tcUserMngRepository.existsByUserId(tcUserMng.getUserId());
			if (userIdDuplicate) {
				// 예외처리
				msgTrgt = "mngrAccountId";
				errorMsg = "Duplicate User Id";
				return CommonResponse.ResponseCodeAndMessage(9999,  msgTrgt + "/" + errorMsg);
			}
			
			return CommonResponse.ResponseCodeAndMessage(9999, msgTrgt + "/" + errorMsg);
		}
		
		tcUserMngService.saveTcUserMng(tcUserMng);
		result.put("code", 200);
		result.put("message", "회원가입 성공");
		return CommonResponse.successToData(result,"");
	}
	
	@PostMapping(value="/logout")
	public String logout(HttpSession session) {
		return "redirect:/";
	}
	
	@GetMapping("/login/find/id/info")
	public String loginIdFindInfoPage(Model model) {
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
	public @ResponseBody CommonResponse<?> loginIdFind(@RequestBody TcUserMngDTO tcUserMng){
		TcUserMng newTcUserMng = tcUserMngRepository.findByUserEmail(tcUserMng.getUserEmail());
		if (!newTcUserMng.getUserEmail().equals(tcUserMng.getUserEmail())
				|| !newTcUserMng.getUserNm().equals(tcUserMng.getUserNm())) {
			// 예외처리
		}
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("message", "아이디 찾기 성공");
		
		return CommonResponse.successToData(result, "");
	}

	/**
	 * @Method Name : pwFind
	 * @작성일 : 2024. 01. 03.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 비밀번호 찾기
	 * @param model
	 * @return
	 */
	@GetMapping("/login/find/pw/info")
	public String loginPwFindInfoPage(Model model) {
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
	public @ResponseBody CommonResponse<?> loginPwFind(@RequestBody TcUserMngDTO tcUserMngDTO){
		TcUserMng newTcUserMng = tcUserMngRepository.findByUserEmail(tcUserMngDTO.getUserEmail());
		if (!newTcUserMng.getUserEmail().equals(tcUserMngDTO.getUserEmail())
				|| !newTcUserMng.getUserId().equals(tcUserMngDTO.getUserId())) {
			// 예외처리
		}
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("message", "비밀번호 찾기 성공");
		return CommonResponse.successToData(result, "");
	}

	/**
	 * @Method Name : pwChange
	 * @작성일 : 2024. 01. 03.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 비밀번호 변경
	 * @param model
	 * @return
	 */
	@GetMapping("/login/find/pw/change")
	public String loginPwChangePage(Model model) {
		return "views/login/loginPwChange";
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
		
		model.addAttribute("userId", newTcUserMng.getUserId());
		
		return "views/login/modal/idFindResult";
	}
}
