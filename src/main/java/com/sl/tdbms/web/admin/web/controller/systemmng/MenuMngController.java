package com.sl.tdbms.web.admin.web.controller.systemmng;

import java.util.List;

import com.sl.tdbms.web.admin.common.dto.auth.TcAuthGrpDTO;
import com.sl.tdbms.web.admin.common.dto.auth.TcAuthGrpDetailDTO;
import com.sl.tdbms.web.admin.common.dto.bffltd.BffltdAuthGrpDTO;
import com.sl.tdbms.web.admin.common.dto.bffltd.BffltdAuthInfoDTO;
import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.menu.TcMenuMngDTO;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserAuthMngDTO;
import com.sl.tdbms.web.admin.common.entity.TcAuthGrp;
import com.sl.tdbms.web.admin.common.querydsl.QTcAuthGrpRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcUserMngRepository;
import com.sl.tdbms.web.admin.common.repository.TcAuthGrpRepository;
import com.sl.tdbms.web.admin.web.service.systemmng.AuthRqstMngService;
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

import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.GroupCode;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;

@Controller
@RequestMapping("/systemmng/menu")
public class MenuMngController {

	@Autowired
	private QTcAuthGrpRepository qTcAuthGrpRepository;
	
	@Autowired
	private QTcUserMngRepository qTcUserMngRepository;
	
	@Autowired
	private AuthRqstMngService authRqstMngService;
	
	@Autowired
	private TcAuthGrpRepository tcAuthGrpRepository;

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
		
		long totalCnt = 0;

	    // 리스트와 서브 리스트가 비어 있지 않은지 확인
	    if (!bffltdAuthGrpList.isEmpty() && !bffltdAuthGrpList.get(0).getSubAuthGrpList().isEmpty()) {
	    	// 리스트의 첫번째 권한 그룹 PK를 기본값으로 설정 
	    	if(CommonUtils.isNull(searchCommonDTO.getSearchType())) searchCommonDTO.setSearchType(bffltdAuthGrpList.get(0).getSubAuthGrpList().get(0).getAuthgrpId());
	    	
	        List<TcUserAuthMngDTO> userList = qTcUserMngRepository.getUserInfoByAuthgrpId(searchCommonDTO, paging);
	        totalCnt = qTcUserMngRepository.getTotalCountByAuthgrpId(searchCommonDTO);

	        paging.setTotalCount(totalCnt);
	        
	        // 선택된 소속 정보
	        BffltdAuthInfoDTO bffltdAuthInfo = bffltdAuthGrpList.stream()
	                .flatMap(dto -> dto.getSubAuthGrpList().stream()
	                    .filter(authGrpInfo -> authGrpInfo.getAuthgrpId().equals(searchCommonDTO.getSearchType()))
	                    .map(authGrpInfo -> {
	                    	BffltdAuthInfoDTO result = new BffltdAuthInfoDTO();
	                    	result.setAuthgrpId(authGrpInfo.getAuthgrpId());
	                        result.setAuthgrpNm(authGrpInfo.getAuthgrpNm());
	                        result.setBffltdNm(dto.getBffltdNm());
	                        result.setBffltdCd(dto.getBffltdCd());
	                        return result;
	                    }))
	                .findFirst().orElseThrow(() -> new CommonException(ErrorCode.EMPTY_DATA));
	        
	        
	        model.addAttribute("userList", userList);
	        model.addAttribute("bffltdAuthInfo", bffltdAuthInfo);
	    }
	    model.addAttribute("bffltdAuthGrpList", bffltdAuthGrpList);
	    model.addAttribute("totalCnt", totalCnt);
	    model.addAttribute("paging", paging);
	    model.addAttribute("searchInfo", searchCommonDTO);
		return "views/systemmng/menuList";
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
		
		SearchCommonDTO searchCommonDTO = new SearchCommonDTO();
		searchCommonDTO.setSearchType(authgrpId);
		
		TcAuthGrpDTO authInfo = qTcAuthGrpRepository.getAuthInfo(authgrpId);
		
		long totalCnt = qTcUserMngRepository.getTotalCountByAuthgrpId(searchCommonDTO);
		
		TcAuthGrp tcAuthGrp = tcAuthGrpRepository.findOneByAuthgrpId(authgrpId);
		TcAuthGrpDetailDTO authMenuInfo = authRqstMngService.getAuthInfo(tcAuthGrp.getAuthgrpId());
		
		model.addAttribute("tcAuthGrp", tcAuthGrp);
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
	@Authority(authType = AuthType.UPDATE)
	@PostMapping
	public @ResponseBody CommonResponse<?> menuGroupAuthUpdate(@RequestBody TcAuthGrp tcAuthGrp) {
		try {
			authRqstMngService.updateAuth(tcAuthGrp);
			String resMsg = CommonUtils.getMessage("menu.menuGroupAuthUpdate.authority.update.complete");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,e.getMessage());
		}
	}

}
