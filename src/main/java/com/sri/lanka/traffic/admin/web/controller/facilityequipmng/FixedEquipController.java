package com.sri.lanka.traffic.admin.web.controller.facilityequipmng;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.config.authentication.Authority;

@Controller
@RequestMapping("/facilityequipmng/fixedequip")
public class FixedEquipController {

	/**
	 * @Method Name : facilityequipmngFixedequip
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 고정식장비
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String facilityequipmngfixedequipList(Model model) {
		return "views/facilityequipmng/fixedequipList";
	}

	/**
	 * @Method Name : facilityequipmngFixedequipDetail
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 고정식 장비 장비 정보
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/state/detail")
	public String facilityequipmngFixedequipDetail(Model model) {
		return "views/facilityequipmng/modal/fixedequipStateDetail";
	}

	/**
	 * @Method Name : facilityequipmngFixedequipHandling
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 고정식 장비 장애 처리
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/state/handling")
	public String facilityequipmngFixedequipHandling(Model model) {
		return "views/facilityequipmng/modal/fixedequipStateHandling";
	}

	/**
	 * @Method Name : facilityequipmngfixedequipHistory
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 고정식 장비 장애이력
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/history")
	public String facilityequipmngFixedequipHistoryList(Model model) {
		return "views/facilityequipmng/fixedequipHistoryList";
	}

	/**
	 * @Method Name : facilityequipmngfixedequipHistoryDetail
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 고정식 장비 장애이력 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/history/detail")
	public String facilityequipmngFixedequipHistoryDetail(Model model) {
		return "views/facilityequipmng/fixedequipHistoryDetail";
	}

	/**
	 * @Method Name : facilityequipmngfixedequipHistoryUpdate
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 고정식 장비 장애이력 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/history/update")
	public String facilityequipmngFixedequipHistoryUpdate(Model model) {
		return "views/facilityequipmng/fixedequipHistoryUpdate";
	}

	/**
	 * @Method Name : facilityequipmngFixedequipInfo
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 고정식 장비 정보관리
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/info")
	public String facilityequipmngFixedequipInfoList(Model model) {
		return "views/facilityequipmng/fixedequipInfoList";
	}

	/**
	 * @Method Name : facilityequipmngFixedequipInfoDetail
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 고정식 장비 정보관리 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/info/detail")
	public String facilityequipmngFixedequipInfoDetail(Model model) {
		return "views/facilityequipmng/fixedequipInfoDetail";
	}

	/**
	 * @Method Name : facilityequipmngFixedequipInfoUpdate
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 고정식 장비 정보관리 수정
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/info/update")
	public String facilityequipmngFixedequipInfoUpdate(Model model) {
		return "views/facilityequipmng/fixedequipInfoUpdate";
	}

	/**
	 * @Method Name : facilityequipmngFixedequipInfoSave
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 고정식 장비 정보관리 등록
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/info/save")
	public String facilityequipmngFixedequipInfoSave(Model model) {
		return "views/facilityequipmng/fixedequipInfoSave";
	}

}
