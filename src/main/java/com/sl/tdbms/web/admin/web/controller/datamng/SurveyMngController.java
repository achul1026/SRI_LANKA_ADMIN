package com.sl.tdbms.web.admin.web.controller.datamng;

import java.util.List;
import java.util.Optional;

import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.invst.TmSrvyInfoDTO;
import com.sl.tdbms.web.admin.common.dto.invst.TmSrvySectDetailDTO;
import com.sl.tdbms.web.admin.common.dto.invst.TmSrvySectSaveDTO;
import com.sl.tdbms.web.admin.common.dto.invst.TmSrvySectUpdateDTO;
import com.sl.tdbms.web.admin.common.dto.tccdgrp.TcCdInfoDTO;
import com.sl.tdbms.web.admin.common.entity.TmExmnMng;
import com.sl.tdbms.web.admin.common.enums.code.QstnTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTmSrvyInfoRepository;
import com.sl.tdbms.web.admin.common.repository.TmExmnMngRepository;
import com.sl.tdbms.web.admin.common.repository.TmSrvyInfoRepository;
import com.sl.tdbms.web.admin.web.service.datamng.InvstMngService;
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
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;


/**
 * The type Survey mng controller.
 */
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
	 * Survey list page string.
	 *
	 * @param model           the model
	 * @param searchCommonDTO the search common dto
	 * @param paging          the paging
	 * @return string
	 * @Method Name : surveyListPage
	 * @Method 설명 : 설문 목록 화면
	 * @작성일 : 2024. 5. 2.
	 * @작성자 : NK.KIM
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping
	public String surveyListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging) {
		List<TmSrvyInfoDTO> surveyList = qTmSrvyInfoRepository.getSurveyList(searchCommonDTO, paging);

		long totalCnt = qTmSrvyInfoRepository.getTotalCount(searchCommonDTO);

		paging.setTotalCount(totalCnt);
		
		List<TcCdInfoDTO> srvyCdList = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SRVY_TYPE_CD.getCode());

		model.addAttribute("srvyCdList", srvyCdList);
		model.addAttribute("surveyList", surveyList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		model.addAttribute("searchInfo", searchCommonDTO);
		
		//목록 비지니스 로직필요
		return "views/datamng/surveyList";
	}

	/**
	 * Survey save page string.
	 *
	 * @param model the model
	 * @return string
	 * @Method Name : surveySavePage
	 * @Method 설명 : 설문 저장 화면
	 * @작성일 : 2024. 5. 2.
	 * @작성자 : NK.KIM
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/save")
	public String surveySavePage(Model model) {
		model.addAttribute("srvyTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SRVY_TYPE_CD.getCode()));
		model.addAttribute("sectTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SECT_TYPE_CD.getCode()));
		model.addAttribute("srvyMetadataCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SRVY_METADATA_CD.getCode()));
		return "views/datamng/surveySave";
	}

	/**
	 * methodName : surveySaveSelectPage
	 * author : Charles Kim
	 * created at : 06.05.2024
	 * description :
	 * Gets save select page.
	 *
	 * @param model the model
	 * @return the save select page
	 */
	@Authority(authType = AuthType.CREATE, menuType = MenuType.SAVE)
	@GetMapping("/save/select")
	public String surveySaveSelectPage(Model model) {
		model.addAttribute("srvyTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SRVY_TYPE_CD.getCode()));
		model.addAttribute("sectTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SECT_TYPE_CD.getCode()));
		model.addAttribute("srvyMetadataCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SRVY_METADATA_CD.getCode()));
		return "views/datamng/surveySaveSelect";
	}


	/**
	 * methodName : surveySaveODPage
	 * author : Charles Kim
	 * created at : 06.07.2024
	 * description :
	 * Gets save od page.
	 *
	 * @param model  the model
	 * @param preset the preset
	 * @return the save od page
	 */
	@Authority(authType = AuthType.CREATE, menuType = MenuType.SAVE)
	@GetMapping("/save/{presetCode}")
	public String surveySaveODPage(Model model, @PathVariable String presetCode) {
		Optional<TcCdInfoDTO> oCodeForOD = qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SRVY_TYPE_CD.getCode()).stream().filter(x -> x.getCd().equals(presetCode)).findFirst();
		if(oCodeForOD.isPresent()) {
			model.addAttribute("srvyTypeCd", oCodeForOD.get());
		}else{
			String resMsg = CommonUtils.getMessage("survey.surveySavePreset.code.empty");
			throw new CommonException(ErrorCode.EMPTY_DATA, resMsg);
		}
		model.addAttribute("srvyMetadataCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SRVY_METADATA_CD.getCode()));
		model.addAttribute("sectTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SECT_TYPE_CD.getCode()));
		return "views/datamng/surveySavePreset";
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
	@Autowired
    TmExmnMngRepository tmExmnMngRepository;
	
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/{srvyId}")
	public String surveyDetailPage(Model model, @PathVariable String srvyId) {
		TmSrvyInfoDTO surveyInfo = qTmSrvyInfoRepository.getSurveyInfo(srvyId);
		if(CommonUtils.isNull(surveyInfo)) {
			String resMsg = CommonUtils.getMessage("survey.surveyDetail.survey.empty");
			throw new CommonException(ErrorCode.EMPTY_DATA, resMsg);
		}
		
		List<TmExmnMng> exmnMngList = tmExmnMngRepository.findAllBySrvyId(surveyInfo.getSrvyId());
		for(TmExmnMng exmn : exmnMngList) {
			if(!exmn.getSttsCd().getCode().equals("ESC001")) {
				model.addAttribute("investWriting","Y");
			}
		}
		
		TmSrvySectDetailDTO tmSrvySectDetailDTO = invstMngService.getSrvySectInfo(srvyId);
		model.addAttribute("exmnMngSrvyList", tmSrvySectDetailDTO.getTmSrvySectList());
		model.addAttribute("sectTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SECT_TYPE_CD.getCode()));
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
			String resMsg = CommonUtils.getMessage("survey.surveyDetail.survey.empty");
			throw new CommonException(ErrorCode.EMPTY_DATA, resMsg);
		}
		TmSrvySectDetailDTO tmSrvySectDetailDTO = invstMngService.getSrvySectInfo(srvyId);
		List<TmExmnMng> exmnMngList = tmExmnMngRepository.findAllBySrvyId(surveyInfo.getSrvyId());
		for(TmExmnMng exmn : exmnMngList) {
			if(!exmn.getSttsCd().getStatus().equals("notYetProgress")) {
				return "redirect:/datamng/survey/"+srvyId;
			}
		}
		
		model.addAttribute("srvyTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SRVY_TYPE_CD.getCode()));
		model.addAttribute("sectTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SECT_TYPE_CD.getCode()));
		model.addAttribute("metaTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.SRVY_METADATA_CD.getCode()));
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
		
		String resMsg = CommonUtils.getMessage("survey.surveyUpdate.update.complete");
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
	}
	
	/**
	 * @Method Name : invstSurveyUpdateTitle
	 * @작성일 : 2024. 3. 25.
	 * @작성자 : NK.KIM
	 * @Method 설명 : 설문 제목 수정
	 * @param model
	 * @param tmSrvySectUpdateDTO
	 * @param exmnmngId
	 * @return
	 */
	@Authority(authType = AuthType.UPDATE)
	@PutMapping("/sect/{srvyId}")
	public @ResponseBody CommonResponse<?> invstSurveyUpdateTitle(Model model,
			@RequestBody TmSrvySectUpdateDTO tmSrvySectUpdateDTO, @PathVariable String srvyId) {
		invstMngService.updateTmSrvyInfoTitle(tmSrvySectUpdateDTO);
		String resMsg = CommonUtils.getMessage("survey.surveyUpdate.update.complete");
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
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
		
		String resMsg = CommonUtils.getMessage("survey.surveySave.save.complete");
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
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
		
		String resMsg = CommonUtils.getMessage("survey.surveyDetail.delete.complete");
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, resMsg);
	}
}
