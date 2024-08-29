package com.sl.tdbms.web.admin.web.service.datamng;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletResponse;

import com.sl.tdbms.web.admin.common.dto.invst.*;
import com.sl.tdbms.web.admin.common.entity.*;
import com.sl.tdbms.web.admin.common.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sl.tdbms.web.admin.common.component.ExcelDownloadComponent;
import com.sl.tdbms.web.admin.common.dto.common.LoginMngrDTO;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;
import com.sl.tdbms.web.admin.common.enums.code.UserTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.QTlExmnRsltRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTlSrvyAnsRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTmExmnMngRepository;
import com.sl.tdbms.web.admin.common.querydsl.QTmExmnPollsterRepository;
import com.sl.tdbms.web.admin.common.util.CommonUtils;
import com.sl.tdbms.web.admin.common.util.LoginMngrUtils;
import com.sl.tdbms.web.admin.common.util.PagingUtils;
import com.sl.tdbms.web.admin.support.exception.CommonException;
import com.sl.tdbms.web.admin.support.exception.ErrorCode;

/**
  * @FileName : ScheduleMngService.java
  * @Project : sri_lanka_admin
  * @Date : 2024. 3. 28. 
  * @작성자 : NK.KIM
  * @프로그램 설명 :
  */
@Service
public class ScheduleMngService {
	
	@Autowired
    TmExmnMngRepository tmExmnMngRepository;
	
	@Autowired
    TmExmnDrctRepository tmExmnDrctRepository;
	
	@Autowired
    TlExmnRsltRepository tlExmnRsltRepository;
	
	@Autowired
    TmExmnPollsterRepository tmExmnPollsterRepository;
	
	@Autowired
    QTmExmnMngRepository qTmExmnMngRepository;
	
	@Autowired
    QTlExmnRsltRepository qTlExmnRsltRepository;
	
	@Autowired
    QTmExmnPollsterRepository qtmExmnPollsterRepository;
	
	@Autowired
    TlSrvyAnsRepository tlSrvyAnsRepository;

	@Autowired
	QTlSrvyAnsRepository qTlSrvyAnsRepository;
	
	@Autowired
    ExcelDownloadComponent excelDownloadComponent;
    @Autowired
    private TlSrvyRsltRepository tlSrvyRsltRepository;

	/**
	  * @Method Name : getInvstScheduleListForCalendar
	  * @작성일 : 2024. 4. 3.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사 현황 및 이력 > 달력 데이터 세팅
	  * @param invstList
	  * @return
	  */
	public String getInvstScheduleListForCalendar(List<TmExmnMngDTO> invstList) {
		List<TmExmnScheduleDTO> tmExmnScheduleDTOList = invstList.stream()
														    .map(invst -> {
														    	TmExmnScheduleDTO tmExmnScheduleDTO = new TmExmnScheduleDTO();
														    	tmExmnScheduleDTO.setExmnmngId(invst.getExmnmngId());
														    	tmExmnScheduleDTO.setTitle(invst.getExmnNm());
														    	tmExmnScheduleDTO.setBackgroundColor(invst.getColrCd().getName());
														    	tmExmnScheduleDTO.setStart(CommonUtils.formatLocalDateTime(invst.getStartDt(),"yyyy-MM-dd"));
														    	tmExmnScheduleDTO.setEnd(CommonUtils.formatLocalDateTime(invst.getEndDt().plusDays(1),"yyyy-MM-dd"));
														        return tmExmnScheduleDTO;
														    })
														    .collect(Collectors.toList());
		
		return CommonUtils.getListToJsonString(tmExmnScheduleDTOList);
	}
	
