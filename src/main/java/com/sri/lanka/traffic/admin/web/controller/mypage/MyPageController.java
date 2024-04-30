package com.sri.lanka.traffic.admin.web.controller.mypage;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sri.lanka.traffic.admin.common.dto.menu.TcAuthGrpMenuDTO;
import com.sri.lanka.traffic.admin.common.dto.mngr.TcUserMngDTO;
import com.sri.lanka.traffic.admin.common.dto.mngr.TcUserMngSaveDTO;
import com.sri.lanka.traffic.admin.common.entity.TcUserMng;
import com.sri.lanka.traffic.admin.common.entity.TmAuthRqst;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.code.RqstSttsCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTcAuthGrpRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTcUserMngRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTmAuthRqstRepository;
import com.sri.lanka.traffic.admin.common.repository.TcUserMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TmAuthRqstRepository;
import com.sri.lanka.traffic.admin.common.util.LoginMngrUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.web.service.systemmng.TcUserMngService;


@Controller
@RequestMapping("/mypage")
public class MyPageController {

	@Autowired
	QTcUserMngRepository qTcUserMngRepository;
	
	@Autowired
	TcUserMngRepository tcUserMngRepository;
	
	@Autowired
	QTcAuthGrpRepository qTcAuthGrpRepository;
	
	@Autowired
	TcUserMngService tcUserMngService;
	
	@Autowired
	TmAuthRqstRepository tmAuthRqstRepository;
	
	@Autowired
	QTmAuthRqstRepository qTmAuthRqstRepository;
	
	/**
	 * @Method Name : myPage
	 * @작성일 : 2024. 04. 11.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 마이페이지
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.GENERAL)
	@GetMapping
	public String myInfoPage(Model model) {
		String usermngId = LoginMngrUtils.getTcUserMngInfo().getUsermngId();
		TcUserMngDTO tcUserMngDTO = qTcUserMngRepository.getTcUserInfo(usermngId);
		
		RqstSttsCd rqstStts = qTmAuthRqstRepository.waitingApprovalRequest(usermngId);
		
		model.addAttribute("rqstStts", rqstStts);
		model.addAttribute("waiting", RqstSttsCd.WAITING);
		model.addAttribute("userInfo", tcUserMngDTO);
		return "views/mypage/mypage";
	}
	
	/**
	  * @Method Name : myInfoUpdate
	  * @작성일 : 2024. 4. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 내 정보 수정
	  * @param model
	  * @param tcUserMngSaveDTO
	  * @return
	  */
	@Authority(authType = AuthType.GENERAL)
	@PutMapping
	public @ResponseBody CommonResponse<?> myInfoUpdate(Model model, TcUserMngSaveDTO tcUserMngSaveDTO) {
		TcUserMng newTcUserMng = tcUserMngService.setTcUserMngInfo(tcUserMngSaveDTO);
		tcUserMngRepository.save(newTcUserMng);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "회원 정보가 수정 되었습니다.");
	}

	/**
	 * @Method Name : myPageAuthSave
	 * @작성일 : 2024. 04. 11.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 마이페이지 권한신청 페이지
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.GENERAL)
	@GetMapping("/auth")
	public String myPageAuthSave(Model model) {
		TcUserMng myInfo = tcUserMngRepository.findOneByUserId(LoginMngrUtils.getUserId());
		List<TcAuthGrpMenuDTO> authGrp = qTcAuthGrpRepository.getAuthGrpMenuList(myInfo);
		model.addAttribute("authGrp", authGrp);
		return "views/mypage/modal/authSave";
	}
	
	/**
	  * @Method Name : requsetPermission
	  * @작성일 : 2024. 4. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 마이페이지 권한 신청
	  * @param model
	  * @param tmAuthRqst
	  * @return
	  */
	@Authority(authType = AuthType.GENERAL)
	@PostMapping("/auth")
	public @ResponseBody CommonResponse<?> requsetPermission(Model model, TmAuthRqst tmAuthRqst) {
		tmAuthRqst.setUsermngId(LoginMngrUtils.getTcUserMngInfo().getUsermngId());
		tmAuthRqstRepository.save(tmAuthRqst);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "권한 신청이 완료 되었습니다.");
	}
}
