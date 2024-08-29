package com.sl.tdbms.web.admin.web.service.gis;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.tdbms.web.admin.common.component.ExcelDownloadComponent;
import com.sl.tdbms.web.admin.common.dto.gis.*;
import com.sl.tdbms.web.admin.common.entity.TcCdGrp;
import com.sl.tdbms.web.admin.common.enums.GroupCode;
import com.sl.tdbms.web.admin.common.repository.*;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.atp.Switch;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * The type Gis service.
 */
@Service
@RequiredArgsConstructor
public class GISService {

	final private TmExmnMngRepository tmExmnMngRepository;
	final private TlMvmneqCurRepository tlMvmneqCurRepository;
	final private TlFixedCurRepository tlFixedCurRepository;
	final private TmVdsInstllcRepository tmVdsInstllcRepository;
	final private TcCdGrpRepository tcCdGrpRepository;
	final private ExcelDownloadComponent excelDownloadComponent;
	private final TcCdInfoRepository tcCdInfoRepository;
	private final TsFixedYyRepository tsFixedYyRepository;
	private final TsMvmneqYyRepository tsMvmneqYyRepository;
	private final TsVdsYyRepository tsVdsYyRepository;

	/**
	 * methodName : getSurveyGisDataList
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 조사 gis 정보 목록
	 *
	 * @param surveyGISSearchDTO
	 * @return list
	 */
	public List<SurveyGISDTO> getSurveyGisDataList(SurveyGISSearchDTO surveyGISSearchDTO){
		//ver1 dsd > taz 매핑 테이블 활용
		/*List<Map<String,Object>> result = tmExmnMngRepository.getSurveyGisDataListVer1(surveyGISSearchDTO);
		 */

		//ver2 dsd > taz 매핑 테이블 미활용
		List<Map<String,Object>> result = tmExmnMngRepository.getSurveyGisDataList(surveyGISSearchDTO);
		List<SurveyGISDTO> surveyGisDataList = null;
		if(!CommonUtils.isListNull(result)){
			ObjectMapper objectMapper = new ObjectMapper();
			surveyGisDataList = result.stream()
					.map(surveyGisData -> {
						SurveyGISDTO surveyGISDTO = new SurveyGISDTO();
						surveyGISDTO.setExmnmngId(String.valueOf(surveyGisData.get("exmnmngId")));
						surveyGISDTO.setExmnType(String.valueOf(surveyGisData.get("exmnType")));
						surveyGISDTO.setExmnNm(String.valueOf(surveyGisData.get("exmnNm")));
						surveyGISDTO.setExmnLc(String.valueOf(surveyGisData.get("exmnLc")));
						surveyGISDTO.setLon(String.valueOf(surveyGisData.get("lon")));
						surveyGISDTO.setLat(String.valueOf(surveyGisData.get("lat")));
						return surveyGISDTO;
					})
					.collect(Collectors.toList());
		}

		return surveyGisDataList;
	}

	/**
	 * methodName : getFacilitiesVDSList
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 시설물 위치 vds 좌표 목록
	 *
	 * @param fcilitiesGISSearchDTO
	 * @return list
	 */
	public List<FacilitiesGISVDSDTO> getFacilitiesVDSList(FacilitiesGISSearchDTO fcilitiesGISSearchDTO){

		List<Map<String,Object>> result = tmVdsInstllcRepository.getFacilitiesVDSList(fcilitiesGISSearchDTO);
		List<FacilitiesGISVDSDTO> vdsGisDataList = null;
		if(!CommonUtils.isListNull(result)){
			ObjectMapper objectMapper = new ObjectMapper();
			vdsGisDataList = result.stream()
					.map(surveyGisData -> {
						FacilitiesGISVDSDTO facilitiesGISVDSDTO = new FacilitiesGISVDSDTO();
						facilitiesGISVDSDTO.setInstllcId(String.valueOf(surveyGisData.get("instllcId")));
						facilitiesGISVDSDTO.setInstllcNm(String.valueOf(surveyGisData.get("instllcNm")));
						facilitiesGISVDSDTO.setCameraId(String.valueOf(surveyGisData.get("cameraId")));
						facilitiesGISVDSDTO.setIcon(String.valueOf(surveyGisData.get("icon")));
						facilitiesGISVDSDTO.setStatus(String.valueOf(surveyGisData.get("status")));
						facilitiesGISVDSDTO.setLocation(String.valueOf(surveyGisData.get("location")));
						facilitiesGISVDSDTO.setType("VDS");
						facilitiesGISVDSDTO.setLon(String.valueOf(surveyGisData.get("lon")));
						facilitiesGISVDSDTO.setLat(String.valueOf(surveyGisData.get("lat")));
						return facilitiesGISVDSDTO;
					})
					.collect(Collectors.toList());
		}
		return vdsGisDataList;
	}

	/**
	 * methodName : getFacilitiesVDSDetail
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 시설물 위치 vds 상세 정보
	 *
	 * @param instllcId
	 * @return list
	 */
	public List<FacilitiesGISDetailDTO> getFacilitiesVDSDetail(String instllcId){
		TcCdGrp tcCdGrp = tcCdGrpRepository.findOneByGrpCd(GroupCode.VDS_VHCL_TYPE_CD.getCode());
		String lang = LocaleContextHolder.getLocale().toString();
		List<Map<String,Object>> result = tmVdsInstllcRepository.getFacilitiesVDSDetail(instllcId,tcCdGrp.getGrpcdId(),lang);
		List<FacilitiesGISDetailDTO> gisDataList = null;
		if(!CommonUtils.isListNull(result)){
			ObjectMapper objectMapper = new ObjectMapper();
			gisDataList = result.stream()
					.map(surveyGisData -> {
						FacilitiesGISDetailDTO facilitiesGISDetailDTO = new FacilitiesGISDetailDTO();
						facilitiesGISDetailDTO.setRnum(String.valueOf(surveyGisData.get("rnum")));
						facilitiesGISDetailDTO.setTrfvlm(String.valueOf(surveyGisData.get("trfvlm")));
						facilitiesGISDetailDTO.setRate(String.valueOf(surveyGisData.get("rate")));
						facilitiesGISDetailDTO.setVhclClsf(String.valueOf(surveyGisData.get("vhclClsf")));
						facilitiesGISDetailDTO.setVhclClsfnm(String.valueOf(surveyGisData.get("vhclClsfnm")));
						return facilitiesGISDetailDTO;
					})
					.collect(Collectors.toList());
		}
		return gisDataList;
	}

