package com.sl.tdbms.web.admin.web.service.statistics;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.tdbms.web.admin.common.component.ExcelDownloadComponent;
import com.sl.tdbms.web.admin.common.component.StatisticsExcelDownloadComponent;
import com.sl.tdbms.web.admin.common.dto.common.CommonCdDTO;
import com.sl.tdbms.web.admin.common.dto.facilties.FacilitiesDTO;
import com.sl.tdbms.web.admin.common.dto.statistics.*;
import com.sl.tdbms.web.admin.common.entity.TsPopMng;
import com.sl.tdbms.web.admin.common.enums.PersonalTransportationChartType;
import com.sl.tdbms.web.admin.common.enums.RoadSideChartType;
import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.*;
import com.sl.tdbms.web.admin.common.repository.*;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * The type Statistics service.
 *
 * @FileName : StatisticsService
 * @Project : sri_lanka_admin
 * @Date : 2024. 1. 31.
 * @작성자 : NK.KIM
 * @프로그램 설명 : 통계 서비스
 */
@Service
public class StatisticsService {

	@Autowired
	TsMccYyRepository tsMccYyRepository;
	@Autowired
	TsVdsYyRepository tsVdsYyRepository;
	@Autowired
	TsFixedYyRepository tsFixedYyRepository;
	@Autowired
	TsMvmneqYyRepository tsMvmneqYyRepository;
	@Autowired
	TmVdsInstllcRepository tmVdsInstllcRepository;
	@Autowired
	TlMvmneqCurRepository tlMvmneqCurRepository;
	@Autowired
	TsAxleloadYyRepository tsAxleloadYyRepository;
	@Autowired
	QTsMccYyRepository qtsMccYyRepository;
	@Autowired
	QTsPopMngRepository qTsPopMngRepository;
	@Autowired
	QTsRoadSidePassageTypeYyRepository qTsRoadSidePassageTypeYyRepository;
	@Autowired
	QTsRoadSideDepartureTypeYyRepository qTsRoadSideDepartureTypeYyRepository;
	@Autowired
	QTsRoadSideDestinationTypeYyRepository qTsRoadSideDestinationTypeYyRepository;
	@Autowired
	QTsRoadSidePassengersYyRepository qTsRoadSideNumberPassengersYyRepository;
	@Autowired
	QTsRoadSidePurposeYyRepository qTsRoadSidePurposeYyRepository;
	@Autowired
	QTsRoadSideDepartureTimeYyRepository qTsRoadSideDepartureTimeYyRepository;
	@Autowired
	QTsTravelPassageTypeYyRepository qTsTravelPassageTypeYyRepository;
	@Autowired
	QTsTravelPurposeYyRepository qTsTravelPurposeYyRepository;
	@Autowired
	QTsTravelDepartureTypeYyRepository qTsTravelDepartureTypeYyRepository;
	@Autowired
	QTsTravelDestinationTypeYyRepository qTsTravelDestinationTypeYyRepository;
	@Autowired
	QTsTravelDepartureTimeYyRepository qTsTravelDepartureTimeYyRepository;
	@Autowired
	QTsTravelDestinationTimeYyRepository qTsTravelDestinationTimeYyRepository;
	@Autowired
	QTsTravelTransfortInfoYyRepository qTsTravelTransfortInfoYyRepository;
	@Autowired
	QTsTravelUseHighwayYyRepository qTsTravelUseHighwayYyRepository;
	@Autowired
	QTsTravelFeeInfoYyRepository qTsTravelFeeInfoYyRepository;
	@Autowired
	QTsTravelTransfortHourYyRepository qTsTravelTransfortHourYyRepository;
	@Autowired
	QTmVdsInstllcRepository qTmVdsInstllcRepository;
	@Autowired
	QTlFixedCurRepository qTlFixedCurRepository;
	@Autowired
	QTlMvmneqCurRepository qTlMvmneqCurRepository;
	@Autowired
	QTsPersonAgeYyRepository qTsPersonAgeYyRepository;
	@Autowired
	QTsPersonGenderYyRepository qTsPersonGenderYyRepository;
	@Autowired
	QTsPersonEducationYyRepository qTsPersonEducationYyRepository;
	@Autowired
	QTsPersonOccupationYyRepository qTsPersonOccupationYyRepository;
	@Autowired
	QTsPersonWorkingdayYyRepository qTsPersonWorkingdayYyRepository;
	@Autowired
	QTlSrvyAnsRepository qTlSrvyAnsRepository;
	@Autowired
	StatisticsExcelDownloadComponent statisticsExcelDownloadComponent;
	@Autowired
	QTsVdsYyRepository qTsVdsYyRepository;
	@Autowired
	QTsAxleloadYyRepository qTsAxleloadYyRepository;
	@Autowired
	ExcelDownloadComponent excelDownloadComponent;
	@Autowired
	QTsVdsMmRepository qTsVdsMmRepository;
	@Autowired
	QTsVdsDdRepository qTsVdsDdRepository;
	@Autowired
	QTsVdsOnhrRepository qTsVdsOnhrRepository;
	@Autowired
	QTsFixedYyRepository qTsFixedYyRepository;
	@Autowired
	QTsFixedMmRepository qTsFixedMmRepository;
	@Autowired
	QTsFixedDdRepository qTsFixedDdRepository;
	@Autowired
	QTsFixedOnhrRepository qTsFixedOnhrRepository;
	@Autowired
	QTsMvmneqYyRepository qTsMvmneqYyRepository;
	@Autowired
	QTsMvmneqMmRepository qTsMvmneqMmRepository;
	@Autowired
	QTsMvmneqDdRepository qTsMvmneqDdRepository;
	@Autowired
	QTsMvmneqOnhrRepository qTsMvmneqOnhrRepository;
	@Autowired
	QTsMccTrfvlYyRepository qTsMccTrfvlYyRepository;
	@Autowired
	QTsMccTrfvlMmRepository qTsMccTrfvlMmRepository;
	@Autowired
	QTsMccTrfvlDdRepository qTsMccTrfvlDdRepository;
	@Autowired
	QTsMccTrfvlOnhrRepository qTsMccTrfvlOnhrRepository;

	
	public TlTrfvlRsltStatisticsDTO getMCCStatisticsList(TlExmnRsltStatisticsSearchDTO searchDTO){
		ObjectMapper mapper = new ObjectMapper();

		TlTrfvlRsltStatisticsDTO tlTrfvlRsltStatisticsDTO = new TlTrfvlRsltStatisticsDTO();
		List<TlTrfvlRsltStatisticsDTO.TlTrfvlRsltStatisticsInfo> tlTrfvlRsltStatisticsList = null;
		if(!CommonUtils.isNull(searchDTO.getSearchDate()) &&
			(!CommonUtils.isNull(searchDTO.getDsdCd()) || !CommonUtils.isNull(searchDTO.getGnCd()))){

			List<Map<String,Object>> trfvlRsltStatisticsDBList = tsMccYyRepository.getTrfvlRsltStatisticsListGroupByTazCd(searchDTO);
			List<TlTrfvlRsltStatisticsDTO.DirectionInfo> trfvlRsltList = qtsMccYyRepository.getDirectionListByTazCd(searchDTO);


			//map to dto
			if(!CommonUtils.isListNull(trfvlRsltStatisticsDBList)){
				tlTrfvlRsltStatisticsList = trfvlRsltStatisticsDBList.stream()
																.map(x -> (mapper.convertValue(x, TlTrfvlRsltStatisticsDTO.TlTrfvlRsltStatisticsInfo.class)))
																.collect(Collectors.toList());

			}

			tlTrfvlRsltStatisticsDTO.setDirectionList(trfvlRsltList);
			tlTrfvlRsltStatisticsDTO.setTlTrfvlRsltStatisticsList(tlTrfvlRsltStatisticsList);
		}
		return tlTrfvlRsltStatisticsDTO;
	}

