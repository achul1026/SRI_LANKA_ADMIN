package com.sl.tdbms.web.admin.web.controller.systemmng;

import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sl.tdbms.web.admin.common.dto.common.SearchCommonDTO;
import com.sl.tdbms.web.admin.common.dto.facilties.FacilitiesDTO;
import com.sl.tdbms.web.admin.common.entity.TlMvmneqLog;
import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.GroupCode;
import com.sl.tdbms.web.admin.common.enums.MenuType;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTlFixedCurRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTlMvmneqCurRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTmInstllcRoadRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTmVdsInstllcRepository;
import com.sl.tdbms.web.admin.common.repository.TcRoadMngRepository;
import com.sl.tdbms.web.admin.common.repository.TlFixedCurRepository;
import com.sl.tdbms.web.admin.common.repository.TlMvmneqCurRepository;
import com.sl.tdbms.web.admin.common.repository.TlMvmneqLogRepository;
import com.sl.tdbms.web.admin.common.repository.TmInstllcRoadRepository;
import com.sl.tdbms.web.admin.common.repository.TmVdsInstllcRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.web.service.systemmng.FacilitiesMngService;


@Controller
@RequestMapping("/systemmng/facilities")
public class FacilitiesMngController {
	
	@Autowired
    QTmInstllcRoadRepository qTmInstllcRoadRepository;
	
	@Autowired
	QTmVdsInstllcRepository qTmVdsInstllcRepository;
	
	@Autowired
	QTlFixedCurRepository qTlFixedCurRepository;
	
	@Autowired
	QTlMvmneqCurRepository qTlMvmneqCurRepository;
	
	@Autowired
    QTcCdInfoRepository qTcCdInfoRepository;
	
	@Autowired
    TmInstllcRoadRepository instllcRoadRepository;
	
	@Autowired
    TmVdsInstllcRepository instllcRepository;
	
	@Autowired
    TcRoadMngRepository tcRoadMngRepository;
	
	@Autowired
	TlFixedCurRepository tlFixedCurRepository;
	
	@Autowired
	TlMvmneqCurRepository tlMvmneqCurRepository;
	
	@Autowired
	TlMvmneqLogRepository tlMvmneqLogRepository;
	
	@Autowired
    FacilitiesMngService facilitiesMngService;
	
