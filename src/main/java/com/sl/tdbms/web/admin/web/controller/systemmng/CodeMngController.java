package com.sl.tdbms.web.admin.web.controller.systemmng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.tccdgrp.TcCdGrpDTO;
import com.sl.tdbms.web.admin.common.dto.tccdgrp.TcCdInfoDTO;
import com.sl.tdbms.web.admin.common.entity.TcCdGrp;
import com.sl.tdbms.web.admin.common.entity.TcCdInfo;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdGrpRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.common.repository.TcCdGrpRepository;
import com.sl.tdbms.web.admin.common.repository.TcCdInfoRepository;
import com.sl.tdbms.web.admin.web.service.systemmng.CodeMngService;
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
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;

@Controller
@RequestMapping("/systemmng/groupcode")
public class CodeMngController {

	@Autowired
	private TcCdInfoRepository tcCdInfoRepository;

	@Autowired
	private TcCdGrpRepository tcCdGrpRepository;

	@Autowired
	private QTcCdGrpRepository qTcCdGrpRepository;

	@Autowired
	private QTcCdInfoRepository qTcCdInfoRepository;

	@Autowired
	private CodeMngService codeMngService;

	/**
	 * @Method Name : groupCodeList
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 시스템 관리 공통 코드 목록 페이지
	 * @param model
	 * @param searchCommonDTO
	 * @param paging
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String codeListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TcCdGrpDTO> tcCdGrpList = qTcCdGrpRepository.getTcCdGrpList(searchCommonDTO, paging);

		long totalCnt = qTcCdGrpRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);
		model.addAttribute("tcCdGrpList", tcCdGrpList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);

		return "views/systemmng/codeGroupList";
	}
	
	/**
	 * @Method Name : systemmngGroupCodeSave
	 * @작성일 : 2024. 01. 08.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 시스템관리 공통 코드 등록 페이지
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String systemmngGroupCodeSave(Model model) {
		return "views/systemmng/modal/codeGroupSave";
	}

	/**
	 * @Method Name : groupCodeSave
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 시스템관리 공통 코드 등록
	 * @param tcCdGrp
	 * @return
	 */
	@Authority(authType = AuthType.CREATE)
	@PostMapping
	public @ResponseBody CommonResponse<?> groupCodeSave(TcCdGrp tcCdGrp) {
		String resMsg = CommonUtils.getMessage("code.codeGroupSave.regist.fail");
		try {
			tcCdGrpRepository.save(tcCdGrp);
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			resMsg = CommonUtils.getMessage("code.codeGroupSave.regist.complete");
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
	}
	
	/**
	 * @Method Name : systemmngGroupCodeDatail
	 * @작성일 : 2024. 04. 15.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 시스템관리 공통 코드 상세정보 페이지
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/{grpcdId}")
	public String systemmngGroupCodeDetail(Model model, @PathVariable String grpcdId) {
		TcCdGrp tcCdGrp = tcCdGrpRepository.findById(grpcdId).get();
		model.addAttribute("tcCdGrp", tcCdGrp);
		return "views/systemmng/modal/codeGroupDetail";
	}
	
	/**
	 * @Method Name : groupCodeUpdate
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 시스템관리 공통 코드 수정
	 * @param tcCdGrp
	 * @return
	 */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping
	public @ResponseBody CommonResponse<?> groupCodeUpdate(TcCdGrp tcCdGrp) {
		String resMsg = CommonUtils.getMessage("code.groupCodeUpdate.regist.fail");
		try {
			tcCdGrp = codeMngService.setTcCdGrpInfo(tcCdGrp);
			
			tcCdGrpRepository.save(tcCdGrp);
			resMsg = CommonUtils.getMessage("code.codeGroupDetail.update.complete");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
	}

	/**
	 * @Method Name : codeDetailList
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 시스템관리 하위 코드 목록 조회 페이지
	 * @param grpcdId
	 * @param model
	 * @param paging
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/subcode/{grpcdId}")
	public String codeDetailList(@PathVariable String grpcdId, Model model, PagingUtils paging) {
		TcCdGrp tcCdGrp = tcCdGrpRepository.findById(grpcdId).get();
		List<TcCdInfoDTO> tcCdList = qTcCdInfoRepository.getTcCdInfoList(tcCdGrp, paging);

		long totalCnt = qTcCdInfoRepository.searchCount(tcCdGrp);

		paging.setTotalCount(totalCnt);

		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("tcCdGrp", tcCdGrp);
		model.addAttribute("tcCdList", tcCdList);
		return "views/systemmng/codeGroupDetail";
	}
	
	/**
	 * @Method Name : systemmngCodeSave
	 * @작성일 : 2024. 01. 04.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 시스템관리 하위 코드 등록 페이지
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/subcode/save/{grpcdId}")
	public String systemmngCodeSave(Model model, @PathVariable String grpcdId) {
//		TcCdGrp tcCdGrp = tcCdGrpRepository.findById(grpcdId).get();
//		model.addAttribute("tcCdGrp", tcCdGrp);
		model.addAttribute("grpcdId", grpcdId);
		return "views/systemmng/modal/codeSave";
	}

	/**
	 * @Method Name : codeSave
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 시스템관리 하위 코드 등록
	 * @param tcCdInfo
	 * @return
	 */
	@Authority(authType = AuthType.CREATE)
	@PostMapping("/subcode")
	public @ResponseBody CommonResponse<?> codeSave(TcCdInfo tcCdInfo) {
		String resMsg = CommonUtils.getMessage("code.codeSave.regist.fail");
		try {
			tcCdInfoRepository.save(tcCdInfo);
			resMsg = CommonUtils.getMessage("code.codeSave.regist.complete");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
	}

	/**
	 * @Method Name : codeUpdatePage
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 시스템관리 하위 코드 상세정보 페이지
	 * @param cdId
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/subcode/{grpcdId}/update")
	public String codeUpdatePage(Model model, @PathVariable String grpcdId, @RequestParam String cdId) {
//		TcCdGrp tcCdGrp = tcCdGrpRepository.findById(grpcdId).get();
//		TcCdInfoDTO tcCdInfo = qTcCdInfoRepository.getCdInfoById(cdId);
		TcCdInfo tcCdInfo = tcCdInfoRepository.findById(cdId).get();
//		model.addAttribute("tcCdGrp", tcCdGrp);
		model.addAttribute("tcCdInfo", tcCdInfo);
		return "views/systemmng/modal/codeUpdate";
	}

	/**
	 * @Method Name : codeUpdate
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 시스템관리 하위 코드 수정
	 * @param tcCdInfo
	 * @param cdId
	 * @return
	 */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/subcode/{cdId}/update")
	public @ResponseBody CommonResponse<?> codeUpdate(TcCdInfo tcCdInfo, @PathVariable String cdId) {
		String resMsg = CommonUtils.getMessage("code.codeUpdate.update.fail");
		try {
			tcCdInfo = codeMngService.setTcCdInfo(tcCdInfo, cdId);
			
			tcCdInfoRepository.save(tcCdInfo);
			
			resMsg = CommonUtils.getMessage("code.codeUpdate.update.complete");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
			
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
	}

	/**
	 * @Method Name : deleteGroupCode
	 * @작성일 : 2024. 1. 30.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 그룹코드 삭제
	 * @param grpId
	 * @return
	 */
	@Authority(authType = AuthType.DELETE)
	@Transactional
	@DeleteMapping("/{grpId}")
	public @ResponseBody CommonResponse<?> deleteGroupCode(@PathVariable String grpId) {
		String resMsg = CommonUtils.getMessage("code.codeGroupDetail.delete.fail");
		try {
			tcCdInfoRepository.deleteAllByGrpcdId(grpId);
			tcCdGrpRepository.deleteById(grpId);
			
			resMsg = CommonUtils.getMessage("code.codeGroupDetail.delete.complete");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
	}
	
	/**
	 * @Method Name : deleteCode
	 * @작성일 : 2024. 1. 30.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 관리코드 삭제
	 * @param cdId
	 * @return
	 */
	@Authority(authType = AuthType.DELETE)
	@DeleteMapping("/subcode/delete/{cdId}")
	public @ResponseBody CommonResponse<?> deleteCode(@PathVariable String cdId) {
		String resMsg = CommonUtils.getMessage("code.codeUpdate.delete.fail");
		try {
			tcCdInfoRepository.deleteById(cdId);
			
			resMsg = CommonUtils.getMessage("code.codeUpdate.delete.complete");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
	}
}