	/**
	 * methodName : getFacilitiesFixedMetroCountList
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 시설물 위치 고정형 메트로 카운트 좌표 목록
	 *
	 * @param fcilitiesGISSearchDTO
	 * @return list
	 */
	public List<FacilitiesGISFixedMetroCountDTO> getFacilitiesFixedMetroCountList(FacilitiesGISSearchDTO fcilitiesGISSearchDTO){

		List<Map<String,Object>> result = tlFixedCurRepository.getFacilitiesFixedMetroCountList(fcilitiesGISSearchDTO);
		List<FacilitiesGISFixedMetroCountDTO> fixedMetroCountGisDataList = null;
		if(!CommonUtils.isListNull(result)){
			ObjectMapper objectMapper = new ObjectMapper();
			fixedMetroCountGisDataList = result.stream()
					.map(surveyGisData -> {
						FacilitiesGISFixedMetroCountDTO facilitiesGISFixedMetroCountDTO = new FacilitiesGISFixedMetroCountDTO();
						facilitiesGISFixedMetroCountDTO.setInstllcId(String.valueOf(surveyGisData.get("instllcId")));
						facilitiesGISFixedMetroCountDTO.setInstllcNm(String.valueOf(surveyGisData.get("instllcNm")));
						facilitiesGISFixedMetroCountDTO.setIcon(String.valueOf(surveyGisData.get("icon")));
						facilitiesGISFixedMetroCountDTO.setStatus(String.valueOf(surveyGisData.get("status")));
						facilitiesGISFixedMetroCountDTO.setLocation(String.valueOf(surveyGisData.get("location")));
						facilitiesGISFixedMetroCountDTO.setType("Metro Count");
						facilitiesGISFixedMetroCountDTO.setLon(String.valueOf(surveyGisData.get("lon")));
						facilitiesGISFixedMetroCountDTO.setLat(String.valueOf(surveyGisData.get("lat")));
						return facilitiesGISFixedMetroCountDTO;
					})
					.collect(Collectors.toList());
		}
		return fixedMetroCountGisDataList;
	}

	/**
	 * methodName : getFacilitiesFixedMetroCountDetail
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 시설물 위치 고정형 메트로 카운트 상세 정보
	 *
	 * @param instllcId
	 * @return list
	 */
	public List<FacilitiesGISDetailDTO> getFacilitiesFixedMetroCountDetail(String instllcId){

		List<Map<String,Object>> result = tlFixedCurRepository.getFacilitiesFixedMetroCountDetail(instllcId);
		List<FacilitiesGISDetailDTO> gisDataList = null;
		if(!CommonUtils.isListNull(result)){
			ObjectMapper objectMapper = new ObjectMapper();
			gisDataList = result.stream()
								.map(surveyGisData -> {
									FacilitiesGISDetailDTO facilitiesGISDetailDTO = new FacilitiesGISDetailDTO();
									facilitiesGISDetailDTO.setRnum(String.valueOf(surveyGisData.get("rnum")));
									facilitiesGISDetailDTO.setTrfvlm(String.valueOf(surveyGisData.get("trfvlm")));
									facilitiesGISDetailDTO.setRate(String.valueOf(surveyGisData.get("rate")));
									facilitiesGISDetailDTO.setVhclClsf(String.valueOf(surveyGisData.get("vhclClsf")));
									return facilitiesGISDetailDTO;
								})
								.collect(Collectors.toList());
		}
		return gisDataList;
	}

	/**
	 * methodName : getFacilitiesMoveMetroCountList
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 시설물 위치 이동형 메트로카운트 좌표 목록
	 *
	 * @param fcilitiesGISSearchDTO
	 * @return list
	 */
	public List<FacilitiesGISMoveMetroCountDTO> getFacilitiesMoveMetroCountList(FacilitiesGISSearchDTO fcilitiesGISSearchDTO){

		List<Map<String,Object>> result = tlMvmneqCurRepository.getFacilitiesMoveMetroCountList(fcilitiesGISSearchDTO);
		List<FacilitiesGISMoveMetroCountDTO> moveMetroCountGisDataList = null;
		if(!CommonUtils.isListNull(result)){
			ObjectMapper objectMapper = new ObjectMapper();
			moveMetroCountGisDataList = result.stream()
					.map(surveyGisData -> {
						FacilitiesGISMoveMetroCountDTO facilitiesGISMoveMetroCountDTO = new FacilitiesGISMoveMetroCountDTO();
						facilitiesGISMoveMetroCountDTO.setInstllcId(String.valueOf(surveyGisData.get("instllcId")));
						facilitiesGISMoveMetroCountDTO.setInstllcNm(String.valueOf(surveyGisData.get("instllcNm")));
						facilitiesGISMoveMetroCountDTO.setIcon(String.valueOf(surveyGisData.get("icon")));
						facilitiesGISMoveMetroCountDTO.setLocation(String.valueOf(surveyGisData.get("location")));
						facilitiesGISMoveMetroCountDTO.setType("Portable Metro Count");
						facilitiesGISMoveMetroCountDTO.setStatus(String.valueOf(surveyGisData.get("status")));
						facilitiesGISMoveMetroCountDTO.setLon(String.valueOf(surveyGisData.get("lon")));
						facilitiesGISMoveMetroCountDTO.setLat(String.valueOf(surveyGisData.get("lat")));
						return facilitiesGISMoveMetroCountDTO;
					})
					.collect(Collectors.toList());
		}
		return moveMetroCountGisDataList;
	}

