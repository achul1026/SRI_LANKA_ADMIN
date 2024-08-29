package com.sl.tdbms.web.admin.scheduler.job;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import com.sl.tdbms.web.admin.common.entity.TlExmnRslt;
import com.sl.tdbms.web.admin.common.entity.TmExmnMng;
import com.sl.tdbms.web.admin.common.enums.code.ExmnSttsCd;
import com.sl.tdbms.web.admin.common.enums.code.ExmnTypeCd;
import com.sl.tdbms.web.admin.common.querydsl.QTmExmnMngRepository;
import com.sl.tdbms.web.admin.common.repository.TlExmnRsltRepository;
import com.sl.tdbms.web.admin.common.repository.TmExmnMngRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.tdbms.web.admin.common.util.CommonUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * @brief : 조사 상태 값 스케쥴러
 * @details : 조사 상태 값 스케쥴러
 * @author : NK.KIM
 * @date : 2024.02.23
 */
@Service
@Slf4j
public class InvstJobService {

	@Autowired
    QTmExmnMngRepository qTmExmnMngRepository;
	
	@Autowired
    TmExmnMngRepository tmExmnMngRepository;
	
	@Autowired
    TlExmnRsltRepository tlExmnRsltRepository;

	/**
	  * @Method Name : updateIvnstSurveySttS
	  * @작성일 : 2024. 6. 7.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 설문형 조사 상태값 수정
	  */
	public void updateIvnstSurveyStts() {
		try {
			ExmnTypeCd[] surveyTypeArr = {ExmnTypeCd.OD,ExmnTypeCd.AXLELOAD,ExmnTypeCd.ROADSIDE};
			List<TmExmnMng> invstSurveyList =  qTmExmnMngRepository.getInvstScheduler(surveyTypeArr);
			updateData(invstSurveyList);
		}catch (Exception e) {
			log.debug("Invst Survey scheduler Status Update Failed.");
		}
	}

	/**
	  * @Method Name : updateIvnstSurveySttS
	  * @작성일 : 2024. 6. 7.
	  * @작성자 : NK.KIM
	  * @Method 설명 : 교통량 조사 상태값 수정
	  */
	public void updateIvnstTrafficStts() {
		try {
			ExmnTypeCd[] trafficTypeArr = {ExmnTypeCd.TM,ExmnTypeCd.MCC};
			List<TmExmnMng> invstCountingList =  qTmExmnMngRepository.getInvstScheduler(trafficTypeArr);
			updateData(invstCountingList);
		}catch (Exception e) {
			log.debug("Invst Traffic scheduler Status Update Failed.");
		}
		
	}

	@Transactional
	void updateData(List<TmExmnMng> tmExmnMngList){
		if(!CommonUtils.isListNull(tmExmnMngList)){
			List<TlExmnRslt> tlExmnRsltList = new ArrayList<>();
			tmExmnMngList.forEach(x -> {x.setSttsCd(ExmnSttsCd.INVEST_COMPLETE);
										TlExmnRslt tlExmnRslt = tlExmnRsltRepository.findOneByExmnmngId(x.getExmnmngId());
										tlExmnRslt.setSttsCd(ExmnSttsCd.INVEST_COMPLETE);
										tlExmnRsltList.add(tlExmnRslt);}
								);
			tmExmnMngRepository.saveAll(tmExmnMngList);
			tlExmnRsltRepository.saveAll(tlExmnRsltList);
		}
	}
}
