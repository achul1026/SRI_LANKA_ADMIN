package com.sri.lanka.traffic.admin.web.controller.datalink;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;

@Controller
@RequestMapping("/datalink/openapi")
public class OpenApiController {

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
	public String openApiListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		java.util.List<String> sriOpenApiList = null;
//		qSriOpenApiRepository.getOpenApiList(searchCommonDTO, paging);

		long totalCnt = 1;
//		qSriOpenApiRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);
		model.addAttribute("sriOpenApiList", sriOpenApiList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
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
	public String openApiSavePage() {
		return "views/datalink/modal/openApiSave";
	}

	/**
	 * @Method Name : openApiSave
	 * @작성일 : 2024. 2. 13.
	 * @작성자 : SM.KIM
	 * @Method 설명 : OPEN API 등록
	 * @param sriOpenApi
	 * @return
	 */

	/**
	 * @Method Name : openApiDelete
	 * @작성일 : 2024. 2. 13.
	 * @작성자 : SM.KIM
	 * @Method 설명 : OPEN API 삭제
	 * @param body
	 * @return
	 */

	/**
	 * @Method Name : openApiUpdatePage
	 * @작성일 : 2024. 2. 13.
	 * @작성자 : SM.KIM
	 * @Method 설명 : OPEN API 수정 페이지
	 * @param model
	 * @param apiId
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/{apiId}/update")
	public String openApiUpdatePage(Model model, @PathVariable("apiId") String apiId) {
//		Optional<SriOpenApi> sriOpenApi = sriOpenApiRepository.findById(apiId);
//		if (sriOpenApi.isEmpty()) {
//			// 예외처리
//		}
//		model.addAttribute("sriOpenApi", sriOpenApi.get());

		return "views/datalink/modal/openApiUpdate";
	}

	/**
	 * @Method Name : openApiUpdate
	 * @작성일 : 2024. 2. 13.
	 * @작성자 : SM.KIM
	 * @Method 설명 : OPEN API 수정
	 * @param sriOpenApi
	 * @return
	 */

}