	/**
	 * methodName : getFacilitiesMoveMetroCountDetail
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : 모니터링 > 시설물 위치 이동형 메트로카운트 상세 정보
	 *
	 * @param instllcId
	 * @return list
	 */
	public List<FacilitiesGISDetailDTO> getFacilitiesMoveMetroCountDetail(String instllcId){

		List<Map<String,Object>> result = tlMvmneqCurRepository.getFacilitiesMoveMetroCountDetail(instllcId);
		List<FacilitiesGISDetailDTO> gisDataList = null;
		if(!CommonUtils.isListNull(result)){
			ObjectMapper objectMapper = new ObjectMapper();
			gisDataList = result.stream()
								.map(item -> {
									FacilitiesGISDetailDTO facilitiesGISDetailDTO = new FacilitiesGISDetailDTO();
									facilitiesGISDetailDTO.setRnum(String.valueOf(item.get("rnum")));
									facilitiesGISDetailDTO.setTrfvlm(String.valueOf(item.get("trfvlm")));
									facilitiesGISDetailDTO.setRate(String.valueOf(item.get("rate")));
									facilitiesGISDetailDTO.setVhclClsf(String.valueOf(item.get("vhclClsf")));
									return facilitiesGISDetailDTO;
								})
								.collect(Collectors.toList());
		}
		return gisDataList;
	}

	/**
	 * methodName : getStatisticsVDS
	 * author : Nk.KIM
	 * date : 2024-08-08
	 * description : GIS 시각화 통계/분석 > VDS 정보 목록
	 *
	 * @param statisticsGISSearchDTO
	 * @return common response
	 */
	public List<StatisticsGISVDSDTO> getStatisticsVDSList(StatisticsGISSearchDTO statisticsGISSearchDTO,String searchType ){
		TcCdGrp tcCdGrp = tcCdGrpRepository.findOneByGrpCd(GroupCode.TRF_DRCT_CD.getCode());
		String lang = LocaleContextHolder.getLocale().toString();
		List<Map<String,Object>> result = null;
		//검색조건 시간별일 경우
		if("time".equals(searchType)){
			result = tmVdsInstllcRepository.getStatisticsVDSListForTime(statisticsGISSearchDTO,tcCdGrp.getGrpcdId(),lang);
		}else{
		//검색조건 월별일 경우
			result = tmVdsInstllcRepository.getStatisticsVDSListForMonth(statisticsGISSearchDTO,tcCdGrp.getGrpcdId(),lang);
		}

		List<StatisticsGISVDSDTO> statisticsVdsGisDataList = null;
		if(!CommonUtils.isListNull(result)){
			ObjectMapper objectMapper = new ObjectMapper();
			statisticsVdsGisDataList = result.stream()
					.map(item -> {
						StatisticsGISVDSDTO statisticsGISVDSDTO = new StatisticsGISVDSDTO();
						statisticsGISVDSDTO.setInstllcId(String.valueOf(item.get("instllcId")));
						statisticsGISVDSDTO.setInstllcNm(String.valueOf(item.get("instllcNm")));
						statisticsGISVDSDTO.setCameraId(String.valueOf(item.get("cameraId")));
						statisticsGISVDSDTO.setLocation(String.valueOf(item.get("location")));
						statisticsGISVDSDTO.setLaneCnt(String.valueOf(item.get("laneCnt")));
						statisticsGISVDSDTO.setLon(String.valueOf(item.get("lon")));
						statisticsGISVDSDTO.setLat(String.valueOf(item.get("lat")));
						statisticsGISVDSDTO.setVhclDrct(String.valueOf(item.get("vhclDrct")));
						statisticsGISVDSDTO.setVhclDrctCd(String.valueOf(item.get("vhclDrctCd")));
						statisticsGISVDSDTO.setTotalCnt(String.valueOf(item.get("totalCnt")));
						statisticsGISVDSDTO.setTotalAvg(String.valueOf(item.get("totalAvg")));
						return statisticsGISVDSDTO;
					})
					.collect(Collectors.toList());
		}
		return statisticsVdsGisDataList;
	}

