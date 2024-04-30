package com.sri.lanka.traffic.admin.web.controller.developer;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

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

import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuMngDTO;
import com.sri.lanka.traffic.admin.common.entity.TcCdInfo;
import com.sri.lanka.traffic.admin.common.entity.TcMenuMng;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.GroupCode;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.querydsl.QTcCdInfoRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTcMenuMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TcMenuMngRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonException;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.support.exception.ErrorCode;
import com.sri.lanka.traffic.admin.web.service.developer.DevMenuMngService;

@Controller
@RequestMapping("/developer/menu")
public class DevMenuMngController {

	@Autowired
	private DevMenuMngService menuMngService;

	@Autowired
	private TcMenuMngRepository tcMenuMngRepository;

	@Autowired
	private QTcMenuMngRepository qTcMenuMngRepository;

	@Autowired
	private QTcCdInfoRepository qTcCdInfoRepository;

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
	public String menuListNew(Model model, TcMenuMngDTO tcMenuMngDTO) {
		List<TcMenuMngDTO> tcMenuMngList = qTcMenuMngRepository.getMenuList(tcMenuMngDTO);
		model.addAttribute("tcMenuMngList", tcMenuMngList);
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

		List<TcCdInfo> tcCdInfoList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(tcCdGrp);
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
		Optional<TcMenuMng> tcMenuMng = tcMenuMngRepository.findById(menuId);
		if (!tcMenuMng.isPresent()) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Menu is empty");
		}
		String tcCdGrp;

		if (uppermenuCd.equals("ALL"))
			tcCdGrp = GroupCode.MAIN_MENU_CD.getCode();
		else
			tcCdGrp = GroupCode.SUB_MENU_CD.getCode();

		List<TcCdInfo> tcCdInfoList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(tcCdGrp);
		Collections.reverse(tcCdInfoList);

		model.addAttribute("tcCdInfoList", tcCdInfoList);
		model.addAttribute("tcMenuMng", tcMenuMng.get());
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
	 * @Method 설명 : 개발자 서브메뉴 삭제
	 * @param menuId
	 * @return
	 */
	@Authority(authType = AuthType.DELETE)
	@DeleteMapping("/{menuId}")
	public @ResponseBody CommonResponse<?> subMenuDelete(@PathVariable String menuId) {
		TcMenuMng newTcMenuMng = tcMenuMngRepository.findById(menuId).get();

		List<TcMenuMng> originSubTcMenuMng = tcMenuMngRepository.findAllByUppermenuCd(newTcMenuMng.getMenuCd());
		if (!CommonUtils.isNull(originSubTcMenuMng)) {
			for (TcMenuMng subMenu : originSubTcMenuMng) {
				tcMenuMngRepository.delete(subMenu);
			}
		}

		tcMenuMngRepository.delete(newTcMenuMng);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "메뉴 정보가 삭제 되었습니다.");
	}

}
