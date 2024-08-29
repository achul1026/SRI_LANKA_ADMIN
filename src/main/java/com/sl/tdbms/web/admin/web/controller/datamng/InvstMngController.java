package com.sl.tdbms.web.admin.web.controller.datamng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.sl.tdbms.web.admin.common.dto.invst.TmExmnMngDTO;
import com.sl.tdbms.web.admin.common.dto.invst.TmExmnMngSaveDTO;
import com.sl.tdbms.web.admin.common.dto.invst.TmExmnMngUpdateDTO;
import com.sl.tdbms.web.admin.common.dto.invst.TmSrvyInfoDTO;
import com.sl.tdbms.web.admin.common.dto.invst.TmSrvySectDetailDTO;
import com.sl.tdbms.web.admin.common.dto.mngr.TcUserMngDTO;
import com.sl.tdbms.web.admin.common.entity.TmExmnDrct;
import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.common.enums.code.AthrztSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.ColorTypeCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;
import com.sl.tdbms.web.admin.common.enums.code.LocationPointTypeCd;
import com.sl.tdbms.web.admin.common.enums.code.QstnTypeCd;
import com.sl.tdbms.web.admin.common.enums.code.RoadNmTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTcUserMngRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTmExmnMngRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTmSrvyInfoRepository;
import com.sl.tdbms.web.admin.common.repository.TcCdInfoRepository;
import com.sl.tdbms.web.admin.common.repository.TmExmnDrctRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;
import com.sl.tdbms.web.admin.web.service.datamng.InvstMngService;

@Controller
@RequestMapping("/datamng/invst")
public class InvstMngController {

	@Autowired
	QTcCdInfoRepository qTcCdInfoRepository;

	@Autowired
	QTcUserMngRepository qTcUserMngRepository;

	@Autowired
	QTmExmnMngRepository qTmExmnMngRepository;

	@Autowired
	QTmSrvyInfoRepository qTmSrvyInfoRepository;
	
	@Autowired
	TmExmnDrctRepository tmExmnDrctRepository;
	
	@Autowired
	TcCdInfoRepository tcCdInfoRepository;

	@Autowired
	InvstMngService invstMngService;

