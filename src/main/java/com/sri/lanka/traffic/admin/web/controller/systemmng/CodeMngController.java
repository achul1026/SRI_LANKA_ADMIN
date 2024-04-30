package com.sri.lanka.traffic.admin.web.controller.systemmng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.dto.tccdgrp.TcCdGrpDTO;
import com.sri.lanka.traffic.admin.common.entity.TcCdGrp;
import com.sri.lanka.traffic.admin.common.entity.TcCdInfo;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.querydsl.QTcCdGrpRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTcCdInfoRepository;
import com.sri.lanka.traffic.admin.common.repository.TcCdGrpRepository;
import com.sri.lanka.traffic.admin.common.repository.TcCdInfoRepository;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.web.service.systemmng.CodeMngService;

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
		tcCdGrpRepository.save(tcCdGrp);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("message", "그룹코드 등록 성공");
		return CommonResponse.successToData(result, "");
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
	@GetMapping("/{grpCdId}")
	public String systemmngGroupCodeDetail(Model model, @PathVariable String grpCdId) {
		TcCdGrp tcCdGrp = tcCdGrpRepository.findById(grpCdId).get();
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
		tcCdGrp = codeMngService.setTcCdGrpInfo(tcCdGrp);

		tcCdGrpRepository.save(tcCdGrp);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("message", "관리코드 변경 성공");
		return CommonResponse.successToData(result, "");
	}

	/**
	 * @Method Name : codeDetailList
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 시스템관리 하위 코드 목록 조회 페이지
	 * @param grpCdId
	 * @param model
	 * @param paging
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/subcode/{grpCdId}")
	public String codeDetailList(@PathVariable String grpCdId, Model model, PagingUtils paging) {
		TcCdGrp tcCdGrp = tcCdGrpRepository.findById(grpCdId).get();
		List<TcCdInfo> tcCdList = qTcCdInfoRepository.getTcCdInfoList(tcCdGrp, paging);

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
	@GetMapping("/subcode/save/{grpCdId}")
	public String systemmngCodeSave(Model model, @PathVariable String grpCdId) {
		TcCdGrp tcCdGrp = tcCdGrpRepository.findById(grpCdId).get();
		model.addAttribute("tcCdGrp", tcCdGrp);
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
		tcCdInfoRepository.save(tcCdInfo);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("message", "관리코드 등록 성공");
		return CommonResponse.successToData(result, "");
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
	@GetMapping("/subcode/{grpCdId}/update")
	public String codeUpdatePage(Model model, @PathVariable String grpCdId, @RequestParam String cdId) {
		TcCdGrp tcCdGrp = tcCdGrpRepository.findById(grpCdId).get();
		TcCdInfo tcCdInfo = tcCdInfoRepository.findById(cdId).get();
		model.addAttribute("tcCdGrp", tcCdGrp);
		model.addAttribute("tcCdInfo", tcCdInfo);
		return "views/systemmng/modal/codeUpdate";
	}

	/**
	 * @Method Name : codeUpdate
	 * @작성일 : 2024. 1. 10.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 시스템관리 하위 코드  수정
	 * @param tcCdInfo
	 * @param cdId
	 * @return
	 */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/subcode/{cdId}/update")
	public @ResponseBody CommonResponse<?> codeUpdate(TcCdInfo tcCdInfo, @PathVariable String cdId) {
		tcCdInfo = codeMngService.setTcCdInfo(tcCdInfo, cdId);

		tcCdInfoRepository.save(tcCdInfo);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("message", "관리코드 수정 성공");
		return CommonResponse.successToData(result, "");
	}

	/**
	 * @Method Name : deleteGroupCode
	 * @작성일 : 2024. 1. 30.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 그룹코드 삭제
	 * @param grpId
	 * @return
	 */
//	@Authority(authType = AuthType.DELETE)
//	@Transactional
//	@DeleteMapping("/{grpId}")
//	public @ResponseBody CommonResponse<?> deleteGroupCode(@PathVariable String grpId) {
//		tcCdInfoRepository.deleteAllByGrpcdId(grpId);
//		tcCdGrpRepository.deleteById(grpId);
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("code", 200);
//		result.put("message", "그룹코드 삭제 성공");
//		return CommonResponse.successToData(result, "");
//	}
	
	/**
	 * @Method Name : deleteCode
	 * @작성일 : 2024. 1. 30.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 관리코드 삭제
	 * @param cdId
	 * @return
	 */
//	@Authority(authType = AuthType.DELETE)
//	@DeleteMapping("/subcode/delete/{cdId}")
//	public @ResponseBody CommonResponse<?> deleteCode(@PathVariable String cdId) {
//		tcCdInfoRepository.deleteById(cdId);
//
//		Map<String, Object> result = new HashMap<String, Object>();
//		result.put("code", 200);
//		result.put("message", "관리코드 삭제 성공");
//		return CommonResponse.successToData(result, "");
//	}
}
