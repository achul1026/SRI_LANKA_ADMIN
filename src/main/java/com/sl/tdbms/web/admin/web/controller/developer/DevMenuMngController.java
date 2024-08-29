package com.sl.tdbms.web.admin.web.controller.developer;

import java.util.Collections;
import java.util.List;

import javax.transaction.Transactional;

import com.sl.tdbms.web.admin.common.dto.menu.TcMenuMngInfoDTO;
import com.sl.tdbms.web.admin.common.dto.tccdgrp.TcCdInfoDTO;
import com.sl.tdbms.web.admin.common.entity.TcMenuMng;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcMenuMngRepository;
import com.sl.tdbms.web.admin.common.repository.TcMenuAuthRepository;
import com.sl.tdbms.web.admin.common.repository.TcMenuMngRepository;
import com.sl.tdbms.web.admin.web.service.developer.DevMenuMngService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.GroupCode;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;

@Controller
@RequestMapping("/developer/menu")
public class DevMenuMngController {

	@Autowired
	private DevMenuMngService menuMngService;

	@Autowired
	private TcMenuMngRepository tcMenuMngRepository;

	@Autowired
	private QTcCdInfoRepository qTcCdInfoRepository;
	
	@Autowired
	private TcMenuAuthRepository tcMenuAuthRepository;
	
	@Autowired
	private QTcMenuMngRepository qTcMenuMngRepository;

	/**
	 * @Method Name : menuListNew
	 * @작성일 : 2024. 04. 03.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 개발자 메뉴 권한 관리 목록
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String menuListNew(Model model) {
		return "views/developer/devMenuList";
	}

	/**
	 * @Method Name : menuSave
	 * @작성일 : 2024. 01. 08.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 개발자 메뉴관리 메뉴등록 페이지
	 * @param model
	 * @param menuUpperId
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String menuSavePage(Model model, @RequestParam String uppermenuCd) {
		String tcCdGrp;

		if (uppermenuCd.equals("ALL"))
			tcCdGrp = GroupCode.MAIN_MENU_CD.getCode();
		else
			tcCdGrp = GroupCode.SUB_MENU_CD.getCode();

		List<TcCdInfoDTO> tcCdInfoList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(tcCdGrp);
		Collections.reverse(tcCdInfoList);

		model.addAttribute("tcCdInfoList", tcCdInfoList);
		model.addAttribute("uppermenuCd", uppermenuCd);
		return "views/developer/modal/devMenuSave";
	}

	/**
	 * @Method Name : menuSave
	 * @작성일 : 2024. 1. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 개발자 메뉴 등록
	 * @param tcMenuMng
	 * @return
	 */
	@Authority(authType = AuthType.CREATE)
	@PostMapping
	public @ResponseBody CommonResponse<?> menuSave(TcMenuMng tcMenuMng) {
		menuMngService.saveMenu(tcMenuMng);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "메뉴 정보가 등록 되었습니다.");
	}

	/**
	 * @Method Name : menuDetail
	 * @작성일 : 2024. 01. 08.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 개발자 메뉴관리 메뉴상세 페이지
	 * @param model
	 * @param menuId
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/{menuId}")
	public String menuDetailPage(Model model, @PathVariable String menuId, @RequestParam String uppermenuCd) {
		TcMenuMngInfoDTO tcMenuMng = qTcMenuMngRepository.getMenuInfoById(menuId);
		if (CommonUtils.isNull(tcMenuMng)) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Menu is empty");
		}
		String tcCdGrp;

		if (uppermenuCd.equals("ALL"))
			tcCdGrp = GroupCode.MAIN_MENU_CD.getCode();
		else
			tcCdGrp = GroupCode.SUB_MENU_CD.getCode();

		List<TcCdInfoDTO> tcCdInfoList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(tcCdGrp);
		Collections.reverse(tcCdInfoList);

		model.addAttribute("tcCdInfoList", tcCdInfoList);
		model.addAttribute("tcMenuMng", tcMenuMng);
		return "views/developer/modal/devMenuDetail";
	}

	/**
	 * @Method Name : menuUpdate
	 * @작성일 : 2024. 1. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 개발자 메뉴 수정
	 * @param tcMenuMng
	 * @param menuId
	 * @return
	 */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/{menuId}")
	public @ResponseBody CommonResponse<?> menuUpdate(TcMenuMng tcMenuMng, @PathVariable String menuId) {
		menuMngService.updateMenu(menuId, tcMenuMng);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "메뉴 정보가 수정 되었습니다.");
	}

	/**
	 * @Method Name : subMenuDelete
	 * @작성일 : 2024. 1. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 개발자 메뉴 삭제
	 * @param menuId
	 * @return
	 */
	@Authority(authType = AuthType.DELETE)
	@DeleteMapping("/{menuId}")
	@Transactional
	public @ResponseBody CommonResponse<?> subMenuDelete(@PathVariable String menuId) {
		TcMenuMng newTcMenuMng = tcMenuMngRepository.findById(menuId).get();

		List<TcMenuMng> originSubTcMenuMng = tcMenuMngRepository.findAllByUppermenuCd(newTcMenuMng.getMenuCd());
		if (!CommonUtils.isNull(originSubTcMenuMng)) {
			for (TcMenuMng subMenu : originSubTcMenuMng) {
				tcMenuMngRepository.delete(subMenu);
			}
		}
		
		tcMenuAuthRepository.deleteAllByMenuId(menuId); 

		tcMenuMngRepository.delete(newTcMenuMng);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "메뉴 정보가 삭제 되었습니다.");
	}

}