	/**
	  * @Method Name : saveInvestigatorInfo
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사원 목록 등록
	  * @param tmExmnPollsterSaveDTO
	  */
	@Transactional
	public TmExmnPollster saveInvestigatorList(TmExmnPollsterSaveDTO tmExmnPollsterSaveDTO) {
		TmExmnPollster retrunTmExmnPollster = new TmExmnPollster();
		String resMsg;
		try {
			Optional<TmExmnMng> invstInfo = tmExmnMngRepository.findById(tmExmnPollsterSaveDTO.getExmnmngId());
			if (!invstInfo.isPresent()) {
				resMsg = CommonUtils.getMessage("invst.invstDetail.data.empty");
				throw new CommonException(ErrorCode.EMPTY_DATA, resMsg);
			}
			TmExmnMng tmExmnMng = invstInfo.get();
			
			List<TmExmnPollster> tmExmnPollsterList = tmExmnPollsterSaveDTO.getTmExmnPollsterList();
			if(!CommonUtils.isListNull(tmExmnPollsterList)) {
				for(TmExmnPollster tmExmnPollster : tmExmnPollsterList) {
					boolean isDataUpdate = false;
					String pollsterId = tmExmnPollster.getPollsterId();
					String pollsterTel = tmExmnPollster.getPollsterTel();
					String pollsterEmail = tmExmnPollster.getPollsterEmail();
					//같은 조사 기간내 동일한 연락처 또는 이메일이 존재하면 x
					
					//정보 수정인경우
					if(!CommonUtils.isNull(pollsterId)) {
						//db 조회
						Optional<TmExmnPollster> dbTmExmnPollster = tmExmnPollsterRepository.findById(pollsterId);
						//데이터가 바뀌지 않았다면 querydsl 조건문에서 제외시키기 위해 pollsterTel or pollsterEmail null 
						if(dbTmExmnPollster.get().getPollsterTel().equals(pollsterTel)) pollsterTel = null;
						if(dbTmExmnPollster.get().getPollsterEmail().equals(pollsterEmail)) pollsterEmail = null;
						//둘중에 하나라도 null 아니라면 데이터가 바뀌었으므로 중복검사 하기 위해서 true
						if(!CommonUtils.isNull(pollsterTel) || !CommonUtils.isNull(pollsterEmail)) isDataUpdate = true;
					}
					
					//저장하거나 데이터가 바뀐 경우
					if(CommonUtils.isNull(pollsterId) || isDataUpdate) {
						//같은 기간내의 조사원 정보 가져오기
						List<TmExmnPollster> dbTmExmnPollsterList = qtmExmnPollsterRepository.getPollsterListByInvstDate(tmExmnMng.getStartDt(), 
								tmExmnMng.getEndDt(),
								pollsterTel,
								pollsterEmail);
						
						//존제하다면 break;
						if(dbTmExmnPollsterList.size() > 0) {
							retrunTmExmnPollster = tmExmnPollster;
							break;
						}
						
						if(CommonUtils.isNull(pollsterId)){
							tmExmnPollster.setPollsterId(CommonUtils.getUuid());
						}
					}
					tmExmnPollsterRepository.save(tmExmnPollster);
				}
			}
			
			//수정시 목록 DB데이터 삭제
			if(tmExmnPollsterSaveDTO.getDeletePollsterIdArray().length > 0) {
				qtmExmnPollsterRepository.deleteByIdArr(tmExmnPollsterSaveDTO.getDeletePollsterIdArray());
				
			}
		}catch (Exception e) {
			resMsg = CommonUtils.getMessage("schedule.saveInvestigatorList.fail");
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
		
		return retrunTmExmnPollster;
	}
	
	/**
	  * @Method Name : savePartcptCd
	  * @작성일 : 2024. 3. 28.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사 코드 생성
	  * @param exmnmngId
	  */
	@Transactional
	public String savePartcptCd(String exmnmngId) {
		Optional<TmExmnMng> invstInfo = tmExmnMngRepository.findById(exmnmngId);
		String resMsg;
		if (!invstInfo.isPresent()) {
			resMsg = CommonUtils.getMessage("invst.invstDetail.data.empty");
			throw new CommonException(ErrorCode.EMPTY_DATA, resMsg);
		}
		String partcptCd = getPartcptCd();
		
		try {
			TmExmnMng tmExmnMng = invstInfo.get();
			tmExmnMng.setPartcptCd(partcptCd);
			tmExmnMngRepository.save(tmExmnMng);
		}catch (Exception e) {
			resMsg = CommonUtils.getMessage("schedule.savePartcptCd.fail");
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, resMsg);
		}
		return partcptCd;
	}
	
