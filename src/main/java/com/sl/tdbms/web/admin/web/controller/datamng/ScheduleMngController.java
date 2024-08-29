package com.sl.tdbms.web.admin.web.controller.datamng;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import com.sl.tdbms.web.admin.common.component.ExcelDownloadComponent;
import com.sl.tdbms.web.admin.common.component.FileComponent;
import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.dto.invst.*;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.support.exception.CommonResponseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.entity.TlExmnRslt;
import com.sl.tdbms.web.admin.common.entity.TmExmnDrct;
import com.sl.tdbms.web.admin.common.entity.TmExmnMng;
import com.sl.tdbms.web.admin.common.entity.TmExmnPollster;
import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.common.enums.code.ExmnScheduleSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.QTlExmnRsltRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTmExmnMngRepository;
import com.sl.tdbms.web.admin.common.repository.TlExmnRsltRepository;
import com.sl.tdbms.web.admin.common.repository.TmExmnDrctRepository;
import com.sl.tdbms.web.admin.common.repository.TmExmnMngRepository;
import com.sl.tdbms.web.admin.common.repository.TmExmnPollsterRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;
import com.sl.tdbms.web.admin.web.service.datamng.ScheduleMngService;


@Controller
@RequestMapping("/datamng/schedule")
public class ScheduleMngController {
	
	@Autowired
    QTmExmnMngRepository qTmExmnMngRepository;
	
	@Autowired
    TmExmnMngRepository tmExmnMngRepository;
	
	@Autowired
    TmExmnDrctRepository tmExmnDrctRepository;
	
	@Autowired
    TmExmnPollsterRepository tmExmnPollsterRepository;

	@Autowired
    TlExmnRsltRepository tlExmnRsltRepository;
	
	@Autowired
    ScheduleMngService scheduleMngService;
	
	@Autowired
	QTlExmnRsltRepository qTlExmnRsltRepository;
	
	@Autowired
	ExcelDownloadComponent excelDownloadComponent;
	
	@Autowired
	FileComponent fileComponent;
	