	/**
	 * methodName : getStatisticsVDSTrfvlmDetail
	 * author : Nk.KIM
	 * date : 2024-08-13
	 * description : VDS 교통량 상세
	 *
	 * @param statisticsGISSearchDetailDTO
	 * @param searchType
	 * @return StatisticsGISDetailDTO
	 */
	public StatisticsGISDetailDTO getStatisticsVDSTrfvlmDetail(StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO,String searchType){
		StatisticsGISDetailDTO statisticsGISDetailDTO = new StatisticsGISDetailDTO();
		TcCdGrp tcCdGrp = tcCdGrpRepository.findOneByGrpCd(GroupCode.VDS_VHCL_TYPE_CD.getCode());
		TcCdGrp tcCdGrpForDrctCd = tcCdGrpRepository.findOneByGrpCd(GroupCode.TRF_DRCT_CD.getCode());
		String lang = LocaleContextHolder.getLocale().toString();
		List<Map<String,Object>> graphDataResult = null;
		List<Map<String,Object>> tableDataResult = null;
		List<Map<String,Object>> vhclDrctResult = tmVdsInstllcRepository.getVhclDrct(lang,tcCdGrpForDrctCd.getGrpcdId(),statisticsGISSearchDetailDTO.getInstllcId());
		long totalCnt = 0;
		
		//검색조건 시간별일 경우
		if("time".equals(searchType)){
			graphDataResult = tmVdsInstllcRepository.getStatisticsVDSLineGraphForTime(statisticsGISSearchDetailDTO);
			tableDataResult = tmVdsInstllcRepository.getStatisticsVDSTableForTrfvlmTime(statisticsGISSearchDetailDTO,tcCdGrp.getGrpcdId(),lang);
			totalCnt		= tmVdsInstllcRepository.getTotalCntForTrfvlmTime(statisticsGISSearchDetailDTO);
		}else{
			//검색조건 월별일 경우
			graphDataResult = tmVdsInstllcRepository.getStatisticsVDSLineGraphForMonth(statisticsGISSearchDetailDTO);
			tableDataResult = tmVdsInstllcRepository.getStatisticsVDSTableForTrfvlmMonth(statisticsGISSearchDetailDTO,tcCdGrp.getGrpcdId(),lang);
			totalCnt		= tmVdsInstllcRepository.getTotalCntForTrfvlmMonth(statisticsGISSearchDetailDTO);
		}
		statisticsGISDetailDTO.setTableDataList(getTableList(tableDataResult));
		statisticsGISDetailDTO.setDrctCdList(getvhclDrctList(vhclDrctResult));
		statisticsGISDetailDTO.setLineGraphList(getLineGraphList(graphDataResult));
		statisticsGISDetailDTO.setTotalCnt(totalCnt);
		return statisticsGISDetailDTO;
	}


	/**
	 * methodName : getStatisticsVDSAvgSpeedDetail
	 * author : Nk.KIM
	 * date : 2024-08-13
	 * description : VDS 평균 속도 상세
	 *
	 * @param statisticsGISSearchDetailDTO
	 * @param searchType
	 * @return StatisticsGISDetailDTO
	 */
	public StatisticsGISDetailDTO getStatisticsVDSAvgSpeedDetail(StatisticsGISSearchDetailDTO statisticsGISSearchDetailDTO,String searchType){
		StatisticsGISDetailDTO statisticsGISDetailDTO = new StatisticsGISDetailDTO();
		TcCdGrp tcCdGrp = tcCdGrpRepository.findOneByGrpCd(GroupCode.VDS_VHCL_TYPE_CD.getCode());
		TcCdGrp tcCdGrpForDrctCd = tcCdGrpRepository.findOneByGrpCd(GroupCode.TRF_DRCT_CD.getCode());
		String lang = LocaleContextHolder.getLocale().toString();
		List<Map<String,Object>> graphDataResult = null;
		List<Map<String,Object>> tableDataResult = null;
		List<Map<String,Object>> vhclDrctResult = tmVdsInstllcRepository.getVhclDrct(lang,tcCdGrpForDrctCd.getGrpcdId(),statisticsGISSearchDetailDTO.getInstllcId());
		long totalCnt = 0;
		//검색조건 시간별일 경우
		if("time".equals(searchType)){
			graphDataResult = tmVdsInstllcRepository.getStatisticsVDSLineGraphForTime(statisticsGISSearchDetailDTO);
			tableDataResult = tmVdsInstllcRepository.getStatisticsVDSTableForAvgSpeedTime(statisticsGISSearchDetailDTO,tcCdGrp.getGrpcdId(),lang);
			totalCnt		= tmVdsInstllcRepository.getTotalAvgForAvgSpeedTime(statisticsGISSearchDetailDTO);
		}else{
			//검색조건 월별일 경우
			graphDataResult = tmVdsInstllcRepository.getStatisticsVDSLineGraphForMonth(statisticsGISSearchDetailDTO);
			tableDataResult = tmVdsInstllcRepository.getStatisticsVDSTableForAvgSpeedMonth(statisticsGISSearchDetailDTO,tcCdGrp.getGrpcdId(),lang);
			totalCnt		= tmVdsInstllcRepository.getTotalAvgForAvgSpeedMonth(statisticsGISSearchDetailDTO);
		}

		statisticsGISDetailDTO.setTableDataList(getTableList(tableDataResult));
		statisticsGISDetailDTO.setDrctCdList(getvhclDrctList(vhclDrctResult));
		statisticsGISDetailDTO.setLineGraphList(getLineGraphList(graphDataResult));
		statisticsGISDetailDTO.setTotalCnt(totalCnt);

		return statisticsGISDetailDTO;
	}


	/**
	 * methodName : getStatisticsFixedMetroCountList
	 * author : Nk.KIM
	 * date : 2024-08-13
	 * description :
	 *
	 * @param statisticsGISSearchDTO
	 * @param searchType
	 * @return PsiType :List<StatisticsGISFixedMetroCountDTO>
	 */
	public List<StatisticsGISFixedMetroCountDTO> getStatisticsFixedMetroCountList(StatisticsGISSearchDTO statisticsGISSearchDTO,String searchType ){
		List<Map<String,Object>> result = null;
		//검색조건 시간별일 경우
		if("time".equals(searchType)){
			result = tlFixedCurRepository.getStatisticsFixedMetroCountListForTime(statisticsGISSearchDTO);
		}else{
			//검색조건 월별일 경우
			result = tlFixedCurRepository.getStatisticsFixedMetroCountListForMonth(statisticsGISSearchDTO);
		}

		List<StatisticsGISFixedMetroCountDTO> statisticsFixedMetroCountGisDataList = null;
		if(!CommonUtils.isListNull(result)){
			ObjectMapper objectMapper = new ObjectMapper();
			statisticsFixedMetroCountGisDataList = result.stream()
					.map(item -> {
						StatisticsGISFixedMetroCountDTO statisticsGISFixedMetroCountDTO = new StatisticsGISFixedMetroCountDTO();
						statisticsGISFixedMetroCountDTO.setInstllcId(String.valueOf(item.get("instllcId")));
						statisticsGISFixedMetroCountDTO.setInstllcNm(String.valueOf(item.get("instllcNm")));
						statisticsGISFixedMetroCountDTO.setLocation(String.valueOf(item.get("location")));
						statisticsGISFixedMetroCountDTO.setLaneCnt(String.valueOf(item.get("laneCnt")));
						statisticsGISFixedMetroCountDTO.setLon(String.valueOf(item.get("lon")));
						statisticsGISFixedMetroCountDTO.setLat(String.valueOf(item.get("lat")));
						statisticsGISFixedMetroCountDTO.setTotalCnt(String.valueOf(item.get("totalCnt")));
						statisticsGISFixedMetroCountDTO.setTotalAvg(String.valueOf(item.get("totalAvg")));
						return statisticsGISFixedMetroCountDTO;
					})
					.collect(Collectors.toList());
		}
		return statisticsFixedMetroCountGisDataList;
	}