	public List<TlSrvyRsltStatisticsDTO> getRoadSideStatisticsList(TlExmnRsltStatisticsSearchDTO searchDTO){
		List<TlSrvyRsltStatisticsDTO> tlSrvyRsltStatisticsList = new ArrayList<>();

		for(RoadSideChartType roadSideChartType : RoadSideChartType.values()){
			TlSrvyRsltStatisticsDTO tlSrvyRsltStatisticsDTO = new TlSrvyRsltStatisticsDTO();
			try{
				tlSrvyRsltStatisticsDTO.setAppendId(roadSideChartType.getAppendId());
				tlSrvyRsltStatisticsDTO.setChartType(roadSideChartType.getChartType());
				tlSrvyRsltStatisticsDTO.isSelectBox(roadSideChartType.isSelectBox());
				tlSrvyRsltStatisticsDTO = getRoadSideStatisticsListByType(tlSrvyRsltStatisticsDTO,roadSideChartType,searchDTO);
			}catch (Exception e){
				tlSrvyRsltStatisticsDTO = null;
				e.printStackTrace();
			}
			if(tlSrvyRsltStatisticsDTO != null) {
				tlSrvyRsltStatisticsList.add(tlSrvyRsltStatisticsDTO);
			}
		}

		return tlSrvyRsltStatisticsList;
	}

	public List<TlSrvyRsltStatisticsDTO> getODStatisticsList(TlExmnRsltStatisticsSearchDTO searchDTO){

		List<TlSrvyRsltStatisticsDTO> tlSrvyRsltStatisticsList = new ArrayList<>();

		for(PersonalTransportationChartType personalTransportationChartType : PersonalTransportationChartType.values()){
			TlSrvyRsltStatisticsDTO tlSrvyRsltStatisticsDTO = new TlSrvyRsltStatisticsDTO();
			try{
				tlSrvyRsltStatisticsDTO.setAppendId(personalTransportationChartType.getAppendId());
				tlSrvyRsltStatisticsDTO.setChartType(personalTransportationChartType.getChartType());
				tlSrvyRsltStatisticsDTO.isSelectBox(personalTransportationChartType.isSelectBox());
				tlSrvyRsltStatisticsDTO = getOdStatisticsListByType(tlSrvyRsltStatisticsDTO,personalTransportationChartType,searchDTO);
			}catch (Exception e){
				tlSrvyRsltStatisticsDTO = null;
			}
			tlSrvyRsltStatisticsList.add(tlSrvyRsltStatisticsDTO);
		}

		return tlSrvyRsltStatisticsList;
	}
	
