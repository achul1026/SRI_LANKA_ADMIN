package com.sri.lanka.traffic.admin.web.controller.datamng;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.config.authentication.Authority;

@Controller
@RequestMapping("/datamng/dataregist")
public class DataRegistMngController {
	
	/**
	  * @Method Name : dataRegistList
	  * @작성일 : 2024. 3. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 데이터 등록 목록 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String dataRegistListPage(Model model) {
		return "views/datamng/dataRegistList";
	}

	/**
	  * @Method Name : dataRegistSavePage
	  * @작성일 : 2024. 3. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 데이터 등록 등록 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/save")
	public String dataRegistSavePage(Model model) {
		return "views/datamng/modal/dataRegistSave";
	}
	
	/**
	  * @Method Name : dataRegistUpdatePage
	  * @작성일 : 2024. 3. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 데이터 등록 수정 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/update")
	public String dataRegistUpdatePage(Model model) {
		return "views/datamng/modal/dataRegistUpdate";
	}
	
}
