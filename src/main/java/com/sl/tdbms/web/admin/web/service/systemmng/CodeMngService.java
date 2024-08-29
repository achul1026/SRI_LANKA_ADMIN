package com.sl.tdbms.web.admin.web.service.systemmng;

import java.util.List;

import com.sl.tdbms.web.admin.common.dto.tccdgrp.TcCdInfoDTO;
import com.sl.tdbms.web.admin.common.entity.TcCdGrp;
import com.sl.tdbms.web.admin.common.entity.TcCdInfo;
import com.sl.tdbms.web.admin.common.querydsl.QTcCdInfoRepository;
import com.sl.tdbms.web.admin.common.repository.TcCdGrpRepository;
import com.sl.tdbms.web.admin.common.repository.TcCdInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sl.tdbms.web.admin.common.util.CommonUtils;


@Service
public class CodeMngService {
	
	@Autowired
	private TcCdGrpRepository tcCdGrpRepository;
	
	@Autowired
	private TcCdInfoRepository tcCdInfoRepository;
	
	@Autowired
    QTcCdInfoRepository qTcCdInfoRepository;
	
	/**
	  * @Method Name : setTcCdGrpInfo
	  * @작성일 : 2024. 1. 30.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 공통 코드 정보 설정
	  * @param tcCdGrp
	  * @param type
	  * @return
	  */
	public TcCdGrp setTcCdGrpInfo(TcCdGrp tcCdGrp) {
//		String mngrId = LoginMngrUtils.getUserId();
		TcCdGrp newTcCdGrp = tcCdGrpRepository.findById(tcCdGrp.getGrpcdId()).get();
		
		if (!CommonUtils.isNull(tcCdGrp.getGrpCd())) newTcCdGrp.setGrpCd(tcCdGrp.getGrpCd());
		if (!CommonUtils.isNull(tcCdGrp.getGrpcdNm())) newTcCdGrp.setGrpcdNm(tcCdGrp.getGrpcdNm());
		if (!CommonUtils.isNull(tcCdGrp.getUseYn())) newTcCdGrp.setUseYn(tcCdGrp.getUseYn());
//		newTcCdGrp.setUpdId(mngrId);
		
		return newTcCdGrp;
		
	}
	
	/**
	  * @Method Name : setTcCdInfo
	  * @작성일 : 2024. 1. 30.
	  * @작성자 : SM.KIM
	  * @Method 설명 : 하위 코드 정보 설정
	  * @param tcCdInfo
	  * @param type
	  * @return
	  */
	public TcCdInfo setTcCdInfo(TcCdInfo tcCdInfo, String cdId) {
//		String mngrId = LoginMngrUtils.getUserId();
		TcCdInfo newTcCdInfo = tcCdInfoRepository.findById(cdId).get();
		
		if (!CommonUtils.isNull(tcCdInfo.getCd())) newTcCdInfo.setCd(tcCdInfo.getCd());
		if (!CommonUtils.isNull(tcCdInfo.getCdnmEng())) newTcCdInfo.setCdnmEng(tcCdInfo.getCdnmEng());
		if (!CommonUtils.isNull(tcCdInfo.getCdnmKor())) newTcCdInfo.setCdnmKor(tcCdInfo.getCdnmKor());
		if (!CommonUtils.isNull(tcCdInfo.getCdnmSin())) newTcCdInfo.setCdnmSin(tcCdInfo.getCdnmSin());
		if (!CommonUtils.isNull(tcCdInfo.getUseYn())) newTcCdInfo.setUseYn(tcCdInfo.getUseYn());
//		if (!CommonUtils.isNull(tcCdInfo.getCddescrEng())) newTcCdInfo.setCddescrEng(tcCdInfo.getCddescrEng());
//		if (!CommonUtils.isNull(tcCdInfo.getCddescrKor())) newTcCdInfo.setCddescrKor(tcCdInfo.getCddescrKor());
//		if (!CommonUtils.isNull(tcCdInfo.getCddescrSin())) newTcCdInfo.setCddescrSin(tcCdInfo.getCddescrSin());
//		newTcCdInfo.setUpdId(mngrId);
		return newTcCdInfo;
	}
	
	/**
	  * @Method Name : getTcCdInfo
	  * @작성일 : 2024. 5. 31.
	  * @작성자 : KY.LEE
	  * @Method 설명 : 하위코드 정보 조회
	  * @param grpCd
	  * @return List<TcCdInfo>
	 */
	public List<TcCdInfoDTO> getTcCdInfoForGrpCd(String grpCd){
		return qTcCdInfoRepository.getTcCdInfoListByGrpCd(grpCd);
	}
}
