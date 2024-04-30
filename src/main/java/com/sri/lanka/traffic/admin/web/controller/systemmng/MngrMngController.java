package com.sri.lanka.traffic.admin.web.controller.systemmng;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.dto.mngr.TcUserMngDTO;
import com.sri.lanka.traffic.admin.common.dto.mngr.TcUserMngSaveDTO;
import com.sri.lanka.traffic.admin.common.entity.TcAuthMng;
import com.sri.lanka.traffic.admin.common.entity.TcCdInfo;
import com.sri.lanka.traffic.admin.common.entity.TcUserMng;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.GroupCode;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.enums.code.AthrztSttsCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTcCdInfoRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTcUserMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TcAuthMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TcUserMngRepository;
import com.sri.lanka.traffic.admin.common.util.LoginMngrUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.web.service.systemmng.TcUserMngService;

@Controller
@RequestMapping("/systemmng/mngr")
public class MngrMngController {

	@Autowired
	TcUserMngRepository tcUserMngRepository;

	@Autowired
	TcUserMngService tcUserMngService;

	@Autowired
	QTcUserMngRepository qTcUserMngRepository;

	@Autowired
	TcAuthMngRepository tcAuthMngRepository;

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
		
		List<TcCdInfo> bffltdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode());

		model.addAttribute("sttsCds", AthrztSttsCd.values());
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
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String mngrSavePage(Model model) {
		List<TcAuthMng> authList = tcAuthMngRepository.findAll();
		List<TcCdInfo> bffltdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode());

		model.addAttribute("authList", authList);
		model.addAttribute("bffltdList", bffltdList);
		return "views/systemmng/mngrSave";
	}

	/**
	 * @Method Name : mngrSave
	 * @작성일 : 2023. 12. 27.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 관리자 등록
	 * @param tcUserMng
	 * @return
	 * @throws Exception
	 */
	@Authority(authType = AuthType.CREATE)
	@PostMapping
	public @ResponseBody CommonResponse<?> mngrSave(TcUserMng tcUserMng) {
		tcUserMng.setRegistId(LoginMngrUtils.getUserId());
		tcUserMngService.saveTcUserMng(tcUserMng);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "관리자 정보가 등록 되었습니다.");
	}

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
		List<TcCdInfo> bffltdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode());
		
		AthrztSttsCd[] filteredValues = Arrays.stream(AthrztSttsCd.values())
								                .filter(e -> e != AthrztSttsCd.NOT_APPROVED)
								                .toArray(AthrztSttsCd[]::new);
		
		model.addAttribute("sttsCds", filteredValues);
		model.addAttribute("userInfo", tcUserMngDTO);
		model.addAttribute("bffltdList", bffltdList);

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

		TcUserMng newTcUserMng = tcUserMngService.setTcUserMngInfo(tcUserMngSaveDTO);

		tcUserMngRepository.save(newTcUserMng);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("message", "관리자 정보 변경 성공");
		return CommonResponse.successToData(result, "");
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
	@PutMapping("/pw/update/{userId}")
	public @ResponseBody CommonResponse<?> mngrPwUpdate(@PathVariable String userId,
			TcUserMngSaveDTO tcUserMngSaveDTO) {

		TcUserMng tcUserMng = tcUserMngService.changeTcUserMngAccountPwd(tcUserMngSaveDTO);
		if (tcUserMng == null) {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 201);
			result.put("message", "기존 비밀번호와 일치하지 않음");
			return CommonResponse.successToData(result, "");
		}

		tcUserMngRepository.save(tcUserMng);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("code", 200);
		result.put("message", "관리자 비밀번호 변경 성공");
		return CommonResponse.successToData(result, "");
	}

}