	/**
	 * methodName : getStatisticsFixedMetroCountTrfvlmDetail
	 * author : Nk.KIM
	 * date : 2024-08-13
	 * description : 고정형 메트로 카운트 교통량 상세
	 *
	 * @param statisticsGISSearchMetroCountDetailDTO
	 * @param searchType
	 * @return StatisticsGISDetailDTO
	 */
	public StatisticsGISDetailDTO getStatisticsFixedMetroCountTrfvlmDetail(StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO,String searchType){
		StatisticsGISDetailDTO statisticsGISDetailDTO = new StatisticsGISDetailDTO();
		TcCdGrp tcCdGrp = tcCdGrpRepository.findOneByGrpCd(GroupCode.METROVHCL_TYPE_CD.getCode());
		TcCdGrp tcCdGrpForVhclDrct = tcCdGrpRepository.findOneByGrpCd(GroupCode.TRF_DRCT_CD.getCode());
		String lang = LocaleContextHolder.getLocale().toString();
		List<Map<String,Object>> graphDataResult = null;
		List<Map<String,Object>> tableDataResult = null;
		List<Map<String,Object>> vhclDrctResult = null;
		long totalCnt = 0;
		//검색조건 시간별일 경우
		if("time".equals(searchType)){
			graphDataResult = tlFixedCurRepository.getStatisticsFixedMetroCountLineGraphForTime(statisticsGISSearchMetroCountDetailDTO);
			vhclDrctResult  = tlFixedCurRepository.getVhclDrctForTime(statisticsGISSearchMetroCountDetailDTO,tcCdGrpForVhclDrct.getGrpcdId(),lang);
			tableDataResult = tlFixedCurRepository.getStatisticsFixedMetroCountTableForTrfvlmTime(statisticsGISSearchMetroCountDetailDTO,tcCdGrp.getGrpcdId(),lang);
			totalCnt		= tlFixedCurRepository.getTotalCntForTrfvlmTime(statisticsGISSearchMetroCountDetailDTO);
			
		}else{
			//검색조건 월별일 경우
			graphDataResult = tlFixedCurRepository.getStatisticsFixedMetroCountLineGraphForMonth(statisticsGISSearchMetroCountDetailDTO);
			vhclDrctResult  = tlFixedCurRepository.getVhclDrctForMonth(statisticsGISSearchMetroCountDetailDTO,tcCdGrpForVhclDrct.getGrpcdId(),lang);
			tableDataResult = tlFixedCurRepository.getStatisticsFixedMetroCountTableForTrfvlmMonth(statisticsGISSearchMetroCountDetailDTO,tcCdGrp.getGrpcdId(),lang);
			totalCnt 		= tlFixedCurRepository.getTotalCntForTrfvlmMonth(statisticsGISSearchMetroCountDetailDTO);
		}

		statisticsGISDetailDTO.setLineGraphList(getLineGraphList(graphDataResult));
		statisticsGISDetailDTO.setDrctCdList(getvhclDrctList(vhclDrctResult));
		statisticsGISDetailDTO.setTableDataList(getTableList(tableDataResult));
		statisticsGISDetailDTO.setTotalCnt(totalCnt);	
		
		return statisticsGISDetailDTO;
	}


