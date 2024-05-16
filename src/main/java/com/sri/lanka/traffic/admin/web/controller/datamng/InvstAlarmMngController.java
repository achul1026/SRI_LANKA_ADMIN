package com.sri.lanka.traffic.admin.web.controller.datamng;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

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
import com.sri.lanka.traffic.admin.common.entity.TlBbsInfo;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.enums.code.BbsTypeCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTlBbsInfoRepository;
import com.sri.lanka.traffic.admin.common.repository.TlBbsInfoRepository;
import com.sri.lanka.traffic.admin.common.util.LoginMngrUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.web.service.potalmng.PotalMngService;


@Controller
@RequestMapping("/datamng/alarm")
public class InvstAlarmMngController {
	
	@Autowired
	private QTlBbsInfoRepository qTlBbsInfoRepository;
	
	@Autowired
	private TlBbsInfoRepository tlBbsInfogRepository;
	
	@Autowired
	private PotalMngService potalMngService;

	/**
	  * @Method Name : invstAlarmListPage
	  * @작성일 : 2024. 3. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 데이터 관리 알림 목록 조회
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String invstAlarmListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		searchCommonDTO.setSearchType(BbsTypeCd.ALARM.getCode());
		
		List<TlBbsInfo> sriAlarmList = qTlBbsInfoRepository.getBbsList(searchCommonDTO, paging);
		
		long totalCnt = qTlBbsInfoRepository.getTotalCount(searchCommonDTO);
		
		paging.setTotalCount(totalCnt);

		model.addAttribute("sriAlarmList", sriAlarmList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
		
		return "views/datamng/invstAlarmList";
	}

	/**
	 * @Method Name : invstAlarmSave
	 * @작성일 : 2024. 1. 30.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 데이터 관리 알림 등록
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String invstAlarmSave(Model model) {
		String mngrId = LoginMngrUtils.getUserId();
		model.addAttribute("writer", mngrId);
		return "views/datamng/modal/invstAlarmSave";
	}
	
	/**
	  * @Method Name : invstAlarmSave
	  * @작성일 : 2024. 3. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 데이터 관리 알림 등록
	  * @param tlBbsInfo
	  * @param files
	  * @param extsChk
	  * @return
	  */
	@Authority(authType = AuthType.CREATE)
	@PostMapping("/save")
	public @ResponseBody CommonResponse<?> invstAlarmSave(TlBbsInfo tlBbsInfo){
		tlBbsInfo.setRegistDt(LocalDateTime.now());
		tlBbsInfo.setUpdtDt(LocalDateTime.now());
		tlBbsInfogRepository.save(tlBbsInfo);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "알림이 등록 되었습니다.");
	}
	
	/**
	  * @Method Name : invstAlarmUpdate
	  * @작성일 : 2024. 3. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 데이터 관리 알림 수정 페이지
	  * @param model
	  * @param brdId
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/{bbsId}")
	public String invstAlarmUpdate(Model model, @PathVariable String bbsId) {
		TlBbsInfo findAlarm = tlBbsInfogRepository.findById(bbsId).get();
		model.addAttribute("tlBbsInfo", findAlarm);
		return "views/datamng/modal/invstAlarmUpdate";
	}
	
	/**
	  * @Method Name : invstAlarmUpdate
	  * @작성일 : 2024. 3. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 데이터 관리 알림 수정
	  * @param tlBbsInfo
	  * @param bbsId
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/{bbsId}")
	public @ResponseBody CommonResponse<?> invstAlarmUpdate(TlBbsInfo tlBbsInfo, @PathVariable String bbsId){
		tlBbsInfo = potalMngService.setTlBbsInfoInfo(tlBbsInfo, bbsId);
		tlBbsInfogRepository.save(tlBbsInfo);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "알림이 수정 되었습니다.");
	}
	
	/**
	  * @Method Name : invstAlarmDelete
	  * @작성일 : 2024. 3. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 데이터 관리 알림 삭제
	  * @param body
	  * @return
	  */
	@Authority(authType = AuthType.DELETE)
	@Transactional
	@DeleteMapping
	public @ResponseBody CommonResponse<?> invstAlarmDelete(@RequestBody Map<String, List<String>> body){
		List<String> bbsIds = body.get("bbsIds");
        for (String bbsId : bbsIds) {
            try {
				potalMngService.deletePotalAndRelatedFiles(bbsId);
			} catch (Exception e) {
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.CONFLICT, "알림 삭제 실패");
			}
        }
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "알림이 삭제 되었습니다.");
	}
	
}