	/**
	 * @Method Name : invstList
	 * @작성일 : 2024. 1. 30.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 조사 관리 목록 화면
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String invstListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TmExmnMngDTO> invstList = qTmExmnMngRepository.getInvstList(searchCommonDTO, paging);

		long totalCnt = qTmExmnMngRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);

		model.addAttribute("invstList", invstList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("exmnTypeCd", ExmnTypeCd.values());
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);

		return "views/datamng/invstList";
	}

	/**
	 * @Method Name : invstSave
	 * @작성일 : 2024. 01. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 서비스 관리 조사 등록
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String invstSave(Model model) {
		model.addAttribute("colorTypeCd", ColorTypeCd.values());
		model.addAttribute("cmCdList", ExmnTypeCd.values());
		model.addAttribute("locationPointTypeCd", LocationPointTypeCd.values());
		model.addAttribute("roadNmTypeCd", RoadNmTypeCd.values());
		return "views/datamng/invstSave";
	}

	/**
	 * @Method Name : invstSave
	 * @작성일 : 2024. 1. 31.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 교통량 조사 등록
	 * @param TmExmnMngSaveDTO
	 * @return
	 */
	@Authority(authType = AuthType.CREATE)
	@PostMapping
	public @ResponseBody CommonResponse<?> invstSave(@RequestBody TmExmnMngSaveDTO tmExmnMngSaveDTO) {
		try {
			invstMngService.saveInvstInfo(tmExmnMngSaveDTO);
			String resMsg = CommonUtils.getMessage("invst.invstSave.save.complete");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("exmnmngId", tmExmnMngSaveDTO.getTmExmnMng().getExmnmngId());
			return CommonResponse.successToData(result, resMsg);
		} catch (CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * @Method Name : invstUpdate
	 * @작성일 : 2024. 1. 31.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 교통량 조사 수정
	 * @param TmExmnMngUpdateDTO
	 * @return
	 */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/{exmnmngId}")
	public @ResponseBody CommonResponse<?> invstUpdate(@RequestBody TmExmnMngUpdateDTO tmExmnMngUpdateDTO, @PathVariable String exmnmngId) {
		try {
			invstMngService.updateInvstInfo(tmExmnMngUpdateDTO,exmnmngId);
			String resMsg = CommonUtils.getMessage("invst.invstUpdate.update.complete");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("exmnmngId", exmnmngId);
			return CommonResponse.successToData(result, resMsg);
		} catch (CommonException e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * @Method Name : modalInvstInvestigator
	 * @작성일 : 2024. 01. 09.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 서비스 관리 조사 관리 조사원 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/modal/mngr")
	public String modalInvstInvestigator(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		searchCommonDTO.setSearchSttsCd(AthrztSttsCd.APPROVAL.getCode());
		paging.setLimitCount(5);
		paging.setPageSize(5);
		List<TcUserMngDTO> mngrList = qTcUserMngRepository.getTcUserList(searchCommonDTO, paging);

		long totalCnt = qTcUserMngRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);

		model.addAttribute("mngrList", mngrList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);

		return "views/datamng/modal/invstMngrList";
	}
	
	/**
	 * @Method Name : modalSurveyForm
	 * @작성일 : 2024. 01. 09.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 모달 설문 양식 조회
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/modal/survey")
	public String modalSurveyForm(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		
		String exmnType = ExmnTypeCd.getEnums(searchCommonDTO.getSearchType()).name();
		String srvyTypeCd = tcCdInfoRepository.findByCdnmKor(exmnType).getCd();
		searchCommonDTO.setSearchTypeCd(srvyTypeCd);
		paging.setLimitCount(5);
		paging.setPageSize(5);
		
		List<TmSrvyInfoDTO> surveyList = qTmSrvyInfoRepository.getSurveyList(searchCommonDTO, paging);

		long totalCnt = qTmSrvyInfoRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);
		model.addAttribute("surveyList", surveyList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
		
		return "views/datamng/modal/invstSurveyList";
	}
	
	@Authority(authType = AuthType.READ)
	@GetMapping("/modal/survey/asynchronous")
	public @ResponseBody CommonResponse<?> surveyAsynchronousSetting(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		String exmnType = ExmnTypeCd.getEnums(searchCommonDTO.getSearchType()).name();
		String srvyTypeCd = tcCdInfoRepository.findByCdnmKor(exmnType).getCd();
		searchCommonDTO.setSearchTypeCd(srvyTypeCd);
		paging.setLimitCount(5);
		paging.setPageSize(5);
		
		List<TmSrvyInfoDTO> surveyList = qTmSrvyInfoRepository.getSurveyList(searchCommonDTO, paging);

		long totalCnt = qTmSrvyInfoRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);
		
		Map<String, Object> result = new HashMap<String, Object>();
		
		result.put("surveyList", surveyList);
		result.put("totalCnt", totalCnt);
		result.put("paging", paging);
		result.put("searchInfo", searchCommonDTO);
		
		return CommonResponse.successToData(result,"");
	}

	/**
	 * @Method Name : invstPageSurveyQuestionSave
	 * @작성일 : 2024. 1. 22.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 서비스관리 조사관리 설문 작성
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/survey/question/{exmnmngId}/save")
	public String invstSurveySaveQuestionPage(Model model, @PathVariable String exmnmngId) {
		TmExmnMngDTO srvyInvstInfo = invstMngService.getInvstInfo(exmnmngId);

		// 조사 타입 enum 호출
		model.addAttribute("qstnTypeCd", 
				QstnTypeCd.values()
//				qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.QSTN_TYPE_CD.getCode())
				);
		model.addAttribute("srvyInvstInfo", srvyInvstInfo);

		return "views/datamng/invstSurveySave";
	}

	/**
	 * @Method Name : invstSurveyUpdateQuestionPage
	 * @작성일 : 2024. 3. 22.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 설문 수정 화면
	 * @param model
	 * @param exmnmngId
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/survey/question/{exmnmngId}/update")
	public String invstSurveyUpdateQuestionPage(Model model, @PathVariable String exmnmngId) {
		TmExmnMngDTO srvyInvstInfo = invstMngService.getInvstInfo(exmnmngId);
		TmSrvySectDetailDTO tmSrvySectDetailDTO = invstMngService.getSrvySectInfo(exmnmngId);
		if(CommonUtils.isListNull(tmSrvySectDetailDTO.getTmSrvySectList())) {
			return "redirect:/datamng/invst/survey/question/"+exmnmngId+"/save";
		}
		model.addAttribute("exmnMngSrvyList", tmSrvySectDetailDTO.getTmSrvySectList());
		model.addAttribute("srvyInvstInfo", srvyInvstInfo);

		return "views/datamng/invstSurveyUpdate";
	}

	/**
	 * @Method Name : invstDetail
	 * @작성일 : 2024. 3. 18.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 서비스관리 조사관리 상세
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/{exmnmngId}")
	public String invstMngDetailPage(Model model, @PathVariable String exmnmngId) {
		TmExmnMngDTO invstInfo = qTmExmnMngRepository.getInvstInfo(exmnmngId);
		if (CommonUtils.isNull(invstInfo)) {
			String resMsg = CommonUtils.getMessage("invst.invstDetail.data.empty");
			throw new CommonException(ErrorCode.EMPTY_DATA, resMsg);
		}

		//설문형 조사인 경우에만 설문 정보 호출
		if("survey".equals(invstInfo.getExmnType().getType())){
			TmSrvySectDetailDTO tmSrvySectDetailDTO = invstMngService.getSrvySectInfo(invstInfo.getSrvyId());
			model.addAttribute("exmnMngSrvyList", tmSrvySectDetailDTO.getTmSrvySectList());
		} 
		//TC조사인경우
		if ("true".equals(invstInfo.getExmnType().getHasDrct())) {
			List<TmExmnDrct> tmExmnDrctList = tmExmnDrctRepository.findAllByExmnmngIdOrderByDrctSqnoAsc(exmnmngId);
			model.addAttribute("tmExmnDrctList", tmExmnDrctList);
		}

		model.addAttribute("invstInfo", invstInfo);
		model.addAttribute("colorTypeCd", ColorTypeCd.values());
		
		return "views/datamng/invstDetail";
	}

	/**
	 * @Method Name : invstUpdate
	 * @작성일 : 2024. 3. 18.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 서비스관리 조사관리 수정
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/{exmnmngId}/update")
	public String invstMngUpdatePage(Model model, @PathVariable String exmnmngId) {
		TmExmnMngDTO invstInfo = qTmExmnMngRepository.getInvstInfo(exmnmngId);
		String invstType = invstInfo.getExmnType().getType();
		if (CommonUtils.isNull(invstInfo)) {
			String resMsg = CommonUtils.getMessage("invst.invstDetail.data.empty");
			throw new CommonException(ErrorCode.EMPTY_DATA, resMsg);
		}
		
		String roadTypeCd = invstInfo.getCordonLine() != null ? RoadNmTypeCd.CORDON_LINE.getCode() :
            invstInfo.getScreenLine() != null ? RoadNmTypeCd.SCREEN_LINE.getCode() :
            LocationPointTypeCd.TOLL_BOOTH.getCode();
		
		model.addAttribute("roadTypeCd", roadTypeCd);
		model.addAttribute("invstType", invstType);
		model.addAttribute("invstInfo", invstInfo);
		model.addAttribute("cmCdList", ExmnTypeCd.values());
		model.addAttribute("colorTypeCd", ColorTypeCd.values());
		model.addAttribute("locationPointTypeCd", LocationPointTypeCd.values());
		model.addAttribute("roadNmTypeCd", RoadNmTypeCd.values());
		
		//방향이 존재 하는 조사인경우
		if ("true".equals(invstInfo.getExmnType().getHasDrct())) {
			List<TmExmnDrct> tmExmnDrctList = tmExmnDrctRepository.findAllByExmnmngIdOrderByDrctSqnoAsc(exmnmngId);
			model.addAttribute("tmExmnDrctList", tmExmnDrctList);
		}
		return "views/datamng/invstUpdate";
	}
	
	/**
	  * @Method Name : exmnRangeUpdate
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 좌표 반경 수정
	  * @param exmnmngId
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/{exmnmngId}/exmnRange")
	public @ResponseBody CommonResponse<?> exmnRangeUpdate(@PathVariable String exmnmngId,@RequestBody String exmnRange) {
		try {
			invstMngService.updateExmnRange(exmnmngId,exmnRange);
			String resMsg = CommonUtils.getMessage("invst.exmnRangeUpdate.complete");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	/**
	 * @Method Name : invstInfoDelete
	 * @작성일 : 2024. 1. 11.
	 * @작성자 : SM.KIM
	 * @Method 설명 : 조사 정보 삭제
	 * @param exmnmngId
	 * @return
	 */
	@Authority(authType = AuthType.DELETE)
	@DeleteMapping("/{exmnmngId}")
	public @ResponseBody CommonResponse<?> invstInfoDelete(@PathVariable String exmnmngId){
		try {
			invstMngService.deleteInvstInfo(exmnmngId);
			String resMsg = CommonUtils.getMessage("invst.invstInfoDelete.complete");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
	/**
	  * @Method Name : periodUpdate
	  * @작성일 : 2024. 8. 26.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 조사 기간 종료일 변경
	  * @param exmnmngId
	  * @param exmnRange
	  * @return
	  */
	@Authority(authType = {AuthType.UPDATE,AuthType.CREATE})
	@PutMapping("/{exmnmngId}/period")
	public @ResponseBody CommonResponse<?> periodUpdate(@PathVariable String exmnmngId, @RequestBody String endDate) {
		try {
			invstMngService.updateExmnEndDate(exmnmngId,endDate);
			String resMsg = CommonUtils.getMessage("invst.periodUpdate.complete");
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}
	
}