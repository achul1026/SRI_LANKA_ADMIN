package com.sl.tdbms.web.admin.web.controller.systemmng;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserMngDTO;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserMngSaveDTO;
import com.sl.tdbms.web.admin.common.dto.tccdgrp.TcCdInfoDTO;
import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.GroupCode;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.common.enums.code.AthrztSttsCd;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcUserMngRepository;
import com.sl.tdbms.web.admin.common.repository.TcAuthGrpRepository;
import com.sl.tdbms.web.admin.common.repository.TcUserMngRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.web.service.systemmng.TcUserMngService;

@Controller
@RequestMapping("/systemmng/mngr")
public class MngrMngController {

	@Autowired
    TcUserMngRepository tcUserMngRepository;

	@Autowired
    TcUserMngService tcUserMngService;
	
	@Autowired
    TcAuthGrpRepository tcAuthGrpRepository;

	@Autowired
    QTcUserMngRepository qTcUserMngRepository;

	@Autowired
    QTcCdInfoRepository qTcCdInfoRepository;

	/**
	 * @Method Name : mngrListPage
	 * @작성일 : 2024. 1. 3.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 관리자 관리 목록
	 * @param model
	 * @param searchCommonDTO
	 * @param paging
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String mngrListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {

		List<TcUserMngDTO> userList = qTcUserMngRepository.getTcUserList(searchCommonDTO, paging);

		long totalCnt = qTcUserMngRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);
		
		List<TcCdInfoDTO> bffltdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode());

		model.addAttribute("sttsCds", AthrztSttsCd.values());
		model.addAttribute("loginUser", LoginMngrUtils.getTcUserMngInfo());
		model.addAttribute("bffltdList", bffltdList);
		model.addAttribute("userList", userList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);

		return "views/systemmng/mngrList";
	}

	/**
	 * @Method Name : mngrSavePage
	 * @작성일 : 2024. 1. 3.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 관리자 등록 페이지
	 * @param model
	 * @return
	 */
//	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
//	@GetMapping("/save")
//	public String mngrSavePage(Model model) {
//		List<TcAuthGrp> authList = tcAuthGrpRepository.findAll();
//		List<TcCdInfoDTO> bffltdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode());
//
//		model.addAttribute("authList", authList);
//		model.addAttribute("bffltdList", bffltdList);
//		return "views/systemmng/mngrSave";
//	}

	/**
	 * @Method Name : mngrSave
	 * @작성일 : 2023. 12. 27.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 관리자 등록
	 * @param tcUserMng
	 * @return
	 * @throws Exception
	 */
//	@Authority(authType = AuthType.CREATE)
//	@PostMapping
//	public @ResponseBody CommonResponse<?> mngrSave(TcUserMng tcUserMng) {
//		tcUserMng.setRegistId(LoginMngrUtils.getUserId());
//		tcUserMngService.saveTcUserMng(tcUserMng);
//		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "관리자 정보가 등록 되었습니다.");
//	}

	/**
	 * @Method Name : mngrDetailPage
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 관리자 상세 페이지
	 * @param usermngId
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/{usermngId}")
	public String mngrDetailPage(@PathVariable String usermngId, Model model) {
		TcUserMngDTO tcUserMngDTO = qTcUserMngRepository.getTcUserInfo(usermngId);
		model.addAttribute("userInfo", tcUserMngDTO);
		return "views/systemmng/mngrDetail";
	}

	/**
	 * @Method Name : mngrModifyPage
	 * @작성일 : 2024. 1. 4.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 관리자 수정 페이지
	 * @param usermngId
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/{usermngId}/update")
	public String mngrModifyPage(@PathVariable String usermngId, Model model) {
		TcUserMngDTO tcUserMngDTO = qTcUserMngRepository.getTcUserInfo(usermngId);
		List<TcCdInfoDTO> bffltdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode());
		List<TcCdInfoDTO> deptList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.DEPT_CD.getCode());
		
		AthrztSttsCd[] filteredValues = Arrays.stream(AthrztSttsCd.values())
								                .filter(e -> e != AthrztSttsCd.NOT_APPROVED)
								                .toArray(AthrztSttsCd[]::new);
		
		model.addAttribute("sttsCds", filteredValues);
		model.addAttribute("userInfo", tcUserMngDTO);
		model.addAttribute("bffltdList", bffltdList);
		model.addAttribute("deptList", deptList);

		return "views/systemmng/mngrUpdate";
	}

	/**
	 * @Method Name : mngrUpdate
	 * @작성일 : 2024. 1. 26.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 관리자 수정
	 * @param tcUserMngSaveDTO
	 * @return
	 */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping
	public @ResponseBody CommonResponse<?> mngrUpdate(TcUserMngSaveDTO tcUserMngSaveDTO) {

		try {
			tcUserMngService.setTcUserMngInfo(tcUserMngSaveDTO);
			
			String resMsg = CommonUtils.getMessage("mngr.mngrUpdate.update.complete");
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * @Method Name : mngrPwUpdatePage
	 * @작성일 : 2024. 01. 04.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 시스템관리 관리자수정 비밀번호 변경 페이지
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/pw/update")
	public String tcUserMngPwUpdatePage(Model model) {
		return "views/systemmng/modal/mngrPwUpdate";
	}

	/**
	 * @Method Name : mngrPwUpdate
	 * @작성일 : 2024. 1. 30.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 관리자 비밀번호 변경
	 * @param userId
	 * @param tcUserMngSaveDTO
	 * @return
	 */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/pw/update")
	public @ResponseBody CommonResponse<?> mngrPwUpdate(TcUserMngSaveDTO tcUserMngSaveDTO) {

		try {
			tcUserMngService.changeTcUserMngAccountPwd(tcUserMngSaveDTO);
			
			String resMsg = CommonUtils.getMessage("mngr.mngrPwUpdate.complete");
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/**
	 * @Method Name : mngrPwRequest
	 * @작성일 : 2024. 5. 17
	 * @작성자 : TY.LEE
	 * @Method 설명 : 비밀번호 초기화 요청
	 * @param model
	 * @return
	 */
//	@GetMapping("/pw/request")
//	public String loginIdFindResult(Model model) {
//		return "views/systemmng/modal/mngrPwRequest";
//	}
	
	/**
	  * @Method Name : accountApproval
	  * @작성일 : 2024. 5. 24.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 관리자 승인
	  * @param tcUserMngSaveDTO
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/approval")
	public @ResponseBody CommonResponse<?> accountApproval(TcUserMngSaveDTO tcUserMngSaveDTO) {
		
		try {
			tcUserMngService.accountApproval(tcUserMngSaveDTO);
			
			String resMsg = CommonUtils.getMessage("mngr.accountApproval.complete");
			
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
}
