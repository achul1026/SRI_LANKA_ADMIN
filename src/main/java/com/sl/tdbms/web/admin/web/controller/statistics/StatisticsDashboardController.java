package com.sl.tdbms.web.admin.web.controller.statistics;

import com.sl.tdbms.web.admin.common.component.PopulationStatisticsComponent;
import com.sl.tdbms.web.admin.common.dto.statistics.*;
import com.sl.tdbms.web.admin.common.enums.AuthType;
import com.sl.tdbms.web.admin.common.enums.PersonalTransportationChartType;
import com.sl.tdbms.web.admin.common.enums.RoadSideChartType;
import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;
import com.sl.tdbms.web.admin.common.enums.code.PopStatsTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.QTsPopMngRepository;
import com.sl.tdbms.web.admin.common.repository.TmExmnMngRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.config.authentication.Authority;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.CommonResponse;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;
import com.sl.tdbms.web.admin.web.service.statistics.StatisticsService;
import com.sl.tdbms.web.admin.web.service.systemmng.CodeMngService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The type Statistics dashboard controller.
 */
@Controller
@RequestMapping("/statistics/dashboard")
public class StatisticsDashboardController {
	@Autowired
    StatisticsService statisticsService;

	@Autowired
	TmExmnMngRepository tmExmnMngRepository;

	@Autowired
    QTsPopMngRepository qTsPopMngRepository;

	@Autowired
    CodeMngService codeMngService;

	@Autowired
    PopulationStatisticsComponent populationStatisticsComponent;

