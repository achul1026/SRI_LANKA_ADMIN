package com.sl.tdbms.web.admin.web.controller.systemmng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sl.tdbms.web.admin.common.dto.auth.TcAuthGrpDTO;
import com.sl.tdbms.web.admin.common.dto.bffltd.BffltdAuthInfoDTO;
import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserRqstAuthDTO;
import com.sl.tdbms.web.admin.common.dto.tccdgrp.TcCdInfoDTO;
import com.sl.tdbms.web.admin.common.entity.TcAuthGrp;
import com.sl.tdbms.web.admin.common.enums.code.RqstSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.QTcAuthGrpRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcMenuMngRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTmAuthRqstRepository;
import com.sl.tdbms.web.admin.common.repository.TcAuthGrpRepository;
import com.sl.tdbms.web.admin.web.service.systemmng.AuthRqstMngService;
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
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;

@Controller
@RequestMapping("/systemmng/auth")
public class AuthRqstMngController {

	@Autowired
    QTcMenuMngRepository qTcMenuMngRepository;
	
	@Autowired
    QTmAuthRqstRepository qTmAuthRqstRepository;
	
	@Autowired
    QTcAuthGrpRepository qTcAuthGrpRepository;
	
	@Autowired
    QTcCdInfoRepository qTcCdInfoRepository;
	
	@Autowired
    TcAuthGrpRepository tcAuthGrpRepository;
	
	@Autowired
    AuthRqstMngService authRqstMngService;
	
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
		
		List<BffltdAuthInfoDTO> authGrpsList = qTcAuthGrpRepository.getAuthGrpListByBfflt(LoginMngrUtils.getTcUserMngInfo().getUserBffltd());

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
	  * @param authgrpId
	  * @param authrqstId
	  * @param isApproval
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@PostMapping
	public @ResponseBody CommonResponse<?> authRqstApprovalProcess(Model model, @RequestParam String authgrpId
																	, @RequestParam String userId, @RequestParam String authrqstId
																	, @RequestParam boolean isApproval) {
		String resMsg;
		
		try {
			authRqstMngService.setUserAuth(authgrpId, userId, authrqstId, isApproval);
			
			resMsg = isApproval ? 
					CommonUtils.getMessage("authRqst.authRqstDetail.approval.success") 
					: CommonUtils.getMessage("authRqst.authRqstDetail.reject.success");
			Map<String, Object> result = new HashMap<>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/**
	  * @Method Name : authSetting
	  * @작성일 : 2024. 4. 25.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시스템관리 요청 권한 관리 권한 설정 모달 페이지
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/setting")
	public String authSetting(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		
		List<TcAuthGrpDTO> authList = qTcAuthGrpRepository.getAuthList(searchCommonDTO, paging);
		
		long totalCnt = qTcAuthGrpRepository.getTotalCount(searchCommonDTO);
		
		paging.setTotalCount(totalCnt);
		
		List<TcCdInfoDTO> bffltdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode());
		
		model.addAttribute("userInfo", LoginMngrUtils.getTcUserMngInfo());
		model.addAttribute("bffltdList", bffltdList);
		model.addAttribute("authList", authList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
		return "views/systemmng/authSetting";
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
//	@Authority(authType = AuthType.READ)
//	@GetMapping("/asynchronous")
//	public @ResponseBody CommonResponse<?> authAsynchronousSetting(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
//		paging.setLimitCount(3);
//		paging.setPageSize(3);
//		List<TcAuthGrpDTO> authList = qTcAuthGrpRepository.getAuthList(searchCommonDTO, paging);
//		List<TcCdInfoDTO> bffltdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode());
//		
//		long totalCnt = qTcAuthGrpRepository.getTotalCount(searchCommonDTO);
//		
//		paging.setTotalCount(totalCnt);
//		
//		Map<String, Object> result = new HashMap<String, Object>();
//		
//		result.put("authList", authList);
//		result.put("bffltdList", bffltdList);
//		result.put("totalCnt", totalCnt);
//		result.put("paging", paging);
//		
//		return CommonResponse.successToData(result,"");
//	}
	
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
	public @ResponseBody CommonResponse<?> authSave(Model model, TcAuthGrp tcAuthGrp) {
		try {
			LoginMngrDTO loginUser = LoginMngrUtils.getTcUserMngInfo();
			if (!(loginUser.getUserType() == UserTypeCd.SUPER)
//				&& !(loginUser.getUserBffltd().equals("BFC001"))
					) {
//		if (CommonUtils.isNull(tcAuthGrp.getBffltdCd())) {
				tcAuthGrp.setBffltdCd(loginUser.getUserBffltd());
			}
			authRqstMngService.checkDuplicateAuth(tcAuthGrp.getAuthgrpId(), tcAuthGrp.getAuthgrpNm());
			authRqstMngService.saveAuthGrp(tcAuthGrp);
			
			String resMsg = CommonUtils.getMessage("authRqst.authSetting.success");
			Map<String, Object> result = new HashMap<>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
			
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/**
	  * @Method Name : authUpdate
	  * @작성일 : 2024. 4. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시스템관리 요청 관한 관리 권한 수정
	  * @param authgrpId
	  * @param tcAuthMng
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/setting/{authgrpId}")
	public @ResponseBody CommonResponse<?> authUpdate(@PathVariable String authgrpId, TcAuthGrp tcAuthGrp) {
		try {
			authRqstMngService.checkDuplicateAuth(authgrpId, tcAuthGrp.getAuthgrpNm());
			
			tcAuthGrpRepository.save(authRqstMngService.setAuthgrp(authgrpId, tcAuthGrp));
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("authgrpId", authgrpId);
			String resMsg = CommonUtils.getMessage("authRqst.authSetting.update.success");
			return CommonResponse.successToData(result, resMsg);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/**
	  * @Method Name : authDelete
	  * @작성일 : 2024. 4. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시스템관리 요청 권한 관리 권한 삭제
	  * @param authgrpId
	  * @param tcAuthMng
	  * @return
	  */
	@Authority(authType = AuthType.DELETE)
	@DeleteMapping("/setting/{authgrpId}")
	public @ResponseBody CommonResponse<?> authDelete(@PathVariable String authgrpId, TcAuthGrp tcAuthGrp) {
		try {
			authRqstMngService.deleteAuth(authgrpId);
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, CommonUtils.getMessage("authRqst.authSetting.delete.success"));
		} catch (CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
