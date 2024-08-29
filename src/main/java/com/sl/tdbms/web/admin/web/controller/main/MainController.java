package com.sl.tdbms.web.admin.web.controller.main;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.dto.gis.FacilitiesGISDetailDTO;
import com.sl.tdbms.web.admin.common.dto.gis.FacilitiesGISFixedMetroCountDTO;
import com.sl.tdbms.web.admin.common.dto.gis.FacilitiesGISMoveMetroCountDTO;
import com.sl.tdbms.web.admin.common.dto.gis.FacilitiesGISSearchDTO;
import com.sl.tdbms.web.admin.common.dto.gis.FacilitiesGISVDSDTO;
import com.sl.tdbms.web.admin.common.dto.gis.SurveyGISDTO;
import com.sl.tdbms.web.admin.common.dto.gis.SurveyGISDetailDTO;
import com.sl.tdbms.web.admin.common.dto.gis.SurveyGISSearchDTO;
import com.sl.tdbms.web.admin.common.entity.TcAuthGrp;
import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.GroupCode;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTmExmnMngRepository;
import com.sl.tdbms.web.admin.common.repository.TcAuthGrpRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.web.service.gis.GISService;

import lombok.RequiredArgsConstructor;


/**
 * The type Main controller.
 */
@Controller
@RequestMapping("/main")
@RequiredArgsConstructor
public class MainController {

	final private TcAuthGrpRepository tcAuthGrpRepository;
	final private QTcCdInfoRepository qTcCdInfoRepository;
	final private GISService gISService;
	final private QTmExmnMngRepository qTmExmnMngRepository;


	/**
	 * Main string.
	 *
	 * @param model the model
	 * @return the string
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping
    public String main(Model model){
		String authgrpId = LoginMngrUtils.getTcUserMngInfo().getAuthgrpId();
		if (!CommonUtils.isNull(authgrpId)) {
			TcAuthGrp tcAuthMng = tcAuthGrpRepository.findOneByAuthgrpId(authgrpId);
			if (tcAuthMng.getBscauthYn().equals("Y")) {
				model.addAttribute("baseAuthYn", "Y");
			} 
		}
		LoginMngrDTO loginMngrDTO = LoginMngrUtils.getTcUserMngInfo();
		if(loginMngrDTO.getUserType().equals(UserTypeCd.SUPER)){
			model.addAttribute("bffltdCdList", qTcCdInfoRepository.getTcCdInfoListByGrpCd(GroupCode.BFFLTD_CD.getCode()));
		}
		model.addAttribute("loginMngrDTO", loginMngrDTO);
        return "views/main/main";
    }

	/**
	 * methodName : getSurvey
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 조사 gis 정보 목록
	 *
	 * @param surveyGISSearchDTO
	 * @return common response
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/survey")
	public @ResponseBody CommonResponse<?> getSurvey(SurveyGISSearchDTO surveyGISSearchDTO) {
		List<SurveyGISDTO> gisDataList =  gISService.getSurveyGisDataList(surveyGISSearchDTO);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",gisDataList);
	}

	/**
	 * methodName : getSurveyDetail
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 조사 gis 상세 정보
	 *
	 * @param exmnmngId
	 * @return common response
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/survey/{exmnmngId}")
	public @ResponseBody CommonResponse<?> getSurveyDetail(@PathVariable("exmnmngId") String exmnmngId) {
		SurveyGISDetailDTO gisData =  qTmExmnMngRepository.getSurveyGisDataDetail(exmnmngId);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",gisData);
	}
	
	/**
	 * methodName : getFacilitiesVDS
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 시설물 위치 vds 좌표 목록
	 *
	 * @param fcilitiesGISSearchDTO
	 * @return common response
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/facilities/vds")
	public @ResponseBody CommonResponse<?> getFacilitiesVDS(FacilitiesGISSearchDTO fcilitiesGISSearchDTO) {
		List<FacilitiesGISVDSDTO> vdsDataList =  gISService.getFacilitiesVDSList(fcilitiesGISSearchDTO);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",vdsDataList);
	}

	/**
	 * methodName : getFacilitiesVDSDetail
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 시설물 위치 vds 상세 정보
	 *
	 * @param instllcId
	 * @return common response
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/facilities/vds/{instllcId}")
	public @ResponseBody CommonResponse<?> getFacilitiesVDSDetail(@PathVariable("instllcId") String instllcId) {
		List<FacilitiesGISDetailDTO> detailList =  gISService.getFacilitiesVDSDetail(instllcId);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",detailList);
	}

	/**
	 * methodName : getFacilitiesFixedMetroCount
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 시설물 위치 고정형 메트로 카운트 좌표 목록
	 *
	 * @param fcilitiesGISSearchDTO
	 * @return common response
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/facilities/fixed/metrocount")
	public @ResponseBody CommonResponse<?> getFacilitiesFixedMetroCount(FacilitiesGISSearchDTO fcilitiesGISSearchDTO) {
		List<FacilitiesGISFixedMetroCountDTO> fixedMetroCountDataList =  gISService.getFacilitiesFixedMetroCountList(fcilitiesGISSearchDTO);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",fixedMetroCountDataList);
	}

	/**
	 * methodName : getFacilitiesFixedMetroCountDetail
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 시설물 위치 고정형 메트로 카운트 상세 정보
	 *
	 * @param instllcId
	 * @return common response
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/facilities/fixed/metrocount/{instllcId}")
	public @ResponseBody CommonResponse<?> getFacilitiesFixedMetroCountDetail(@PathVariable("instllcId") String instllcId) {
		List<FacilitiesGISDetailDTO> detailList =  gISService.getFacilitiesFixedMetroCountDetail(instllcId);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",detailList);
	}

	/**
	 * methodName : getFacilitiesMoveMetroCount
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 시설물 위치 이동형 메트로카운트 좌표 목록
	 *
	 * @param fcilitiesGISSearchDTO
	 * @return common response
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/facilities/move/metrocount")
	public @ResponseBody CommonResponse<?> getFacilitiesMoveMetroCount(FacilitiesGISSearchDTO fcilitiesGISSearchDTO) {
		List<FacilitiesGISMoveMetroCountDTO> moveMetroCountDataList =  gISService.getFacilitiesMoveMetroCountList(fcilitiesGISSearchDTO);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",moveMetroCountDataList);
	}

	/**
	 * methodName : getFacilitiesMoveMetroCountDetail
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 시설물 위치 이동형 메트로카운트 상세 정보
	 *
	 * @param instllcId
	 * @return common response
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/facilities/move/metrocount/{instllcId}")
	public @ResponseBody CommonResponse<?> getFacilitiesMoveMetroCountDetail(@PathVariable("instllcId") String instllcId) {
		List<FacilitiesGISDetailDTO> detailList =  gISService.getFacilitiesMoveMetroCountDetail(instllcId);
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK,"",detailList);
	}
}