	/**
	 * methodName : getStatisticsFixedMetroCountAvgSpeedDetail
	 * author : Nk.KIM
	 * date : 2024-08-13
	 * description : 고정형 메트로카운트 평균속도 상세
	 *
	 * @param statisticsGISSearchMetroCountDetailDTO
	 * @param searchType
	 * @return StatisticsGISDetailDTO
	 */
	public StatisticsGISDetailDTO getStatisticsFixedMetroCountAvgSpeedDetail(StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO,String searchType){
		StatisticsGISDetailDTO statisticsGISDetailDTO = new StatisticsGISDetailDTO();
		TcCdGrp tcCdGrp = tcCdGrpRepository.findOneByGrpCd(GroupCode.METROVHCL_TYPE_CD.getCode());
		TcCdGrp tcCdGrpForVhclDrct = tcCdGrpRepository.findOneByGrpCd(GroupCode.TRF_DRCT_CD.getCode());
		String lang = LocaleContextHolder.getLocale().toString();
		List<Map<String,Object>> graphDataResult = null;
		List<Map<String,Object>> tableDataResult = null;
		List<Map<String,Object>> vhclDrctResult = null;
		long totalCnt = 0;
		//검색조건 시간별일 경우
		if("time".equals(searchType)){
			graphDataResult = tlFixedCurRepository.getStatisticsFixedMetroCountLineGraphForTime(statisticsGISSearchMetroCountDetailDTO);
			vhclDrctResult  = tlFixedCurRepository.getVhclDrctForTime(statisticsGISSearchMetroCountDetailDTO,tcCdGrpForVhclDrct.getGrpcdId(),lang);
			tableDataResult = tlFixedCurRepository.getStatisticsFixedMetroCountTableForAvgSpeedTime(statisticsGISSearchMetroCountDetailDTO,tcCdGrp.getGrpcdId(),lang);
			totalCnt		= tlFixedCurRepository.getTotalAvgForAvgSpeedTime(statisticsGISSearchMetroCountDetailDTO);
		}else{
			//검색조건 월별일 경우
			graphDataResult = tlFixedCurRepository.getStatisticsFixedMetroCountLineGraphForMonth(statisticsGISSearchMetroCountDetailDTO);
			vhclDrctResult  = tlFixedCurRepository.getVhclDrctForMonth(statisticsGISSearchMetroCountDetailDTO,tcCdGrpForVhclDrct.getGrpcdId(),lang);
			tableDataResult = tlFixedCurRepository.getStatisticsFixedMetroCountTableForAvgSpeedMonth(statisticsGISSearchMetroCountDetailDTO,tcCdGrp.getGrpcdId(),lang);
			totalCnt		= tlFixedCurRepository.getTotalAvgForAvgSpeedMonth(statisticsGISSearchMetroCountDetailDTO);
		}

		statisticsGISDetailDTO.setLineGraphList(getLineGraphList(graphDataResult));
		statisticsGISDetailDTO.setDrctCdList(getvhclDrctList(vhclDrctResult));
		statisticsGISDetailDTO.setTableDataList(getTableList(tableDataResult));
		statisticsGISDetailDTO.setTotalCnt(totalCnt);	
		
		return statisticsGISDetailDTO;
	}


	/**
	 * methodName : getStatisticsMoveMetroCountList
	 * author : Nk.KIM
	 * date : 2024-08-13
	 * description :
	 *
	 * @param statisticsGISSearchDTO
	 * @param searchType
	 * @return List<StatisticsGISMoveMetroCountDTO>
	 */
	public List<StatisticsGISMoveMetroCountDTO> getStatisticsMoveMetroCountList(StatisticsGISSearchDTO statisticsGISSearchDTO,String searchType ){
		List<Map<String,Object>> result = null;
		//검색조건 시간별일 경우
		if("time".equals(searchType)){
			result = tlMvmneqCurRepository.getStatisticsMoveMetroCountListForTime(statisticsGISSearchDTO);
		}else{
			//검색조건 월별일 경우
			result = tlMvmneqCurRepository.getStatisticsMoveMetroCountListForMonth(statisticsGISSearchDTO);
		}

		List<StatisticsGISMoveMetroCountDTO> statisticsMoveMetroCountGisDataList = null;
		if(!CommonUtils.isListNull(result)){
			ObjectMapper objectMapper = new ObjectMapper();
			statisticsMoveMetroCountGisDataList = result.stream()
					.map(item -> {
						StatisticsGISMoveMetroCountDTO statisticsGISMoveMetroCountDTO = new StatisticsGISMoveMetroCountDTO();
						statisticsGISMoveMetroCountDTO.setInstllcId(String.valueOf(item.get("instllcId")));
						statisticsGISMoveMetroCountDTO.setInstllcNm(String.valueOf(item.get("instllcNm")));
						statisticsGISMoveMetroCountDTO.setLocation(String.valueOf(item.get("location")));
						statisticsGISMoveMetroCountDTO.setLaneCnt(String.valueOf(item.get("laneCnt")));
						statisticsGISMoveMetroCountDTO.setLon(String.valueOf(item.get("lon")));
						statisticsGISMoveMetroCountDTO.setLat(String.valueOf(item.get("lat")));
						statisticsGISMoveMetroCountDTO.setTotalCnt(String.valueOf(item.get("totalCnt")));
						statisticsGISMoveMetroCountDTO.setTotalAvg(String.valueOf(item.get("totalAvg")));
						return statisticsGISMoveMetroCountDTO;
					})
					.collect(Collectors.toList());
		}
		return statisticsMoveMetroCountGisDataList;
	}

	/**
	 * methodName : getStatisticsMoveMetroCountTrfvlmDetail
	 * author : Nk.KIM
	 * date : 2024-08-13
	 * description : 이동형 메트로 카운트 교통량 상세
	 *
	 * @param statisticsGISSearchMetroCountDetailDTO
	 * @param searchType
	 * @return StatisticsGISDetailDTO
	 */
	public StatisticsGISDetailDTO getStatisticsMoveMetroCountTrfvlmDetail(StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO,String searchType){
		StatisticsGISDetailDTO statisticsGISDetailDTO = new StatisticsGISDetailDTO();
		TcCdGrp tcCdGrp = tcCdGrpRepository.findOneByGrpCd(GroupCode.METROVHCL_TYPE_CD.getCode());
		TcCdGrp tcCdGrpForVhclDrct = tcCdGrpRepository.findOneByGrpCd(GroupCode.TRF_DRCT_CD.getCode());
		String lang = LocaleContextHolder.getLocale().toString();
		List<Map<String,Object>> graphDataResult = null;
		List<Map<String,Object>> tableDataResult = null;
		List<Map<String,Object>> vhclDrctResult = null;
		long totalCnt = 0;
		
		//검색조건 시간별일 경우
		if("time".equals(searchType)){
			graphDataResult = tlMvmneqCurRepository.getStatisticsMoveMetroCountLineGraphForTime(statisticsGISSearchMetroCountDetailDTO);
			vhclDrctResult  = tlMvmneqCurRepository.getVhclDrctForTime(statisticsGISSearchMetroCountDetailDTO,tcCdGrpForVhclDrct.getGrpcdId(),lang);
			tableDataResult = tlMvmneqCurRepository.getStatisticsMoveMetroCountTableForTrfvlmTime(statisticsGISSearchMetroCountDetailDTO,tcCdGrp.getGrpcdId(),lang);
			totalCnt		= tlMvmneqCurRepository.getTotalCntForTrfvlmTime(statisticsGISSearchMetroCountDetailDTO);
		}else{
			//검색조건 월별일 경우
			graphDataResult = tlMvmneqCurRepository.getStatisticsMoveMetroCountLineGraphForMonth(statisticsGISSearchMetroCountDetailDTO);
			vhclDrctResult  = tlMvmneqCurRepository.getVhclDrctForMonth(statisticsGISSearchMetroCountDetailDTO,tcCdGrpForVhclDrct.getGrpcdId(),lang);
			tableDataResult = tlMvmneqCurRepository.getStatisticsMoveMetroCountTableForTrfvlmMonth(statisticsGISSearchMetroCountDetailDTO,tcCdGrp.getGrpcdId(),lang);
			totalCnt		= tlMvmneqCurRepository.getTotalCntForTrfvlmMonth(statisticsGISSearchMetroCountDetailDTO);
		}

		statisticsGISDetailDTO.setLineGraphList(getLineGraphList(graphDataResult));
		statisticsGISDetailDTO.setDrctCdList(getvhclDrctList(vhclDrctResult));
		statisticsGISDetailDTO.setTableDataList(getTableList(tableDataResult));
		statisticsGISDetailDTO.setTotalCnt(totalCnt);
		
		return statisticsGISDetailDTO;
	}