	/**
	  * @Method Name : changeAccssCode
	  * @작성일 : 2023. 10. 26.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 참여코드 중복 체크
	  * @return
	  */
	public String getPartcptCd() {
		
		String partcptCd = "";
		while(true) {
			partcptCd = CommonUtils.getRandomKey(8);
			boolean accssCode = tmExmnMngRepository.existsByPartcptCd(partcptCd);
			if(!accssCode) {
				break;
			}
		}
		
		return partcptCd;
	}
	
	
	/**
	  * @Method Name : getInvstScheduleInfo
	  * @작성일 : 2024. 4. 3.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 캘린더 상세 목록
	  * @param paramDate
	  * @return
	  */
	public TmExmnScheduleDetailDTO getInvstScheduleInfo(String paramDate) {
		TmExmnScheduleDetailDTO tmExmnScheduleDetailDTO = new TmExmnScheduleDetailDTO();
		if(CommonUtils.isNull(paramDate)) {
			//exception
		}
		List<TmExmnMng> tmExmnMngList = qTmExmnMngRepository.getInvstScheduleDetailList(paramDate); 
		if(!CommonUtils.isListNull(tmExmnMngList)) {
			for(TmExmnMng tmExmnMng : tmExmnMngList) {
				TmExmnScheduleDetailDTO.TmExmnScheduleInfo tmExmnScheduleInfo = new TmExmnScheduleDetailDTO.TmExmnScheduleInfo();
				tmExmnScheduleInfo.setTmExmnMng(tmExmnMng);
				List<TmExmnPollster> tmExmnPollster = tmExmnPollsterRepository.findAllByExmnmngIdOrderByRegistDtAsc(tmExmnMng.getExmnmngId());
				String pollsterNm = "-";
				if(!CommonUtils.isListNull(tmExmnPollster)) {
					pollsterNm = tmExmnPollster.stream()
												      .map(TmExmnPollster::getPollsterNm)
												      .reduce((s1, s2) -> s1 + "," + s2)
												      .get();
					
				}
				tmExmnScheduleInfo.setPollsterNm(pollsterNm);
				tmExmnScheduleDetailDTO.getTmExmnScheduleList().add(tmExmnScheduleInfo);
			}
			int totalCnt = qTmExmnMngRepository.getInvstScheduleDetailTotalCount(paramDate); 
			tmExmnScheduleDetailDTO.setTotalCnt(totalCnt);
		}
		return tmExmnScheduleDetailDTO;
	}
	
	
	/**
	  * @Method Name : getScheduleStatisticsInfo
	  * @작성일 : 2024. 4. 4.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 조사 현황 및 이력 월/일별 통계
	  * @return
	  */
	public TmExmnScheduleStatisticsDTO getScheduleStatisticsInfo() {
		LoginMngrDTO userInfo = LoginMngrUtils.getTcUserMngInfo();
		
		String bffltd = userInfo.getUserType() == UserTypeCd.SUPER? null :userInfo.getUserBffltd();
		
		TmExmnScheduleStatisticsDTO tmExmnScheduleStatisticsDTO = new TmExmnScheduleStatisticsDTO();
		LocalDate currentDate = LocalDate.now();
		ObjectMapper mapper = new ObjectMapper();
		
		Map<String,Object> monthStatisticsInfo = tmExmnMngRepository.getScheduleStatistics(CommonUtils.getMonthStartOfDay(currentDate),CommonUtils.getMonthEndOfDay(currentDate), bffltd
																							, ExmnSttsCd.INVEST_COMPLETE.getCode(),ExmnSttsCd.INVEST_WRITING.getCode(),ExmnSttsCd.INVEST_PROGRESS.getCode());
		TmExmnScheduleStatisticsDTO.StatisticsMonthInfo statisticsMonthInfo = mapper.convertValue(monthStatisticsInfo, TmExmnScheduleStatisticsDTO.StatisticsMonthInfo.class);

		Map<String,Object> todayStatisticsInfo = tmExmnMngRepository.getScheduleStatistics(CommonUtils.getStartOfDay(currentDate),CommonUtils.getEndOfDay(currentDate), bffltd
																							,ExmnSttsCd.INVEST_COMPLETE.getCode(),ExmnSttsCd.INVEST_WRITING.getCode(),ExmnSttsCd.INVEST_PROGRESS.getCode());
		TmExmnScheduleStatisticsDTO.StatisticsTodayInfo statisticsTodayInfo = mapper.convertValue(todayStatisticsInfo, TmExmnScheduleStatisticsDTO.StatisticsTodayInfo.class);
		
		tmExmnScheduleStatisticsDTO.setStatisticsMonthInfo(statisticsMonthInfo);
		tmExmnScheduleStatisticsDTO.setStatisticsTodayInfo(statisticsTodayInfo);
		
		return tmExmnScheduleStatisticsDTO;
	}
	
