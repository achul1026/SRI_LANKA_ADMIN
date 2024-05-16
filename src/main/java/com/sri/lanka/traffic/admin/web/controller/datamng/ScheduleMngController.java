package com.sri.lanka.traffic.admin.web.controller.datamng;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sri.lanka.traffic.admin.common.dto.common.SearchCommonDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TlExmnRsltHistoryDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TlExmnRsltHistorySearchDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnMngDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnPollsterSaveDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnScheduleDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnScheduleDetailDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnScheduleStatisticsDTO;
import com.sri.lanka.traffic.admin.common.entity.TmExmnDrct;
import com.sri.lanka.traffic.admin.common.entity.TmExmnMng;
import com.sri.lanka.traffic.admin.common.entity.TmExmnPollster;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.enums.code.ExmnScheduleSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.ExmnSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.ExmnTypeCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTmExmnMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TmExmnDrctRepository;
import com.sri.lanka.traffic.admin.common.repository.TmExmnMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TmExmnPollsterRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonException;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.support.exception.ErrorCode;
import com.sri.lanka.traffic.admin.web.service.datamng.ScheduleMngService;

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
	ScheduleMngService scheduleMngService;
	
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
		paging.setLimitCount(7);
		List<TmExmnScheduleDTO> invstList = qTmExmnMngRepository.getInvstScheduleList(searchCommonDTO, paging);
		TmExmnScheduleStatisticsDTO scheduleStatistics = scheduleMngService.getScheduleStatisticsInfo();
		
		List<TmExmnMngDTO> invstCalendarList = qTmExmnMngRepository.getInvstScheduleListByNotSttsCd(ExmnSttsCd.INVEST_WRITING);
		if(!CommonUtils.isListNull(invstCalendarList)) {
			String calendarData = scheduleMngService.getInvstScheduleListForCalendar(invstCalendarList); 
			model.addAttribute("calendarData", calendarData);
		}
		long totalCnt = qTmExmnMngRepository.getInvstScheduleTotalCount(searchCommonDTO);
		
		paging.setPageSize(7);
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
		
		paging.setLimitCount(7);
		List<TmExmnScheduleDTO> invstList = qTmExmnMngRepository.getInvstScheduleList(searchCommonDTO, paging);
		long totalCnt = qTmExmnMngRepository.getInvstScheduleTotalCount(searchCommonDTO);
		paging.setPageSize(7);
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
			throw new CommonException(ErrorCode.EMPTY_DATA, "Examination is empty");
		}
		List<TmExmnPollster> tmExmnPollsterList = tmExmnPollsterRepository.findAllByExmnmngIdOrderByRegistDtAsc(exmnmngId);
		if(!CommonUtils.isListNull(tmExmnPollsterList)) {
			model.addAttribute("tmExmnPollsterList",tmExmnPollsterList);
		}
		if("true".equals(invstInfo.getExmnType().getHasDrct())){
			List<TmExmnDrct> tmExmnDrctList = tmExmnDrctRepository.findAllByExmnmngIdOrderByDrctSqnoAsc(exmnmngId);
			model.addAttribute("tmExmnDrctList", tmExmnDrctList);
		}
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
		
		String resultMessage = "조사원 정보가 등록되었습니다.";
		if(!CommonUtils.isNull(tmExmnPollster.getPollsterTel()) || 
				!CommonUtils.isNull(tmExmnPollster.getPollsterEmail())) {
			resultMessage = "조사원 정보(연락처,이메일) 중 동일한 기간내의 <br>연락처("+tmExmnPollster.getPollsterTel()+") 또는 이메일("+tmExmnPollster.getPollsterEmail()+") 정보가 존재합니다.";
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,resultMessage);
	}
	
	
	/**
	  * @Method Name : invstCountingHistoryPage
	  * @작성일 : 2024. 4. 16.
	  * @작성자 : NK.KIM
	  * @Method 설명 : TC 조사 이력 화면
	  * @param model
	  * @param tlExmnRsltHistoryDTO
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/traffic/{exmnmngId}/history")
	public String invstCountingHistoryPage(Model model,@PathVariable String exmnmngId,TlExmnRsltHistorySearchDTO tlExmnRsltHistorySearchDTO) {
		
		Optional<TmExmnMng> invstInfo = tmExmnMngRepository.findById(exmnmngId);
		if (!invstInfo.isPresent()) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Examination is empty");
		}
		
		//방향 목록
		List<TmExmnDrct> exmnDrctList = tmExmnDrctRepository.findAllByExmnmngIdOrderByDrctSqnoAsc(exmnmngId);
		model.addAttribute("exmnDrctList", exmnDrctList);
		
		//방향 파라미터가 존재하지않으면 목록의 첫번째 인덱스 값
		if(CommonUtils.isNull(tlExmnRsltHistorySearchDTO.getExmndrctId())) {
			tlExmnRsltHistorySearchDTO.setExmndrctId(exmnDrctList.get(0).getExmndrctId());
		}
		
		//시간 정보
		TlExmnRsltHistoryDTO tlExmnRsltHistoryDTO = scheduleMngService.getTrafficHistoryInfo(tlExmnRsltHistorySearchDTO, invstInfo.get());
		model.addAttribute("tlExmnRsltHistoryDTO", tlExmnRsltHistoryDTO);
		model.addAttribute("paging", tlExmnRsltHistoryDTO.getPaging());
		
		model.addAttribute("tlExmnRsltHistorySearchDTO", tlExmnRsltHistorySearchDTO);
		
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
	public String invstSurveyHistoryPage(Model model,@PathVariable String exmnmngId,TlExmnRsltHistorySearchDTO tlExmnRsltHistorySearchDTO) {
		
		Optional<TmExmnMng> invstInfo = tmExmnMngRepository.findById(exmnmngId);
		if (!invstInfo.isPresent()) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Examination is empty");
		}
		
		model.addAttribute("invstInfo", invstInfo.get());
		return "views/datamng/scheduleSurveyDetail"; 
	}
}
