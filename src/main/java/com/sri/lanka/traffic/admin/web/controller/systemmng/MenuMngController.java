package com.sri.lanka.traffic.admin.web.controller.systemmng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sri.lanka.traffic.admin.common.dto.auth.TcAuthGrpDTO;
import com.sri.lanka.traffic.admin.common.dto.auth.TcAuthMngDetailDTO;
import com.sri.lanka.traffic.admin.common.dto.bffltd.BffltdAuthGrpDTO;
import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.dto.menu.TcMenuMngDTO;
import com.sri.lanka.traffic.admin.common.dto.mngr.TcUserAuthMngDTO;
import com.sri.lanka.traffic.admin.common.entity.TcAuthMng;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.GroupCode;
import com.sri.lanka.traffic.admin.common.querydsl.QTcAuthGrpRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTcUserMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TcAuthMngRepository;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.web.service.developer.DevAuthMngService;

@Controller
@RequestMapping("/systemmng/menu")
public class MenuMngController {

	@Autowired
	private QTcAuthGrpRepository qTcAuthGrpRepository;
	
	@Autowired
	private QTcUserMngRepository qTcUserMngRepository;
	
	@Autowired
	private DevAuthMngService authMngService;
	
	@Autowired
	private TcAuthMngRepository tcAuthMngRepository;

	/**
	 * @Method Name : menuList
	 * @작성일 : 2024. 1. 3.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 메뉴 권한 목록 페이지
	 * @param model
	 * @param tcMenuMngDTO
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String menuListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<BffltdAuthGrpDTO> bffltdAuthGrpList = qTcAuthGrpRepository.getBffltdAuthGrpList(GroupCode.BFFLTD_CD.getCode());
		
		// 리스트의 첫번째 권한 그룹 PK를 기본값으로 설정 
		String authgrpId = bffltdAuthGrpList.get(0).getSubAuthGrpList().get(0).getAuthgrpId();
		
		List<TcUserAuthMngDTO> userList = qTcUserMngRepository.getUserInfoByAuthgrpId(authgrpId, searchCommonDTO, paging);
		
		long totalCnt = qTcUserMngRepository.getTotalCountByAuthgrpId(authgrpId, searchCommonDTO);
		
		paging.setTotalCount(totalCnt);
		
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("userList", userList);
		model.addAttribute("bffltdAuthGrpList", bffltdAuthGrpList);
		return "views/systemmng/menuList";
	}
	
	/**
	  * @Method Name : menuAsynchronousList
	  * @작성일 : 2024. 4. 18.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 권한 유형에 대한 유저 정보 목록 조회
	  * @param searchCommonDTO
	  * @param paging
	  * @param authgrpId
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/asynchronous")
	public @ResponseBody CommonResponse<?> menuAsynchronousList(SearchCommonDTO searchCommonDTO, PagingUtils paging, String authgrpId) {
		
		List<TcUserAuthMngDTO> userList = qTcUserMngRepository.getUserInfoByAuthgrpId(authgrpId, searchCommonDTO, paging);
		
		long totalCnt = qTcUserMngRepository.getTotalCountByAuthgrpId(authgrpId, searchCommonDTO);
		
		paging.setTotalCount(totalCnt);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("userList", userList);
		result.put("totalCnt", totalCnt);
		result.put("paging", paging);
		
		return CommonResponse.successToData(result,"");
	}

	/**
	 * @Method Name : menuGroupAuthUpdate
	 * @작성일 : 2024. 04. 04.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 그룹 유형 권한 설정 페이지
	 * @param model
	 * @return
	 */

	@Authority(authType = AuthType.READ)
	@GetMapping("/group/update/{authgrpId}")
	public String menuGroupAuthUpdate(Model model, @PathVariable String authgrpId, TcMenuMngDTO tcMenuMngDTO) {
		
		TcAuthGrpDTO authInfo = qTcAuthGrpRepository.getAuthInfo(authgrpId);
		
		long totalCnt = qTcUserMngRepository.getTotalCountByAuthgrpId(authgrpId, null);
		
		TcAuthMng tcAuthMng = tcAuthMngRepository.findByAuthgrpId(authgrpId).get();
		TcAuthMngDetailDTO authMenuInfo = authMngService.getAuthInfo(tcAuthMng.getAuthId());
		
		model.addAttribute("tcAuthMng", tcAuthMng);
		model.addAttribute("authMenuInfo", authMenuInfo);
		model.addAttribute("authInfo", authInfo);
		model.addAttribute("totalCnt", totalCnt);
		return "views/systemmng/modal/menuGroupAuthUpdate";
	}
	
	/**
	  * @Method Name : menuGroupAuthUpdate
	  * @작성일 : 2024. 4. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 그룹 유형 권한 설정
	  * @param tcAuthMng
	  * @return
	  */
	@Authority(authType = AuthType.CREATE)
	@PostMapping
	public @ResponseBody CommonResponse<?> menuGroupAuthUpdate(@RequestBody TcAuthMng tcAuthMng) {
		authMngService.updateAuth(tcAuthMng);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "권한이 등록 되었습니다.");
	}

	/**
	 * @Method Name : menuAuthUpdate
	 * @작성일 : 2024. 04. 04.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 개인 권한 설정 페이지
	 * @param model
	 * @return
	 */
//	@Authority(authType = AuthType.READ)
//	@GetMapping("/update")
//	public String menuAuthUpdate(Model model) {
//		return "views/systemmng/modal/menuAuthUpdate";
//	}

}
