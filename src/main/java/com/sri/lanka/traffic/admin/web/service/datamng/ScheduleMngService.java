package com.sri.lanka.traffic.admin.web.service.datamng;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sri.lanka.traffic.admin.common.dto.invst.TlExmnRsltHistoryDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TlExmnRsltHistorySearchDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnMngDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnPollsterSaveDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnScheduleDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnScheduleDetailDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnScheduleDetailDTO.TmExmnScheduleInfo;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnScheduleStatisticsDTO;
import com.sri.lanka.traffic.admin.common.dto.invst.TlExmnRsltHistoryDTO.TrafficHistoryTableInfo;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnScheduleStatisticsDTO.StatisticsMonthInfo;
import com.sri.lanka.traffic.admin.common.dto.invst.TmExmnScheduleStatisticsDTO.StatisticsTodayInfo;
import com.sri.lanka.traffic.admin.common.entity.TmExmnDrct;
import com.sri.lanka.traffic.admin.common.entity.TmExmnMng;
import com.sri.lanka.traffic.admin.common.entity.TmExmnPollster;
import com.sri.lanka.traffic.admin.common.enums.code.ExmnSttsCd;
import com.sri.lanka.traffic.admin.common.querydsl.QTmExmnMngRepository;
import com.sri.lanka.traffic.admin.common.querydsl.QTmExmnPollsterRepository;
import com.sri.lanka.traffic.admin.common.repository.TlExmnRsltRepository;
import com.sri.lanka.traffic.admin.common.repository.TmExmnDrctRepository;
import com.sri.lanka.traffic.admin.common.repository.TmExmnMngRepository;
import com.sri.lanka.traffic.admin.common.repository.TmExmnPollsterRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;
import com.sri.lanka.traffic.admin.common.util.PagingUtils;
import com.sri.lanka.traffic.admin.support.exception.CommonException;
import com.sri.lanka.traffic.admin.support.exception.ErrorCode;

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
	QTmExmnMngRepository qTmExmnMngRepository;
	
	@Autowired
	TmExmnPollsterRepository tmExmnPollsterRepository; 
	
	@Autowired
	QTmExmnPollsterRepository qtmExmnPollsterRepository; 
	
	
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
		
		try {
			Optional<TmExmnMng> invstInfo = tmExmnMngRepository.findById(tmExmnPollsterSaveDTO.getExmnmngId());
			if (!invstInfo.isPresent()) {
				throw new CommonException(ErrorCode.EMPTY_DATA, "Examination is empty");
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
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "Investigator save failed");
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
		if (!invstInfo.isPresent()) {
			throw new CommonException(ErrorCode.EMPTY_DATA, "Examination is empty");
		}
		String partcptCd = getPartcptCd();
		
		try {
			TmExmnMng tmExmnMng = invstInfo.get();
			tmExmnMng.setPartcptCd(partcptCd);
			tmExmnMngRepository.save(tmExmnMng);
		}catch (Exception e) {
			throw new CommonException(ErrorCode.ENTITY_SAVE_FAILED, "Examination Partcode save failed");
		}
		return partcptCd;
	}
	
	/**
	  * @Method Name : changeAccssCode
	  * @작성일 : 2023. 10. 26.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 참여코드 중복 체크
	  * @param shtInfoId
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
				TmExmnScheduleInfo tmExmnScheduleInfo = new TmExmnScheduleInfo();
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
		TmExmnScheduleStatisticsDTO tmExmnScheduleStatisticsDTO = new TmExmnScheduleStatisticsDTO();
		LocalDate currentDate = LocalDate.now();
		ObjectMapper mapper = new ObjectMapper();
		
		Map<String,Object> monthStatisticsInfo = tmExmnMngRepository.getScheduleStatistics(CommonUtils.getMonthStartOfDay(currentDate),CommonUtils.getMonthEndOfDay(currentDate)
																							,ExmnSttsCd.INVEST_COMPLETE.getCode(),ExmnSttsCd.INVEST_WRITING.getCode(),ExmnSttsCd.INVEST_PROGRESS.getCode());
		StatisticsMonthInfo statisticsMonthInfo = mapper.convertValue(monthStatisticsInfo, StatisticsMonthInfo.class);

		Map<String,Object> todayStatisticsInfo = tmExmnMngRepository.getScheduleStatistics(CommonUtils.getStartOfDay(currentDate),CommonUtils.getEndOfDay(currentDate)
																							,ExmnSttsCd.INVEST_COMPLETE.getCode(),ExmnSttsCd.INVEST_WRITING.getCode(),ExmnSttsCd.INVEST_PROGRESS.getCode());
		StatisticsTodayInfo statisticsTodayInfo = mapper.convertValue(todayStatisticsInfo, StatisticsTodayInfo.class);
		
		tmExmnScheduleStatisticsDTO.setStatisticsMonthInfo(statisticsMonthInfo);
		tmExmnScheduleStatisticsDTO.setStatisticsTodayInfo(statisticsTodayInfo);
		
		return tmExmnScheduleStatisticsDTO;
	}
	
	/**
	  * @Method Name : getHourList
	  * @작성일 : 2024. 4. 9.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 조사 시간 및 방향에 따른 시간 목록 생성
	  * @param exmnInfo
	  * @return
	  */
	public TlExmnRsltHistoryDTO getTrafficHistoryInfo(TlExmnRsltHistorySearchDTO tlExmnRsltHistorySearchDTO, TmExmnMng tmExmnMng){
		TlExmnRsltHistoryDTO tlExmnRsltHistoryDTO = new TlExmnRsltHistoryDTO();
		List<Map<String,Object>> hourList = new ArrayList<>();
	    
		int startHour = Integer.parseInt(CommonUtils.formatLocalDateTime(tmExmnMng.getStartDt(), "HH"));
	    int endHour = Integer.parseInt(CommonUtils.formatLocalDateTime(tmExmnMng.getEndDt(), "HH"));
	    
	    LocalDate searchDate = null;
	    if(CommonUtils.isNull(tlExmnRsltHistorySearchDTO.getSearchDate())){
	    	searchDate = LocalDate.now();
	    }else {
	    	searchDate = LocalDate.parse(tlExmnRsltHistorySearchDTO.getSearchDate(), DateTimeFormatter.ISO_DATE); 
	    }
	    
	    //방향 정보
	    Optional<TmExmnDrct> exmnDrctInfo = tmExmnDrctRepository.findById(tlExmnRsltHistorySearchDTO.getExmndrctId());
	    
	    //당일 교통량 조사 이력 정보 
	    List<Map<String,Object>> dataInfoList = tlExmnRsltRepository.getTimeListForHistory(tmExmnMng.getExmnmngId(), searchDate
	    																					, exmnDrctInfo.get().getStartlcNm(), exmnDrctInfo.get().getEndlcNm());
		List<String> dataTimeList = dataInfoList.stream()
												.filter(x -> x.containsKey("dataHour") && CommonUtils.isNull(x.get("lcchgRsn")))
												.map(m -> m.get("dataHour").toString())
												.collect(Collectors.toList());
		int invstTotalCnt = 0;
		int invstCompletedCnt = 0;
	    for (int hour = startHour; hour <= endHour; hour++) {
	    	Map<String,Object> hourInfo = new HashMap<>();
	    	boolean isExists = false;
	        String formattedHour = String.format("%02d:00", hour);
	        hourInfo.put("hour", formattedHour);
	        if(!CommonUtils.isListNull(dataTimeList) && dataTimeList.contains(formattedHour)) {
	        	isExists = true;
	        }
	        hourInfo.put("isExists", isExists);
	        hourList.add(hourInfo);
	        invstTotalCnt++;
	    }
	    
	    tlExmnRsltHistoryDTO.setHourList(hourList);
	    tlExmnRsltHistoryDTO.setStartDt(tmExmnMng.getStartDt());
	    tlExmnRsltHistoryDTO.setEndDt(tmExmnMng.getEndDt());
	    tlExmnRsltHistoryDTO.setExmnDiv(tmExmnMng.getExmnDiv());
	    tlExmnRsltHistoryDTO.setInvstTotalCnt(invstTotalCnt);
	    
	    if(!CommonUtils.isListNull(dataTimeList)) invstCompletedCnt = dataTimeList.size();
	    tlExmnRsltHistoryDTO.setInvstCompletedCnt(invstCompletedCnt);
	    
	    int invstNotCompletedCnt = invstTotalCnt - invstCompletedCnt;
	    tlExmnRsltHistoryDTO.setInvstNotCompletedCnt(invstNotCompletedCnt);
	    //당일 교통량 조사 이력 정보  END
	    
	    //교통량 조사 이력 테이블 정보  START
	    PagingUtils paging = new PagingUtils();
	    paging.setLimitCount(5);
	    
	    List<Map<String,Object>> dataTableInfoList = tlExmnRsltRepository.getDateListForTableHistory(tmExmnMng.getExmnmngId(), exmnDrctInfo.get().getStartlcNm(), exmnDrctInfo.get().getEndlcNm()
	    																								,paging.getOffsetCount(),paging.getLimitCount());
	    List<TrafficHistoryTableInfo> trafficHistoryTableList = new ArrayList<>();
	    if(!CommonUtils.isListNull(dataTableInfoList)) {
	    	ObjectMapper mapper = new ObjectMapper();
	    	trafficHistoryTableList = dataTableInfoList.stream()
									                    .map(x -> mapper.convertValue(x, TrafficHistoryTableInfo.class))
									                    .collect(Collectors.toList());
	    	final int totalCnt = invstTotalCnt;
	    	trafficHistoryTableList.forEach(x -> { x.setTotalCnt(totalCnt); int incompleteCnt = totalCnt - x.getCompleteCnt(); x.setIncompleteCnt(incompleteCnt);});
	    	
	    	long trafficHistoryTotalCnt = tlExmnRsltRepository.getDateListForTableHistoryTotalCnt(tmExmnMng.getExmnmngId(), exmnDrctInfo.get().getStartlcNm(), exmnDrctInfo.get().getEndlcNm());
	    	paging.setPageSize(5);
			paging.setTotalCount(trafficHistoryTotalCnt);
			
			tlExmnRsltHistoryDTO.setPaging(paging);
	    }
	    tlExmnRsltHistoryDTO.setTrafficHistoryTableList(trafficHistoryTableList);
	    //교통량 조사 이력 테이블 정보  END
	    
	    return tlExmnRsltHistoryDTO;
	}
}
