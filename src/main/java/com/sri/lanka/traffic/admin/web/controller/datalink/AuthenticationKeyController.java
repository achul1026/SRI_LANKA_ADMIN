package com.sri.lanka.traffic.admin.web.controller.datalink;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.config.authentication.Authority;

@Controller
@RequestMapping("/datalink/authentication")
public class AuthenticationKeyController {

	/**
	 * @Method Name : authenticationkeyList
	 * @작성일 : 2024. 4. 15.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 인증키요청관리 리스트
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String authenticationKeyPage() {
		return "views/datalink/authenticationKeyList";
	}

	/**
	 * @Method Name : authenticationkeyDetail
	 * @작성일 : 2024. 04. 15.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 인증키요청관리 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/detail")
	public String authenticationKeyDetail(Model model) {
		return "views/datalink/modal/authenticationKeyDetail";
	}

}