	/**
	  * @Method Name : getHourList
	  * @작성일 : 2024. 4. 9.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 교통량 조사 시간 및 방향에 따른 시간 목록 생성
	  * @param tlExmnRsltHistorySearchDTO
	  * @return
	  */
	public TlExmnTrfvlRsltHistoryDTO getTrafficHistoryInfo(TlExmnRsltHistorySearchDTO tlExmnRsltHistorySearchDTO, TmExmnMng tmExmnMng){
		TlExmnTrfvlRsltHistoryDTO tlExmnTrfvlRsltHistoryDTO = new TlExmnTrfvlRsltHistoryDTO();
		ObjectMapper mapper = new ObjectMapper();

	    LocalDate searchDate = null;
	    if(CommonUtils.isNull(tlExmnRsltHistorySearchDTO.getSearchDate())){
	    	searchDate = LocalDate.now();
	    }else {
	    	searchDate = LocalDate.parse(tlExmnRsltHistorySearchDTO.getSearchDate(), DateTimeFormatter.ISO_DATE); 
	    }
	    
	    //방향 정보
	    Optional<TmExmnDrct> exmnDrctInfo = tmExmnDrctRepository.findById(tlExmnRsltHistorySearchDTO.getExmndrctId());

	    //교통량 조사 이력 테이블 정보  START
	    List<String> trfvlmexmnIdList = tlExmnRsltRepository.getTrfvlmexmnIdList(tmExmnMng.getExmnmngId(),
																				searchDate,
																				exmnDrctInfo.get().getStartlcNm(),
																				exmnDrctInfo.get().getEndlcNm());

		List<Map<String,Object>> lcchgRsnCheckList = tlExmnRsltRepository.getLcchgRsnList(tmExmnMng.getExmnmngId(),
																							//searchDate,
																							exmnDrctInfo.get().getStartlcNm(),
																							exmnDrctInfo.get().getEndlcNm());
		if(!CommonUtils.isListNull(lcchgRsnCheckList)){
			List<TlExmnTrfvlRsltHistoryDTO.LcchgRsnInfo> lcchgRsnList = lcchgRsnCheckList.stream()
																						.map(x -> mapper.convertValue(x, TlExmnTrfvlRsltHistoryDTO.LcchgRsnInfo.class))
																						.collect(Collectors.toList());
			tlExmnTrfvlRsltHistoryDTO.setLcchgRsnList(lcchgRsnList);
		}

		LocalTime startTime = tmExmnMng.getStartDt().toLocalTime();
		LocalTime endTime = tmExmnMng.getEndDt().toLocalTime();
		LocalDateTime searchStartDt = LocalDateTime.of(searchDate, startTime);
		LocalDateTime searchEndDt = LocalDateTime.of(searchDate, endTime);

		List<Map<String,Object>> trafficHistoryList = tlExmnRsltRepository.getDateListForTableTrafficHistory(trfvlmexmnIdList,searchStartDt,searchEndDt);
		List<TlExmnTrfvlRsltHistoryDTO.TrafficHistoryTableInfo> trafficHistoryTableList = trafficHistoryList.stream()
													.map(x -> mapper.convertValue(x, TlExmnTrfvlRsltHistoryDTO.TrafficHistoryTableInfo.class))
													.collect(Collectors.toList());

	    tlExmnTrfvlRsltHistoryDTO.setTrafficHistoryTableList(trafficHistoryTableList);
	    //교통량 조사 이력 테이블 정보  END
	    
	    return tlExmnTrfvlRsltHistoryDTO;
	}
	
