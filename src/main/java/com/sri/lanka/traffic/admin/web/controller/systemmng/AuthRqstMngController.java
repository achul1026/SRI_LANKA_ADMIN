package com.sri.lanka.traffic.admin.web.controller.systemmng;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sri.lanka.traffic.admin.common.dto.auth.TcAuthMngDTO;
import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.dto.mngr.TcUserRqstAuthDTO;
import com.sri.lanka.traffic.admin.common.entity.TcAuthGrp;
import com.sri.lanka.traffic.admin.common.entity.TcAuthMng;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.code.RqstSttsCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTcAuthMngRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTcMenuMngRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTmAuthRqstRepository;
import com.sri.lanka.traffic.admin.common.repository.TcAuthGrpRepository;
import com.sri.lanka.traffic.admin.common.repository.TcAuthMngRepository;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.web.service.developer.DevAuthMngService;
import com.sri.lanka.traffic.admin.web.service.systemmng.AuthRqstMngService;

@Controller
@RequestMapping("/systemmng/auth")
public class AuthRqstMngController {

	@Autowired
	QTcAuthMngRepository qTcAuthMngRepository;

	@Autowired
	QTcMenuMngRepository qTcMenuMngRepository;

	@Autowired
	DevAuthMngService authMngService;
	
	@Autowired
	QTmAuthRqstRepository qTmAuthRqstRepository;
	
	@Autowired
	TcAuthGrpRepository tcAuthGrpRepository;
	
	@Autowired
	AuthRqstMngService authRqstMngService;

	@Autowired
	TcAuthMngRepository tcAuthMngRepository;
	
	@Autowired
	DevAuthMngService devAuthMngService;
	
	/**
	 * @Method Name : authListPage
	 * @작성일 : 2024. 1. 3.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 시스템관리 요청 권한 관리 목록 페이지
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String authRqstListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {

		List<TcUserRqstAuthDTO> rqstAuthList = qTmAuthRqstRepository.getRqstAuthList(searchCommonDTO, paging);

		long totalCnt = qTmAuthRqstRepository.getTotalCount(searchCommonDTO);
		
		paging.setTotalCount(totalCnt);
		
		List<TcAuthGrp> authGrpsList = tcAuthGrpRepository.findAll();

		model.addAttribute("rqstAuthList", rqstAuthList);
		model.addAttribute("authGrpsList", authGrpsList);
		model.addAttribute("rqstSttsCd", RqstSttsCd.values());
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);

		return "views/systemmng/authRqstList";
	}

	/**
	 * @Method Name : authDetail
	 * @작성일 : 2024. 04. 03.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 시스템관리 요청 권한 관리 상세 페이지
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/{authrqstId}")
	public String authRqstDetail(Model model, @PathVariable String authrqstId) {
		TcUserRqstAuthDTO tmAuthRqstInfo = qTmAuthRqstRepository.getRqstAuthInfo(authrqstId);
		model.addAttribute("tmAuthRqstInfo", tmAuthRqstInfo);
		return "views/systemmng/modal/authRqstDetail";
	}
	
	/**
	  * @Method Name : authRqstApprovalProcess
	  * @작성일 : 2024. 4. 18.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시스템관리 요청 권한 관리 승인 또는 반려
	  * @param model
	  * @param authId
	  * @param authrqstId
	  * @param isApproval
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@PostMapping
	public @ResponseBody CommonResponse<?> authRqstApprovalProcess(Model model, @RequestParam String authId
																	, @RequestParam String userId, @RequestParam String authrqstId
																	, @RequestParam boolean isApproval) {
		authRqstMngService.setUserAuth(authId, userId, authrqstId, isApproval);
		
		String message = isApproval ? "요청 승인 성공" : "요청 거절 성공";
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("message", message);
		return CommonResponse.successToData(result, "");
	}
	
	/**
	  * @Method Name : authSetting
	  * @작성일 : 2024. 4. 25.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시스템관리 요청 권한 관리 권한 설정 모달 페이지
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/setting")
	public String authSetting(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		paging.setLimitCount(3);
		paging.setPageSize(3);
		List<TcAuthMngDTO> authList = qTcAuthMngRepository.getAuthList(searchCommonDTO, paging);
		
		long totalCnt = qTcAuthMngRepository.getTotalCount(searchCommonDTO);
		
		paging.setTotalCount(totalCnt);
		
		model.addAttribute("authList", authList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		return "views/systemmng/modal/authSetting";
	}
	
	/**
	  * @Method Name : authAsynchronousSetting
	  * @작성일 : 2024. 4. 25.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 권한 목록 비동기 조회
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/asynchronous")
	public @ResponseBody CommonResponse<?> authAsynchronousSetting(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		paging.setLimitCount(3);
		paging.setPageSize(3);
		List<TcAuthMngDTO> authList = qTcAuthMngRepository.getAuthList(searchCommonDTO, paging);
		
		long totalCnt = qTcAuthMngRepository.getTotalCount(searchCommonDTO);
		
		paging.setTotalCount(totalCnt);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("authList", authList);
		result.put("totalCnt", totalCnt);
		result.put("paging", paging);
		
		return CommonResponse.successToData(result,"");
	}
	
	/**
	  * @Method Name : authSave
	  * @작성일 : 2024. 4. 25.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시스템관리 요청 권한 관리 권한 등록
	  * @param model
	  * @param tcAuthMng
	  * @return
	  */
	@Authority(authType = AuthType.CREATE)
	@PostMapping("/setting")
	public @ResponseBody CommonResponse<?> authSave(Model model, TcAuthMng tcAuthMng) {
		devAuthMngService.saveAuthByAuthGrp(null, tcAuthMng);
		
		Map<String, Object> result = new HashMap<>();
		result.put("code", 200);
		result.put("message", "권한 등록 성공");
		return CommonResponse.successToData(result, "");
	}
	
	/**
	  * @Method Name : authUpdate
	  * @작성일 : 2024. 4. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시스템관리 요청 관한 관리 권한 수정
	  * @param authId
	  * @param tcAuthMng
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/setting/{authId}")
	public @ResponseBody CommonResponse<?> authUpdate(@PathVariable String authId, TcAuthMng tcAuthMng) {
		
		tcAuthMngRepository.save(tcAuthMng);
		
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("authId", authId);
		return CommonResponse.successToData(result, "권한이 수정 되었습니다.");
	}
	
	/**
	  * @Method Name : authDelete
	  * @작성일 : 2024. 4. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시스템관리 요청 권한 관리 권한 삭제
	  * @param authId
	  * @param tcAuthMng
	  * @return
	  */
	@Authority(authType = AuthType.DELETE)
	@DeleteMapping("/setting/{authId}")
	public @ResponseBody CommonResponse<?> authDelete(@PathVariable String authId, TcAuthMng tcAuthMng) {
		authMngService.deleteAuth(authId);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "권한이 삭제 되었습니다.");
	}

}
