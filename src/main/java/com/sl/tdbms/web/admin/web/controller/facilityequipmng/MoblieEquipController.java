package com.sl.tdbms.web.admin.web.controller.facilityequipmng;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.config.authentication.Authority;

@Controller
@RequestMapping("/facilityequipmng/mobileequip")
public class MoblieEquipController {

	/**
	 * @Method Name : facilityequipmngMobileequip
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 이동식 장비
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String facilityequipmngMobileequipList(Model model) {
		return "views/facilityequipmng/mobileequipList";
	}

	/**
	 * @Method Name : facilityequipmngMobileequipDetail
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 이동식 장비 장비 정보
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/state/detail")
	public String facilityequipmngMobileequipDetail(Model model) {
		return "views/facilityequipmng/modal/moblieeqpuopStateDetail";
	}

	/**
	 * @Method Name : facilityequipmngMobileequipHandling
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 이동식 장비 장비 처리
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/state/handling")
	public String facilityequipmngMobileequipHandling(Model model) {
		return "views/facilityequipmng/modal/moblieeqpuopStateHandling";
	}

	/**
	 * @Method Name : facilityequipmngMobileequipHistory
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 이동식 장비 장애이력
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/history")
	public String facilityequipmngMobileequipHistoryList(Model model) {
		return "views/facilityequipmng/mobileequipHistoryList";
	}

	/**
	 * @Method Name : facilityequipmngMobileequipHistoryDetail
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 이동식 장비 장애이력 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/history/detail")
	public String facilityequipmngMobileequipHistoryDetail(Model model) {
		return "views/facilityequipmng/mobileequipHistoryDetail";
	}

	/**
	 * @Method Name : facilityequipmngMobileequipHistoryUpdate
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 이동식 장비 장애이력 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/history/update")
	public String facilityequipmngMobileequipHistoryUpdate(Model model) {
		return "views/facilityequipmng/mobileequipHistoryUpdate";
	}

	/**
	 * @Method Name : facilityequipmngMobileequipInfo
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 이동식 장비 정보관리
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/info")
	public String facilityequipmngMobileequipInfoList(Model model) {
		return "views/facilityequipmng/mobileequipInfoList";
	}

	/**
	 * @Method Name : facilityequipmngMobileequipInfoDetail
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 이동식 장비 정보관리 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/info/detail")
	public String facilityequipmngMobileequipInfoDetail(Model model) {
		return "views/facilityequipmng/mobileequipInfoDetail";
	}

	/**
	 * @Method Name : facilityequipmngMobileequipInfoUpdate
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 이동식 장비 정보관리 수정
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/info/update")
	public String facilityequipmngMobileequipInfoUpdate(Model model) {
		return "views/facilityequipmng/mobileequipInfoUpdate";
	}

	/**
	 * @Method Name : facilityequipmngMobileequipInfoSave
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 이동식 장비 정보관리 등록
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/info/save")
	public String facilityequipmngMobileequipInfoSave(Model model) {
		return "views/facilityequipmng/mobileequipInfoSave";
	}
}
