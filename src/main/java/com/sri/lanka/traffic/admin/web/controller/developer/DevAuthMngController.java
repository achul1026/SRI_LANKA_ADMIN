package com.sri.lanka.traffic.admin.web.controller.developer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sri.lanka.traffic.admin.common.dto.auth.TcAuthMngDTO;
import com.sri.lanka.traffic.admin.common.dto.auth.TcAuthMngDetailDTO;
import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuMngDTO;
import com.sri.lanka.traffic.admin.common.entity.TcAuthMng;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.querydsl.QTcAuthMngRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTcMenuMngRepository;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.web.service.developer.DevAuthMngService;

@Controller
@RequestMapping("/developer/auth")
public class DevAuthMngController {

	@Autowired
	QTcAuthMngRepository qTcAuthMngRepository;

	@Autowired
	QTcMenuMngRepository qTcMenuMngRepository;

	@Autowired
	DevAuthMngService authMngService;

	/**
	 * @Method Name : authListPage
	 * @작성일 : 2024. 1. 3.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 개발자 권한 관리 목록
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String authList(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		
		List<TcAuthMngDTO> authList = qTcAuthMngRepository.getAuthList(searchCommonDTO, paging);
		
		long totalCnt = qTcAuthMngRepository.getTotalCount(searchCommonDTO);
		
		paging.setTotalCount(totalCnt);
		
		model.addAttribute("authList", authList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
		
		return "views/developer/devAuthList";
	}

	/**
	 * @Method Name : authSavePage
	 * @작성일 : 2024. 1. 5.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 개발자 권한 관리 등록
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String authListSave(Model model, TcMenuMngDTO tcMenuMngDTO) {
		tcMenuMngDTO.setUseYn("Y");
		tcMenuMngDTO.setBscmenuYn("N");
		List<TcMenuMngDTO> menuList = qTcMenuMngRepository.getMenuList(tcMenuMngDTO);
		model.addAttribute("menuList", menuList);
		return "views/developer/devAuthSave";
	}

	/**
	 * @Method Name : authSave
	 * @작성일 : 2024. 1. 23.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 개발자 권한 등록
	 * @param tcAuthMng
	 * @return
	 */
	@Authority(authType = AuthType.CREATE)
	@PostMapping
	public @ResponseBody CommonResponse<?> authSave(@RequestBody TcAuthMng tcAuthMng) {
		authMngService.saveAuth(tcAuthMng);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "권한이 등록 되었습니다.");
	}

	/**
	 * @Method Name : authDetailPage
	 * @작성일 : 2024. 1. 5.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 개발자 권한 상세
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/{authId}")
	public String authDetailSave(@PathVariable String authId, Model model) {
		TcAuthMngDetailDTO authInfo = authMngService.getAuthInfo(authId);
		model.addAttribute("authInfo", authInfo);
		return "views/developer/devAuthDetail";
	}

	/**
	 * @Method Name : authDetailPage
	 * @작성일 : 2024. 1. 5.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 개발자 권한 수정 페이지
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/update/{authId}")
	public String authUpdateSave(@PathVariable String authId, Model model) {
		TcAuthMngDetailDTO authInfo = authMngService.getAuthInfo(authId);
		model.addAttribute("authInfo", authInfo);
		return "views/developer/devAuthUpdate";
	}

	/**
	 * @Method Name : authSave
	 * @작성일 : 2024. 1. 23.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 개발자 권한 수정
	 * @param tcAuthMng
	 * @return
	 */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/{authId}")
	public @ResponseBody CommonResponse<?> authUpdate(@PathVariable String authId, @RequestBody TcAuthMng tcAuthMng) {
		authMngService.updateAuth(tcAuthMng);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("authId", authId);
		return CommonResponse.successToData(result, "권한이 수정 되었습니다.");
	}

	/**
	 * @Method Name : authDelete
	 * @작성일 : 2024. 1. 25.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 개발자 권한 삭제
	 * @param authId
	 * @return
	 */
	@Authority(authType = AuthType.DELETE)
	@DeleteMapping("/{authId}")
	public @ResponseBody CommonResponse<?> authDelete(@PathVariable String authId) {
		authMngService.deleteAuth(authId);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "권한이 삭제 되었습니다.");
	}

}