	/**
	 * Statistics dashboard string.
	 *
	 * @param model the model
	 * @return string string
	 * @Method Name : statistics dashboard
	 * @Method 설명 : 통계
	 * @작성일 : 2024. 06. 17.
	 * @작성자 : TY.LEE
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping
    public String statisticsDashboard(Model model ){
		//model.addAttribute("years", tmExmnMngRepository.getSurveySearchYears(ExmnTypeCd.OD.getCode()));
        return "views/statistics/statisticsDashboard";

    }


	/**
	 * methodName : statisticsDashboardByType
	 * author : Peo.Lee
	 * date : 2024-08-13
	 * description : 통계 화면 위 버튼 화면 전환
	 * @param model
	 * @param type
	 * @return String
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/{type}")
    public String statisticsDashboardByType(Model model, @PathVariable("type") String type){
        String returnPage = "tags/statistics/odStatistics";
        switch (type){
			case "OD":
				returnPage = "tags/statistics/odStatistics";//model.addAttribute("years", tmExmnMngRepository.getSurveySearchYears(ExmnTypeCd.OD.getCode()));
				break;
            case "MCC":
                returnPage = "tags/statistics/mccStatistics";
				//model.addAttribute("years", tmExmnMngRepository.getSurveySearchYears(ExmnTypeCd.MCC.getCode()));
                break;
			case "AXLELOAD":
				returnPage = "tags/statistics/axleloadStatistics";
				model.addAttribute("fixedMetroCountList", statisticsService.getMetroCntFixedFcltList());
				break;
            case "TRAFFIC":
            	model.addAttribute("vdsList", statisticsService.getVdsFcltList());
            	returnPage = "tags/statistics/trafficStatistics";
                break;
            case "AREA":
            	model.addAttribute("popStatTypeCd", PopStatsTypeCd.values());
            	model.addAttribute("bffltdCd", codeMngService.getTcCdInfoForGrpCd("BFFLTD_CD"));
            	model.addAttribute("years", qTsPopMngRepository.findMinMaxYear());
                returnPage = "tags/statistics/areaStatistics";
                break;
			case "TRAFFICPERIOD":
				model.addAttribute("vdsList", statisticsService.getVdsFcltList());
				returnPage = "tags/statistics/trafficPeriodStatistics";
				break;
            default:
				//잘못된 url 형식 에러 페이지
                returnPage = "views/common/errorPage";
                break;
        }
        return returnPage;

    }

	/**
	 * @param searchDTO the search dto
	 * @return common response
	 * @Method Name : statisticsDashboardMccSearch
	 * @Method 설명 : MCC 통계 데이터
	 * @작성일 : 2024. 06. 17.
	 * @작성자 : NK.KIM
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/mcc/search")
    public @ResponseBody CommonResponse<?> statisticsDashboardMCCSearch(TlExmnRsltStatisticsSearchDTO searchDTO) {
        Map<String,Object> result = new HashMap<>();
        TlTrfvlRsltStatisticsDTO tlTrfvlRsltStatisticsDTO = statisticsService.getMCCStatisticsList(searchDTO);
        result.put("statisticsList",tlTrfvlRsltStatisticsDTO.getTlTrfvlRsltStatisticsList());
        result.put("colorArrays",tlTrfvlRsltStatisticsDTO.getColorArrays());
        result.put("directionList",tlTrfvlRsltStatisticsDTO.getDirectionList());
        result.put("searchDTO",searchDTO);
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "",result);
    }

	@Authority(authType = AuthType.READ)
	@GetMapping("/mcc/excel/download")
	public void mccStatsExcelDownload(HttpServletResponse response, TlExmnRsltStatisticsSearchDTO searchDTO) throws IOException {
		try {
			statisticsService.mccExcelDownLoad(response,searchDTO);
		} catch (CommonException e) {
			throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED);
		}
	}

	/**
	 * Statistics dashboard sheet search common response.
	 *
	 * @param searchDTO the search dto
	 * @return common response
	 * @Method Name : statisticsDashboardYearSearch
	 * @Method 설명 : OD 통계 > 날짜 검색
	 * @작성일 : 2024. 06. 17.
	 * @작성자 : NK.KIM
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/od/search/year")
    public @ResponseBody CommonResponse<?> statisticsDashboardYearSearch(TlExmnRsltStatisticsSearchDTO searchDTO) {
		Map<String,Object> result = new HashMap<>();
		//result.put("sheetList",qTmSrvyInfoRepository.getSurveyListForStatistics(searchDTO));
		List<String> years = null;
		if(ExmnTypeCd.OD.equals(searchDTO.getExmnTypeCd())){
			years = tmExmnMngRepository.getOdStatisticsSearchYears(searchDTO);
		}else{
			years = tmExmnMngRepository.getOdStatisticsSearchYears(searchDTO);
		}
		result.put("years",years);

        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "",result);
    }

	/**
	 * @param exmnType  the exmn type
	 * @return string string
	 * @Method Name : statisticsDashboardOdSearch
	 * @Method 설명 : OD 통계 데이터
	 * @작성일 : 2024. 06. 17.
	 * @작성자 : NK.KIM
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/{exmnType}/search")
    public String statisticsDashboardODSearch(@PathVariable String exmnType) {
    	String returnPage = "";
    	if("od".equals(exmnType)){
    		returnPage = "tags/statistics/od/odChartStats";
    	}else{
    		returnPage = "tags/statistics/od/roadSideChartStats";
    	}
    	return returnPage;
    }

	/**
	 * Statistics dashboard od search data common response.
	 *
	 * @param exmnType  the exmn type
	 * @param searchDTO the search dto
	 * @return common response
	 * @Method Name : statisticsDashboardOdSearch
	 * @Method 설명 : OD 통계 데이터
	 * @작성일 : 2024. 06. 17.
	 * @작성자 : NK.KIM
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/{exmnType}/searchData")
    public @ResponseBody CommonResponse<?> statisticsDashboardODSearchData(@PathVariable String exmnType,TlExmnRsltStatisticsSearchDTO searchDTO) {
        Map<String,Object> result = new HashMap<>();
		List<TlSrvyRsltStatisticsDTO> statisticsChartList = null;
		if("od".equals(exmnType)){
			statisticsChartList = statisticsService.getODStatisticsList(searchDTO);
		}else{
			statisticsChartList = statisticsService.getRoadSideStatisticsList(searchDTO);
		}

        result.put("statisticsChartList",statisticsChartList);
        result.put("searchDTO",searchDTO);
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "",result);
    }

	/**
	 * Statistics excel down load.
	 *
	 * @param response  the response
	 * @param excelType the excel type
	 * @param searchDTO the search dto
	 * @return
	 * @Method Name : statisticsDashboardOdSearch
	 * @Method 설명 : OD 통계 검색 차트 데이터
	 * @작성일 : 2024. 06. 17.
	 * @작성자 : NK.KIM
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/{excelType}/excelDownload")
    public void statisticsExcelDownLoad(
    		HttpServletResponse response,
    		@PathVariable String excelType, 
    		TlExmnRsltStatisticsSearchDTO searchDTO
    		) {
    	try {
			statisticsService.excelDownLoad(response,searchDTO,excelType);
    	} catch (CommonException e) {
    		throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED);
    	}
    }

	/**
	 * Statistics excel down load.
	 *
	 * @param response  the response
	 * @param searchDTO the search dto
	 * @return
	 * @Method Name : populationStatsExcelDownload
	 * @Method 설명 : OD 통계 검색 차트 데이터
	 * @작성일 : 2024. 06. 17.
	 * @작성자 : NK.KIM
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/popstats/excelDownload")
    public void statisticsExcelDownLoad(
    		HttpServletResponse response,
    		TsPopulationStatisticsSearchDTO searchDTO
    		) {
    	try {
			populationStatisticsComponent.excelDownLoad(response,searchDTO);
    	} catch (CommonException e) {
    		throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED);
    	}
    }

	/**
	 * Statistics dashboard od search chart common response.
	 *
	 * @param exmnType                        the exmn type
	 * @param roadSideChartType               the road side chart type
	 * @param personalTransportationChartType the personal transportation chart type
	 * @param searchDTO                       the search dto
	 * @return common response
	 * @Method Name : statisticsDashboardOdSearch
	 * @Method 설명 : OD 통계 검색 차트 데이터
	 * @작성일 : 2024. 06. 17.
	 * @작성자 : NK.KIM
	 */
	@Authority(authType = AuthType.READ)
    @PostMapping("/{exmnType}/search/chart")
    public @ResponseBody CommonResponse<?> statisticsDashboardODSearchChart(
    		@PathVariable String exmnType, 
    		@RequestParam(required = false) RoadSideChartType roadSideChartType ,
    		@RequestParam(required = false) PersonalTransportationChartType personalTransportationChartType, 
    		@RequestBody TlExmnRsltStatisticsSearchDTO searchDTO) {
		TlSrvyRsltStatisticsDTO tlSrvyRsltStatisticsDTO = new TlSrvyRsltStatisticsDTO();
		if("od".equals(exmnType)){
			tlSrvyRsltStatisticsDTO.setAppendId(personalTransportationChartType.getAppendId());
			tlSrvyRsltStatisticsDTO.setChartType(personalTransportationChartType.getChartType());
			tlSrvyRsltStatisticsDTO = statisticsService.getOdStatisticsListByType(tlSrvyRsltStatisticsDTO ,personalTransportationChartType , searchDTO);
		}else{
			tlSrvyRsltStatisticsDTO.setAppendId(roadSideChartType.getAppendId());
			tlSrvyRsltStatisticsDTO.setChartType(roadSideChartType.getChartType());
			tlSrvyRsltStatisticsDTO = statisticsService.getRoadSideStatisticsListByType(tlSrvyRsltStatisticsDTO ,roadSideChartType,searchDTO);
		}
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "",tlSrvyRsltStatisticsDTO);	
    }

	/**
	 * Statistics dashboard area search string.
	 *
	 * @param model     the model
	 * @param searchDTO the search dto
	 * @return the string
	 * @Method Name : statisticsDashboardAreaSearch
	 * @Method 설명 : 지역 별 인구 통계 데이터
	 * @작성일 : 2024. 06. 27.
	 * @작성자 : KY.LEE
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/area/search")
    public String statisticsDashboardAreaSearch(Model model,TsPopulationStatisticsSearchDTO searchDTO) {
    	String returnPage = null;
    	
    	switch(searchDTO.getPopStatTypeCd()) {
	    	case POPULATION_BY_REGION:
	    		returnPage = "tags/statistics/popstats/regionPopulationStats";
	    		break;
	    	case POPULATION_BY_AGE:
	    		returnPage = "tags/statistics/popstats/agePopulationStats";
	    		break;
			case POPULATION_BY_ECONOMIC_ACTIBITY:
				returnPage = "tags/statistics/popstats/economicActibityPopulationStats";
				break;
			case THE_NUMBER_OF_STUDENTS:
				returnPage = "tags/statistics/popstats/stdntPopulationStats";
				break;
			case POPULATION_BY_HOUSEHOLD:
				returnPage = "tags/statistics/popstats/houseHoldPopulationStats";
				break;
			case HOUSEHOLD_POPULATION_BY_HOUSING_TYPE:
				returnPage = "tags/statistics/popstats/housingTypePopulationStats";
				break;
			case NUMBER_OF_POPULATION_BY_OCCUPIE_HOUSING_UNIT:
				returnPage = "tags/statistics/popstats/occHousingUnitPopulationStats";
				break;
			case SCHOOL_STATUS_BY_REGION_NATIONAL:
				returnPage = "tags/statistics/popstats/nationalSchoolPopulationStats";
				break;
			case SCHOOL_STATUS_BY_REGION_PRIVATE:
				returnPage = "tags/statistics/popstats/privateSchoolPopulationStats";
				break;
			case SCHOOL_STATUS_BY_REGION_SPECIAL:
				returnPage = "tags/statistics/popstats/privateSchoolPopulationStats";
				break;
			case SCHOOL_STATUS_BY_REGION_PIRIVENA:
				returnPage = "tags/statistics/popstats/pirivenaSchoolPopulationStats";
				break;
			case NUMBER_OF_FACULTY_BY_REGION_EDUCATION:
				returnPage = "tags/statistics/popstats/facultyEducationPopulationStats";
				break;
			case NUMBER_OF_FACULTY_BY_REGION_STUDENTS:
				returnPage = "tags/statistics/popstats/facultyStudentPopulationStats";
				break;
			case NUMBER_OF_FACULTY_BY_REGION_CYCLE:
				returnPage = "tags/statistics/popstats/facultyCyclePopulationStats";
				break;
			case THE_NUMBER_OF_FACULTY_BY_SCHOOL_SYSTEM:
				returnPage = "tags/statistics/popstats/schoolSystemPopulationStats";
				break;
			case STATUS_OF_INDUSTRIAL_FACILITIES_INFORMAL:
				returnPage = "tags/statistics/popstats/idstryFacInformalPopulationStats";
				break;
			case STATUS_OF_INDUSTRIAL_FACILITIES_FORMAL:
				returnPage = "tags/statistics/popstats/idstryFacFormalPopulationStats";
				break;
			case INDUSTRIAL_PRODUCTIVITY_BY_SECTORS:
				returnPage = "tags/statistics/popstats/indstrialOrdSectorsPopulationStats";
				break;
			case STATUS_OF_EMPLOYMENT:
				returnPage = "tags/statistics/popstats/emplymntStatusPopulationStats";
				break;
			case LAND_AREA:
				returnPage = "tags/statistics/popstats/landAreaPopulationStats";
				break;
			case GROSS_REGIONAL_PRODUCT:
				returnPage = "tags/statistics/popstats/grossRegnProdPopulationStats";
				break;
			case VEHICLE_REGISTRATION:
				returnPage = "tags/statistics/popstats/vehicleRegPopulationStats";
				break;
			default:
				throw new CommonException(ErrorCode.ENTITY_DATA_NOT_FOUND);
    	}
		model.addAttribute("statsList", statisticsService.getTsPopMngStatsList(searchDTO));
    	model.addAttribute("searchDTO", searchDTO);
        return returnPage;
    }

	/**
	 * Pop stats region common response.
	 *
	 * @param model     the model
	 * @param searchDTO the search dto
	 * @return the common response
	 * @Method Name : popStatsRegion
	 * @Method 설명 : 인구통계데이터 지역정보 조회
	 * @작성일 : 2024. 06. 27.
	 * @작성자 : KY.LEE
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/area/region")
    @ResponseBody
    public CommonResponse<?> popStatsRegion(Model model,TsPopulationStatisticsSearchDTO searchDTO) {
    	TsPopulationStatisticsRegionDTO result = populationStatisticsComponent.getRegionInfo(searchDTO);
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "", result);
    }

	/**
	 * Gets pop stats data.
	 *
	 * @param model     the model
	 * @param searchDTO the search dto
	 * @return the pop stats data
	 * @Method Name : popStatsRegion
	 * @Method 설명 : 인구통계데이터 차트데이터 조회
	 * @작성일 : 2024. 06. 27.
	 * @작성자 : KY.LEE
	 */
	@Authority(authType = AuthType.READ)
    @PostMapping("/area/popstats")
    @ResponseBody
    public CommonResponse<?> getPopStatsData(Model model,TsPopulationStatisticsSearchDTO searchDTO) {
    	TsPopulationRsltStatisticsDTO result = populationStatisticsComponent.getStatisticsData(searchDTO);
    	return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "", result);
    }

	/**
	 * Statistics gis string.
	 *
	 * @param model the model
	 * @return string string
	 * @Method Name : gis statistics
	 * @Method 설명 : GIS통계
	 * @작성일 : 2024. 07. 08.
	 * @작성자 : TY.LEE
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/gis")
    public String statisticsGis(Model model ){
        return "views/statistics/gisStatistics";
    }

	/**
	 * Gets traffic stats facility type.
	 *
	 * @param fcltsType the fclts type
	 * @return traffic stats facility type
	 * @Method Name : getTrafficStatsFacilityType
	 * @Method 설명 : 교통량 통계 > 장비 id 정보 조회
	 * @작성일 : 2024. 07. 25.
	 * @작성자 : KC.KIM
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/facilityType")
    public @ResponseBody CommonResponse<?> getTrafficStatsFacilityType(@RequestParam(required = false) String fcltsType) {
		Map<String,Object> result = new HashMap<>();
		if(fcltsType.equals("VDS")) {
			result.put("fcltsList", statisticsService.getVdsFcltList());
		}else if(fcltsType.equals("METROCOUNT_FIXED")) {
			result.put("fcltsList", statisticsService.getMetroCntFixedFcltList());
		}else if(fcltsType.equals("METROCOUNT_MOVED")) {
			result.put("fcltsList", statisticsService.getMetroCntMovedFcltList());
		}
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "", result);
    }

	/**
	 * Statistics dashboard traffic search common response.
	 *
	 * @param searchDTO the search dto
	 * @return common response
	 * @Method Name : statisticsDashboardTrafficSearch
	 * @Method 설명 : 교통량 통계 데이터
	 * @작성일 : 2024. 07. 26.
	 * @작성자 : KC.KIM
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/traffic/search")
    public @ResponseBody CommonResponse<?> statisticsDashboardTrafficSearch(TsTrafficStatisticsSearchDTO searchDTO) {
        Map<String,Object> result = new HashMap<>();
        
        TsVdsRsltStatisticsDTO tsVdsRsltStatisticsDTO = statisticsService.getTrafficStatisticsList(searchDTO);
        
        result.put("colorArrays", tsVdsRsltStatisticsDTO.getColorArrays());
        result.put("trfStatisticsList", tsVdsRsltStatisticsDTO.getTsVdsTrfRsltStatisticsList());
        result.put("spdStatisticsList", tsVdsRsltStatisticsDTO.getTsVdsSpdRsltStatisticsList());
        result.put("totTrfvlm", tsVdsRsltStatisticsDTO.getTotTrfvlm());
        result.put("totAvgSpd", tsVdsRsltStatisticsDTO.getTotAvgSpd());
        result.put("searchDTO",searchDTO);
        
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "",result);
    }

	/**
	 * Traffic statistics download.
	 *
	 * @param response  the response
	 * @param searchDTO the search dto
	 * @return
	 * @Method Name : trafficStatisticsDownload
	 * @Method 설명 : 교통량 통계 엑셀 다운로드
	 * @작성일 : 2024. 08. 05.
	 * @작성자 : KC.KIM
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/traffic/excelDownload")
    public void trafficStatisticsDownload(HttpServletResponse response, TsTrafficStatisticsSearchDTO searchDTO) throws IOException  {
    	try {
    		statisticsService.trafficStatisticsExcelDownLoad(response, searchDTO);
    	} catch (Exception e) {
    		throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED);
    	}
    }

	/**
	 * M cc search for additional information common response.
	 *
	 * @param searchType the search type
	 * @param searchDTO  the search dto
	 * @return common response
	 * @Method Name : mCCSearchForAdditionalInformation
	 * @Method 설명 : MCC 통계 road, location, year 정보 조회
	 * @작성일 : 2024. 08. 05.
	 * @작성자 : KC.KIM
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/mcc/{searchType}/search")
    public @ResponseBody CommonResponse<?> mCCSearchForAdditionalInformation(
    		@PathVariable String searchType, TlExmnRsltStatisticsSearchDTO searchDTO) {
        List<Map<String, Object>> dataList = statisticsService.getMccStatsAdditionalInformation(searchType, searchDTO);
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "",dataList);
    }

	/**
	 * Traffic search for additional information common response.
	 *
	 * @param searchType the search type
	 * @param searchDTO  the search dto
	 * @return common response
	 * @Method Name : trafficSearchForAdditionalInformation
	 * @Method 설명 : 교통량 통계 site, year 정보 조회
	 * @작성일 : 2024. 08. 05.
	 * @작성자 : KC.KIM
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/traffic/{searchType}/search")
    public @ResponseBody CommonResponse<?> trafficSearchForAdditionalInformation(
    		@PathVariable String searchType, TsTrafficStatisticsSearchDTO searchDTO) {
        Map<String,Object> result = new HashMap<>();
        List<Map<String, Object>> dataList = statisticsService.getTrafficStatsAdditionalInformation(searchType, searchDTO);
        result.put("dataList",dataList);
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "",result);
    }

	/**
	 * methodName : getSiteIdList
	 * author : Peo.Lee
	 * date : 2024-08-14
	 * description : 지역별통계 site Id 조회
	 * @param searchDTO
	 * @return CommonResponse<?>
	 */
	@Authority(authType = AuthType.READ)
	@PostMapping("/period/list")
	@ResponseBody
	public CommonResponse<?> getSiteIdList(@RequestBody StatisticsByPeriodSerachDTO searchDTO){
		List<PeriodSiteDTO> result = null;
		if(!CommonUtils.isNull(searchDTO.getSearchSurveyType())){
			switch(searchDTO.getSearchSurveyType()){
				case "VDS" :
					result = statisticsService.getSiteIdListFromVDS(searchDTO);
					break;
				case "METROCOUNT_FIXED"	:
					result = statisticsService.getSiteIdListFromMetroCntFixed(searchDTO);
					break;
				case "METROCOUNT_MOVED" :
					result = statisticsService.getSiteIdListFromMetroCntMoved(searchDTO);
					break;
				case "MCC" :
					result = statisticsService.getRoadInfoListFromMcc(searchDTO);
					break;
				default:
					throw new CommonException(ErrorCode.INVALID_PARAMETER);
			}
		}
		return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "",result);
	}

	/**
	 * methodName : statisticsTrafficPeroid
	 * author : Peo.Lee
	 * date : 2024-08-20
	 * description : 기간별 통계 차트 화면
	 * @param model
	 * @return String
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/period/stats")
	public String statisticsTrafficPeriod(Model model){
		return "tags/statistics/period/periodStats";
	}

	/**
	 * methodName : getPeriodStatsChart
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : 기간별통계 차트데이터
	 * @param searchDTO
	 * @return CommonResponse<?>
	 */
	@Authority(authType = AuthType.READ)
	@GetMapping("/period/chart")
	public @ResponseBody CommonResponse<?> getPeriodStatsChart(StatisticsByPeriodSerachDTO searchDTO){
		TlPeriodRsltStatisticsDTO result = null;
		if(!CommonUtils.isNull(searchDTO.getSearchSurveyType())){
			switch(searchDTO.getSearchSurveyType()){
				case "VDS" :
					result = statisticsService.getVDSChartData(searchDTO);
					break;
				case "METROCOUNT_FIXED"	:
					result = statisticsService.getMetroCntFixedChartData(searchDTO);
					break;
				case "METROCOUNT_MOVED" :
					result = statisticsService.getMetroCntMovedChartData(searchDTO);
					break;
				case "MCC" :
					result = statisticsService.getMccChartData(searchDTO);
					break;
				default:
					throw new CommonException(ErrorCode.INVALID_PARAMETER);
			}
		}
		return new CommonResponse<>(HttpStatus.OK,"차트 조회 성공",result);
	}

	/**
     * @Method Name : statisticsDashboardAxleloadSearch
     * @작성일 : 2024. 08. 13.
     * @작성자 : KC.KIM
     * @Method 설명 : AXLELOAD 통계 데이터
     * @param searchDTO
     * @return
     */
    @Authority(authType = AuthType.READ)
    @GetMapping("/axleload/search")
    public @ResponseBody CommonResponse<?> statisticsDashboardAxleloadSearch(TsTrafficStatisticsSearchDTO searchDTO) {
        Map<String,Object> result = new HashMap<>();
        
        TsVdsRsltStatisticsDTO tsVdsRsltStatisticsDTO = statisticsService.getAxleloadStatisticsList(searchDTO);
        
        result.put("colorArrays", tsVdsRsltStatisticsDTO.getColorArrays());
        result.put("trfStatisticsList", tsVdsRsltStatisticsDTO.getTsVdsTrfRsltStatisticsList());
        result.put("axleStatisticsList", tsVdsRsltStatisticsDTO.getTsAxleSpdRsltStatisticsList());
        result.put("searchDTO",searchDTO);
        
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "", result);
    }
    
    /**
     * @Method Name : axleloadSearchForAdditionalInformation
     * @작성일 : 2024. 08. 13.
     * @작성자 : KC.KIM
     * @Method 설명 : AXLELOAD 통계 year 정보 조회
     * @param searchDTO
     * @return
     */
    @Authority(authType = AuthType.READ)
    @GetMapping("/axleload/{searchType}/search")
    public @ResponseBody CommonResponse<?> axleloadSearchForAdditionalInformation(
    		@PathVariable String searchType, TsTrafficStatisticsSearchDTO searchDTO) {
        Map<String,Object> result = new HashMap<>();
        List<String> dataList = statisticsService.getAxleloadStatsAdditionalInformation(searchType, searchDTO);
        result.put("dataList",dataList);
        return CommonResponse.ResponseCodeAndMessage(HttpStatus.OK, "", result);
    }
    
    /**
	 * Statistics excel down load.
	 *
	 * @param response  the response
	 * @param searchDTO the search dto
	 * @return
     * @throws IOException 
	 * @Method Name : axleloadStatsExcelDownload
	 * @Method 설명 : axleload 통계 데이터 다운로드
	 * @작성일 : 2024. 08. 13.
	 * @작성자 : KC.KIM
	 */
	@Authority(authType = AuthType.READ)
    @GetMapping("/axleload/excelDownload")
    public void axleloadStatsExcelDownload(HttpServletResponse response, TsTrafficStatisticsSearchDTO searchDTO) throws IOException {
    	try {
    		statisticsService.axleloadExcelDownLoad(response,searchDTO);
    	} catch (CommonException e) {
    		throw new CommonException(ErrorCode.FILE_DOWNLOAD_FAILED);
    	}
    }
}