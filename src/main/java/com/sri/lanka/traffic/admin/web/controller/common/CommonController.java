package com.sri.lanka.traffic.admin.web.controller.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sri.lanka.traffic.admin.common.repository.TcUserMngRepository;

@Controller
@RequestMapping("/common")
public class CommonController {

	@Autowired
	TcUserMngRepository tcUserMngRepository;

	/**
	 * @Method Name : bookmark
	 * @작성일 : 2024. 04. 12.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 즐겨찾기
	 * @param model
	 * @return
	 */
//	@Authority(authType = AuthType.READ)
	@GetMapping("/bookmark")
	public String myPage(Model model) {
		return "views/common/modal/bookMark";
	}

}
