package com.sri.lanka.traffic.admin.web.controller.developer;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sri.lanka.traffic.admin.common.dto.auth.TcAuthGrpDTO;
import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.entity.TcAuthGrp;
import com.sri.lanka.traffic.admin.common.entity.TcCdInfo;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.GroupCode;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.querydsl.QTcAuthGrpRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTcCdInfoRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTcMenuMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TcAuthGrpRepository;
import com.sri.lanka.traffic.admin.common.repository.TcAuthMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TcCdInfoRepository;
import com.sri.lanka.traffic.admin.common.repository.TcMenuAuthRepository;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.web.service.developer.DevAuthMngService;

@Controller
@RequestMapping("/developer/authgrp")
public class DevAuthGrpController {
	
	@Autowired
	QTcAuthGrpRepository qTcAuthGrpRepository;
	
	@Autowired
	TcAuthGrpRepository tcAuthGrpRepository;
	
	@Autowired
	QTcCdInfoRepository qTcCdInfoRepository;
	
	@Autowired
	QTcMenuMngRepository qTcMenuMngRepository;
	
	@Autowired
	TcCdInfoRepository tcCdInfoRepository;
	
	@Autowired
	DevAuthMngService authMngService;
	
	@Autowired
	TcAuthMngRepository tcAuthMngRepository;
	
	@Autowired
	TcMenuAuthRepository tcMenuAuthRepository;

	/**
	 * @Method Name : authListPage
	 * @작성일 : 2024. 1. 3.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 개발자 권한 관리 목록 페이지
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String authList(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		
		List<TcAuthGrpDTO> authList = qTcAuthGrpRepository.getAuthList(searchCommonDTO, paging);
		
		long totalCnt = qTcAuthGrpRepository.getTotalCount(searchCommonDTO);
		
		paging.setTotalCount(totalCnt);
		
		model.addAttribute("authList", authList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
		
		return "views/developer/devAuthGrpList";
	}

	/**
	 * @Method Name : authSavePage
	 * @작성일 : 2024. 1. 5.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 개발자 권한 관리 등록 페이지
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String authListSave(Model model) {
		List<TcCdInfo> bffltdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode());
		model.addAttribute("bffltdList", bffltdList);
		return "views/developer/devAuthGrpSave";
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
	public @ResponseBody CommonResponse<?> authSave(TcAuthGrp tcAuthGrp) {
		authMngService.saveAuthByAuthGrp(tcAuthGrp, null);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "권한이 등록 되었습니다.");
	}

	/**
	 * @Method Name : authDetailPage
	 * @작성일 : 2024. 1. 5.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 개발자 권한 상세 페이지
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/{authgrpId}")
	public String authDetailSave(@PathVariable String authgrpId, Model model) {
		TcAuthGrpDTO authInfo = qTcAuthGrpRepository.getAuthInfo(authgrpId);
		model.addAttribute("authInfo", authInfo);
		return "views/developer/devAuthGrpDetail";
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
	@GetMapping("/update/{authgrpId}")
	public String authUpdateSave(@PathVariable String authgrpId, Model model) {
		TcAuthGrpDTO authInfo = qTcAuthGrpRepository.getAuthInfo(authgrpId);
		List<TcCdInfo> bffltdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode());
		model.addAttribute("bffltdList", bffltdList);
		model.addAttribute("authInfo", authInfo);
		return "views/developer/devAuthGrpUpdate";
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
	@PutMapping("/{authgrpId}")
	public @ResponseBody CommonResponse<?> authUpdate(TcAuthGrp tcAuthGrp) {
		tcAuthGrpRepository.save(tcAuthGrp);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "권한이 수정 되었습니다.");
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
	@DeleteMapping("/{authgrpId}")
	@Transactional
	public @ResponseBody CommonResponse<?> authDelete(@PathVariable String authgrpId) {
		tcAuthGrpRepository.deleteById(authgrpId);
		String authId = tcAuthMngRepository.findByAuthgrpId(authgrpId).get().getAuthId();
		tcAuthMngRepository.deleteAllByAuthgrpId(authgrpId);
		tcMenuAuthRepository.deleteAllByAuthId(authId);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "권한이 삭제 되었습니다.");
	}

}
