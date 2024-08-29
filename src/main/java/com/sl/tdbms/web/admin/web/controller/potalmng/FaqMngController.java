package com.sl.tdbms.web.admin.web.controller.potalmng;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.tccdgrp.TcCdInfoDTO;
import com.sl.tdbms.web.admin.common.entity.TcFaqMng;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcFaqMngRepository;
import com.sl.tdbms.web.admin.common.repository.TcFaqMngRepository;
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

import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.GroupCode;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;


@Controller
@RequestMapping("/potalmng/faq")
public class FaqMngController {
	
	@Autowired
	private QTcFaqMngRepository qTcFaqMngRepository;
	
	@Autowired
	private TcFaqMngRepository tcFaqMngRepository;
	
	@Autowired
	private QTcCdInfoRepository qTcCdInfoRepository;
	
	/**
	 * @Method Name : fqaListPage
	 * @작성일 : 2024. 5. 23.
	 * @작성자 : TY.LEE
	 * @Method 설명 : FAQ
	 * @param model
	 * @return
	 */
	
	@Authority(authType = AuthType.READ)
	@GetMapping
    public String fqaListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging){
		List<TcFaqMng> faqList =  qTcFaqMngRepository.getFaqList(searchCommonDTO, paging);
		long totalCnt = qTcFaqMngRepository.getTotalCount(searchCommonDTO);
		
		paging.setTotalCount(totalCnt);
		
		List<TcCdInfoDTO> faqCdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.FAQ_TYPE_CD.getCode());
		Collections.reverse(faqCdList);
		
		model.addAttribute("faqCdList", faqCdList);
		model.addAttribute("faqList", faqList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
        return "views/potalmng/faqList";
    }
	
	
	/**
	 * @Method Name : faqSavePage
	 * @작성일 : 2024. 5. 20.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 시설물관리 등록
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
    @GetMapping("/save") 
    public String faqSavePage(Model model){
		String mngrId = LoginMngrUtils.getUserId();
		List<TcCdInfoDTO> faqCdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.FAQ_TYPE_CD.getCode());
		Collections.reverse(faqCdList);
		
		model.addAttribute("faqCdList", faqCdList);
		model.addAttribute("writer", mngrId);
		return "views/potalmng/faqSave";
    }
	
	/**
	  * @Method Name : faqSave
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : FAQ 저장
	  * @param tcFaqMng
	  * @return
	  */
	@Authority(authType = AuthType.CREATE)
	@PostMapping("/save")
	public @ResponseBody CommonResponse<?> faqSave(TcFaqMng tcFaqMng){
		String resMsg = CommonUtils.getMessage("faq.faqSave.complete");
		try {
			tcFaqMngRepository.save(tcFaqMng);
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (Exception e) {
			resMsg = CommonUtils.getMessage("faq.faqSave.fail");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
	}
	
	/**
	  * @Method Name : faqDetailPage
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : FAQ 상세 페이지
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
    @GetMapping("/{faqId}") 
    public String faqDetailPage(Model model, @PathVariable String faqId){
		TcFaqMng faqInfo = qTcFaqMngRepository.getFaqMng(faqId);
		model.addAttribute("faqInfo", faqInfo);
		return "views/potalmng/faqDetail";
    }
	
	/**
	  * @Method Name : faqUpdatePage
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : FAQ 수정 페이지
	  * @param model
	  * @param faqId
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/{faqId}/update") 
	public String faqUpdatePage(Model model, @PathVariable String faqId){
		TcFaqMng faqInfo = qTcFaqMngRepository.getFaqMng(faqId);
		List<TcCdInfoDTO> faqCdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.FAQ_TYPE_CD.getCode());
		Collections.reverse(faqCdList);
		
		model.addAttribute("faqCdList", faqCdList);
		model.addAttribute("faqInfo", faqInfo);
		return "views/potalmng/faqUpdate";
	}
	
	/**
	  * @Method Name : faqUpdate
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : FAQ 수정
	  * @param tcFaqMng
	  * @param faqId
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@Transactional
	@PutMapping("/{faqId}")
	public @ResponseBody CommonResponse<?> faqUpdate(TcFaqMng tcFaqMng, @PathVariable String faqId){
		String resMsg = CommonUtils.getMessage("faq.faqUpdate.complete");
		try {
			tcFaqMngRepository.save(tcFaqMng);
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (Exception e) {
			resMsg = CommonUtils.getMessage("faq.faqUpdate.fail");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		}
	}
	
	/**
	  * @Method Name : faqDelete
	  * @작성일 : 2024. 7. 1.
	  * @작성자 : SM.KIM
	  * @Method 설명 : FAQ 삭제
	  * @param body
	  * @return
	  */
	@Authority(authType = AuthType.DELETE)
	@Transactional
	@DeleteMapping
	public @ResponseBody CommonResponse<?> faqDelete(@RequestBody Map<String, List<String>> body){
		String resMsg = CommonUtils.getMessage("faq.faqDelete.fail");
		List<String> faqIds = body.get("faqIds");
        for (String faqId : faqIds) {
            try {
            	tcFaqMngRepository.deleteById(faqId);
			} catch (Exception e) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.CONFLICT, resMsg);
			}
        }
        resMsg = CommonUtils.getMessage("faq.faqDelete.complete");
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
	}
	 
}

