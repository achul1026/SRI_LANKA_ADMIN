package com.sl.tdbms.web.admin.web.controller.datalink;

import java.util.List;

import com.sl.tdbms.web.admin.common.dto.datalink.OpenApiDataDTO;
import com.sl.tdbms.web.admin.common.dto.datalink.OpenApiSearchDTO;
import com.sl.tdbms.web.admin.common.entity.TmApiSrvc;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.web.service.datalink.OpenApiService;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.GroupCode;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;

@Controller
@RequestMapping("/datalink/openapi")
public class OpenApiController {
	
	@Autowired
    CodeMngService codeMngService;
	
	@Autowired
    OpenApiService openApiService;

	@Autowired
    QTcCdInfoRepository qTcCdInfoRepository;
	
	/**
	 * @Method Name : openApiListPage
	 * @작성일 : 2024. 2. 13.
	 * @작성자 : SM.KIM
	 * @Method 설명 : OPEN API 목록 조회 페이지
	 * @param model
	 * @param searchCommonDTO
	 * @param paging
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String openApiListPage(Model model, OpenApiSearchDTO openApiSearchDTO, PagingUtils paging) {
		List<TmApiSrvc> sriOpenApiList = openApiService.getOpenApiList(openApiSearchDTO, paging);
		
		Long totalCnt = openApiService.getTotalCount(openApiSearchDTO);

		paging.setTotalCount(totalCnt); 
		
		model.addAttribute("bffltdCd", codeMngService.getTcCdInfoForGrpCd(GroupCode.BFFLTD_CD.getCode()));
		model.addAttribute("sriOpenApiList", sriOpenApiList);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", openApiSearchDTO);
		return "views/datalink/openApiList";
	}

	/**
	 * @Method Name : openApiSavePage
	 * @작성일 : 2024. 2. 13.
	 * @작성자 : SM.KIM
	 * @Method 설명 : OPEN API 등록 페이지
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String openApiSavePage(Model model) {
	 	model.addAttribute("apiSrvcClsfCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.API_SRVC_CLSF_CD.getCode()));
	 	model.addAttribute("apiItemTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.API_ITEM_TYPE_CD.getCode()));
		return "views/datalink/openApiSave";
	}

	/**
	 * @Method Name : openApiSave
	 * @작성일 : 2024. 2. 13.
	 * @작성자 : SM.KIM
	 * @Method 설명 : OPEN API 등록
	 * @param sriOpenApi
	 * @return
	 */
	@ResponseBody
	@Authority(authType = AuthType.CREATE, menuType = MenuType.SAVE)
	@PostMapping("/save")
	public CommonResponse<?> openApiSave(OpenApiDataDTO openApiRegistDTO) {
		String srvcId = null;
		try {
			srvcId = openApiService.openApiSave(openApiRegistDTO);
			String resMsg = CommonUtils.getMessage("api.openApiSave.complete");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg,srvcId);
		}catch(CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	
	/**
	 * @Method Name : openApiDetail
	 * @작성일 : 2024. 5. 17.
	 * @작성자 : TY.LEE
	 * @Method 설명 : OPEN API 상세
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/detail")
	public String openApiDetailPage(Model model ,@RequestParam(name="srvcId") String srvcId) {
		model.addAttribute("openApiDetail", openApiService.getOpenApiDetail(srvcId));
		return "views/datalink/openApiDetail";
	}
	
	/**
	 * @Method Name : openApiUpdate
	 * @작성일 : 2024. 5. 17.
	 * @작성자 : TY.LEE
	 * @Method 설명 : OPEN API 수정
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/update")
	public String openApiUpdatePage(Model model ,@RequestParam(name="srvcId") String srvcId) {
	 	model.addAttribute("apiSrvcClsfCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.API_SRVC_CLSF_CD.getCode()));
	 	model.addAttribute("apiItemTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.API_ITEM_TYPE_CD.getCode()));
 		model.addAttribute("openApiDetail", openApiService.getOpenApiDetail(srvcId));
		return "views/datalink/openApiUpdate";
	}	
	
	/**
	 * @Method Name : openApiDelete
	 * @작성일 : 2024. 6. 26.
	 * @작성자 : KY.LEE
	 * @Method 설명 :  OPEN API 삭제
	 * @param srvcId
	 */
	@Authority(authType = AuthType.DELETE)
	@DeleteMapping("/{srvcId}/delete")
	@ResponseBody
	public CommonResponse<?> openApiDelete(@PathVariable String srvcId) {
		try {
			openApiService.deleteOpenAPI(srvcId);
			String resMsg = CommonUtils.getMessage("api.openApiDelete.complete");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/**
	 * @Method Name : openApiSave
	 * @작성일 : 2024. 2. 13.
	 * @작성자 : SM.KIM
	 * @Method 설명 : OPEN API 등록
	 * @param sriOpenApi
	 * @return
	 */
	@ResponseBody
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/update")
	public CommonResponse<?> openApiUpdate(@RequestBody OpenApiDataDTO openApiRegistDTO) {
		try {
			openApiService.openApiUpdate(openApiRegistDTO);
			String resMsg = CommonUtils.getMessage("api.openApiUpdate.complete");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg, openApiRegistDTO);
		}catch(CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