	public TlSrvyRsltStatisticsDTO getOdStatisticsListByType(TlSrvyRsltStatisticsDTO tlSrvyRsltStatisticsDTO,PersonalTransportationChartType personalTransportationChartType,TlExmnRsltStatisticsSearchDTO searchDTO){
		Double avgNumber = 0.0;
		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = new ArrayList<>();
		List<String> searchList = null;
		switch (personalTransportationChartType){
			case TRAVEL_MODE_TRANSPORTATION: //통행 수단
				result = qTsTravelPassageTypeYyRepository.getTravelModeTransportationList(searchDTO);
				break;
			case TRAVEL_PURPOSE: //줄발지 유형
				result = qTsTravelDepartureTypeYyRepository.getTravelPurposeList(searchDTO);
				break;
			case TRAVEL_DEPARTURE: //도착지 유형
				result = qTsTravelDestinationTypeYyRepository.getTravelDepartureList(searchDTO);
				break;
			case TRAVEL_DESTINATION: //통행 목적
				result = qTsTravelPurposeYyRepository.getTravelDestinationList(searchDTO);
				break;
			case TRAVEL_DEPARTURE_TIME: //출발 시간
				result = qTsTravelDepartureTimeYyRepository.getTravelDepartureTimeList(searchDTO);
				searchList = qTsTravelDepartureTimeYyRepository.getSearchList(searchDTO);
				break;
			case TRAVEL_DESTINATION_TIME: //도착 시간
				result = qTsTravelDestinationTimeYyRepository.getTravelDestinationTimeList(searchDTO);
				searchList = qTsTravelDestinationTimeYyRepository.getSearchList(searchDTO);
				break;
			case AVG_TRAVEL_PURPOSE_TIME: //통행 목적 평균 이동 시간
				avgNumber = qTsTravelPurposeYyRepository.getAvgTravelPurposeList(searchDTO);
				searchList = qTsTravelPurposeYyRepository.getSearchList(searchDTO);
				break;
//			case AVG_TRAVEL_TRANSFORT_TIME: //통행 수단 평균 이동 시간 테이블 미생성
				//selectbox true
//				break;
			case TRAVEL_TRANSFORT_CNT: // 환승 횟수
				result = qTsTravelTransfortInfoYyRepository.getTravelTransfortCntList(searchDTO);
				break;
			case AVG_TRAVEL_TRANSFORT_HOUR: //평균 환승 시간
				avgNumber = qTsTravelTransfortInfoYyRepository.getAvgTravelTransfortHour(searchDTO);
				break;
			case TRAVEL_USE_HIGHWAY: //고속도로 이용
				result = qTsTravelUseHighwayYyRepository.getTravelUseHighwayList(searchDTO);
				break;
//			case TRAVEL_FEE_TYPE: //요금 유형
//				result = qTsTravelFeeInfoYyRepository.getTravelFeeTypeList(searchDTO);
//				break;
			case AVG_TRAVEL_MODE_TRANSPORTATION: //평균 승차 인원
				avgNumber = qTsTravelPassageTypeYyRepository.getAvgTravelModeTransportationList(searchDTO);
				searchList = qTsTravelPassageTypeYyRepository.getSearchList(searchDTO);
				break;
//			case AVG_TRAVEL_FEE: //평균 요금
				//result = qTsRoadSideHourTransportationYyRepository.getAvCgTravelFeeList(searchDTO);
				//selectbox true
//				break;
			case PERSONAL_STATUS_BY_AGE: //개인실태 나이 차트
				result = qTsPersonAgeYyRepository.getPersonalStatusByAgeList(searchDTO);
				break;
			case PERSONAL_STATUS_BY_GENDER: //개인실태 성별 테이블
				result = qTsPersonGenderYyRepository.getPersonalStatusByGenderList(searchDTO);
				break;
			case PERSONAL_STATUS_BY_EDUCATION: //개인실태 학력 테이블
				result = qTsPersonEducationYyRepository.getPersonalStatusByEducationList(searchDTO);
				break;
			case PERSONAL_STATUS_BY_OCCUPATION: //개인실태 직업 테이블
				result = qTsPersonOccupationYyRepository.getPersonalStatusByOccupationList(searchDTO);
				break;
			case PERSONAL_STATUS_BY_WORKINGDAY: //개인실태 평균 근무 일수 테이블
				result = qTsPersonWorkingdayYyRepository.getPersonalStatusByWorkingdayList(searchDTO);
				break;
			default:
				result = null;
				break;
		}
		tlSrvyRsltStatisticsDTO.setEnumType(personalTransportationChartType.toString());
		tlSrvyRsltStatisticsDTO.setStatisticsList(result);
		tlSrvyRsltStatisticsDTO.setAvgNumber(avgNumber);
		tlSrvyRsltStatisticsDTO.setSearchList(searchList);

		return tlSrvyRsltStatisticsDTO;
	}

	public TlSrvyRsltStatisticsDTO getRoadSideStatisticsListByType(TlSrvyRsltStatisticsDTO tlSrvyRsltStatisticsDTO,RoadSideChartType roadSideChartType,TlExmnRsltStatisticsSearchDTO searchDTO){
		List<TlSrvyRsltStatisticsDTO.TlSrvyRsltStatisticsInfo> result = new ArrayList<>();
		List<TlSrvyRsltStatisticsDTO.PassengerStatisticsInfo> passengerStats = new ArrayList<>();
		List<TlSrvyRsltStatisticsDTO.PurposeStatisticsInfo> purposeStats = new ArrayList<>();
		List<String> searchList = null;
		switch (roadSideChartType){
			case MODE_OF_TRANSPORTATION: //교통 수단
				result = qTsRoadSidePassageTypeYyRepository.getModeOfTransportationList(searchDTO);
				searchList = qTsRoadSidePassageTypeYyRepository.getSearchList(searchDTO);
				break;
			case NUMBER_OF_PASSENGERS: //탑승 승객 수
				passengerStats = qTsRoadSideNumberPassengersYyRepository.getNumberOfPassengersList(searchDTO);
				searchList = qTsRoadSideNumberPassengersYyRepository.getSearchList(searchDTO);
				break;
			case TYPE_OF_DEPARTURE_POINT: //출발지 유형
				result = qTsRoadSideDepartureTypeYyRepository.getTypeOfDeparturePointList(searchDTO);
				break;
			case TYPE_OF_ARRIVAL_POINT: //도착지 유형
				result = qTsRoadSideDestinationTypeYyRepository.getTypeOfArrivalPointList(searchDTO);
				break;
			case PURPOSE_OF_TRAVEL: //통행 목적
				result = qTsRoadSidePurposeYyRepository.getPurposeOfTravelList(searchDTO);
				break;
			case DEPARTURE_TIME: //출발시간별 년 통계
				result = qTsRoadSideDepartureTimeYyRepository.getDepartureTimelList(searchDTO);
				searchList = qTsRoadSideDepartureTimeYyRepository.getSearchList(searchDTO);
				break;
			case HOUR_TRANSPORTATION: //통행목적별 이동시간 년 통계
				purposeStats = qTsRoadSidePurposeYyRepository.getHourTransportationlList(searchDTO);
				searchList = qTsRoadSidePurposeYyRepository.getSearchList(searchDTO);
				break;
			/*case HOUR_PURPOSE: //통행수단별 이동시간 년 통계
				result = qTsRoadSideHourTransportationYyRepository.getHourPurposeList(searchDTO);
				break;*/
			default:
				result = null;
				break;
		}
		tlSrvyRsltStatisticsDTO.setEnumType(roadSideChartType.toString());
		tlSrvyRsltStatisticsDTO.setPassengerStatisticsList(passengerStats);
		tlSrvyRsltStatisticsDTO.setPurposeStatisticsList(purposeStats);
		tlSrvyRsltStatisticsDTO.setStatisticsList(result);
		tlSrvyRsltStatisticsDTO.setSearchList(searchList);

		return tlSrvyRsltStatisticsDTO;
	}

