package com.sri.lanka.traffic.admin.web.service.systemmng;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sri.lanka.traffic.admin.common.entity.TcCdGrp;
import com.sri.lanka.traffic.admin.common.entity.TcCdInfo;
import com.sri.lanka.traffic.admin.common.repository.TcCdGrpRepository;
import com.sri.lanka.traffic.admin.common.repository.TcCdInfoRepository;
import com.sri.lanka.traffic.admin.common.util.CommonUtils;


@Service
public class CodeMngService {
	
	@Autowired
	private TcCdGrpRepository tcCdGrpRepository;
	
	@Autowired
	private TcCdInfoRepository tcCdInfoRepository;
	
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
		if (!CommonUtils.isNull(tcCdInfo.getCdNm())) newTcCdInfo.setCdNm(tcCdInfo.getCdNm());
		if (!CommonUtils.isNull(tcCdInfo.getUseYn())) newTcCdInfo.setUseYn(tcCdInfo.getUseYn());
		if (!CommonUtils.isNull(tcCdInfo.getCdDescr())) newTcCdInfo.setCdDescr(tcCdInfo.getCdDescr());
//		newTcCdInfo.setUpdId(mngrId);
		return newTcCdInfo;
	}
	
}
