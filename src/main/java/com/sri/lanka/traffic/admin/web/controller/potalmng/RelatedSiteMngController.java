package com.sri.lanka.traffic.admin.web.controller.potalmng;

import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.entity.TcSiteMng;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.querydsl.QTcSiteMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TcSiteMngRepository;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.web.service.potalmng.RelatedSiteMngService;

@Controller
@RequestMapping("/potalmng/relatedsite")
public class RelatedSiteMngController {
	
	@Autowired
	private QTcSiteMngRepository qTcSiteMngRepository;
	
	@Autowired
	private TcSiteMngRepository tcSiteMngRepository;
	
	@Autowired
	private RelatedSiteMngService relatedSiteMngService;
	
	/**
	  * @Method Name : siteListPage
	  * @작성일 : 2024. 1. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 관련사이트 목록 화면
	  * @param model
	  * @param tlBbsInfo
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String siteListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TcSiteMng> tcSiteMngList = qTcSiteMngRepository.getSiteList(searchCommonDTO, paging);
		long totalCnt = qTcSiteMngRepository.getTotalCount(searchCommonDTO);
		
		paging.setTotalCount(totalCnt);

		model.addAttribute("tcSiteMngList", tcSiteMngList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
		
		return "views/potalmng/relatedSiteList";
	}
	
	/**
	  * @Method Name : potalmngSiteSave
	  * @작성일 : 2024. 1. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 관련사이트 등록 페이지
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String potalmngSiteSave() {
		return "views/potalmng/modal/relatedSiteSave";
	}
	
	/**
	  * @Method Name : siteSave
	  * @작성일 : 2024. 1. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 관련사이트 등록
	  * @param tlBbsInfo
	  * @return
	  */
	@Authority(authType = AuthType.CREATE)
	@PostMapping("/save")
	public @ResponseBody CommonResponse<?> siteSave(TcSiteMng tcSiteMng){
		tcSiteMngRepository.save(tcSiteMng);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "관련사이트가 등록 되었습니다.");
	}
	
	/**
	  * @Method Name : potalmngSiteUpdate
	  * @작성일 : 2024. 1. 22.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 관련사이트 수정 페이지
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/{siteId}")
	public String potalmngSiteDetail(Model model, @PathVariable String siteId) {
		Optional<TcSiteMng> findSite = tcSiteMngRepository.findById(siteId);
		
		model.addAttribute("tcSiteMng", findSite.get());
		
		return "views/potalmng/modal/relatedSiteUpdate";
	}
	
	/**
	  * @Method Name : siteUpdate
	  * @작성일 : 2024. 1. 23.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 관련사이트 수정
	  * @param tlBbsInfo
	  * @param brdId
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/{siteId}")
	public @ResponseBody CommonResponse<?> siteUpdate(TcSiteMng tcSiteMng, @PathVariable String siteId){
		tcSiteMng = relatedSiteMngService.setSiteInfo(tcSiteMng, siteId);
		
		tcSiteMngRepository.save(tcSiteMng);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "관련사이트가 수정 되었습니다.");
	}
	
	/**
	  * @Method Name : siteDelete
	  * @작성일 : 2024. 1. 23.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 포탈관리 관련사이트 선택 삭제
	  * @param body
	  * @return
	  */
	@Authority(authType = AuthType.DELETE)
	@DeleteMapping
	public @ResponseBody CommonResponse<?> siteDelete(@RequestBody Map<String, List<String>> body){
		List<String> siteIds = body.get("siteIds");
        for (String siteId : siteIds) {
        	tcSiteMngRepository.deleteById(siteId);
        }
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "관련사이트가 삭제 되었습니다.");
	}
}