	/**
	 * methodName : getStatisticsMoveMetroCountAvgSpeedDetail
	 * author : Nk.KIM
	 * date : 2024-08-13
	 * description : 메트로 카운트 평균속도 상세
	 *
	 * @param statisticsGISSearchMetroCountDetailDTO
	 * @param searchType
	 * @return StatisticsGISDetailDTO
	 */
	public StatisticsGISDetailDTO getStatisticsMoveMetroCountAvgSpeedDetail(StatisticsGISSearchMetroCountDetailDTO statisticsGISSearchMetroCountDetailDTO,String searchType){
		StatisticsGISDetailDTO statisticsGISDetailDTO = new StatisticsGISDetailDTO();
		TcCdGrp tcCdGrp = tcCdGrpRepository.findOneByGrpCd(GroupCode.METROVHCL_TYPE_CD.getCode());
		TcCdGrp tcCdGrpForVhclDrct = tcCdGrpRepository.findOneByGrpCd(GroupCode.TRF_DRCT_CD.getCode());
		String lang = LocaleContextHolder.getLocale().toString();
		List<Map<String,Object>> graphDataResult = null;
		List<Map<String,Object>> tableDataResult = null;
		List<Map<String,Object>> vhclDrctResult = null;
		long totalCnt = 0;
		
		//검색조건 시간별일 경우
		if("time".equals(searchType)){
			graphDataResult = tlMvmneqCurRepository.getStatisticsMoveMetroCountLineGraphForTime(statisticsGISSearchMetroCountDetailDTO);
			vhclDrctResult  = tlMvmneqCurRepository.getVhclDrctForTime(statisticsGISSearchMetroCountDetailDTO,tcCdGrpForVhclDrct.getGrpcdId(),lang);
			tableDataResult = tlMvmneqCurRepository.getStatisticsMoveMetroCountTableForAvgSpeedTime(statisticsGISSearchMetroCountDetailDTO,tcCdGrp.getGrpcdId(),lang);
			totalCnt		= tlMvmneqCurRepository.getTotalAvgForAvgSpeedTime(statisticsGISSearchMetroCountDetailDTO);
		}else{
			//검색조건 월별일 경우
			graphDataResult = tlMvmneqCurRepository.getStatisticsMoveMetroCountLineGraphForMonth(statisticsGISSearchMetroCountDetailDTO);
			vhclDrctResult  = tlMvmneqCurRepository.getVhclDrctForMonth(statisticsGISSearchMetroCountDetailDTO,tcCdGrpForVhclDrct.getGrpcdId(),lang);
			tableDataResult = tlMvmneqCurRepository.getStatisticsMoveMetroCountTableForAvgSpeedMonth(statisticsGISSearchMetroCountDetailDTO,tcCdGrp.getGrpcdId(),lang);
			totalCnt		= tlMvmneqCurRepository.getTotalAvgForAvgSpeedMonth(statisticsGISSearchMetroCountDetailDTO);
		}

		statisticsGISDetailDTO.setLineGraphList(getLineGraphList(graphDataResult));
		statisticsGISDetailDTO.setDrctCdList(getvhclDrctList(vhclDrctResult));
		statisticsGISDetailDTO.setTableDataList(getTableList(tableDataResult));
		statisticsGISDetailDTO.setTotalCnt(totalCnt);
		return statisticsGISDetailDTO;
	}