	/**
	 * @Method Name : facilitiesListPage
	 * @작성일 : 2024. 5. 20.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 시설물관리
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.LIST)
	@GetMapping
    public String facilitiesListPage(Model model, SearchCommonDTO searchCommonDTO, PagingUtils paging){
//		model.addAttribute("facilityTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.FACILITY_TYPE_CD.getCode()));
        return "views/systemmng/facilitiesList";
    }
	
	/**
	  * @Method Name : facilitiesListPageByType
	  * @작성일 : 2024. 8. 8.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 타입별 시설물 목록 조회
	  * @param typeCd
	  * @param searchCommonDTO
	  * @param paging
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/{typeCd}/list")
	public String facilitiesListPageByType(@PathVariable(name = "typeCd") String typeCd, SearchCommonDTO searchCommonDTO, PagingUtils paging , Model model) {
		long totalCnt = 0L;
		String resultHtml = "";
		List<FacilitiesDTO> facilitiesList = null;
		switch(typeCd) {
			case "FCT001" :
				facilitiesList = qTmVdsInstllcRepository.getFacilityVdsList(searchCommonDTO, paging, typeCd);
				totalCnt = qTmVdsInstllcRepository.getTotalVdsCount(searchCommonDTO, typeCd);
				resultHtml = "views/systemmng/facilitiesVdsList";
				break;
			case "FCT002" : 
				facilitiesList = qTlFixedCurRepository.getFacilityFixedList(searchCommonDTO, paging, typeCd);
				totalCnt = qTlFixedCurRepository.getTotalFixedCount(searchCommonDTO, typeCd);
				resultHtml = "views/systemmng/facilitiesFixedList";
				break;
			case "FCT003" :
				facilitiesList = qTlMvmneqCurRepository.getFacilityPortableList(searchCommonDTO, paging, typeCd);
				totalCnt = qTlMvmneqCurRepository.getTotalPortableCount(searchCommonDTO, typeCd);
				resultHtml = "views/systemmng/facilitiesPortableList";
				break;
		}
		paging.setTotalCount(totalCnt);
		model.addAttribute("facilitiesList", facilitiesList);
		model.addAttribute("searchInfo", searchCommonDTO);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("typeCd", typeCd);
		model.addAttribute("paging", paging);
		return resultHtml;
	}
	
	/**
	 * @Method Name : facilitiesSavePage
	 * @작성일 : 2024. 5. 20.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 시설물관리 등록
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.SAVE)
	@GetMapping("/{typeCd}/save")
	public String facilitiesSavePage(Model model, @PathVariable(name = "typeCd") String typeCd){
		String resultHtml = "";
		switch(typeCd) {
		case "FCT001" :
			resultHtml = "views/systemmng/facilitiesVdsSave";
			break;
		case "FCT002" :
			resultHtml = "views/systemmng/facilitiesFixedSave";
			break;
		case "FCT003" :
			resultHtml = "views/systemmng/facilitiesPortableSave";
			break;
		}
		model.addAttribute("typeCd", typeCd);
		model.addAttribute("drctCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.TRF_DRCT_CD.getCode()));
//		model.addAttribute("facilityTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.FACILITY_TYPE_CD.getCode()));
		model.addAttribute("roadCd", tcRoadMngRepository.getRoadCdList());
		return resultHtml;
	}
	
	/**
	  * @Method Name : facilitiesSave
	  * @작성일 : 2024. 8. 8.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시설물 등록
	  * @param facilitiesDTO
	  * @return
	  */
	@Authority(authType = AuthType.CREATE)
	@Transactional
	@PostMapping
	public @ResponseBody CommonResponse<?> facilitiesSave(FacilitiesDTO facilitiesDTO){
		String resMsg = CommonUtils.getMessage("facilities.facilitiesSave.fail");
		try {
			facilitiesMngService.facilitySave(facilitiesDTO);
			resMsg = CommonUtils.getMessage("facilities.facilitiesSave.complete");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
	}
	
	/**
	 * @Method Name : facilitiesDetailPage
	 * @작성일 : 2024. 6. 3.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 시설물관리 상세
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.DETAIL)
	@GetMapping("/{typeCd}/{instllcId}")
	public String facilitiesDetail(Model model, @PathVariable String instllcId, @PathVariable(name = "typeCd") String typeCd){
		FacilitiesDTO facility = null;
		String resultHtml = "";
		switch(typeCd) {
		case "FCT001" :
			facility = qTmVdsInstllcRepository.getFacilityVdsInfo(instllcId, GroupCode.TRF_DRCT_CD.getCode(), typeCd);
			resultHtml = "views/systemmng/facilitiesVdsDetail";
			break;
		case "FCT002" :
			facility = qTlFixedCurRepository.getFacilityFixedInfo(instllcId, GroupCode.TRF_DRCT_CD.getCode(), typeCd);
			resultHtml = "views/systemmng/facilitiesFixedDetail";
			break;
		case "FCT003" :
			facility = qTlMvmneqCurRepository.getFacilityPortableInfo(instllcId, GroupCode.TRF_DRCT_CD.getCode(), typeCd);
			resultHtml = "views/systemmng/facilitiesPortableDetail";
			break;
		}
		model.addAttribute("typeCd", typeCd);
		model.addAttribute("facilityInfo", facility);
		return resultHtml;
	}
	
	/**
	  * @Method Name : getMvmneqLogList
	  * @작성일 : 2024. 8. 13.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 이동형 Metro Count 이력 목록 조회
	  * @param instllcId
	  * @param paging
	  * @param model
	  * @return
	  */
	@Authority(authType = AuthType.READ)
	@GetMapping("/{instllcId}/log")
	public String getMvmneqLogList(@PathVariable(name = "instllcId") String instllcId, PagingUtils paging, Model model) {
		List<TlMvmneqLog> mvmneqLogList = qTlMvmneqCurRepository.getMvmneqLogList(instllcId, paging);
		long totalCnt = qTlMvmneqCurRepository.getTotalMvmneqLogCount(instllcId);
		paging.setTotalCount(totalCnt);
		model.addAttribute("mvmneqLogList", mvmneqLogList);
		model.addAttribute("totalCnt", totalCnt);
		model.addAttribute("paging", paging);
		return "views/systemmng/mvmneqLogList";
	}
	
	/**
	 * @Method Name : facilitiesUpdatePage
	 * @작성일 : 2024. 6. 3.
	 * @작성자 : TY.LEE
	 * @Method 설명 : 시설물관리 수정 페이지
	 * @param model
	 * @return
	 */
	@Authority(authType = AuthType.READ, menuType = MenuType.UPDATE)
	@GetMapping("/{typeCd}/{instllcId}/update")
	public String facilitiesUpdate(Model model, @PathVariable String typeCd, @PathVariable String instllcId){
		FacilitiesDTO facility = null;
		String resultHtml = "";
		switch(typeCd) {
		case "FCT001" :
			facility = qTmVdsInstllcRepository.getFacilityVdsInfo(instllcId, GroupCode.TRF_DRCT_CD.getCode(), typeCd);
			resultHtml = "views/systemmng/facilitiesVdsUpdate";
			break;
		case "FCT002" :
			facility = qTlFixedCurRepository.getFacilityFixedInfo(instllcId, GroupCode.TRF_DRCT_CD.getCode(), typeCd);
			resultHtml = "views/systemmng/facilitiesFixedUpdate";
			break;
		case "FCT003" :
			facility = qTlMvmneqCurRepository.getFacilityPortableInfo(instllcId, GroupCode.TRF_DRCT_CD.getCode(), typeCd);
			resultHtml = "views/systemmng/facilitiesPortableUpdate";
			break;
		}
		
		model.addAttribute("roadCd", tcRoadMngRepository.getRoadCdList());
		model.addAttribute("drctCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.TRF_DRCT_CD.getCode()));
		model.addAttribute("facilityTypeCd", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.FACILITY_TYPE_CD.getCode()));
		model.addAttribute("facilityInfo", facility);
		return resultHtml;
	}
	
	/**
	  * @Method Name : facilitiesUpdate
	  * @작성일 : 2024. 8. 8.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시설물 수정
	  * @param facilitiesDTO
	  * @param instllcId
	  * @return
	  */
	@Authority(authType = AuthType.UPDATE)
	@Transactional
	@PutMapping("/{typeCd}/{instllcId}")
	public @ResponseBody CommonResponse<?> facilitiesUpdate(FacilitiesDTO facilitiesDTO, @PathVariable String typeCd, @PathVariable String instllcId){
		String resMsg = CommonUtils.getMessage("facilities.facilitiesUpdate.fail");
		try {
			facilitiesMngService.facilityUpdate(facilitiesDTO, instllcId);
			resMsg = CommonUtils.getMessage("facilities.facilitiesUpdate.complete");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
			
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
	}
	
	/**
	  * @Method Name : facilitiesDelete
	  * @작성일 : 2024. 8. 8.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 시설물 삭제
	  * @param instllcId
	  * @return
	  */
	@Authority(authType = AuthType.DELETE)
	@Transactional
	@DeleteMapping("/{typeCd}/{instllcId}")
	public @ResponseBody CommonResponse<?> facilitiesDelete(@PathVariable(name = "typeCd") String typeCd, @PathVariable(name = "instllcId") String instllcId){
		String resMsg = CommonUtils.getMessage("facilities.facilitiesDelete.fail");
		try {
			switch(typeCd) {
			case "FCT001" :
				instllcRepository.deleteById(instllcId);
				break;
			case "FCT002" :
				tlFixedCurRepository.deleteById(instllcId);
				break;
			case "FCT003" :
				tlMvmneqCurRepository.deleteById(instllcId);
				tlMvmneqLogRepository.deleteAllByInstllcId(instllcId);
				break;
			}
			instllcRoadRepository.deleteByInstllcIdAndEqpmntClsf(instllcId, typeCd);
			
			resMsg = CommonUtils.getMessage("facilities.facilitiesDelete.complete");
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("code", 200);
			result.put("message", resMsg);
			return CommonResponse.successToData(result, "");
		} catch (Exception e) {
			return CommonResponse.ResponseCodeAndMessage(HttpStatus.BAD_REQUEST, resMsg);
		}
	}
}