	public List<TsPopMng> getTsPopMngStatsList(TsPopulationStatisticsSearchDTO searchDTO) {
		return qTsPopMngRepository.getTsPopMngStatsList(searchDTO);
	}

	public List<FacilitiesDTO> getVdsFcltList() {
		return qTmVdsInstllcRepository.getTmVdsInstllcList();
	}

	public List<FacilitiesDTO> getMetroCntFixedFcltList() {
		return qTlFixedCurRepository.getTlFixedCurList();
	}

	public List<FacilitiesDTO> getMetroCntMovedFcltList() {
		return qTlMvmneqCurRepository.getTlMvmneqCurList();
	}

	public TsVdsRsltStatisticsDTO getTrafficStatisticsList(TsTrafficStatisticsSearchDTO searchDTO) {
		ObjectMapper mapper = new ObjectMapper();
		
		TsVdsRsltStatisticsDTO tsVdsRsltStatisticsDTO = new TsVdsRsltStatisticsDTO();
		
		List<TsVdsRsltStatisticsDTO.TsVdsTrfRsltStatisticsInfo> trfStatsList = null;
		List<TsVdsRsltStatisticsDTO.TsVdsSpdRsltStatisticsInfo> spdStatsList = null;
		
		int totTrfvlm = 0;
		double totAvgSpd = 0;
		List<Map<String, Object>> tsVdsTrfRsltStatisticsDBList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> tsVdsSpdRsltStatisticsDBList = new ArrayList<Map<String,Object>>();
		
		if(searchDTO.getSurveyType().equals("VDS")) {
			tsVdsTrfRsltStatisticsDBList = tsVdsYyRepository.getTsVdsTrfRsltStatisticsList(searchDTO);
			tsVdsSpdRsltStatisticsDBList = tsVdsYyRepository.getTsVdsSpdRsltStatisticsList(searchDTO);
        }else if(searchDTO.getSurveyType().equals("METROCOUNT_FIXED")) {
        	tsVdsTrfRsltStatisticsDBList = tsFixedYyRepository.getTsTrfRsltStatistcsList(searchDTO);
        	tsVdsSpdRsltStatisticsDBList = tsFixedYyRepository.getTsTrfSpdRsltStatisticsList(searchDTO);
        }else if(searchDTO.getSurveyType().equals("METROCOUNT_MOVED")) {
        	tsVdsTrfRsltStatisticsDBList = tsMvmneqYyRepository.getTsTrfRsltStatistcsList(searchDTO);
        	tsVdsSpdRsltStatisticsDBList = tsMvmneqYyRepository.getTsTrfSpdRsltStatisticsList(searchDTO);
        }
		
		if(!CommonUtils.isListNull(tsVdsTrfRsltStatisticsDBList)){
			trfStatsList = tsVdsTrfRsltStatisticsDBList.stream()
				.map(x -> (mapper.convertValue(x, TsVdsRsltStatisticsDTO.TsVdsTrfRsltStatisticsInfo.class)))
				.collect(Collectors.toList());
			
			totTrfvlm = trfStatsList.stream()
					.mapToInt(TsVdsRsltStatisticsDTO.TsVdsTrfRsltStatisticsInfo::getTrfvlm)
					.sum();
		}
		
		if(!CommonUtils.isListNull(tsVdsSpdRsltStatisticsDBList)){
			spdStatsList = tsVdsSpdRsltStatisticsDBList.stream()
				.map(x -> (mapper.convertValue(x, TsVdsRsltStatisticsDTO.TsVdsSpdRsltStatisticsInfo.class)))
				.collect(Collectors.toList());
		
			totAvgSpd = spdStatsList.stream()
					.mapToDouble(TsVdsRsltStatisticsDTO.TsVdsSpdRsltStatisticsInfo::getAvgspeed)
					.sum() / spdStatsList.size();
			
			totAvgSpd = Math.round(totAvgSpd * 1000.0 ) / 1000.0;
		
		}
		tsVdsRsltStatisticsDTO.setTotTrfvlm(totTrfvlm);
		tsVdsRsltStatisticsDTO.setTotAvgSpd(totAvgSpd);
		tsVdsRsltStatisticsDTO.setTsVdsSpdRsltStatisticsList(spdStatsList);
		tsVdsRsltStatisticsDTO.setTsVdsTrfRsltStatisticsList(trfStatsList);
		
		return tsVdsRsltStatisticsDTO;
	}

	public void excelDownLoad(HttpServletResponse response, TlExmnRsltStatisticsSearchDTO searchDTO,String excelType) {
		List<TlSrvyExcelDTO> result = null;
		switch(excelType) {
		case "od" :
			result = qTlSrvyAnsRepository.getSurveyRsltListForSearchOption(searchDTO,ExmnTypeCd.OD);
			break;
		case "roadside" :
			result = qTlSrvyAnsRepository.getSurveyRsltListForSearchOption(searchDTO,ExmnTypeCd.ROADSIDE);
			break;
		default:
			throw new CommonException(ErrorCode.EXCEL_DOWNLOAD_FAILED);
		}
		
		if(result != null && !result.isEmpty()) {
			try {
				switch(excelType) {
					case "od" :
						statisticsExcelDownloadComponent.odStatsDataExcelDownload(response,result);
						break;
					case "roadside" :
						statisticsExcelDownloadComponent.roadsideStatsDataExcelDownload(response,result);
						break;
					default:
						throw new CommonException(ErrorCode.EXCEL_DOWNLOAD_FAILED);
				}
			} catch (IOException e) {
				throw new CommonException(ErrorCode.EXCEL_DOWNLOAD_FAILED);
			}
		}
	}

