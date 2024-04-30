package com.sri.lanka.traffic.admin.web.controller.datamng;

import java.util.HashMap;
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
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnMngDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnMngSaveDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnMngUpdateDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectDetailDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectSaveDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectUpdateDTO;
import com.sri.lanka.traffic.admin.common.dto.mngr.TcUserMngDTO;
import com.sri.lanka.traffic.admin.common.entity.TmExmnDrct;
import com.sri.lanka.traffic.admin.common.entity.TmExmnMng;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.enums.code.AthrztSttsCd;
import com.sri.lanka.traffic.admin.common.enums.code.ColorTypeCd;
import com.sri.lanka.traffic.admin.common.enums.code.ExmnTypeCd;
import com.sri.lanka.traffic.admin.common.enums.code.QstnTypeCd;
import com.sri.lanka.traffic.admin.common.enums.code.SectTypeCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTcCdInfoRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTcUserMngRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTmExmnMngRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTmSrvySectRepository;
import com.sri.lanka.traffic.admin.common.repository.TmExmnDrctRepository;
import com.sri.lanka.traffic.admin.common.repository.TmExmnMngRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonException;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.support.exception.ErrorCode;
import com.sri.lanka.traffic.admin.web.service.datamng.InvstMngService;

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
	QTmSrvySectRepository qTmSrvySectRepository;

	@Autowired
	TmExmnMngRepository tmExmnMngRepository;
	
	@Autowired
	TmExmnDrctRepository tmExmnDrctRepository;

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
		invstMngService.saveInvstInfo(tmExmnMngSaveDTO);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("exmnmngId", tmExmnMngSaveDTO.getTmExmnMng().getExmnmngId());
		return CommonResponse.successToData(result, "조사 정보가 등록되었습니다.");
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
		invstMngService.updateInvstInfo(tmExmnMngUpdateDTO,exmnmngId);
		Map<String, Object> result = new HashMap<String, Object>();
		result.put("exmnmngId", exmnmngId);
		return CommonResponse.successToData(result, "조사 정보가 수정되었습니다.");
	}

	/**
	 * @Method Name : invstInvestigator
	 * @작성일 : 2024. 01. 09.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 서비스 관리 조사 관리 조사원 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/mngr")
	public String invstInvestigator(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		searchCommonDTO.setSearchSttsCd(AthrztSttsCd.APPROVAL.getCode());
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
	 * @Method Name : invstInvestigatoList
	 * @작성일 : 2024. 01. 10.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 서비스 관리 조사 등록 조사 담당자
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/invst/invstmng/investigato")
	public String invstInvestigatoList(Model model) {
		return "views/datamng/modal/invstInvestigatoList";
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
		model.addAttribute("sectTypeCd", SectTypeCd.values());
		model.addAttribute("qstnTypeCd", QstnTypeCd.values());
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
	 * @Method Name : invstSurveySaveQuestion
	 * @작성일 : 2024. 3. 25.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 설문 등록
	 * @param model
	 * @param tmSrvySectSaveDTO
	 * @param exmnmngId
	 * @return
	 */
	@Authority(authType = AuthType.CREATE)
	@PostMapping("/survey/question")
	public @ResponseBody CommonResponse<?> invstSurveySaveQuestion(Model model,
			@RequestBody TmSrvySectSaveDTO tmSrvySectSaveDTO) {
		invstMngService.saveSrvyInvstQstn(tmSrvySectSaveDTO);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "설문 양식 정보가 등록되었습니다.");
	}

	/**
	 * @Method Name : invstSurveyUpdateQuestion
	 * @작성일 : 2024. 3. 25.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 설문 수정
	 * @param model
	 * @param tmSrvySectUpdateDTO
	 * @param exmnmngId
	 * @return
	 */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/survey/question/{exmnmngId}")
	public @ResponseBody CommonResponse<?> invstSurveyUpdateQuestion(Model model,
			@RequestBody TmSrvySectUpdateDTO tmSrvySectUpdateDTO, @PathVariable String exmnmngId) {
		invstMngService.updateSrvyInvstQstn(tmSrvySectUpdateDTO, exmnmngId);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "설문 양식 정보가 수정되었습니다.");
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
		Optional<TmExmnMng> invstInfo = tmExmnMngRepository.findById(exmnmngId);
		if (!invstInfo.isPresent()) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Examination is empty");
		}

		//설문형 조사인 경우에만 설문 정보 호출
		if("survey".equals(invstInfo.get().getExmnType().getType())){
			TmSrvySectDetailDTO tmSrvySectDetailDTO = invstMngService.getSrvySectInfo(exmnmngId);
			model.addAttribute("exmnMngSrvyList", tmSrvySectDetailDTO.getTmSrvySectList());
		} else {
		//TC조사인경우
			List<TmExmnDrct> tmExmnDrctList = tmExmnDrctRepository.findAllByExmnmngIdOrderByDrctSqnoAsc(exmnmngId);
			model.addAttribute("tmExmnDrctList", tmExmnDrctList);
		}

		model.addAttribute("invstInfo", invstInfo.get());
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
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/{exmnmngId}/update")
	public String invstMngUpdatePage(Model model, @PathVariable String exmnmngId) {
		Optional<TmExmnMng> invstInfo = tmExmnMngRepository.findById(exmnmngId);
		if (!invstInfo.isPresent()) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Examination is empty");
		}
		model.addAttribute("invstInfo", invstInfo.get());
		model.addAttribute("cmCdList", ExmnTypeCd.values());
		model.addAttribute("colorTypeCd", ColorTypeCd.values());
		
		//TC조사인경우
		if("traffic".equals(invstInfo.get().getExmnType().getType())){
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
		invstMngService.updateExmnRange(exmnmngId,exmnRange);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "좌표 반경이 변경되었습니다.");
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
		invstMngService.deleteInvstInfo(exmnmngId);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"조사 정보가 삭제 되었습니다.");
	}
}