	/**
	 * @Method Name : scheduleListPage
	 * @작성일 : 2024. 2. 1.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 일정 목록 화면
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String scheduleListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		paging.setLimitCount(5);
		List<TmExmnScheduleDTO> invstList = qTmExmnMngRepository.getInvstScheduleList(searchCommonDTO, paging);
		TmExmnScheduleStatisticsDTO scheduleStatistics = scheduleMngService.getScheduleStatisticsInfo();
		
		List<TmExmnMngDTO> invstCalendarList = qTmExmnMngRepository.getInvstScheduleListByNotSttsCd(ExmnSttsCd.INVEST_WRITING);
		if(!CommonUtils.isListNull(invstCalendarList)) {
			String calendarData = scheduleMngService.getInvstScheduleListForCalendar(invstCalendarList); 
			model.addAttribute("calendarData", calendarData);
		}
		long totalCnt = qTmExmnMngRepository.getInvstScheduleTotalCount(searchCommonDTO);
		
		paging.setPageSize(5);
		paging.setTotalCount(totalCnt);

		model.addAttribute("exmnScheduleSttsCd", ExmnScheduleSttsCd.values());
		model.addAttribute("exmnTypeCd", ExmnTypeCd.values());
		model.addAttribute("invstList", invstList);
		model.addAttribute("scheduleStatistics", scheduleStatistics);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
		
		return "views/datamng/scheduleList";
	}

	@Authority(authType = AuthType.READ)
	@GetMapping("/asynchronous")
	public @ResponseBody CommonResponse<?> scheduleAsynchronousList(SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		
		paging.setLimitCount(5);
		List<TmExmnScheduleDTO> invstList = qTmExmnMngRepository.getInvstScheduleList(searchCommonDTO, paging);
		long totalCnt = qTmExmnMngRepository.getInvstScheduleTotalCount(searchCommonDTO);
		paging.setPageSize(5);
		paging.setTotalCount(totalCnt);

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("invstList", invstList);
		result.put("totalCnt", totalCnt);
		result.put("paging", paging);

		return CommonResponse.successToData(result,"");
	}

	/**
	 * @Method Name : scheduleSavePage
	 * @작성일 : 2024. 1. 25.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 교통조사 관리 일정관리 등록 페이지
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String scheduleSavePage(Model model) {
		return "views/datamng/scheduleSave";
	}

	/**
	 * @Method Name : scheduleInvstListPage
	 * @작성일 : 2024. 1. 25.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 모달 조사 찾기
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/invst/list")
	public String scheduleInvstListPage(Model model) {
		return "views/datamng/modal/scheduleInvstList";
	}

	/**
	 * @Method Name : scheduleInteriorSave
	 * @작성일 : 2024. 1. 25.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 모달 내부 조사원 등록
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/invst/interior/save")
	public String scheduleInteriorSave(Model model) {
		return "views/datamng/modal/scheduleInteriorSave";
	}

	/**
	 * @Method Name : modalScheduleMngExternalSave
	 * @작성일 : 2024. 1. 26.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 모달 외부 조사원 등록
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/invst/external/save")
	public String scheduleExternalSave(Model model) {
		return "views/datamng/modal/scheduleExternalSave";
	}

	/**
	 * @Method Name : scheduleDateList
	 * @작성일 : 2024. 1. 29.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 모달 날짜에 해당하는 조사리스트
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/invst/date/list")
	public String scheduleDateList(Model model,@RequestParam String paramDate) {
		TmExmnScheduleDetailDTO tmExmnScheduleDetailDTO = scheduleMngService.getInvstScheduleInfo(paramDate);
		if(!CommonUtils.isListNull(tmExmnScheduleDetailDTO.getTmExmnScheduleList())) {
			model.addAttribute("tmExmnScheduleList", tmExmnScheduleDetailDTO.getTmExmnScheduleList());
			model.addAttribute("totalCnt", tmExmnScheduleDetailDTO.getTotalCnt());
		}
		return "views/datamng/modal/scheduleDateList";
	}

	/**
	 * @Method Name : scheduleDetail
	 * @작성일 : 2024. 3. 21.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 조사정보 및 조사원 등록
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/{exmnmngId}")
	public String scheduleDetail(Model model,@PathVariable String exmnmngId) {
		TmExmnMngDTO invstInfo = qTmExmnMngRepository.getInvstInfo(exmnmngId);
		if (CommonUtils.isNull(invstInfo)) {
			String resMsg = CommonUtils.getMessage("invst.invstDetail.data.empty");
			throw new CommonException(ErrorCode.EMPTY_DATA, resMsg);
		}
		List<TmExmnPollsterDTO> tmExmnPollsterList = null;
		long registExmnPollsterCnt = 0;
		if (invstInfo.getExmnType().getType().equals("traffic")) {
			tmExmnPollsterList = qTlExmnRsltRepository.getTrafficPollsterList(exmnmngId);
			registExmnPollsterCnt = qTlExmnRsltRepository.getTrafficRegistExmnPollsterCnt(exmnmngId);
//					tmExmnPollsterRepository.findAllByExmnmngIdOrderByRegistDtAsc(exmnmngId);
		} else if(invstInfo.getExmnType().getType().equals("survey")) {
			tmExmnPollsterList = qTlExmnRsltRepository.getSurveyPollsterList(exmnmngId);
			registExmnPollsterCnt = qTlExmnRsltRepository.getSurveyRegistExmnPollsterCnt(exmnmngId);
		}
		if(!CommonUtils.isListNull(tmExmnPollsterList)) {
			model.addAttribute("tmExmnPollsterList",tmExmnPollsterList);
		}
		model.addAttribute("pollsterCnt",tmExmnPollsterList.size());
		if("true".equals(invstInfo.getExmnType().getHasDrct())){
			List<TmExmnDrct> tmExmnDrctList = tmExmnDrctRepository.findAllByExmnmngIdOrderByDrctSqnoAsc(exmnmngId);
			model.addAttribute("tmExmnDrctList", tmExmnDrctList);
		}
		if(LocalDateTime.now().isAfter(invstInfo.getEndDt()) || LocalDateTime.now().isEqual(invstInfo.getEndDt())) 
			invstInfo.setSttsCdNm("notComplete");
		model.addAttribute("registExmnPollsterCnt",registExmnPollsterCnt);
		model.addAttribute("invstInfo",invstInfo);
		return "views/datamng/scheduleDetail";
	}
	
	/**
	  * @Method Name : PartcptCdSave
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 참여코드 등록
	  * @param exmnmngId
	  * @return
	  */
	@Authority(authType = AuthType.CREATE)
	@PostMapping("/{exmnmngId}/partcptCd")
	public @ResponseBody CommonResponse<?> PartcptCdSave(@PathVariable String exmnmngId) {
		String partcptCd = scheduleMngService.savePartcptCd(exmnmngId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("partcptCd", partcptCd);
		return CommonResponse.successToData(result, "");
	}
	
	/**
	  * @Method Name : invstInvestigatorSave
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사원 등록
	  * @param TmExmnPollsterSaveDTO
	  * @return
	  */
	@Authority(authType = AuthType.CREATE)
	@PostMapping("/investigator")
	public @ResponseBody CommonResponse<?> invstInvestigatorSave(@RequestBody TmExmnPollsterSaveDTO TmExmnPollsterSaveDTO) {
		TmExmnPollster tmExmnPollster = scheduleMngService.saveInvestigatorList(TmExmnPollsterSaveDTO);
		
		String resultMessage = CommonUtils.getMessage("schedule.invstInvestigatorSave.complete");
		if(!CommonUtils.isNull(tmExmnPollster.getPollsterTel()) || 
				!CommonUtils.isNull(tmExmnPollster.getPollsterEmail())) {
//			resultMessage = "조사원 정보(연락처,이메일) 중 동일한 기간내의 <br>연락처("+tmExmnPollster.getPollsterTel()+") 또는 이메일("+tmExmnPollster.getPollsterEmail()+") 정보가 존재합니다.";
			resultMessage = CommonUtils.getMessage("schedule.invstInvestigatorSave.duplication") +"("+ tmExmnPollster.getPollsterTel() + ", " + tmExmnPollster.getPollsterEmail() + ")";
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST,resultMessage);
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,resultMessage);
	}
	
	
	/**
	  * @Method Name : invstCountingHistoryPage
	  * @작성일 : 2024. 4. 16.
	  * @작성자 : NK.KIM
	  * @Method 설명 : TC 조사 이력 화면
	  * @param model
	  * @param exmnmngId
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/traffic/{exmnmngId}/history")
	public String invstCountingHistoryPage(Model model,@PathVariable String exmnmngId,TlExmnRsltHistorySearchDTO tlExmnRsltHistorySearchDTO) {
		
		Optional<TmExmnMng> invstInfo = tmExmnMngRepository.findById(exmnmngId);
		if (!invstInfo.isPresent()) {
			String resMsg = CommonUtils.getMessage("invst.invstDetail.data.empty");
			throw new CommonException(ErrorCode.EMPTY_DATA, resMsg);
		}
		
		//방향 목록
		List<TmExmnDrct> exmnDrctList = tmExmnDrctRepository.findAllByExmnmngIdOrderByDrctSqnoAsc(exmnmngId);
		model.addAttribute("exmnDrctList", exmnDrctList);

		//방향 파라미터가 존재하지않으면 목록의 첫번째 인덱스 값
		if(CommonUtils.isNull(tlExmnRsltHistorySearchDTO.getExmndrctId())) {
			tlExmnRsltHistorySearchDTO.setExmndrctId(exmnDrctList.get(0).getExmndrctId());
		}
		
		//시간 정보 / 당일 이력 정보
		TlExmnTrfvlRsltHistoryDTO tlExmnTrfvlRsltHistoryDTO = scheduleMngService.getTrafficHistoryInfo(tlExmnRsltHistorySearchDTO, invstInfo.get());

		List<String> surveyDtList = tlExmnRsltRepository.getSurveyDateList(invstInfo.get(), LocalDateTime.now());
		model.addAttribute("surveyDtList", surveyDtList);

		model.addAttribute("tlExmnTrfvlRsltHistoryDTO", tlExmnTrfvlRsltHistoryDTO);
		model.addAttribute("tlExmnTrfvlRsltHistorySearchDTO", tlExmnRsltHistorySearchDTO);
		
		return "views/datamng/scheduleTrafficDetail"; 
	}
	
	/**
	  * @Method Name : invstSurveyHistoryPage
	  * @작성일 : 2024. 5. 10.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 이력 화면
	  * @param model
	  * @param exmnmngId
	  * @param tlExmnRsltHistorySearchDTO
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/survey/{exmnmngId}/history")
	public String invstSurveyHistoryPage(Model model,@PathVariable String exmnmngId, TlExmnRsltHistorySearchDTO tlExmnRsltHistorySearchDTO, PagingUtils paging) {
		
		Optional<TmExmnMng> invstInfo = tmExmnMngRepository.findById(exmnmngId);
		if (!invstInfo.isPresent()) {
			String resMsg = CommonUtils.getMessage("invst.invstDetail.data.empty");
			throw new CommonException(ErrorCode.EMPTY_DATA, resMsg);
		}
		
		if("true".equals(invstInfo.get().getExmnType().getHasDrct())) {
			//방향 목록
			List<TmExmnDrct> exmnDrctList = tmExmnDrctRepository.findAllByExmnmngIdOrderByDrctSqnoAsc(exmnmngId);
			model.addAttribute("exmnDrctList", exmnDrctList);
			
			//방향 파라미터가 존재하지않으면 목록의 첫번째 인덱스 값
			if(CommonUtils.isNull(tlExmnRsltHistorySearchDTO.getExmndrctId())) {
				tlExmnRsltHistorySearchDTO.setExmndrctId(exmnDrctList.get(0).getExmndrctId());
			}
		}
		
		TlExmnSrvyRsltHistoryDTO tlExmnSrvyRsltHistoryDTO = scheduleMngService.getSurveyHistoryInfo(tlExmnRsltHistorySearchDTO,invstInfo.get(),paging);
		model.addAttribute("tlExmnSrvyRsltHistoryDTO", tlExmnSrvyRsltHistoryDTO);
		model.addAttribute("paging", tlExmnSrvyRsltHistoryDTO.getPaging());
		model.addAttribute("tlExmnTrfvlRsltHistorySearchDTO", tlExmnRsltHistorySearchDTO);
		
		return "views/datamng/scheduleSurveyDetail"; 
	}
	
	/**
	  * @Method Name : invstSurveyResultHistoryPage
	  * @작성일 : 2024. 7. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 설문 결과 이력 화면
	  * @param model
	  * @param srvyrsltId
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/survey/{srvyrsltId}")
	public String invstSurveyResultHistoryPage(Model model, @PathVariable String srvyrsltId) {
		LoginMngrDTO userMngInfo  = LoginMngrUtils.getTcUserMngInfo();
		if("KECC".equals(userMngInfo.getUserAuth().toUpperCase())){
			model.addAttribute("tlSrvyAnsHistoryResultDTO",scheduleMngService.getSurveyHistoryInfoFromKecc(srvyrsltId));
			model.addAttribute("keccYn","Y");
		} else {
			TlSrvyAnsHistoryResultDTO tlSrvyAnsHistoryResultDTO = scheduleMngService.getSurveyResultInfo(srvyrsltId);
			model.addAttribute("keccYn","N");
			model.addAttribute("tlSrvyAnsHistoryResultDTO", tlSrvyAnsHistoryResultDTO);
		}
		model.addAttribute("hourArr",CommonUtils.makeTimeArr(24));
		model.addAttribute("minArr",CommonUtils.makeTimeArr(60));
		return "views/datamng/scheduleSurveyResult";
	}


	@Authority(authType = AuthType.READ)
	@GetMapping("/survey/{exmnmngId}/download")
	public void excelDownload(HttpServletResponse resp, @PathVariable String exmnmngId) throws IOException {
		scheduleMngService.excelDownload(resp, exmnmngId);
	}

	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/survey/{exmnmngId}")
	public @ResponseBody CommonResponse<?> surveySttsUpdate(@PathVariable String exmnmngId) throws IOException {
		Optional<TmExmnMng> tmExmnMng = tmExmnMngRepository.findById(exmnmngId);
		if(!tmExmnMng.isPresent()){
			CommonResponse.ResponseCodeAndMessage(HttpStatus.NO_CONTENT,CommonUtils.getMessage("survey.common.data.empty"));
		}
		TmExmnMng updateTmExmnMng = tmExmnMng.get();
		//기본값 조사 완료
		ExmnSttsCd updateStts = ExmnSttsCd.INVEST_COMPLETE;
		if(ExmnSttsCd.INVEST_COMPLETE.equals(updateTmExmnMng.getSttsCd())){
			updateStts = ExmnSttsCd.INVEST_PROGRESS;
		}
		updateTmExmnMng.setSttsCd(updateStts);
		TlExmnRslt updateTlExmnRslt = tlExmnRsltRepository.findOneByExmnmngId(exmnmngId);
		updateTlExmnRslt.setSttsCd(updateStts);
		try{
			tmExmnMngRepository.save(updateTmExmnMng);
			tlExmnRsltRepository.save(updateTlExmnRslt);
		}catch (Exception e){
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.INTERNAL_SERVER_ERROR,CommonUtils.getMessage("survey.common.data.empty"));
		}


		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,CommonUtils.getMessage("common.save.data.success"));
	}

	/**
	 * @Method Name : scheduleSurveyGpsUpdate
	 * @작성일 : 2024. 08. 23.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 모달 설문 위치 수정
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/invst/survey/gps/update")
	public String scheduleSurveyMapUpdate(Model model) {
		return "views/datamng/modal/surveyGpsUpdate";
	}

	/**
	 * methodName : surveyInfoUpdate
	 * author : Peo.Lee
	 * date : 2024-08-28
	 * description : kecc조사 결과 수정
	 * @param keccSrvyUpdateDTO
	 * @return CommonResponse<?>
	 */
	@Authority(authType = AuthType.UPDATE)
	@PostMapping("/survey/update")
	@ResponseBody
	public CommonResponse<?> surveyInfoUpdate(@RequestBody KeccSrvyUpdateDTO keccSrvyUpdateDTO){

		try{
			if(keccSrvyUpdateDTO != null && !CommonUtils.isNull(keccSrvyUpdateDTO.getSrvyrsltId())){
				scheduleMngService.surveyInfoUpdate(keccSrvyUpdateDTO);
			} else {
				throw new CommonResponseException(ErrorCode.INVALID_PARAMETER);
			}
		} catch (CommonResponseException e){
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,CommonUtils.getMessage("schedule.surveyInfoUpdate.complete"));
	}
	
