package com.sl.tdbms.web.admin.web.controller.facilityequipmng;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.config.authentication.Authority;

@Controller
@RequestMapping("/facilityequipmng/facility")
public class FacilityController {

	/**
	 * @Method Name : facilityequipmngfacility
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 시설물
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String facilityequipmngFacilityList(Model model) {
		return "views/facilityequipmng/facilityList";
	}

	/**
	 * @Method Name : facilityequipmngfacilityDetail
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 시설물 장비 정보
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/state/detail")
	public String facilityequipmngFacilityDetail(Model model) {
		return "views/facilityequipmng/modal/facilityStateDetail";
	}

	/**
	 * @Method Name : facilityequipmngfacilityHandling
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 시설물 장애 처리
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/state/handling")
	public String facilityequipmngFacilityHandling(Model model) {
		return "views/facilityequipmng/modal/facilityStateHandling";
	}

	/**
	 * @Method Name : facilityequipmngfacilityHistory
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 시설물 장애이력
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/history")
	public String facilityequipmngFacilityHistoryList(Model model) {
		return "views/facilityequipmng/facilityHistoryList";
	}

	/**
	 * @Method Name : facilityequipmngfacilityHistoryDetail
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 시설물 장애이력 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/history/detail")
	public String facilityequipmngFacilityHistoryDetail(Model model) {
		return "views/facilityequipmng/facilityHistoryDetail";
	}

	/**
	 * @Method Name : facilityequipmngFacilityHistoryUpdate
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 시설물 장애이력 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/history/update")
	public String facilityequipmngFacilityHistoryUpdate(Model model) {
		return "views/facilityequipmng/facilityHistoryUpdate";
	}

	/**
	 * @Method Name : facilityequipmngFacilityInfo
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 시설물 정보관리
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/info")
	public String facilityequipmngFacilityInfoList(Model model) {
		return "views/facilityequipmng/facilityInfoList";
	}

	/**
	 * @Method Name : facilityequipmngFacilityInfoDetail
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 시설물 정보관리 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/info/detail")
	public String facilityequipmngFacilityInfoDetail(Model model) {
		return "views/facilityequipmng/facilityInfoDetail";
	}

	/**
	 * @Method Name : facilityequipmngFacilityInfoUpdate
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 시설물 정보관리 수정
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/info/update")
	public String facilityequipmngFacilityInfoUpdate(Model model) {
		return "views/facilityequipmng/facilityInfoUpdate";
	}

	/**
	 * @Method Name : facilityequipmngFacilityInfoSave
	 * @작성일 : 2024. 02. 13.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 장비/시설물관리 시설물 정보관리 등록
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/info/save")
	public String facilityequipmngFacilityInfoSave(Model model) {
		return "views/facilityequipmng/facilityInfoSave";
	}
}