	/**
	 * methodName : getSearchYearsForTranscad
	 * author : Nk.KIM
	 * date : 2024-08-21
	 * description : 트랜스케드 년도 검색
	 *
	 * @param saerchType
	 * @return List<String>
	 */
	public List<String> getSearchYearsForTranscad(String saerchType){
		List<String> years = null;
		if(CommonUtils.isNull(saerchType)) saerchType = "vds";
		switch(saerchType){
			case "fixedMetroCount" :
				years = tsFixedYyRepository.getStatsYears();
				break;
			case "moveMetroCount" :
				years = tsMvmneqYyRepository.getStatsYears();
				break;
			default:
				years = tsVdsYyRepository.getStatsYears();
				break;
		}
		return years;
	}
	/**
	 * methodName : excelDownLoad
	 * author : Nk.KIM
	 * date : 2024-08-13
	 * description : TransCAD 다운로드
	 *
	 * @param response
	 * @param downloadType
	 * @param searchYear
	 * @return void
	 */
	public void excelDownLoad(HttpServletResponse response,String downloadType,String searchYear){
		GroupCode groupCode = GroupCode.VDS_VHCL_TYPE_CD; //default = vds
		if(!"vds".equals(downloadType)) groupCode = GroupCode.METROVHCL_TYPE_CD; //default = vds
		String[] headerArray = getExcelHeader(groupCode);
		List<String[]> bodyList = getExcelBody(downloadType,searchYear);
		String fileName = "VDS_TransCAD_";
		if("fixedMetroCount".equals(downloadType)){
			fileName = "MetroCount_TransCAD_";
		}else if("moveMetroCount".equals(downloadType)){
			fileName = "Portable_MetroCount_TransCAD_";
		}
		fileName = fileName+searchYear;

		excelDownloadComponent.excelDownload(response,headerArray,bodyList,fileName);
	}

	private static List<StatisticsGISDetailDTO.LineGraphDataInfo> getLineGraphList(List<Map<String,Object>> resultList){
		List<StatisticsGISDetailDTO.LineGraphDataInfo> lineGraphList = null;
		ObjectMapper objectMapper = new ObjectMapper();
		if(!CommonUtils.isListNull(resultList)){

			lineGraphList = resultList.stream()
					.map(item -> {
						StatisticsGISDetailDTO.LineGraphDataInfo lineGraphDataInfo = new StatisticsGISDetailDTO.LineGraphDataInfo();
						lineGraphDataInfo.setDateInfo(String.valueOf(item.get("dateInfo")));
						lineGraphDataInfo.setAvgSpeed(String.valueOf(item.get("avgspeed")));
						lineGraphDataInfo.setTrfvlm(String.valueOf(item.get("trfvlm")));
						return lineGraphDataInfo;
					})
					.collect(Collectors.toList());
		}

		return lineGraphList;
	}

	private static List<StatisticsGISDetailDTO.TableDataInfo> getTableList(List<Map<String,Object>> resultList){
		List<StatisticsGISDetailDTO.TableDataInfo> tableDataList = null;
		ObjectMapper objectMapper = new ObjectMapper();
		if(!CommonUtils.isListNull(resultList)){

			tableDataList = resultList.stream()
					.map(item -> {
						StatisticsGISDetailDTO.TableDataInfo tableDataInfo = new StatisticsGISDetailDTO.TableDataInfo();
						tableDataInfo.setRnum(String.valueOf(item.get("rnum")));
						tableDataInfo.setVhclClsf(String.valueOf(item.get("vhclclsf")));
						tableDataInfo.setAvgSpeed(String.valueOf(item.get("avgspeed")));
						tableDataInfo.setTrfvlm(String.valueOf(item.get("trfvlm")));
						tableDataInfo.setRate(String.valueOf(item.get("rate")));
						return tableDataInfo;
					})
					.collect(Collectors.toList());
		}

		return tableDataList;
	}

	private static List<StatisticsGISDetailDTO.VhclDrctDataInfo> getvhclDrctList(List<Map<String,Object>> resultList){
		List<StatisticsGISDetailDTO.VhclDrctDataInfo> vhclDrctList = null;
		ObjectMapper objectMapper = new ObjectMapper();
		if(!CommonUtils.isListNull(resultList)){

			vhclDrctList = resultList.stream()
					.map(item -> {
						StatisticsGISDetailDTO.VhclDrctDataInfo vhclDrctDataInfo = new StatisticsGISDetailDTO.VhclDrctDataInfo();
						vhclDrctDataInfo.setVhclDrctCd(String.valueOf(item.get("vhclDrctCd")));
						vhclDrctDataInfo.setVhclDrct(String.valueOf(item.get("vhclDrct")));
						return vhclDrctDataInfo;
					})
					.collect(Collectors.toList());
		}

		return vhclDrctList;
	}

	private String[] getExcelHeader(GroupCode groupCode){

		TcCdGrp tcCdGrp = tcCdGrpRepository.findOneByGrpCd(groupCode.getCode());
		String lang = LocaleContextHolder.getLocale().toString();
		List<String> vhclclsfList = tcCdInfoRepository.getVhclclsfList(tcCdGrp.getGrpcdId(),lang);

		vhclclsfList.add(0,CommonUtils.getMessage("common.transcad.excel.speed"));
		vhclclsfList.add(0,CommonUtils.getMessage("common.transcad.excel.lane"));
		vhclclsfList.add(0,CommonUtils.getMessage("common.transcad.excel.lat"));
		vhclclsfList.add(0,CommonUtils.getMessage("common.transcad.excel.lon"));
		vhclclsfList.add(0,CommonUtils.getMessage("common.transcad.excel.instllc"));
		vhclclsfList.add(CommonUtils.getMessage("common.transcad.excel.total"));
		return vhclclsfList.toArray(new String[0]);
	}

	private List<String[]> getExcelBody(String downloadType,String searchYear){
		List<Map<String,Object>> excelBodyList = null;
		List<String[]> result = null;
		if("vds".equals(downloadType)){
			excelBodyList = tmVdsInstllcRepository.getVDSListForExcel(searchYear);
		}else if("fixedMetroCount".equals(downloadType)){
			excelBodyList = tlFixedCurRepository.getFixedMetroCountListForExcel(searchYear);
		}else{
			excelBodyList = tlMvmneqCurRepository.getMoveMetroCountListForExcel(searchYear);
		}
		if(!CommonUtils.isListNull(excelBodyList)){
			result = excelBodyList.stream()
					.map(map -> map.values().stream()
							.map(Object::toString)
							.toArray(String[]::new))
					.collect(Collectors.toList());
		}
		return result;
	}
}