	/**
	 * @Method Name : modalScheduleInvstrSave
	 * @작성일 : 2024. 8. 25.
	 * @작성자 : KC.KIM
	 * @Method 설명 : 모달 외부 조사원 등록 모달
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/invst/save")
	public String modalScheduleInvstrSave(Model model) {
		return "views/datamng/modal/invstrExcelSave";
	}
	
	/**
	 * @Method Name : sheduleInvstrExcelDownload
	 * @작성일 : 2024. 8. 25.
	 * @작성자 : KC.KIM
	 * @Method 설명 : 모달 외부 조사원 대량 등록 엑셀 파일 다운로드
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/invst/excelDownload")
	public void sheduleInvstrExcelDownload(HttpServletResponse resp) throws IOException {
		try {
			excelDownloadComponent.invstrBulkUploadExcelDownload(resp);
		} catch (Exception e) {
			throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED);
		}
	}
	
	/**
	 * @Method Name : sheduleInvstrExcelFileRead
	 * @작성일 : 2024. 8. 25.
	 * @작성자 : KC.KIM
	 * @Method 설명 : 모달 외부 조사원 대량 등록 엑셀 파일 데이터 조회
	 * @param
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@PostMapping("/invst/excelUpload")
	public @ResponseBody CommonResponse<?> sheduleInvstrExcelFileRead(@RequestParam("file") MultipartFile file){
		Map<String,Object> result = new HashMap<>();
		
		try {
			List<InvstrResultDTO> invstrList = fileComponent.uploadInvstrBulkExcel(file);
			result.put("dataList",invstrList);
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "", result);
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

}