	public void trafficStatisticsExcelDownLoad(HttpServletResponse response, TsTrafficStatisticsSearchDTO searchDTO) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		TsTrafficStatisticsExcelDTO result = new TsTrafficStatisticsExcelDTO();
		TsTrafficStatisticsExcelDTO.VehicleInfoSheet vehicleInfoSheet = null;
		TsTrafficStatisticsExcelDTO.VehicleInfoSheet.VehicleListSheet vehicleListSheet = null;
		List<TsTrafficStatisticsExcelDTO.TimeInfoSheet> timeInfoSheet = null;

		Map<String, Object> vehicleInfoSheetDB = new HashMap<String,Object>();
		Map<String, Object> vehicleListSheetDB = new HashMap<String,Object>();
		List<Map<String, Object>> timeInfoSheetDB = new ArrayList<Map<String, Object>>();
		Map<String, Object> timeListSheetDB = new HashMap<String,Object>();

		if("VDS".equals(searchDTO.getSurveyType())){
			vehicleInfoSheetDB = tsVdsYyRepository.getTrafficRsltInfoForVehicleExcelDownload(searchDTO);
			vehicleListSheetDB = tsVdsYyRepository.getTrafficRsltListForVehicleExcelDownload(searchDTO);
			timeInfoSheetDB = tsVdsYyRepository.getTrafficRsltInfoForTimeExcelDownload(searchDTO);
		} else if("METROCOUNT_FIXED".equals(searchDTO.getSurveyType())){
			vehicleInfoSheetDB = tsFixedYyRepository.getTrafficRsltInfoForVehicleExcelDownload(searchDTO);
			vehicleListSheetDB = tsFixedYyRepository.getTrafficRsltListForVehicleExcelDownload(searchDTO);
			timeInfoSheetDB = tsFixedYyRepository.getTrafficRsltInfoForTimeExcelDownload(searchDTO);
		} else {
			vehicleInfoSheetDB = tsMvmneqYyRepository.getTrafficRsltInfoForVehicleExcelDownload(searchDTO);
			vehicleListSheetDB = tsMvmneqYyRepository.getTrafficRsltListForVehicleExcelDownload(searchDTO);
			timeInfoSheetDB = tsMvmneqYyRepository.getTrafficRsltInfoForTimeExcelDownload(searchDTO);
		}

		vehicleInfoSheet = mapper.convertValue(vehicleInfoSheetDB, TsTrafficStatisticsExcelDTO.VehicleInfoSheet.class);
		vehicleListSheet = mapper.convertValue(vehicleListSheetDB, TsTrafficStatisticsExcelDTO.VehicleInfoSheet.VehicleListSheet.class);

		vehicleInfoSheet.setVehicleListSheet(vehicleListSheet);

		if(!CommonUtils.isListNull(timeInfoSheetDB)){
			timeInfoSheet = timeInfoSheetDB.stream()
				.map(x -> (mapper.convertValue(x, TsTrafficStatisticsExcelDTO.TimeInfoSheet.class)))
				.collect(Collectors.toList());

			for(TsTrafficStatisticsExcelDTO.TimeInfoSheet item : timeInfoSheet) {
				TsTrafficStatisticsExcelDTO.TimeInfoSheet.TimeListSheet timeListSheet = null;
				if("VDS".equals(searchDTO.getSurveyType())) {
					timeListSheetDB = tsVdsYyRepository.getTrafficRsltListForTimeExcelDownload(searchDTO, item);
				}else if("METROCOUNT_FIXED".equals(searchDTO.getSurveyType())){
					timeListSheetDB = tsFixedYyRepository.getTrafficRsltListForTimeExcelDownload(searchDTO, item);
				}else {
					timeListSheetDB = tsMvmneqYyRepository.getTrafficRsltListForTimeExcelDownload(searchDTO, item);
				}
				timeListSheet = mapper.convertValue(timeListSheetDB, TsTrafficStatisticsExcelDTO.TimeInfoSheet.TimeListSheet.class);

				item.setTimeListSheet(timeListSheet);
			}
		}

		result.setVehicleInfoSheet(vehicleInfoSheet);
		result.setTimeInfoSheet(timeInfoSheet);

