package com.sl.tdbms.web.admin.web.controller.datamng;

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

import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.entity.TlBbsInfo;
import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.common.enums.code.BbsTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.QTlBbsInfoRepository;
import com.sl.tdbms.web.admin.common.repository.TlBbsInfoRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.web.service.potalmng.PotalMngService;


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
		model.addAttribute("bbsType", BbsTypeCd.ALARM.getCode());
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
		String resMsg = CommonUtils.getMessage("alarm.invstAlarmSave.save.complete");
		try {
			tlBbsInfogRepository.save(tlBbsInfo);
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (Exception e) {
			resMsg = CommonUtils.getMessage("alarm.invstAlarmSave.save.fail");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
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
		String resMsg = CommonUtils.getMessage("alarm.invstAlarmSave.update.complete");
		try {
			tlBbsInfo = potalMngService.setTlBbsInfoInfo(tlBbsInfo, bbsId);
			tlBbsInfogRepository.save(tlBbsInfo);
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (Exception e) {
			resMsg = CommonUtils.getMessage("alarm.invstAlarmSave.update.fail");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
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
		String resMsg = CommonUtils.getMessage("alarm.invstAlarmSave.delete.complete");
        for (String bbsId : bbsIds) {
            try {
				potalMngService.deletePotalAndRelatedFiles(bbsId);
			} catch (Exception e) {
				resMsg = CommonUtils.getMessage("alarm.invstAlarmSave.delete.fail");
				return CommonResponse.ResponseCodeAndMessage(HttpStatus.CONFLICT, resMsg);
			}
        }
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
	}
	
}