	/**
	  * @Method Name : getSurveyHistoryInfo
	  * @작성일 : 2024. 5. 17.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문 조사 시간 및 방향에 따른 시간 목록 생성
	  * @param tmExmnMng
	  * @param paging
	  * @return
	  */
	public TlExmnSrvyRsltHistoryDTO getSurveyHistoryInfo(TlExmnRsltHistorySearchDTO tlExmnRsltHistorySearchDTO,TmExmnMng tmExmnMng, PagingUtils paging){
		TlExmnSrvyRsltHistoryDTO tlExmnSrvyRsltHistoryDTO = new TlExmnSrvyRsltHistoryDTO();
		
		String startlcNm = null;
		String endlcNm = null;
		if("true".equals(tmExmnMng.getExmnType().getHasDrct())) {
			
			//방향 정보
			Optional<TmExmnDrct> exmnDrctInfo = tmExmnDrctRepository.findById(tlExmnRsltHistorySearchDTO.getExmndrctId());
			startlcNm = exmnDrctInfo.get().getStartlcNm();
			endlcNm = exmnDrctInfo.get().getEndlcNm();
			
		}
		
		int invstCompletedCnt = qTlExmnRsltRepository.getSruveyCompletedCnt(tmExmnMng.getExmnmngId(),startlcNm,endlcNm); 
		int invstGoalCnt = tmExmnMng.getGoalCnt().intValue();
		int invstIncompletedCnt = invstGoalCnt - invstCompletedCnt;
		if(invstIncompletedCnt < 0) {
			invstIncompletedCnt = 0;
		}
		
		tlExmnSrvyRsltHistoryDTO.setExmnmngId(tmExmnMng.getExmnmngId());
		tlExmnSrvyRsltHistoryDTO.setExmnNm(tmExmnMng.getExmnNm());
		tlExmnSrvyRsltHistoryDTO.setExmnType(tmExmnMng.getExmnType());
		tlExmnSrvyRsltHistoryDTO.setStartDt(tmExmnMng.getStartDt());
		tlExmnSrvyRsltHistoryDTO.setEndDt(tmExmnMng.getEndDt());
		tlExmnSrvyRsltHistoryDTO.setInvstGoalCnt(invstGoalCnt);
		tlExmnSrvyRsltHistoryDTO.setInvstCompletedCnt(invstCompletedCnt);
		tlExmnSrvyRsltHistoryDTO.setInvstIncompletedCnt(invstIncompletedCnt);
		
		String srvyMetadataCd = "SMD014";
		if(ExmnTypeCd.ROADSIDE.equals(tmExmnMng.getExmnType())){
			srvyMetadataCd = "SMD065"; //차종 타입
		}
		
		List<Map<String,Object>> dataTableInfoList = tlExmnRsltRepository.getDateListForTableSurveyHistory(tmExmnMng.getExmnmngId()
//																										, startlcNm, endlcNm
																										,paging.getOffsetCount(),paging.getLimitCount()
																										,srvyMetadataCd);
		
		List<TlExmnSrvyRsltHistoryDTO.SurveyHistoryTableInfo> surveyHistoryTableList = new ArrayList<>();
		long surveyHistoryTotalCnt = 0;
		if(!CommonUtils.isListNull(dataTableInfoList)) {
			ObjectMapper mapper = new ObjectMapper();
			surveyHistoryTableList = dataTableInfoList.stream()
														.map(x -> mapper.convertValue(x, TlExmnSrvyRsltHistoryDTO.SurveyHistoryTableInfo.class))
														.collect(Collectors.toList());
			
			surveyHistoryTotalCnt = tlExmnRsltRepository.getDateListForTableHistorySurveyTotalCnt(tmExmnMng.getExmnmngId()
//																									, startlcNm, endlcNm
																									);
		}
		
		tlExmnSrvyRsltHistoryDTO.setSurveyHistoryTableList(surveyHistoryTableList);
		paging.setTotalCount(surveyHistoryTotalCnt);
		tlExmnSrvyRsltHistoryDTO.setPaging(paging);
		//교통량 조사 이력 테이블 정보  END
		
		return tlExmnSrvyRsltHistoryDTO;
	}