		statisticsExcelDownloadComponent.axleloadAndTrafficStatsDataExcelDownload(response,result);
	}

	public List<Map<String, Object>> getMccStatsAdditionalInformation(String searchType, TlExmnRsltStatisticsSearchDTO searchDTO) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		if(searchType.equals("searchRegion")) {
			result = tsMccYyRepository.getTrfvlRsltStatisticsSearchListByRegion(searchDTO);
		}else if(searchType.equals("searchRoadCd")) {
			result = tsMccYyRepository.getTrfvlRsltStatisticsSearchListByRegionAndRoad(searchDTO);
		}else if(searchType.equals("searchExmnDistance")) {
			result = tsMccYyRepository.getTrfvlRsltStatisticsSearchListByRegionAndRoadAndDistance(searchDTO);
		}
		return result;
	}

	public List<Map<String, Object>> getTrafficStatsAdditionalInformation(String searchType, TsTrafficStatisticsSearchDTO searchDTO) {
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();

		switch (searchDTO.getSurveyType()) {
		case "VDS":
			if(searchType.equals("site")) {
				result = tmVdsInstllcRepository.getTmVdsInstllcListBySearchDTO(searchDTO);
			}else if(searchType.equals("year")) {
				result = tsVdsYyRepository.getTsVdsYyListBySearchDTO(searchDTO);
			}
			break;
		case "METROCOUNT_FIXED":
			result = tsFixedYyRepository.getTsFixedYyListBySearchDTO(searchDTO);
			break;
		case "METROCOUNT_MOVED":
			result = tsMvmneqYyRepository.getTsMvmneqYyListBySearchDTO(searchDTO);
			break;
		default:
			result = null;
			break;
		}

		return result;
	}

    public List<PeriodSiteDTO> getSiteIdListFromVDS(StatisticsByPeriodSerachDTO statisticsByPeriodSerachDTO) {
		List<PeriodSiteDTO> result = new ArrayList<PeriodSiteDTO>();
		if(!CommonUtils.isNull(statisticsByPeriodSerachDTO.getSearchDateType())){
			switch(statisticsByPeriodSerachDTO.getSearchDateType()){
				case "year":
					result = qTsVdsYyRepository.getSiteIdList(statisticsByPeriodSerachDTO);
					break;
				case "month":
					result = qTsVdsMmRepository.getSiteIdList(statisticsByPeriodSerachDTO);
					break;
				case "day":
					result = qTsVdsDdRepository.getSiteIdList(statisticsByPeriodSerachDTO);
					break;
				case "time":
					result = qTsVdsOnhrRepository.getSiteIdList(statisticsByPeriodSerachDTO);
					break;
				default:
					throw new CommonException(ErrorCode.INVALID_PARAMETER);
			}
		}
		return result;
	}

    public TsVdsRsltStatisticsDTO getAxleloadStatisticsList(TsTrafficStatisticsSearchDTO searchDTO) {
		ObjectMapper mapper = new ObjectMapper();
		
		TsVdsRsltStatisticsDTO tsVdsRsltStatisticsDTO = new TsVdsRsltStatisticsDTO();
		
		List<TsVdsRsltStatisticsDTO.TsVdsTrfRsltStatisticsInfo> trfStatsList = null;
		List<TsVdsRsltStatisticsDTO.TsAxleRsltStatisticsInfo> axleStatsList = null;
		
		List<Map<String, Object>> tsVdsTrfRsltStatisticsDBList = new ArrayList<Map<String,Object>>();
		List<Map<String, Object>> tsAxleRsltStatisticsDBList = new ArrayList<Map<String,Object>>();
		
    	tsVdsTrfRsltStatisticsDBList = tsAxleloadYyRepository.getTsTrfRsltStatistcsList(searchDTO);
    	tsAxleRsltStatisticsDBList = tsAxleloadYyRepository.getTsAxleRsltStatisticsList(searchDTO);
        
		if(!CommonUtils.isListNull(tsVdsTrfRsltStatisticsDBList)){
			trfStatsList = tsVdsTrfRsltStatisticsDBList.stream()
				.map(x -> (mapper.convertValue(x, TsVdsRsltStatisticsDTO.TsVdsTrfRsltStatisticsInfo.class)))
				.collect(Collectors.toList());
		}
		
		if(!CommonUtils.isListNull(tsAxleRsltStatisticsDBList)){
			axleStatsList = tsAxleRsltStatisticsDBList.stream()
				.map(x -> (mapper.convertValue(x, TsVdsRsltStatisticsDTO.TsAxleRsltStatisticsInfo.class)))
				.collect(Collectors.toList());
		
		}
		tsVdsRsltStatisticsDTO.setTsVdsTrfRsltStatisticsList(trfStatsList);
		tsVdsRsltStatisticsDTO.setTsAxleSpdRsltStatisticsList(axleStatsList);
		
		return tsVdsRsltStatisticsDTO;
	}

	public List<String> getAxleloadStatsAdditionalInformation(String searchType, TsTrafficStatisticsSearchDTO searchDTO) {
		List<String> result = new ArrayList<String>();
		result = qTsAxleloadYyRepository.getTsAxleloadYyListBySearchDTO(searchDTO);
		return result;
	}
	
	public void axleloadExcelDownLoad(HttpServletResponse response, TsTrafficStatisticsSearchDTO searchDTO) throws IOException {
		ObjectMapper mapper = new ObjectMapper();

		TsTrafficStatisticsExcelDTO result = new TsTrafficStatisticsExcelDTO();
		TsTrafficStatisticsExcelDTO.VehicleInfoSheet vehicleInfoSheet = null;
		TsTrafficStatisticsExcelDTO.VehicleInfoSheet.VehicleListSheet vehicleListSheet = null;
		List<TsTrafficStatisticsExcelDTO.TimeInfoSheet> timeInfoSheet = null;
		
		Map<String, Object> vehicleInfoSheetDB = new HashMap<String,Object>();
		Map<String, Object> vehicleListSheetDB = new HashMap<String,Object>();
		List<Map<String, Object>> timeInfoSheetDB = new ArrayList<Map<String, Object>>();
		Map<String, Object> timeListSheetDB = new HashMap<String,Object>();
		
		if(searchDTO.getSurveyType().equals("METROCOUNT_FIXED")){
			vehicleInfoSheetDB = tsAxleloadYyRepository.getAxleloadFixedRsltInfoForVehicleExcelDownload(searchDTO);
			vehicleListSheetDB = tsAxleloadYyRepository.getAxleloadFixedRsltListForVehicleExcelDownload(searchDTO);
			timeInfoSheetDB = tsAxleloadYyRepository.getAxleloadFixedRsltInfoForTimeExcelDownload(searchDTO);			
		}else {
			vehicleInfoSheetDB = tsAxleloadYyRepository.getAxleloadMovedRsltInfoForVehicleExcelDownload(searchDTO);
			vehicleListSheetDB = tsAxleloadYyRepository.getAxleloadMovedRsltListForVehicleExcelDownload(searchDTO);
			timeInfoSheetDB = tsAxleloadYyRepository.getAxleloadMovedRsltInfoForTimeExcelDownload(searchDTO);			
		}

		vehicleInfoSheet = mapper.convertValue(vehicleInfoSheetDB, TsTrafficStatisticsExcelDTO.VehicleInfoSheet.class);
		vehicleListSheet = mapper.convertValue(vehicleListSheetDB, TsTrafficStatisticsExcelDTO.VehicleInfoSheet.VehicleListSheet.class);
		
		vehicleInfoSheet.setVehicleListSheet(vehicleListSheet);
		
		if(!CommonUtils.isListNull(timeInfoSheetDB)){
			timeInfoSheet = timeInfoSheetDB.stream()
				.map(x -> (mapper.convertValue(x, TsTrafficStatisticsExcelDTO.TimeInfoSheet.class)))
				.collect(Collectors.toList());
		}
		
		for(TsTrafficStatisticsExcelDTO.TimeInfoSheet item : timeInfoSheet) {
			TsTrafficStatisticsExcelDTO.TimeInfoSheet.TimeListSheet timeListSheet = null;
			if(searchDTO.getSurveyType().equals("METROCOUNT_FIXED")){			
				timeListSheetDB = tsAxleloadYyRepository.getAxleloadFixedRsltListForTimeExcelDownload(searchDTO, item);
			}else {
				timeListSheetDB = tsAxleloadYyRepository.getAxleloadMovedRsltListForTimeExcelDownload(searchDTO, item);				
			}
			timeListSheet = mapper.convertValue(timeListSheetDB, TsTrafficStatisticsExcelDTO.TimeInfoSheet.TimeListSheet.class);
			
			item.setTimeListSheet(timeListSheet);
		}
		
		result.setVehicleInfoSheet(vehicleInfoSheet);
		result.setTimeInfoSheet(timeInfoSheet);

		statisticsExcelDownloadComponent.axleloadAndTrafficStatsDataExcelDownload(response,result);
	}

	/**
	 * methodName : getSiteIdListFromMetroCntFixed
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : MetroCnt 고정형 SiteId 조회
	 * @param searchDTO
	 * @return List<PeriodSiteDTO>
	 */
	public List<PeriodSiteDTO> getSiteIdListFromMetroCntFixed(StatisticsByPeriodSerachDTO searchDTO) {
		List<PeriodSiteDTO> result = new ArrayList<PeriodSiteDTO>();
		if(!CommonUtils.isNull(searchDTO.getSearchDateType())){
			switch(searchDTO.getSearchDateType()){
				case "year":
					result = qTsFixedYyRepository.getSiteIdList(searchDTO);
					break;
				case "month":
					result = qTsFixedMmRepository.getSiteIdList(searchDTO);
					break;
				case "day":
					result = qTsFixedDdRepository.getSiteIdList(searchDTO);
					break;
				case "time":
					result = qTsFixedOnhrRepository.getSiteIdList(searchDTO);
					break;
				default:
					throw new CommonException(ErrorCode.INVALID_PARAMETER);
			}
		}
		return result;
	}

	/**
	 * methodName : getSiteIdListFromMetroCntMoved
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : MetroCnt 이동형 Siteid 조회
	 * @param searchDTO
	 * @return List<PeriodSiteDTO>
	 */
	public List<PeriodSiteDTO> getSiteIdListFromMetroCntMoved(StatisticsByPeriodSerachDTO searchDTO) {
		List<PeriodSiteDTO> result = new ArrayList<PeriodSiteDTO>();
		if(!CommonUtils.isNull(searchDTO.getSearchDateType())){
			switch(searchDTO.getSearchDateType()){
				case "year":
					result = qTsMvmneqYyRepository.getSiteIdList(searchDTO);
					break;
				case "month":
					result = qTsMvmneqMmRepository.getSiteIdList(searchDTO);
					break;
				case "day":
					result = qTsMvmneqDdRepository.getSiteIdList(searchDTO);
					break;
				case "time":
					result = qTsMvmneqOnhrRepository.getSiteIdList(searchDTO);
					break;
				default:
					throw new CommonException(ErrorCode.INVALID_PARAMETER);
			}
		}
		return result;
	}

	/**
	 * methodName : getRoadInfoListFromMcc
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : Mcc RoadName 조회
	 * @param searchDTO
	 * @return List<PeriodSiteDTO>
	 */
	public List<PeriodSiteDTO> getRoadInfoListFromMcc(StatisticsByPeriodSerachDTO searchDTO) {
		List<PeriodSiteDTO> result = new ArrayList<PeriodSiteDTO>();
		if(!CommonUtils.isNull(searchDTO.getSearchDateType())){
			switch(searchDTO.getSearchDateType()){
				case "year":
					result = qTsMccTrfvlYyRepository.getSiteIdList(searchDTO);
					break;
				case "month":
					result = qTsMccTrfvlMmRepository.getSiteIdList(searchDTO);
					break;
				case "day":
					result = qTsMccTrfvlDdRepository.getSiteIdList(searchDTO);
					break;
				case "time":
					result = qTsMccTrfvlOnhrRepository.getSiteIdList(searchDTO);
					break;
				default:
					throw new CommonException(ErrorCode.INVALID_PARAMETER);
			}
		}
		return result;
	}


	public void mccExcelDownLoad(HttpServletResponse response,TlExmnRsltStatisticsSearchDTO searchDTO){
		List<Map<String,Object>> trfvlRsltStatisticsDBList = tsMccYyRepository.getTrfvlRsltStatisticsListGroupByTazCd(searchDTO);
		List<String[]> bodyList = new ArrayList<>();
		String[] headerArray = null;
		if(!CommonUtils.isListNull(trfvlRsltStatisticsDBList)){
			List<String> vhclclsfList = trfvlRsltStatisticsDBList.stream()
					.map(map -> map.get("name").toString())
					.collect(Collectors.toList());
			//지역,도로명,조사거리,조사년도
			vhclclsfList.add(0,CommonUtils.getMessage("common.mcc.excel.distance"));
			vhclclsfList.add(0,CommonUtils.getMessage("common.mcc.excel.road"));
			vhclclsfList.add(0,CommonUtils.getMessage("common.mcc.excel.region"));
			vhclclsfList.add(0,CommonUtils.getMessage("common.mcc.excel.year"));
			headerArray = vhclclsfList.toArray(new String[0]);

			List<String> vhclDataList = trfvlRsltStatisticsDBList.stream()
					.map(map -> map.get("value").toString())
					.collect(Collectors.toList());

			vhclDataList.add(0,searchDTO.getSearchExmnDistance()+"km");
			vhclDataList.add(0,searchDTO.getRoadNm());
			vhclDataList.add(0,searchDTO.getExmnLc());
			vhclDataList.add(0,searchDTO.getSearchDate());
			String[] stringArray = vhclDataList.toArray(new String[0]);
			bodyList.add(stringArray);
			String fileName = "MCC Statistics("+searchDTO.getSearchDate() + " " +searchDTO.getExmnLc()+")";
			excelDownloadComponent.excelDownload(response,headerArray,bodyList,fileName);
		}
	}

	/**
	 * methodName : getVDSChartData
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : VDS 차트조회 기간별통계
	 * @param searchDTO
	 * @return TlPeriodRsltStatisticsDTO
	 */
	public TlPeriodRsltStatisticsDTO getVDSChartData(StatisticsByPeriodSerachDTO searchDTO) {
		TlPeriodRsltStatisticsDTO result = new TlPeriodRsltStatisticsDTO();
		if(!CommonUtils.isNull(searchDTO.getSearchDateType())){
			List<CommonCdDTO> drctCdList = null;
			List<TlPeriodRsltStatisticsDTO.PeriodChartData> vdsTrfVlmChartData = null;
			switch(searchDTO.getSearchDateType()){
				case "year":
					drctCdList = qTsVdsYyRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsVdsYyRepository.getChartData(searchDTO);
					break;
				case "month":
					drctCdList = qTsVdsMmRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsVdsMmRepository.getChartData(searchDTO);
					break;
				case "day":
					drctCdList = qTsVdsDdRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsVdsDdRepository.getChartData(searchDTO);
					break;
				case "time":
					drctCdList = qTsVdsOnhrRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsVdsOnhrRepository.getChartData(searchDTO);
					break;
				default:
					throw new CommonException(ErrorCode.INVALID_PARAMETER);
			}
			result.setDataType("VDS");
			result.setDirectionList(drctCdList);
			result.setChartData(vdsTrfVlmChartData);
		}
		return result;
	}


	/**
	 * methodName : getMetroCntFixedChartData
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : 고정형 메트로카운트 기간별 통계 조회
	 * @param searchDTO
	 * @return TlPeriodRsltStatisticsDTO
	 */
	public TlPeriodRsltStatisticsDTO getMetroCntFixedChartData(StatisticsByPeriodSerachDTO searchDTO) {
		TlPeriodRsltStatisticsDTO result = new TlPeriodRsltStatisticsDTO();
		if(!CommonUtils.isNull(searchDTO.getSearchDateType())){
			List<CommonCdDTO> drctCdList = null;
			List<TlPeriodRsltStatisticsDTO.PeriodChartData> vdsTrfVlmChartData = null;
			switch(searchDTO.getSearchDateType()){
				case "year":
					drctCdList = qTsFixedYyRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsFixedYyRepository.getChartData(searchDTO);
					break;
				case "month":
					drctCdList = qTsFixedMmRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsFixedMmRepository.getChartData(searchDTO);
					break;
				case "day":
					drctCdList = qTsFixedDdRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsFixedDdRepository.getChartData(searchDTO);
					break;
				case "time":
					drctCdList = qTsFixedOnhrRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsFixedOnhrRepository.getChartData(searchDTO);
					break;
				default:
					throw new CommonException(ErrorCode.INVALID_PARAMETER);
			}
			result.setDataType("METROCOUNT_FIXED");
			result.setDirectionList(drctCdList);
			result.setChartData(vdsTrfVlmChartData);
		}
		return result;
	}

	/**
	 * methodName : getMetroCntMovedChartData
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : 메트로카운트 move 형 차트 데이터조회
	 * @param searchDTO
	 * @return TlPeriodRsltStatisticsDTO
	 */
	public TlPeriodRsltStatisticsDTO getMetroCntMovedChartData(StatisticsByPeriodSerachDTO searchDTO) {
		TlPeriodRsltStatisticsDTO result = new TlPeriodRsltStatisticsDTO();
		if(!CommonUtils.isNull(searchDTO.getSearchDateType())){
			List<CommonCdDTO> drctCdList = null;
			List<TlPeriodRsltStatisticsDTO.PeriodChartData> vdsTrfVlmChartData = null;
			switch(searchDTO.getSearchDateType()){
				case "year":
					drctCdList = qTsMvmneqYyRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsMvmneqYyRepository.getChartData(searchDTO);
					break;
				case "month":
					drctCdList = qTsMvmneqMmRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsMvmneqMmRepository.getChartData(searchDTO);
					break;
				case "day":
					drctCdList = qTsMvmneqDdRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsMvmneqDdRepository.getChartData(searchDTO);
					break;
				case "time":
					drctCdList = qTsMvmneqOnhrRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsMvmneqOnhrRepository.getChartData(searchDTO);
					break;
				default:
					throw new CommonException(ErrorCode.INVALID_PARAMETER);
			}
			result.setDataType("METROCOUNT_MOVED");
			result.setDirectionList(drctCdList);
			result.setChartData(vdsTrfVlmChartData);
		}
		return result;
	}


	/**
	 * methodName : getMccChartData
	 * author : Peo.Lee
	 * date : 2024-08-22
	 * description : MCC 기간별통계 차트 조회
	 * @param searchDTO
	 * @return TlPeriodRsltStatisticsDTO
	 */
	public TlPeriodRsltStatisticsDTO getMccChartData(StatisticsByPeriodSerachDTO searchDTO) {
		TlPeriodRsltStatisticsDTO result = new TlPeriodRsltStatisticsDTO();
		if (!CommonUtils.isNull(searchDTO.getSearchDateType())) {
			List<CommonCdDTO> drctCdList = null;
			List<TlPeriodRsltStatisticsDTO.PeriodChartData> vdsTrfVlmChartData = null;
			switch (searchDTO.getSearchDateType()) {
				case "year":
					drctCdList = qTsMccTrfvlYyRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsMccTrfvlYyRepository.getChartData(searchDTO);
					break;
				case "month":
					drctCdList = qTsMccTrfvlMmRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsMccTrfvlMmRepository.getChartData(searchDTO);
					break;
				case "day":
					drctCdList = qTsMccTrfvlDdRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsMccTrfvlDdRepository.getChartData(searchDTO);
					break;
				case "time":
					drctCdList = qTsMccTrfvlOnhrRepository.getDiriectionList(searchDTO);
					vdsTrfVlmChartData = qTsMccTrfvlOnhrRepository.getChartData(searchDTO);
					break;
				default:
					throw new CommonException(ErrorCode.INVALID_PARAMETER);
			}
			result.setDataType("MCC");
			result.setDirectionList(drctCdList);
			result.setChartData(vdsTrfVlmChartData);
		}
		return result;
	}
}