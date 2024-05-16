package com.sri.lanka.traffic.admin.web.controller.datamng;

import java.util.List;

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
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvyInfoDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectDetailDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectSaveDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmSrvySectUpdateDTO;
import com.sri.lanka.traffic.admin.common.entity.TcCdInfo;
import com.sri.lanka.traffic.admin.common.enums.AuthType;
import com.sri.lanka.traffic.admin.common.enums.GroupCode;
import com.sri.lanka.traffic.admin.common.enums.MenuType;
import com.sri.lanka.traffic.admin.common.enums.code.QstnTypeCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTcCdInfoRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTmSrvyInfoRepository;
import com.sri.lanka.traffic.admin.common.repository.TmSrvyInfoRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.config.authentication.Authority;
import com.sri.lanka.traffic.admin.support.exception.CommonException;
import com.sri.lanka.traffic.admin.support.exception.CommonResponse;
import com.sri.lanka.traffic.admin.support.exception.ErrorCode;
import com.sri.lanka.traffic.admin.web.service.datamng.InvstMngService;


@Controller
@RequestMapping("/datamng/survey")
public class SurveyMngController {
	
	@Autowired
	QTcCdInfoRepository qTcCdInfoRepository;
	
	@Autowired
	TmSrvyInfoRepository tmSrvyInfoRepository;
	
	@Autowired
	QTmSrvyInfoRepository qTmSrvyInfoRepository;

	@Autowired
	InvstMngService invstMngService;
	
	/**
	  * @Method Name : surveyListPage
	  * @작성일 : 2024. 5. 2.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 목록 화면
	  * @param model
	  * @param searchCommonDTO
	  * @param paging
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String surveyListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TmSrvyInfoDTO> surveyList = qTmSrvyInfoRepository.getSurveyList(searchCommonDTO, paging);

		long totalCnt = qTmSrvyInfoRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);
		
		List<TcCdInfo> srvyCdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SRVY_TYPE_CD.getCode());

		model.addAttribute("srvyCdList", srvyCdList);
		model.addAttribute("surveyList", surveyList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
		
		//목록 비지니스 로직필요
		return "views/datamng/surveyList";
	}
	
	/**
	  * @Method Name : surveySavePage
	  * @작성일 : 2024. 5. 2.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 저장 화면
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String surveySavePage(Model model) {
		model.addAttribute("srvyTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SRVY_TYPE_CD.getCode()));
		return "views/datamng/surveySave";
	}
	
	/**
	  * @Method Name : surveyDetailPage
	  * @작성일 : 2024. 5. 7.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사 설문 상세화면
	  * @param model
	  * @param srvyId
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/{srvyId}")
	public String surveyDetailPage(Model model, @PathVariable String srvyId) {
		TmSrvyInfoDTO surveyInfo = qTmSrvyInfoRepository.getSurveyInfo(srvyId);
		if(CommonUtils.isNull(surveyInfo)) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Survey is empty");
		}
		TmSrvySectDetailDTO tmSrvySectDetailDTO = invstMngService.getSrvySectInfo(srvyId);
		model.addAttribute("exmnMngSrvyList", tmSrvySectDetailDTO.getTmSrvySectList());
		model.addAttribute("surveyInfo", surveyInfo);
		return "views/datamng/surveyDetail";
	}
	
	/**
	  * @Method Name : surveyUpdatePage
	  * @작성일 : 2024. 5. 7.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사 설문 수정 화면
	  * @param model
	  * @param srvyId
	  * @return
	  */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/{srvyId}/update")
	public String surveyUpdatePage(Model model, @PathVariable String srvyId) {
		TmSrvyInfoDTO surveyInfo = qTmSrvyInfoRepository.getSurveyInfo(srvyId);
		if(CommonUtils.isNull(surveyInfo)) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Survey is empty");
		}
		TmSrvySectDetailDTO tmSrvySectDetailDTO = invstMngService.getSrvySectInfo(srvyId);
		
		model.addAttribute("srvyTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SRVY_TYPE_CD.getCode()));
		model.addAttribute("qstnTypeCd", QstnTypeCd.values());
		model.addAttribute("exmnMngSrvyList", tmSrvySectDetailDTO.getTmSrvySectList());
		model.addAttribute("surveyInfo", surveyInfo);
		return "views/datamng/surveyUpdate";
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
	@PutMapping("/{srvyId}")
	public @ResponseBody CommonResponse<?> invstSurveyUpdateQuestion(Model model,
			@RequestBody TmSrvySectUpdateDTO tmSrvySectUpdateDTO, @PathVariable String srvyId) {
		invstMngService.updateSrvyInvstQstn(tmSrvySectUpdateDTO, srvyId);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "설문 양식 정보가 수정되었습니다.");
	}
	
	/**
	  * @Method Name : surveySectionSavePage
	  * @작성일 : 2024. 5. 2.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 섹쳔 추가 모달
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/section/save")
	public String surveySectionSavePage(Model model) {
		model.addAttribute("sectTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SECT_TYPE_CD.getCode()));
		return "views/datamng/modal/surveyQuestionSectionList";
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
	@PostMapping
	public @ResponseBody CommonResponse<?> surveySaveQuestion(@RequestBody TmSrvySectSaveDTO tmSrvySectSaveDTO) {
		invstMngService.saveSrvyInvstQstn(tmSrvySectSaveDTO);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "설문 양식 정보가 등록되었습니다.");
	}
	
	/**
	  * @Method Name : surveDelete
	  * @작성일 : 2024. 5. 8.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 설문 삭제
	  * @param srvyId
	  * @return
	  */
	@Authority(authType = AuthType.DELETE)
	@DeleteMapping("{srvyId}")
	public @ResponseBody CommonResponse<?> surveDelete(@PathVariable String srvyId){
		invstMngService.deleteSurvey(srvyId);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "설문이 삭제 되었습니다.");
	}
}