	/**
	  * @Method Name : getSurveyResultInfo
	  * @작성일 : 2024. 7. 15.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 설문 결과 이력 조회
	  * @param srvyrsltId
	  * @return
	  */
	public TlSrvyAnsHistoryResultDTO getSurveyResultInfo(String srvyrsltId) {
		TlSrvyAnsHistoryResultDTO tlSrvyAnsHistoryResultDTO =  new TlSrvyAnsHistoryResultDTO();
		// 메타코드를 이용한 테이블 헤더 표현방식 변경
		List<String> metadataCode = new ArrayList<>();
		metadataCode.add("SMD028");
		metadataCode.add("SMD032");
		metadataCode.add("SMD036");
		metadataCode.add("SMD040");
		metadataCode.add("SMD044");
//		
//		List<Map<String,Object>> dataTableInfoList = tlSrvyAnsRepository.getResultListForTableSurveyHistory(srvyrsltId, metadataCode);
		List<Map<String,Object>> dataTableInfoList = tlSrvyAnsRepository.getResultListForTableSurveyHistory(srvyrsltId, LocaleContextHolder.getLocale().toString(), metadataCode);
		List<TlSrvyAnsHistoryResultDTO.SurveyResultTableInfo> surveyResultTableList = new ArrayList<>();
//		long surveyResultTotalCnt = 0;
		if(!CommonUtils.isListNull(dataTableInfoList)) {
			ObjectMapper mapper = new ObjectMapper();
			surveyResultTableList = dataTableInfoList.stream()
														.map(x -> mapper.convertValue(x, TlSrvyAnsHistoryResultDTO.SurveyResultTableInfo.class))
														.collect(Collectors.toList());
			
//			surveyResultTotalCnt = tlSrvyAnsRepository.getResultListForTableHistorySurveyTotalCnt(srvyrsltId);
			tlSrvyAnsHistoryResultDTO.setSurveyResultTableList(surveyResultTableList);
		}
		return tlSrvyAnsHistoryResultDTO;
	}


	@Transactional
	public void excelDownload(HttpServletResponse resp, String exmnmngId) throws IOException {
		List<String> srvyrsltIdList = qTlExmnRsltRepository.getSrvyrsltIdList(exmnmngId);
		List<String> metadataCode = new ArrayList<>();
		metadataCode.add("SMD028");
		metadataCode.add("SMD032");
		metadataCode.add("SMD036");
		metadataCode.add("SMD040");
		metadataCode.add("SMD044");
		List<Map<String, Object>> resultList = tlSrvyAnsRepository.getSurveyHistoryResultListForExcel(srvyrsltIdList, LocaleContextHolder.getLocale().toString(), metadataCode);
		// ObjectMapper를 사용하여 JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();

        // title의 첫 번째 항목을 저장할 배열과 ansCnts 데이터를 저장할 리스트
        String[] headerArray = null;
        List<String[]> bodyList = new ArrayList<>();

        for (Map<String, Object> result : resultList) {
            // JSON 문자열을 가져옴
            String jsonString = (String) result.get("resultData");

            // JSON 파싱
            JsonNode jsonNode = objectMapper.readTree(jsonString);
            JsonNode titlesNode = jsonNode.get("qstnNm");
            JsonNode ansCntsNode = jsonNode.get("ansCnt");

            if (titlesNode != null) {
            	// 가장 긴 질문만 headerArray 설정
            	if (headerArray == null || headerArray.length < titlesNode.size()) {
            		headerArray = StreamSupport.stream(titlesNode.spliterator(), false)
            				.map(JsonNode::asText)
            				.toArray(String[]::new);
				}
            }

            // 모든 ansCnts 값을 리스트에 저장
            if (ansCntsNode != null) {
                String[] ansCntsArray = StreamSupport.stream(ansCntsNode.spliterator(), false)
                                                     .map(JsonNode::asText)
                                                     .toArray(String[]::new);
                bodyList.add(ansCntsArray);
            }
        }
        String exmnNm = tmExmnMngRepository.findById(exmnmngId).get().getExmnNm().replaceAll(" ", "_");
		String fileName = CommonUtils.formatLocalDateTime(LocalDateTime.now(),"yyyyMMdd")+ "_" + exmnNm;
		excelDownloadComponent.excelDownload(resp, headerArray, bodyList, fileName);
	}

	/**
	 * methodName : getSurveyHistoryInfoFromKecc
	 * author : Peo.Lee
	 * date : 2024-08-26
	 * description : KECC 권한 설문 정보 조회
	 * @param srvyrsltId
	 * @return List<KeccSrvyRsltDTO>
	 */
	public List<KeccSrvyRsltDTO> getSurveyHistoryInfoFromKecc(String srvyrsltId) {
		List<Map<String,Object>> results = tlSrvyAnsRepository.findSurveyResults(srvyrsltId);

		return results.stream().map(result -> {
			KeccSrvyRsltDTO dto = new KeccSrvyRsltDTO();
			dto.setSrvyansId((String) result.get("srvyansId"));
			dto.setQstnTitle((String) result.get("qstnTitle"));
			dto.setAnsCnts((String) result.get("ansCnts"));
			dto.setSectSqno((BigDecimal) result.get("sectSqno"));
			dto.setSectType((String) result.get("sectType"));
			dto.setQstnSqno((BigDecimal) result.get("qstnSqno"));
			dto.setSrvyMetadataCd((String) result.get("srvyMetadataCd"));
			dto.setQstnType(((String) result.get("qstnType")));
			dto.setEtcYn((String) result.get("etcYn"));
			dto.setAnsList(parseJsonbToList((String) result.get("ansList")));
			return dto;
		}).collect(Collectors.toList());
	}

	/**
	 * methodName : parseJsonbToList
	 * author : Peo.Lee
	 * date : 2024-08-28
	 * description : JSON To List
	 * @param jsonbString
	 * @return List<String>
	 */
	private List<String> parseJsonbToList(String jsonbString) {
		if (jsonbString == null || jsonbString.isEmpty()) {
			return Collections.emptyList();
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(jsonbString, new TypeReference<List<String>>() {});
		} catch (IOException e) {
			throw new RuntimeException("Failed to parse JSONB to List<String>", e);
		}
	}

	@Transactional
    public void surveyInfoUpdate(KeccSrvyUpdateDTO keccSrvyUpdateDTO) {
		String srvyrsltId = keccSrvyUpdateDTO.getSrvyrsltId();

		tlSrvyRsltRepository.updateRegistDtBySrvyrsltId(LocalDateTime.now(), srvyrsltId);

		if (keccSrvyUpdateDTO.getAnsList() != null && !keccSrvyUpdateDTO.getAnsList().isEmpty()) {
			for (KeccSrvyUpdateDTO.KeccSrvyAns survyAns : keccSrvyUpdateDTO.getAnsList()) {
				tlSrvyAnsRepository.updateAnsCntsAndEtcYnBySrvyansId(survyAns.getAnsCnts(), survyAns.getEtcYn(), survyAns.getSrvyansId());
			}
		}
    }
}
